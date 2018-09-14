package cn.leo.messenger;

import android.os.Bundle;

import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : Jarry Leo
 * @date : 2018/9/13 11:12
 */
class ProcessMsgCenter {
    private static ConcurrentHashMap<String, MessageCallback> subscribers = new ConcurrentHashMap<>();

    private ProcessMsgCenter() {

    }

    public static void subscribe(String key, MessageCallback callback) {
        subscribers.put(key, callback);
    }

    public static void unsubscribe(String key) {
        subscribers.remove(key);
    }

    public static void onMsgReceive(Bundle bundle) {
        String key = bundle.getString(Constant.KEY_STRING);
        if (key != null) {
            MessageCallback messageCallback = subscribers.get(key);
            if (messageCallback != null) {
                messageCallback.onMsgCallBack(bundle);
            }
        }
    }
}
