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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;

import cn.lltw.nodelist.Model.NodeTask;

public class TaskActivity extends AppCompatActivity {
    static int taskSeq;
    int listID;
    WilddogAuth auth;
    WilddogUser user;
    String uid;
    SyncReference ref;
    TaskAdapter adapter;
    List<NodeTask> mNodeTask = new ArrayList<>();
    private static final String TAG = "TaskActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        taskSeq = 0;
        String testTitle = bundle.getString("listName");
        listID = bundle.getInt("listID");
        // 改变Title为清单名称
        this.setTitle((CharSequence) (testTitle + listID));

        auth = WilddogAuth.getInstance();
        user = auth.getCurrentUser();
        uid = user.getUid();
        ref = WilddogSync.getInstance().getReference("users/" + uid + "/lists/" + listID + "/tasks");
        System.out.println(ref);
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if (dataSnapshot.getValue() != null) {
                    Iterator iter = dataSnapshot.getChildren().iterator();
                    taskSeq = (int)dataSnapshot.getChildrenCount();
                    while (iter.hasNext()) {
                        DataSnapshot data = (DataSnapshot) iter.next();
                        String task_name = (String) data.child("name").getValue();
                        NodeTask nodeTask = new NodeTask(task_name);
                        mNodeTask.add(nodeTask);
                        Log.d(TAG, "onDataChange: " + nodeTask.getName());
                    }
                    adapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(SyncError syncError) {

            }
        });

        RecyclerView recyclerView = (RecyclerView) findViewById(R.id.task_recycler_view);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter = new TaskAdapter(mNodeTask);
        recyclerView.setAdapter(adapter);
    }

    //TODO:加上这两个方法了以后，返回键不能用了 =_=
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
                showCreateNewTaskDialog();
        }
        return true;
    }

    //TODO:增加更多的可选项选项
    private void showCreateNewTaskDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        View view = inflater.inflate(R.layout.new_task_dialog, null);
        final EditText taskName = (EditText)view.findViewById(R.id.dialog_task_name);
        final EditText taskRemark = (EditText)view.findViewById(R.id.dialog_task_remark);
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
    }

    private void createTask(String taskName, String taskRemark) {
        //根据新建参数构造一个NodeList对象
        final NodeTask nodeTask=new NodeTask(taskName, taskRemark);
        HashMap<String,Object> task=new HashMap<>();
        task.put("name", taskName);
        task.put("describe", taskRemark);
        ref= WilddogSync.getInstance().getReference("users/"+uid+"/lists/" + listID + "/tasks/" + taskSeq);
        //修改了添加任务的方式，避免了自动生成，使得任务的编号能够自己控制
        ref.setValue(task, new SyncReference.CompletionListener() {
            @Override
            public void onComplete(SyncError syncError, SyncReference syncReference) {
                if (syncError!=null) {
                    Toast.makeText(getApplicationContext(), "Failed:"+syncError.getErrCode(), Toast.LENGTH_SHORT).show();
                } else {
                    mNodeTask.add(nodeTask);
                    adapter.notifyDataSetChanged();
                    taskSeq += 1;
                }
            }
        });
    }
}