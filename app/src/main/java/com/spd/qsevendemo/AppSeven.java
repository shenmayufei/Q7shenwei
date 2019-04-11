package com.spd.qsevendemo;

import android.app.Application;

import com.spd.qsevendemo.measure.utils.FileUtils;
import com.spd.qsevendemo.model.DatabaseManager;

/**
 * @author xuyan
 */
public class AppSeven extends Application {

    private static AppSeven sInstance;
    public boolean power;

    @Override
    public void onCreate() {
        super.onCreate();
        sInstance = this;
        DatabaseManager.init(this);
        initFile();
    }

    private void initFile() {
        FileUtils.getInstance().init();
    }

    public static AppSeven getInstance() {
        return sInstance;
    }

    public boolean isPower() {
        return power;
    }

    public void setPower(boolean power) {
        this.power = power;
    }

}

