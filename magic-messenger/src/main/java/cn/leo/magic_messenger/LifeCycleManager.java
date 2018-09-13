package cn.leo.magic_messenger;

import android.app.Activity;
import android.arch.lifecycle.Lifecycle;
import android.arch.lifecycle.LifecycleObserver;
import android.arch.lifecycle.LifecycleOwner;
import android.arch.lifecycle.OnLifecycleEvent;

/**
 * @author : Jarry Leo
 * @date : 2018/9/13 9:54
 */
public class LifeCycleManager implements LifecycleObserver {

    public void init(LifecycleOwner lifecycleOwner) {
        lifecycleOwner.getLifecycle().addObserver(this);

    }

    @OnLifecycleEvent(Lifecycle.Event.ON_DESTROY)
    private void onDestroy(LifecycleOwner owner) {
        if (owner instanceof Activity) {
            ProcessMsgCenter.unsubscribe((Activity) owner);
        }
    }

}
