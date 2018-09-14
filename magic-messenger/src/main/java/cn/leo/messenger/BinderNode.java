package cn.leo.messenger;

import android.annotation.TargetApi;
import android.app.Application;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;
import android.os.Messenger;
import android.os.RemoteException;
import android.text.TextUtils;

/**
 * @author : Jarry Leo
 * @date : 2018/9/13 9:37
 */
class BinderNode {

    private static ClientHandler handler = new ClientHandler();
    private static Messenger mMessenger = new Messenger(handler);
    private static ServiceConnection mConnection = new ClientConn();
    private static Messenger mServer;
    private static Application mContext;
    private static String mPkgName;

    /**
     * 绑定通讯池
     *
     * @param context 上下文
     */
    public void bind(Application context) {
        mContext = context;
        connect();
    }

    /**
     * 跨APP通信绑定
     *
     * @param context 上下文
     * @param pkgName app包名
     */
    public void bind(Application context, String pkgName) {
        mContext = context;
        mPkgName = pkgName;
        connect();
    }

    @TargetApi(Build.VERSION_CODES.DONUT)
    private static void connect() {
        Intent intent;
        if (TextUtils.isEmpty(mPkgName)) {
            intent = new Intent(mContext, BinderPool.class);
        } else {
            intent = new Intent(mPkgName + ".messenger");
            intent.setPackage(mPkgName);
        }
        mContext.bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    /**
     * 取消通讯池绑定
     *
     * @param context 上下文
     */
    public void unBind(Context context) {
        context.unbindService(mConnection);
    }

    /**
     * 发送消息
     *
     * @param bundle 消息内容
     */
    public void sendMsg(Bundle bundle) {
        Message msg = Message.obtain(handler, Constant.SEND_MSG_TO_TARGET);
        msg.setData(bundle);
        msg.replyTo = mMessenger;
        try {
            if (mServer != null) {
                mServer.send(msg);
            }
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    private static void reg() {
        //注册通信连接
        int key = ProcessUtil.getProcessName(mContext).hashCode();
        Message subscribeMsg = Message.obtain(handler, Constant.SUBSCRIBE, key, 0);
        try {
            subscribeMsg.replyTo = mMessenger;
            mServer.send(subscribeMsg);
        } catch (RemoteException e) {
            e.printStackTrace();
        }
    }

    static class ClientHandler extends Handler {
        @Override
        public void handleMessage(Message msg) {
            // 收到消息回调
            ProcessMsgCenter.onMsgReceive(msg.getData());
        }
    }

    static class ClientConn implements ServiceConnection {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mServer = new Messenger(service);
            reg();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mServer = null;
            connect();
        }
    }
}
