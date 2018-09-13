package cn.leo.messenger;

import android.os.Bundle;

/**
 * @author : Jarry Leo
 * @date : 2018/9/13 10:21
 */
public interface MessageCallback {
    /**
     * 接收消息回调
     *
     * @param data 消息内容
     */
    void onMsgCallBack(Bundle data);
}
