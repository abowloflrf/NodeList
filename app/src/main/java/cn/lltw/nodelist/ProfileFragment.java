package cn.lltw.nodelist;


import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.EditTextPreference;
import android.preference.ListPreference;
import android.preference.PreferenceFragment;

import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.wilddog.wilddogauth.WilddogAuth;
import com.wilddog.wilddogauth.core.Task;
import com.wilddog.wilddogauth.core.listener.OnCompleteListener;
import com.wilddog.wilddogauth.core.request.UserProfileChangeRequest;
import com.wilddog.wilddogauth.model.WilddogUser;

import cn.lltw.nodelist.error.ErrorHandler;


public class ProfileFragment extends PreferenceFragment implements SharedPreferences.OnSharedPreferenceChangeListener{

    private static final String TAG = "ProfileFragment";

    private ListPreference avatar;
    private EditTextPreference username;
    private EditTextPreference email;
    private EditTextPreference password;

    WilddogAuth auth;
    WilddogUser user;

    public ProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        addPreferencesFromResource(R.xml.profile);

        avatar=(ListPreference)getPreferenceScreen().findPreference("profile_avatar");
        username=(EditTextPreference)getPreferenceScreen().findPreference("profile_username");
        email=(EditTextPreference)getPreferenceScreen().findPreference("profile_email");
        password=(EditTextPreference)getPreferenceScreen().findPreference("profile_password");

    }

        @Override
        public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {

            //TODO:更改 用户名/密码/头像 需要刷新DrawerHeader上的用户名/邮箱，或者强制刷新Activity,或强制注销
            if(s.equals("profile_username")){
                UserProfileChangeRequest profileUpdates=new UserProfileChangeRequest.Builder()
                        .setDisplayName(username.getText())
                        .build();
                user.updateProfile(profileUpdates).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), "已修改用户名为："+username.getText(), Toast.LENGTH_SHORT).show();
                            username.setSummary(username.getText());
                        }else{
                            String errorCode=task.getException().toString().substring(9,14);
                            Toast.makeText(getActivity(),ErrorHandler.convertErrorCode(errorCode), Toast.LENGTH_SHORT).show();
                        }

                    }
                });
            }else if(s.equals("profile_email")){
                //更改邮箱
                user.updateEmail(email.getText()).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(Task<Void> task) {
                        if(task.isSuccessful()){
                            Toast.makeText(getActivity(), "已修改登陆邮箱为："+email.getText(), Toast.LENGTH_SHORT).show();
                            email.setSummary(email.getText());
                        }else{
                            String errorCode=task.getException().toString().substring(9,14);
                            Toast.makeText(getActivity(), ErrorHandler.convertErrorCode(errorCode), Toast.LENGTH_SHORT).show();
                        }
                    }
                });
            }else if(s.equals("profile_avatar")){
                //更改头像，先留着不要这个功能
                avatar.setSummary(avatar.getEntry());
            }else if(s.equals("profile_password")){
                //更改密码，需要强制注销
                //先留着不要这个用能
                //或者可以改成发送修改密码邮件
            }
        }
    @Override
    public void onPause() {
        super.onPause();
        getPreferenceScreen().getSharedPreferences().unregisterOnSharedPreferenceChangeListener(this);
    }

    @Override
    public void onResume() {
        super.onResume();
        getPreferenceScreen().getSharedPreferences().registerOnSharedPreferenceChangeListener(this);
        //TODO:个人信息修改这儿有一个bug，成功修改信息之后本地SharedPreference与Wilddog.user获取到的用户名都是修改后的。但是退出应用再打开就显示为修改之前的
        auth=WilddogAuth.getInstance();
        user=auth.getCurrentUser();
        if (user != null) {
            Log.d(TAG, "onResume before: "+user.getDisplayName()+" email"+username.getText());
            username.setText(user.getDisplayName());
            username.setSummary(username.getText());
            email.setText(user.getEmail());
            email.setSummary(email.getText());
            Log.d(TAG, "onResume after:"+user.getDisplayName()+" email:"+username.getText());
            //TODO:头像，先不管
            avatar.setSummary(avatar.getEntry());
        } else {
            Toast.makeText(getActivity(), "请登陆！", Toast.LENGTH_SHORT).show();
            Intent intent =new Intent(getContext(),LoginActivity.class);
            startActivity(intent);
        }
    }
}
