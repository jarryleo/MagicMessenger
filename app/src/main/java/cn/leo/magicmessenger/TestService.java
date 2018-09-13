package cn.leo.magicmessenger;

import android.app.Service;
import android.content.Intent;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import cn.leo.messenger.MagicMessenger;
import cn.leo.messenger.MessageCallback;

/**
 * @author : Jarry Leo
 * @date : 2018/9/13 17:03
 */
public class TestService extends Service {
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        MagicMessenger.subscribe("service", new MessageCallback() {
            @Override
            public void onMsgCallBack(Bundle data) {
                String test = data.getString("test");
                Toast.makeText(TestService.this, test, Toast.LENGTH_SHORT).show();
            }
        });
        Log.e("service", "onCreate: " );
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        MagicMessenger.unsubscribe("service");
        Log.e("service", "onDestroy: " );
    }
}
