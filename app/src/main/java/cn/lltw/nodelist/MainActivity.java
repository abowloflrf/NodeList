package cn.lltw.nodelist;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.wilddog.wilddogauth.WilddogAuth;
import com.wilddog.wilddogauth.model.WilddogUser;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener{

    private static final String TAG = "MainActivity";
    WilddogAuth auth;
    WilddogUser user;
    ImageView avatar_view;
    TextView nav_email;
    TextView nav_username;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);


        //顶部工具栏
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //首页浮动按钮点击事件，用户添加新的清单
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);


        //实例化Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_login_view);
        navigationView.setNavigationItemSelectedListener(this);
        //默认选择第一项
        if(savedInstanceState==null){
            navigationView.setCheckedItem(R.id.nav_list);
            getSupportFragmentManager().beginTransaction().replace(R.id.main_content,new ListFragment()).commit();
        }
        //获取导航头部
        View navigationHeaderView=navigationView.getHeaderView(0);

        Menu menu=navigationView.getMenu();



        avatar_view = (ImageView)navigationHeaderView.findViewById(R.id.nav_header_avatar);
        nav_email=(TextView)navigationHeaderView.findViewById(R.id.nav_header_email);
        nav_username=(TextView)navigationHeaderView.findViewById(R.id.nav_header_username);

        auth=WilddogAuth.getInstance();
        user=auth.getCurrentUser();
        //判断是否登陆
        if(user!=null){
            initialLoginView();
            //移除logout按钮

        }else{
            //判断为没有登陆
            //TODO:修改为case写法点击头像图片以及下面的TextView都跳转到LoginActivity
            avatar_view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(MainActivity.this,LoginActivity.class);
                    startActivity(intent);
                }
            });
            menu.removeGroup(R.id.nav_account_group);
            //为点击FAB添加事件
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Snackbar.make(view, "请先登陆！", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();
                }
            });
        }



    }

    //点击返回关闭Drawer
    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    //点击右上角三个点
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    //更多选项展开后点击事件
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    //点击Drawer上的list
    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        Fragment fragment=null;
        Class fragmentClass=null;

        if (id == R.id.nav_list) {
            fragmentClass=ListFragment.class;
        } else if (id == R.id.nav_ground) {
            fragmentClass=GroundFragment.class;
        } else if (id == R.id.nav_graph) {
            fragmentClass=GraphFragment.class;
        } else if (id == R.id.nav_profile) {
            fragmentClass=ProfileFragment.class;
        } else if (id == R.id.nav_setting) {
            fragmentClass=SettingFragment.class;
        } else if (id == R.id.nav_logout) {
            logout();
            fragmentClass=ListFragment.class;
        }else{
            fragmentClass=ListFragment.class;
        }

        try{
            fragment=(Fragment)fragmentClass.newInstance();
        }catch (Exception e){
            e.printStackTrace();
        }
        setTitle(item.getTitle());
        FragmentManager fragmentManager=getSupportFragmentManager();
        fragmentManager.beginTransaction().replace(R.id.main_content,fragment).commit();
        //点击之后关闭Drawer
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void initialLoginView(){
        //判断为已经登陆则重写一些界面
        String username=user.getDisplayName();
        String email=user.getEmail();
        String uid=user.getUid();
        nav_username.setText(username);
        nav_email.setText(email);


    }

    private void logout()
    {
        //注销并回到登陆界面
        auth.signOut();
        Intent intent = new Intent(MainActivity.this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
    }
}
