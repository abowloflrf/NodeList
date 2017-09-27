package cn.lltw.nodelist;

import android.app.Application;

import com.wilddog.wilddogcore.WilddogApp;
import com.wilddog.wilddogcore.WilddogOptions;

/**
 * Created by ruofeng on 2017/9/27.
 */

public class NodeListApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //初始化SDK
        WilddogOptions options=new WilddogOptions.Builder().setSyncUrl("https://wd8779608031iwumoc.wilddogio.com").build();
        WilddogApp wilddogApp=WilddogApp.initializeApp(this,options);
    }
}
