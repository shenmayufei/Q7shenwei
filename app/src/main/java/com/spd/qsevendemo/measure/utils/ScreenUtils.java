package com.spd.qsevendemo.measure.utils;

import android.app.Activity;
import android.graphics.Point;
import android.view.View;
import android.view.WindowManager;

import java.lang.ref.WeakReference;

/**
 * 作者:jtl
 * 日期:Created in 2018/11/16 23:12
 * 描述:
 * 更改:
 */

public class ScreenUtils {
private ScreenUtils(){}
    public static ScreenUtils getInstance() {
        return ScreenUtilsHolder.sScreenUtils;
    }

    private static class ScreenUtilsHolder {
        private static ScreenUtils sScreenUtils = new ScreenUtils();
    }

    public static void setFullScreenOnWindowFocusChanged(Activity activity, boolean hasFocus) {
        if (hasFocus) {
            activity
                    .getWindow()
                    .getDecorView()
                    .setSystemUiVisibility(
                            View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                                    | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                    | View.SYSTEM_UI_FLAG_FULLSCREEN
                                    | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        }
    }

    public Point getScreenPoint(Activity activity){
        WeakReference<Activity> weakReference=new WeakReference<Activity>(activity);
        Point mScreenPoint=new Point();
        weakReference.get().getWindowManager().getDefaultDisplay().getSize(mScreenPoint);
        return mScreenPoint;
    }
}
