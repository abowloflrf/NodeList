package cn.lltw.nodelist;

/**
 * Created by watso on 2017/10/5.
 */

import android.graphics.Paint;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import java.util.List;

import cn.lltw.nodelist.Model.NodeTask;

import android.widget.Toast;

import com.wilddog.client.SyncReference;
import com.wilddog.client.WilddogSync;
import com.wilddog.wilddogauth.WilddogAuth;
import com.wilddog.wilddogauth.model.WilddogUser;


public class TaskCompleteAdapter extends RecyclerView.Adapter<TaskCompleteAdapter.ViewHolder> {
    private List<NodeTask> mNodeTaskComplete;
    private TaskActivity taskActivity;
    private String listKey;

    public TaskCompleteAdapter(List<NodeTask> mNodeTaskComplete, String listKey, TaskActivity taskActivity) {
        this.mNodeTaskComplete = mNodeTaskComplete;
        this.listKey = listKey;
        this.taskActivity = taskActivity;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View nodetaskView;
        TextView nodetaskName;
        CheckBox nodetaskCompleted;
        public ViewHolder(View view) {
            super(view);
            nodetaskView = view;
            nodetaskCompleted = (CheckBox) view.findViewById(R.id.task_complete_checkbox);
            nodetaskName = (TextView) view.findViewById(R.id.task_complete_card_name);
            nodetaskName.setPaintFlags(nodetaskName.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_complete_card, parent, false);
        final ViewHolder holder = new ViewHolder(view);
        holder.nodetaskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        holder.nodetaskCompleted.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean isChecked) {
                int position = holder.getAdapterPosition();
                final NodeTask task = mNodeTaskComplete.get(position);
                String taskKey = task.getKey();
                if (isChecked) {
                    holder.nodetaskCompleted.setChecked(true);
                    Toast.makeText(view.getContext(), "选中", Toast.LENGTH_SHORT).show();
                } else {
                    holder.nodetaskCompleted.setChecked(false);
                    markTaskUnfinished(taskKey, position);
                    Toast.makeText(view.getContext(), "取消选中", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NodeTask nodeTask= mNodeTaskComplete.get(position);
        holder.nodetaskName.setText(nodeTask.getName());
    }

    @Override
    public int getItemCount() {
        return mNodeTaskComplete.size();
    }

    public void addmNodeTaskComplete(NodeTask nodeTask) {
        this.mNodeTaskComplete.add(0, nodeTask);
        notifyDataSetChanged();
    }

    private void markTaskUnfinished(String taskKey, int position) {
        NodeTask nodeTaskComplete = mNodeTaskComplete.get(position);
        nodeTaskComplete.setComplete(false);
        taskActivity.adapter.addmNodeTask(nodeTaskComplete);
        WilddogAuth auth=WilddogAuth.getInstance();
        WilddogUser user=auth.getCurrentUser();
        String uid=user.getUid();
        SyncReference ref= WilddogSync
                .getInstance()
                .getReference("users/" + uid + "/lists/" + listKey + "/tasks/" + taskKey);
        ref.child("complete").setValue(false);
        mNodeTaskComplete.remove(position);
        notifyDataSetChanged();
    }
}
