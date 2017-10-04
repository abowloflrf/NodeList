package cn.lltw.nodelist;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wilddog.wilddogauth.WilddogAuth;
import com.wilddog.wilddogauth.core.Task;
import com.wilddog.wilddogauth.core.listener.OnCompleteListener;
import com.wilddog.wilddogauth.core.request.UserProfileChangeRequest;
import com.wilddog.wilddogauth.core.result.AuthResult;
import com.wilddog.wilddogauth.model.WilddogUser;

import cn.lltw.nodelist.error.ErrorHandler;

public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    private EditText register_email_edit;
    private EditText register_password_edit;
    private EditText register_password_confirm_edit;
    private EditText register_display_name_edit;
    private Button register_btn;

    private WilddogAuth wilddogAuth;
    private WilddogUser wilddogUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //获取Wilddog实例
        wilddogAuth=WilddogAuth.getInstance();

        register_email_edit=(EditText)findViewById(R.id.reg_email);
        register_password_edit=(EditText)findViewById(R.id.reg_password);
        register_password_confirm_edit=(EditText)findViewById(R.id.reg_confirm_password);
        register_display_name_edit=(EditText)findViewById(R.id.reg_display_name);
        register_btn=(Button)findViewById(R.id.register_btn);

        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate())
                    registerWithEmail();
            }
        });
    }

    private void registerWithEmail()
    {
        String email=register_email_edit.getText().toString();
        String password=register_password_edit.getText().toString();
        final String display_name=register_display_name_edit.getText().toString();

        wilddogAuth.createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(Task<AuthResult> task) {
                        if(task.isSuccessful()){
                            //更新显示昵称并跳转到MainActivity
                            WilddogUser user = task.getResult().getWilddogUser();
                            UserProfileChangeRequest profileChangeRequest=new UserProfileChangeRequest.Builder()
                                    .setDisplayName(display_name).build();
                            user.updateProfile(profileChangeRequest);

                            //写入用户信息到SharedPreference
                            SharedPreferences.Editor editor=getSharedPreferences("cn.lltw.nodelist_preferences",MODE_PRIVATE).edit();
                            editor.putString("profile_username",display_name);
                            editor.putString("profile_email",user.getEmail());
                            editor.apply();

                            Toast.makeText(RegisterActivity.this,"Login as:"+display_name,Toast.LENGTH_SHORT).show();
                            backToMain();
                        }else{
                            //这里的Exception不需要抛出
                            String errorCode=task.getException().toString().substring(9,14);
                            Toast.makeText(RegisterActivity.this, ErrorHandler.convertErrorCode(errorCode),Toast.LENGTH_SHORT).show();
                        }
                    }
                });

    }

    private void backToMain(){
        Intent intent =new Intent(RegisterActivity.this,MainActivity.class);
        startActivity(intent);
    }

    private boolean validate(){
        boolean validateResult=true;
        //非空验证
        if(register_email_edit.getText().toString().equals("")){
            register_email_edit.setError(register_email_edit.getHint()+"不能为空");
            validateResult=false;
        }
        if(register_display_name_edit.getText().toString().equals("")){
            register_display_name_edit.setError(register_display_name_edit.getHint()+"不能为空");
            validateResult=false;
        }
        if(register_password_edit.getText().toString().equals("")){
            register_password_edit.setError(register_password_edit.getHint()+"不能为空");
            validateResult=false;
        }
        if(register_password_confirm_edit.getText().toString().equals("")){
            register_password_confirm_edit.setError(register_password_confirm_edit.getHint()+"不能为空");
            validateResult=false;
        }
        //重复输入密码匹配验证
        if(!register_password_edit.getText().toString().equals(register_password_confirm_edit.getText().toString())){
            register_password_confirm_edit.setError("确认密码不匹配");
            validateResult=false;
        }
        return validateResult;

    }
}
