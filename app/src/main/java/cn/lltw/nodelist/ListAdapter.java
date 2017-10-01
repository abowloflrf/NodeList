package cn.lltw.nodelist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.lltw.nodelist.Model.NodeList;

/**
 * Created by ruofeng on 2017/10/1.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<NodeList> mNodeList;
    static class ViewHolder extends RecyclerView.ViewHolder{
        View nodelistView;
        TextView nodelistName;
        TextView nodelistDescribe;
        public ViewHolder(View view){
            super(view);
            nodelistView=view;
            nodelistName=(TextView)view.findViewById(R.id.list_card_name);
            nodelistDescribe=(TextView)view.findViewById(R.id.list_card_describe);
        }
    }
    public ListAdapter(List<NodeList> nodeList){
        mNodeList=nodeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_card,parent,false);
        ViewHolder holder=new ViewHolder(view);
        holder.nodelistView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(view.getContext(), "你点击了", Toast.LENGTH_SHORT).show();
            }
        });
        return holder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NodeList nodeList=mNodeList.get(position);
        holder.nodelistName.setText(nodeList.getName());
        holder.nodelistDescribe.setText(nodeList.getDescribe());
    }

    @Override
    public int getItemCount() {
        return mNodeList.size();
    }
}
