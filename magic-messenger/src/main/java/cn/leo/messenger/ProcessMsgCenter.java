package cn.leo.messenger;

import android.os.Bundle;
import android.util.Log;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;
import java.util.ListIterator;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author : Jarry Leo
 * @date : 2018/9/13 11:12
 */
class ProcessMsgCenter {
    private static ConcurrentHashMap<String, List<WeakReference<MessageCallback>>>
            subscribers = new ConcurrentHashMap<>();

    private ProcessMsgCenter() {

    }

    public static void subscribe(String key, MessageCallback callback) {
        if (subscribers.containsKey(key)) {
            List<WeakReference<MessageCallback>> list = subscribers.get(key);
            list.add(new WeakReference<>(callback));
        } else {
            List<WeakReference<MessageCallback>> list = new ArrayList<>();
            list.add(new WeakReference<>(callback));
            subscribers.put(key, list);
        }
    }

    /**
     * @deprecated
     */
    public static void unsubscribe(String key) {
    }

    public static void onMsgReceive(Bundle bundle) {
        String key = bundle.getString(Constant.KEY_STRING);
        if (key != null) {
            List<WeakReference<MessageCallback>> list = subscribers.get(key);
            if (list != null) {
                ListIterator<WeakReference<MessageCallback>> iterator = list.listIterator();
                while (iterator.hasNext()) {
                    WeakReference<MessageCallback> callback = iterator.next();
                    MessageCallback messageCallback = callback.get();
                    if (messageCallback != null) {
                        messageCallback.onMsgCallBack(bundle);
                    } else {
                        iterator.remove();
                        Log.e("清除关闭的页面", "----");
                    }
                }
            }
        }
    }
}
