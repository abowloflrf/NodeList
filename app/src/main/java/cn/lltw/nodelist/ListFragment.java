package cn.lltw.nodelist;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Toast;

import com.wilddog.client.DataSnapshot;
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

import cn.lltw.nodelist.Model.NodeList;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListFragment extends Fragment {

    View view;
    WilddogAuth auth;
    WilddogUser user;
    String uid;
    SyncReference ref;
    List<NodeList> mNodeList=new ArrayList<>();
    ListAdapter adapter;
    FloatingActionButton fab;
    private static final String TAG = "ListFragment";
    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=WilddogAuth.getInstance();
        user=auth.getCurrentUser();
        uid=user.getUid();
        ref= WilddogSync.getInstance().getReference("users/"+uid+"/lists");
        ref.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                if(dataSnapshot.getValue()!=null){
                    Iterator iter=dataSnapshot.getChildren().iterator();
                    while (iter.hasNext()){
                        DataSnapshot data=(DataSnapshot) iter.next();
                        String list_name=(String)data.child("name").getValue();
                        String list_describe=(String)data.child("describe").getValue();
                        NodeList node_list=new NodeList(list_name,list_describe);
                        mNodeList.add(0,node_list);
                        Log.d(TAG, "onDataChange: "+node_list.getName());
                    }
                    adapter.notifyDataSetChanged();

                }

            }

            @Override
            public void onCancelled(SyncError syncError) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        view = inflater.inflate(R.layout.fragment_list, container, false);
        RecyclerView recyclerView=(RecyclerView)view.findViewById(R.id.list_recycler_view);
        LinearLayoutManager layoutManager=new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(layoutManager);
        adapter=new ListAdapter(mNodeList);
        recyclerView.setAdapter(adapter);
        fab=(FloatingActionButton)view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showCreateNewListDialog();
            }
        });

        return view;
    }

    public void showCreateNewListDialog(){
        AlertDialog.Builder builder= new AlertDialog.Builder(getActivity());
        LayoutInflater inflater=getActivity().getLayoutInflater();
        View view=inflater.inflate(R.layout.new_list_dialog,null);
        final EditText list_name=(EditText)view.findViewById(R.id.dialog_list_name);
        final EditText list_describe=(EditText)view.findViewById(R.id.dialog_list_describe);
        builder.setTitle("创建清单")
                .setView(view)
                .setPositiveButton("创建", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        createList(list_name.getText().toString(),list_describe.getText().toString());
                    }
                })
                .setNegativeButton("取消",null)
                .show();
    }

    public void createList(String name, final String des){
        //根据新建参数构造一个NodeList对象
        final NodeList nodelist=new NodeList(name,des);
        HashMap<String,Object> list=new HashMap<>();
        list.put("name",name);
        list.put("describe",des);
        //TODO:这里是使用Wilddog提供的push方法，默认新建一个节点时自动生成了一个key，不知道这样好不好操作，因为后面删除list时依据传入什么还不知道若要使用这个自动生成的key需要重写一些Nodelist对象构造的时候要传入这个key
        ref.push().setValue(list, new SyncReference.CompletionListener() {
            @Override
            public void onComplete(SyncError syncError, SyncReference syncReference) {
                if (syncError!=null){
                    Toast.makeText(getContext(), "Failed:"+syncError.getErrCode(), Toast.LENGTH_SHORT).show();
                }else{
                    //将新建的NodeList对象添加到列表中并刷新recyclerview
                    mNodeList.add(0,nodelist);
                    adapter.notifyDataSetChanged();
                }
            }
        });
    }

}
