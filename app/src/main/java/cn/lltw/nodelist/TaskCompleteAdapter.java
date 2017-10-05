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

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.lltw.nodelist.Model.NodeTask;


public class TaskCompleteAdapter extends RecyclerView.Adapter<TaskCompleteAdapter.ViewHolder> {
    private List<NodeTask> mNodeTask;

    public TaskCompleteAdapter(List<NodeTask> mNodeTask) {
        this.mNodeTask = mNodeTask;
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
                if (isChecked) {
                    Toast.makeText(view.getContext(), "选中", Toast.LENGTH_SHORT).show();
                } else {
                    Toast.makeText(view.getContext(), "取消选中", Toast.LENGTH_SHORT).show();
                }
            }
        });

        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NodeTask nodeTask=mNodeTask.get(position);
        holder.nodetaskName.setText(nodeTask.getName());
    }

    @Override
    public int getItemCount() {
        return mNodeTask.size();
    }
}
