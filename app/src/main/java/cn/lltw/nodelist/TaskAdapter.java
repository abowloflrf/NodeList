package cn.lltw.nodelist;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
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

    public TaskAdapter(List<NodeTask> mNodeTask, String listKey, TaskActivity taskActivity) {
        this.mNodeTask = mNodeTask;
        this.listKey = listKey;
        this.taskActivity = taskActivity;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
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
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(final ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card, parent, false);
        final ViewHolder holder = new ViewHolder(view);

        holder.nodetaskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.nodetaskComplete.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                int position = holder.getAdapterPosition();
                final NodeTask task = mNodeTask.get(position);
                String taskKey = task.getKey();
                if (isChecked) {
                    holder.nodetaskComplete.setChecked(true);
                    finishTask(taskKey, position);
                    Toast.makeText(view.getContext(), task.getName() + "选中" + position, Toast.LENGTH_SHORT).show();
                } else {
                    holder.nodetaskComplete.setChecked(false);
                    Toast.makeText(view.getContext(), "取消选中" + position, Toast.LENGTH_SHORT).show();
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NodeTask nodeTask = mNodeTask.get(position);
        holder.nodetaskName.setText(nodeTask.getName());
        holder.nodetaskRepeat.setText(nodeTask.getRepeat());
        holder.nodetaskDueTime.setText(nodeTask.getDueTime());
    }

    @Override
    public int getItemCount() {
        return mNodeTask.size();
    }

    public void addmNodeTask(NodeTask nodeTask) {
        this.mNodeTask.add(0, nodeTask);
        notifyDataSetChanged();
    }

    private void finishTask(String taskKey, int position) {
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
}
