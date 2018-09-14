package cn.leo.messenger;

import android.os.Bundle;

import java.lang.ref.WeakReference;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : Jarry Leo
 * @date : 2018/9/13 11:12
 */
class ProcessMsgCenter {
    private static ConcurrentHashMap<String, WeakReference<MessageCallback>> subscribers = new ConcurrentHashMap<>();

    private ProcessMsgCenter() {

    }

    public static void subscribe(String key, MessageCallback callback) {
        WeakReference<MessageCallback> cb = new WeakReference<>(callback);
        subscribers.put(key, cb);
    }

    public static void unsubscribe(String key) {
        subscribers.remove(key);
    }

    public static void onMsgReceive(Bundle bundle) {
        String key = bundle.getString(Constant.KEY_STRING);
        if (key != null) {
            WeakReference<MessageCallback> callbackWeakReference = subscribers.get(key);
            if (callbackWeakReference != null) {
                MessageCallback messageCallback = callbackWeakReference.get();
                if (messageCallback != null) {
                    messageCallback.onMsgCallBack(bundle);
                } else {
                    subscribers.remove(key);
                }
            }
        }
    }
}
