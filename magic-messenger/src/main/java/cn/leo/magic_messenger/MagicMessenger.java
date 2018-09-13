package cn.leo.magic_messenger;

import android.app.Activity;
import android.app.Application;
import android.arch.lifecycle.LifecycleOwner;
import android.content.Intent;
import android.os.Bundle;

/**
 * @author : Jarry Leo
 * @date : 2018/9/13 9:55
 */
public class MagicMessenger {

    private static BinderClient client;
    private static Application context;

    private MagicMessenger() {
    }

    /**
     * 初始化框架,在主进程初始化
     */
    public static void init(Application application) {
        if (ProcessUtil.isMainProcess(application)) {
            context = application;
            Intent intent = new Intent(application, BinderPool.class);
            application.startService(intent);
        } else {
            client = new BinderClient();
            client.bind(application);
        }
    }

    public static void subscribe(Activity activity, MessageCallback callback) {
        if (activity instanceof LifecycleOwner) {
            LifeCycleManager lifeCycleManager = new LifeCycleManager();
            lifeCycleManager.init((LifecycleOwner) activity);
        }

        ProcessMsgCenter.subscribe(activity, callback);

    }

    public static void unsubscribe(Activity activity) {
        ProcessMsgCenter.unsubscribe(activity);
    }

    public static void post(Bundle bundle) {
        if (client != null) {
            client.sendMsg(bundle);
        } else {
            Intent intent = new Intent(context, BinderPool.class);
            intent.putExtras(bundle);
            context.startService(intent);
        }
    }

}
