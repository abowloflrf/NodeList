package cn.lltw.nodelist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import cn.lltw.nodelist.Model.NodeTask;

/**
 * Created by watso on 2017/10/4.
 */

public class TaskAdapter extends RecyclerView.Adapter<TaskAdapter.ViewHolder> {
    private List<NodeTask> mNodeTask;

    public TaskAdapter(List<NodeTask> mNodeTask) {
        this.mNodeTask = mNodeTask;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        View nodetaskView;
        TextView nodetaskName;
        TextView nodetaskRepeat;
        TextView nodetaskDueTime;
        public ViewHolder(View view) {
            super(view);
            nodetaskView = view;
            nodetaskName = (TextView) view.findViewById(R.id.task_card_name);
            nodetaskRepeat = (TextView) view.findViewById(R.id.task_card_repeat);
            nodetaskDueTime = (TextView) view.findViewById(R.id.task_card_due_time);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        ViewHolder holder = new ViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.task_card, parent, false));

        holder.nodetaskView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NodeTask nodeTask=mNodeTask.get(position);
        holder.nodetaskName.setText(nodeTask.getName());
        holder.nodetaskRepeat.setText(nodeTask.getRepeat());
        holder.nodetaskDueTime.setText(nodeTask.getDueTime());
    }

    @Override
    public int getItemCount() {
        return mNodeTask.size();
    }
}
