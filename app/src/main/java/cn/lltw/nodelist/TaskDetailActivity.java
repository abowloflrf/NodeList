package cn.lltw.nodelist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;

import com.wilddog.client.DataSnapshot;
import com.wilddog.client.SyncError;
import com.wilddog.client.SyncReference;
import com.wilddog.client.ValueEventListener;
import com.wilddog.client.WilddogSync;
import com.wilddog.wilddogauth.WilddogAuth;
import com.wilddog.wilddogauth.model.WilddogUser;

import java.util.Iterator;
import java.util.List;

public class TaskDetailActivity extends AppCompatActivity {

    private String taskKey;
    WilddogAuth auth;
    WilddogUser user;
    String listKey;
    String uid;
    SyncReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_task_detail);

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String taskTitle = bundle.getString("taskName");
        taskKey = bundle.getString("taskKey");
        // 改变Title为清单名称
        this.setTitle((CharSequence) taskTitle);

        /*auth = WilddogAuth.getInstance();
        user = auth.getCurrentUser();
        uid = user.getUid();
        ref = WilddogSync.getInstance().getReference("users/" + uid + "/lists/" + listKey + "/tasks/" + taskKey);*/

        TextView detail = (TextView) findViewById(R.id.task_detail_test_text);
        detail.setText(taskTitle);
    }
}
