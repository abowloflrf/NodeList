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

import com.wilddog.client.SyncError;
import com.wilddog.client.SyncReference;
import com.wilddog.client.WilddogSync;
import com.wilddog.wilddogauth.WilddogAuth;
import com.wilddog.wilddogauth.model.WilddogUser;

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
        final ViewHolder holder=new ViewHolder(view);
        holder.nodelistView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int position=holder.getAdapterPosition();
                NodeList list=mNodeList.get(position);
                //跳转到TaskActivity
                Intent intent = new Intent(view.getContext(), TaskActivity.class);
                Bundle bundle = new Bundle();
                bundle.putString("listName", list.getName());
                bundle.putString("listKey", list.getKey());
                intent.putExtras(bundle);
                view.getContext().startActivity(intent);
                Toast.makeText(view.getContext(), "你点击了"+list.getName(), Toast.LENGTH_SHORT).show();
            }
        });
        //长按弹出PopMenu执行删除
        holder.nodelistView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(final View view) {
                final int position=holder.getAdapterPosition();
                final NodeList list=mNodeList.get(position);
                //TODO:考虑改成使用ContextMenu
                PopupMenu popup=new PopupMenu(view.getContext(),holder.nodelistView);
                popup.inflate(R.menu.delete_list);
                popup.show();
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem menuItem) {
                        switch (menuItem.getItemId()){
                            case R.id.popup_delete_list:
                                //从远程数据库中删除清单
                                deleteList(list.getKey(),position);
                                //更新界面移除清单item
                                mNodeList.remove(position);
                                notifyDataSetChanged();
                                Toast.makeText(view.getContext(), "删除成功："+list.getKey(), Toast.LENGTH_SHORT).show();
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

    private void deleteList(String key, final int position){
        WilddogAuth auth=WilddogAuth.getInstance();
        WilddogUser user=auth.getCurrentUser();
        String uid=user.getUid();
        SyncReference ref= WilddogSync.getInstance().getReference("users/"+uid+"/lists/"+key);
        ref.removeValue();
    }
}
