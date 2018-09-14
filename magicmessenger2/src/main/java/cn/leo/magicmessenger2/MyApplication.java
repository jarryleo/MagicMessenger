package cn.leo.magicmessenger2;

import android.app.Application;

import cn.leo.messenger.MagicMessenger;

/**
 * @author : Jarry Leo
 * @date : 2018/9/14 14:05
 */
public class MyApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        //MagicMessenger.init(this);
        MagicMessenger.bindOtherAPP(this, "cn.leo.magicmessenger");
    }
}
