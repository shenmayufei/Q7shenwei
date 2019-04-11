package com.spd.qsevendemo.measure.helper;

import android.content.Context;

import com.imi.sdk.base.CameraIntrinsics;
import com.imi.sdk.volume.BoxSize;
import com.imi.sdk.volume.Plane;
import com.imi.sdk.volume.VolumeConfig;
import com.imi.sdk.volume.VolumeSession;
import com.spd.qsevendemo.measure.utils.FileUtils;

import java.nio.ByteBuffer;

/**
 * 作者:jtl
 * 日期:Created in 2018/9/4 19:46
 * 描述:测量帮助类
 * 更改:
 */

public class MeasureHelper {
    private Context mContext;
    private String path;
    private CameraIntrinsics parameters;
    private long pDevice;
    private VolumeSession calculator; //体积测量接口
    public MeasureHelper(Context context, CameraIntrinsics parameters, long pDevice){
        //license文件放在的文件夹
        path = FileUtils.getInstance().getSDCardFolderPath()+"/license";
        this.pDevice=pDevice;
        this.mContext=context;
        this.parameters=parameters;
        initialize();
    }

    /**
     * 初始化
     * @return
     */
    private boolean initialize(){
        try {
            calculator = new VolumeSession(mContext);
            VolumeConfig config = new VolumeConfig();
            config.licenseParam = new VolumeConfig.LicenseParam();
            config.licenseParam.deviceHandle = pDevice;
            config.licenseParam.version ="";
            config.licenseParam.licensePath =path;
            config.cameraIntrinsics=parameters;
            calculator.setConfig(config);
           return calculator.initialize();
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    /**
     * 计算盒子
     * @param width
     * @param height
     * @param bufferRgb
     * @param bufferDepth
     * @param plane
     * @return
     */
    public BoxSize calculatorBox(int width, int height, ByteBuffer bufferRgb, ByteBuffer bufferDepth, Plane plane){
       return calculator.boxVolume(width,height,bufferRgb,bufferDepth,plane);
    }

    public BoxSize calculatorBox(ByteBuffer bufferRgb, ByteBuffer bufferDepth, Plane plane){
        return calculator.boxVolume(640,480,bufferRgb,bufferDepth,plane);
    }



    /**
     * 计算平面
     * @param width
     * @param height
     * @param depthData
     * @return
     */
    public Plane calculatorPlane(int width, int height, ByteBuffer depthData){
       return calculator.getPlane(width,height,depthData);
    }
}
