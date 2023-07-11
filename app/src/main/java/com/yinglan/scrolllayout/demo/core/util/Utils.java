package com.yinglan.scrolllayout.demo.core.util;

import android.app.ActivityManager;
import android.app.Application;
import com.yinglan.scrolllayout.demo.AppVdeo;

import java.util.List;

public class Utils {

    public static boolean isAppRunningForeground() {
        ActivityManager activityManager =
                (ActivityManager) AppVdeo.getInstance().getSystemService(Application.ACTIVITY_SERVICE);
        List<ActivityManager.RunningAppProcessInfo> runningAppProcesses = activityManager.getRunningAppProcesses();
        for (ActivityManager.RunningAppProcessInfo appProcessInfo : runningAppProcesses) {
            if (appProcessInfo.importance == ActivityManager.RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                if (appProcessInfo.processName.equals(AppVdeo.getInstance().getApplicationInfo().processName))
                    return true;
            }
        }
        return false;
    }
}
