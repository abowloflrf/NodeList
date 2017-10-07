package cn.lltw.nodelist;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.Toast;

import com.wilddog.client.ChildEventListener;
import com.wilddog.client.DataSnapshot;
import com.wilddog.client.Query;
import com.wilddog.client.SyncError;
import com.wilddog.client.SyncReference;
import com.wilddog.client.ValueEventListener;
import com.wilddog.client.WilddogSync;
import com.wilddog.wilddogauth.WilddogAuth;
import com.wilddog.wilddogauth.model.WilddogUser;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cn.lltw.nodelist.Model.NodeTask;

public class TaskActivity extends AppCompatActivity {

    WilddogAuth auth;
    WilddogUser user;
    String listKey;
    String uid;
    SyncReference ref;
    public TaskAdapter adapter;
    public TaskCompleteAdapter taskCompleteAdapter;
    List<NodeTask> mNodeTask = new ArrayList<>();
    List<NodeTask> mNodeTaskCompleted = new ArrayList<>();
    private static final String TAG = "TaskActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String listTitle = bundle.getString("listName");
        listKey = bundle.getString("listKey");
        // 改变Title为清单名称
        this.setTitle((CharSequence) listTitle);

        auth = WilddogAuth.getInstance();
        user = auth.getCurrentUser();
        uid = user.getUid();
        ref = WilddogSync.getInstance().getReference("users/" + uid + "/lists/" + listKey + "/tasks");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Iterator iter = dataSnapshot.getChildren().iterator();
                    while (iter.hasNext()) {
                        DataSnapshot data = (DataSnapshot) iter.next();
                        String taskKey = (String) data.getKey();
                        String taskName = (String) data.child("name").getValue();
                        String taskDescribe = (String) data.child("describe").getValue();
                        String taskDueTime = (String) data.child("dueTime").getValue();
                        String taskRepeat = (String) data.child("repeat").getValue();
                        boolean taskComplete = (boolean) data.child("complete").getValue();
                        NodeTask nodeTask = new NodeTask(taskName, taskDescribe, taskKey);
                        nodeTask.setComplete(taskComplete);
                        nodeTask.setDueTime(taskDueTime);
                        nodeTask.setRepeat(taskRepeat);
                        if (taskComplete) {
                            mNodeTaskCompleted.add(0, nodeTask);
                        } else {
                            mNodeTask.add(0, nodeTask);
                        }
                        Log.d(TAG, "onDataChange: " + nodeTask.getName());
                    }
                    adapter.notifyDataSetChanged();
                    taskCompleteAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(SyncError syncError) {

            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.task_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new TaskAdapter(mNodeTask, listKey, this);
        recyclerView.setAdapter(adapter);
        registerForContextMenu(recyclerView);

        RecyclerView recyclerViewComplete = (RecyclerView) findViewById(R.id.task_complete_recycler_view);
        LinearLayoutManager linearLayoutManagerComplete = new LinearLayoutManager(this);
        recyclerViewComplete.setLayoutManager(linearLayoutManagerComplete);
        taskCompleteAdapter = new TaskCompleteAdapter(mNodeTaskCompleted, listKey, this);
        recyclerViewComplete.setAdapter(taskCompleteAdapter);
//        registerForContextMenu(recyclerViewComplete);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        super.onCreateOptionsMenu(menu);
        getMenuInflater().inflate(R.menu.add_task, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        super.onOptionsItemSelected(item);
        switch (item.getItemId()) {
            case R.id.add_task:
                showCreateNewTaskDialog(R.layout.new_task_dialog, null, -1);
                break;
            case android.R.id.home:
                finish();
                break;
        }
        return true;
    }

    //TODO:增加更多的可选项选项
    private void showCreateNewTaskDialog(int dialogLayout, final NodeTask nodeTask, final int position) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(dialogLayout, null);
        switch (dialogLayout) {
            case R.layout.new_task_dialog:
                final EditText taskName = (EditText)view.findViewById(R.id.dialog_task_name);
                final EditText taskRemark = (EditText)view.findViewById(R.id.dialog_task_describe);
                builder.setTitle("创建任务")
                        .setView(view)
                        .setPositiveButton("创建", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                createTask(taskName.getText().toString(), taskRemark.getText().toString());
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
                break;
            case R.layout.task_describe_dialog:
                final EditText taskNewDescribe = (EditText) view.findViewById(R.id.dialog_task_describe_edit);
                taskNewDescribe.setText(nodeTask.getDescribe());
                builder.setTitle("修改和查看备注")
                        .setView(view)
                        .setPositiveButton("修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
                                adapter.modifyDescribe(nodeTask.getKey(), position, taskNewDescribe.getText().toString());
                            }
                        })
                        .setNegativeButton("返回",null)
                        .show();
                break;
            case R.layout.task_due_time_dialog:
                //TODO:得到选中时间
                final CalendarView taskNewDueTimeView = (CalendarView) view.findViewById(R.id.dialog_task_calendar_edit);
                final Long taskNewDueTime = taskNewDueTimeView.getDate();
                final SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy/MM/dd");
                builder.setTitle("设置日期")
                        .setView(view)
                        .setPositiveButton("修改", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialogInterface, int i) {
//                                adapter.modifyDescribe(nodeTask.getKey(), position, taskNewDescribe.getText().toString());
                                Toast.makeText(getApplicationContext(), "" + simpleDateFormat.format(taskNewDueTime), Toast.LENGTH_SHORT).show();
                            }
                        })
                        .setNegativeButton("取消",null)
                        .show();
                break;
        }
    }

    private void createTask(final String taskName, final String taskRemark) {
        //根据新建参数构造一个NodeList对象
        HashMap<String,Object> task=new HashMap<>();
        task.put("name", taskName);
        task.put("describe", taskRemark);
        task.put("repeat", "");
        task.put("complete", false);
        task.put("dueTime", "");
        //修改了添加任务的方式，避免了自动生成，使得任务的编号能够自己控制
        ref.push().setValue(task, new SyncReference.CompletionListener() {
            @Override
            public void onComplete(SyncError syncError, SyncReference syncReference) {
                if (syncError!=null) {
                    Toast.makeText(getApplicationContext(), "Failed:"+syncError.getErrCode(), Toast.LENGTH_SHORT).show();
                } else {
                    String taskKey = syncReference.getKey();
                    final NodeTask nodeTask = new NodeTask(taskName, taskRemark, taskKey);
                    nodeTask.setComplete(false);
                    nodeTask.setDueTime("");
                    nodeTask.setRepeat("");
                    mNodeTask.add(0, nodeTask);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

    @Override
    public boolean onContextItemSelected(MenuItem item) {
        int position = -1;
        NodeTask nodeTask;
        try {
            position = adapter.getPosition();
            nodeTask = mNodeTask.get(position);
        } catch (Exception e) {
            Log.d(TAG, e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        switch (item.getItemId()) {
            case 0:
                Toast.makeText(getApplicationContext(), "完成了" + nodeTask.getName(), Toast.LENGTH_SHORT).show();
                adapter.finishTask(nodeTask.getKey(), position);
                break;
            case 1:
                // do your stuff
                Toast.makeText(getApplicationContext(), "点击了1", Toast.LENGTH_SHORT).show();
                break;
            case 2:
                // do your stuff
                Toast.makeText(getApplicationContext(), "点击了2", Toast.LENGTH_SHORT).show();
                break;
            case 3:
                // do your stuff
                Toast.makeText(getApplicationContext(), "点击了3", Toast.LENGTH_SHORT).show();
                showCreateNewTaskDialog(R.layout.task_due_time_dialog, nodeTask, position);
                break;
            case 4:
                // do your stuff
                Toast.makeText(getApplicationContext(), "修改了" + nodeTask.getName() + "的备注", Toast.LENGTH_SHORT).show();
                showCreateNewTaskDialog(R.layout.task_describe_dialog, nodeTask, position);
                break;
            case 5:
                // do your stuff
                Toast.makeText(getApplicationContext(), "删除了" + nodeTask.getName(), Toast.LENGTH_SHORT).show();
                adapter.deleteTask(nodeTask.getKey(), position);
                break;
        }
        return super.onContextItemSelected(item);
    }
}