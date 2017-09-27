package cn.lltw.nodelist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.wilddog.wilddogauth.WilddogAuth;
import com.wilddog.wilddogauth.core.Task;
import com.wilddog.wilddogauth.core.listener.OnCompleteListener;
import com.wilddog.wilddogauth.core.result.AuthResult;

import cn.lltw.nodelist.error.ErrorHandler;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";

    private WilddogAuth wilddogAuth;
    private EditText email_edit;
    private EditText password_edit;
    private Button login_btn;
    private Button register_btn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //注册布局组件
        email_edit=(EditText) findViewById(R.id.email);
        password_edit=(EditText) findViewById(R.id.password);
        login_btn=(Button) findViewById(R.id.login_btn);
        register_btn=(Button)findViewById(R.id.register_btn);

        wilddogAuth =WilddogAuth.getInstance();

        //点击登陆按钮事件
        login_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(validate())
                    loginWithEmail();
            }
        });


        //点击注册按钮事件进入到RegisterActivity
        register_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this,RegisterActivity.class);
                startActivity(intent);
            }
        });
    }

    private void loginWithEmail()
    {
        String login_email=email_edit.getText().toString();
        String login_password=password_edit.getText().toString();

        wilddogAuth.signInWithEmailAndPassword(login_email,login_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(Task<AuthResult> task) {
                if(task.isSuccessful()){
                    //跳转到MainActivity
                    Toast.makeText(LoginActivity.this,"Login as:"+task.getResult().getWilddogUser().getDisplayName(),Toast.LENGTH_SHORT).show();
                    Intent intent=new Intent(LoginActivity.this,MainActivity.class);
                    startActivity(intent);
                }else{
                    //判断错误并弹出Toast提示
                    String errorCode=task.getException().toString().substring(9,14);
                    Toast.makeText(LoginActivity.this, ErrorHandler.convertErrorCode(errorCode),Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private boolean validate()
    {
        boolean validateResult=true;
        //邮箱非空验证
        if(email_edit.getText().toString().equals("")){
            email_edit.setError(email_edit.getHint()+"不能为空");
            validateResult=false;
        }
        //输入密码非空验证
        if(password_edit.getText().toString().equals("")){
            password_edit.setError(password_edit.getHint()+"不能为空");
            validateResult=false;
        }

        return validateResult;
    }
}
