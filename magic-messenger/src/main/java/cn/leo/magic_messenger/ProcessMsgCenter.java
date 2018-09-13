package cn.leo.magic_messenger;

import android.app.Activity;
import android.os.Bundle;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : Jarry Leo
 * @date : 2018/9/13 11:12
 */
public class ProcessMsgCenter {
    private static ConcurrentHashMap<Activity, MessageCallback> subscribers = new ConcurrentHashMap<>();

    private ProcessMsgCenter() {

    }

    public static void subscribe(Activity activity, MessageCallback callback) {
        subscribers.put(activity, callback);
    }

    public static void unsubscribe(Activity activity) {
        subscribers.remove(activity);
    }

    public static void onMsgReceive(Bundle bundle) {
        for (MessageCallback callback : subscribers.values()) {
            callback.onMsgCallBack(bundle);
        }
    }
}
