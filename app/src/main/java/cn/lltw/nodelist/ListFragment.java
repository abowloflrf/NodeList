package cn.lltw.nodelist;


import android.os.Bundle;
import android.app.Fragment;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wilddog.client.DataSnapshot;
import com.wilddog.client.SyncError;
import com.wilddog.client.SyncReference;
import com.wilddog.client.ValueEventListener;
import com.wilddog.client.WilddogSync;
import com.wilddog.wilddogauth.WilddogAuth;
import com.wilddog.wilddogauth.model.WilddogUser;

import java.util.ArrayList;
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
    SyncReference ref;
    List<NodeList> mNodeList=new ArrayList<>();
    ListAdapter adapter;
    private static final String TAG = "ListFragment";
    public ListFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        auth=WilddogAuth.getInstance();
        user=auth.getCurrentUser();
        String uid=user.getUid();
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
                        mNodeList.add(node_list);
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
        return view;
    }

}
