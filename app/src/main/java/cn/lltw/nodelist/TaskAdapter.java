package cn.lltw.nodelist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;
import android.widget.Toast;

import com.wilddog.client.SyncReference;
import com.wilddog.client.WilddogSync;
import com.wilddog.wilddogauth.WilddogAuth;
import com.wilddog.wilddogauth.model.WilddogUser;

import java.util.List;

import cn.lltw.nodelist.Model.NodeTask;

/**
 * Created by watso on 2017/10/4.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<NodeTask> mNodeTask;
    private String listKey;
    private TaskActivity taskActivity;
    private View view;
    private int position;

    public TaskAdapter(List<NodeTask> mNodeTask, String listKey, TaskActivity taskActivity) {
        this.mNodeTask = mNodeTask;
        this.listKey = listKey;
        this.taskActivity = taskActivity;
    }

    static class ViewHolder extends RecyclerView.ViewHolder
            implements View.OnCreateContextMenuListener {
        View nodetaskView;
        TextView nodetaskName;
        TextView nodetaskRepeat;
        TextView nodetaskDueTime;
        CheckBox nodetaskComplete;

        public ViewHolder(View view) {
            super(view);
            nodetaskView = view;
            nodetaskName = (TextView) view.findViewById(R.id.task_card_name);
            nodetaskRepeat = (TextView) view.findViewById(R.id.task_card_repeat);
            nodetaskDueTime = (TextView) view.findViewById(R.id.task_card_due_time);
            nodetaskComplete = (CheckBox) view.findViewById(R.id.task_checkbox);
            view.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
            contextMenu.setHeaderTitle("请选择操作:");
            contextMenu.add(Menu.NONE, 0, Menu.NONE, "标记为完成");
            contextMenu.add(Menu.NONE, 1, Menu.NONE, "查看详情");
            contextMenu.add(Menu.NONE, 2, Menu.NONE, "修改日期");
            contextMenu.add(Menu.NONE, 3, Menu.NONE, "设置提醒时间");
            contextMenu.add(Menu.NONE, 4, Menu.NONE, "修改备注");
            contextMenu.add(Menu.NONE, 5, Menu.NONE, "删除");
        }


    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.nodetaskComplete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                int position = holder.getAdapterPosition();
                final NodeTask task = mNodeTask.get(position);
                String taskKey = task.getKey();
                if (isChecked) {
                    finishTask(taskKey, position);
                    Toast.makeText(view.getContext(), task.getName() + "选中" + position, Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(view.getContext(), "取消选中" + position, Toast.LENGTH_SHORT).show();
                }
            }
        });

        holder.nodetaskView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                int position = holder.getAdapterPosition();
//                final NodeTask task = mNodeTask.get(position);
                setPosition(position);
                return false;

                //跳转到TaskActivity
                /*Intent intent = new Intent(view.getContext(), TaskDetailActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("taskName", task.getName());
                bundle.putString("taskKey", task.getKey());
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
                Toast.makeText(view.getContext(), "你点击了"+task.getName(), Toast.LENGTH_SHORT).show();
*/

            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, int position) {
        NodeTask nodeTask = mNodeTask.get(position);
        holder.nodetaskName.setText(nodeTask.getName());
        holder.nodetaskRepeat.setText(nodeTask.getRepeat());
        holder.nodetaskDueTime.setText(nodeTask.getDueTime());
        holder.nodetaskComplete.setChecked(false);


    }

    @Override
    public int getItemCount() {
        return mNodeTask.size();
    }

    public void addmNodeTask(NodeTask nodeTask) {
        this.mNodeTask.add(0, nodeTask);
        notifyDataSetChanged();
    }

    public void finishTask(String taskKey, int position) {
        NodeTask nodeTask = mNodeTask.get(position);
        nodeTask.setComplete(true);
        taskActivity.taskCompleteAdapter.addmNodeTaskComplete(nodeTask);
        WilddogAuth auth = WilddogAuth.getInstance();
        WilddogUser user = auth.getCurrentUser();
        String uid = user.getUid();
        SyncReference ref = WilddogSync
                .getInstance()
                .getReference("users/" + uid + "/lists/" + listKey + "/tasks/" + taskKey);
        ref.child("complete").setValue(true);
        mNodeTask.remove(position);
        notifyDataSetChanged();
    }

    public void deleteTask(String taskKey, int position) {
        WilddogAuth auth = WilddogAuth.getInstance();
        WilddogUser user = auth.getCurrentUser();
        String uid = user.getUid();
        SyncReference ref = WilddogSync
                .getInstance()
                .getReference("users/" + uid + "/lists/" + listKey + "/tasks/" + taskKey);
        ref.removeValue();
        mNodeTask.remove(position);
        notifyDataSetChanged();
    }

    public void modifyDescribe(String taskKey, int position, String newDescribe) {
        mNodeTask.get(position).setDescribe(newDescribe);
        WilddogAuth auth = WilddogAuth.getInstance();
        WilddogUser user = auth.getCurrentUser();
        String uid = user.getUid();
        SyncReference ref = WilddogSync
                .getInstance()
                .getReference("users/" + uid + "/lists/" + listKey + "/tasks/" + taskKey);
        ref.child("describe").setValue(newDescribe);
        notifyDataSetChanged();
    }

    public void setPosition(int position) {
        this.position = position;
    }

    public int getPosition() {
        return position;
    }
}
