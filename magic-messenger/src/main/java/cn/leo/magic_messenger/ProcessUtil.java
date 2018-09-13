package cn.leo.magic_messenger;

import android.annotation.TargetApi;
import android.app.ActivityManager;
import android.content.Context;
import android.os.Build;

import java.util.List;

/**
 * @author : Jarry Leo
 * @date : 2018/9/13 14:59
 */
public class ProcessUtil {
    private ProcessUtil() {
    }

    /**
     * 包名判断是否为主进程
     */
    public static boolean isMainProcess(Context context) {
        return context.getPackageName().equals(getProcessName(context));
    }

    /**
     * 获取进程名称
     *
     * @param context 上下文
     * @return 进程名
     */
    @TargetApi(Build.VERSION_CODES.CUPCAKE)
    public static String getProcessName(Context context) {
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningApps = am.getRunningAppProcesses();
        if (runningApps == null) {
            return null;
        }
        for (ActivityManager.RunningAppProcessInfo proInfo : runningApps) {
            if (proInfo.pid == android.os.Process.myPid()) {
                if (proInfo.processName != null) {
                    return proInfo.processName;
                }
            }
        }
        return null;
    }
}
