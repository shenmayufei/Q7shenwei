package com.spd.qsevendemo;

import android.content.Intent;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.serialport.DeviceControlSpd;
import android.support.v7.app.AppCompatActivity;

import com.spd.qsevendemo.utils.LicenseUtil;

import java.io.IOException;

/**
 * @author xuyan
 */
public class FirstActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        initLicense();
        initDevice();
        initScanTime();
    }

    private void initLicense() {
        String path = Environment.getExternalStorageDirectory().getPath() + "/IMI";
        String name = "license";
        LicenseUtil.copy(AppSeven.getInstance(), name, path, name);
    }

    private DeviceControlSpd deviceControl;

    private void initDevice() {
        try {
            deviceControl = new DeviceControlSpd(DeviceControlSpd.POWER_NEWMAIN);
            deviceControl.newSetGpioOn(21);
            deviceControl.newSetGpioOn(57);
            deviceControl.newSetGpioOn(16);
            deviceControl.newSetGpioOn(14);
            deviceControl.newSetGpioOn(89);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void initScanTime() {

        Handler handler = new Handler();
        handler.postDelayed(() -> {
            /**
             *要执行的操作
             */
            startActivity(new Intent(FirstActivity.this, MainActivity.class));
            try {
                deviceControl.DeviceClose();
            } catch (IOException e) {
                e.printStackTrace();
            }
            finish();
        }, 3000); //3秒后执行Runnable中的run方法,否则初始化失败
    }

}
