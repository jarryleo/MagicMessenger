package cn.leo.magicmessenger;

import android.app.Application;

import cn.leo.magic_messenger.MagicMessenger;

/**
 * @author : Jarry Leo
 * @date : 2018/9/13 11:50
 */
public class MainApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        MagicMessenger.init(this);
    }
}
