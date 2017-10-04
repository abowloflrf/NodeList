package cn.lltw.nodelist;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;
import android.widget.Toast;

import java.util.List;

import cn.lltw.nodelist.Model.NodeList;

/**
 * Created by ruofeng on 2017/10/1.
 */

public class ListAdapter extends RecyclerView.Adapter<ListAdapter.ViewHolder> {
    private List<NodeList> mNodeList;
    private Context mContext;
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
    public ListAdapter(Context context, List<NodeList> nodeList){
        mContext = context;
        mNodeList=nodeList;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view= LayoutInflater.from(parent.getContext())
                .inflate(R.layout.list_card,parent,false);
        final ViewHolder holder=new ViewHolder(view);
        holder.nodelistView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                NodeList list=mNodeList.get(position);
                //跳转到TaskActivity
                Intent intent = new Intent(mContext, TaskActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("listName", list.getName());
                bundle.putInt("listID", position);
                intent.putExtras(bundle);
                mContext.startActivity(intent);
                Toast.makeText(view.getContext(), "你点击了"+list.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        //长按弹出PopMenu执行删除
        holder.nodelistView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                int position=holder.getAdapterPosition();
                final NodeList list=mNodeList.get(position);
                PopupMenu popup=new PopupMenu(view.getContext(),holder.nodelistView);
                popup.inflate(R.menu.delete_list);
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.popup_delete_list:
                                //TODO:完成deleteList()
                                Toast.makeText(view.getContext(), "删除："+list.getName(), Toast.LENGTH_SHORT).show();
                                break;
                            case R.id.popup_share_list:
                                //TODO:完成shareList()
                                Toast.makeText(view.getContext(), "分享："+list.getName(), Toast.LENGTH_SHORT).show();
                        }
                        return true;
                    }
                });
                return true;
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
