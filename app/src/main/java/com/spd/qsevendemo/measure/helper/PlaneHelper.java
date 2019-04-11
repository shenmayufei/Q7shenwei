package com.spd.qsevendemo.measure.helper;

import android.content.Context;

import com.imi.sdk.volume.Plane;
import com.spd.qsevendemo.measure.Constants;

import java.nio.ByteBuffer;

/**
 * 作者:jtl
 * 日期:Created in 2019/3/1 18:11
 * 描述:平面辅助类
 * 更改:
 */
public class PlaneHelper {
    public static final String planeHeight = "PLANE_HEIGHT";
    public static final String planeA = "PLANE_A";
    public static final String planeB = "PLANE_B";
    public static final String planeC = "PLANE_C";
    public static final String isPlane = "IS_PLANE";
    private static Context mContext;
    private Plane mPlane;

    private PlaneHelper() {
        initPlane();
    }

    /**
     * InitPlane
     */
    public void initPlane() {
        if (mPlane == null && SharedPreferenceHelper.getInstance(mContext, isPlane).getBoolean()) {
            float a = SharedPreferenceHelper.getInstance(mContext, planeA).getFloat();
            float b = SharedPreferenceHelper.getInstance(mContext, planeB).getFloat();
            float c = SharedPreferenceHelper.getInstance(mContext, planeC).getFloat();
            float h = SharedPreferenceHelper.getInstance(mContext, planeHeight).getFloat();
            mPlane = new Plane(a, b, c, h);
        }
    }

    public Plane getPlane() {
        return mPlane;
    }

    /**
     * 设置Plane保存本地
     *
     * @param plane
     */
    public void setPlane(Plane plane) {
        this.mPlane = plane;
        double a = plane.getEle()[0];
        double b = plane.getEle()[1];
        double c = plane.getEle()[2];
        double h = plane.getEle()[3];
        SharedPreferenceHelper.getInstance(mContext, PlaneHelper.planeA).putFloat((float) a);
        SharedPreferenceHelper.getInstance(mContext, PlaneHelper.planeB).putFloat((float) b);
        SharedPreferenceHelper.getInstance(mContext, PlaneHelper.planeC).putFloat((float) c);
        SharedPreferenceHelper.getInstance(mContext, PlaneHelper.planeHeight).putFloat((float) h);
        SharedPreferenceHelper.getInstance(mContext, PlaneHelper.isPlane).putBoolean(true);
    }

    /**
     * 计算平面
     *
     * @param measureHelper
     * @param byteBuffer
     */
    public Plane calPlane(MeasureHelper measureHelper, ByteBuffer byteBuffer) {
        return this.calPlane(measureHelper, Constants.WIDTH, Constants.HEIGHT, byteBuffer);
    }

    /**
     * 计算平面
     *
     * @param measureHelper
     * @param byteBuffer
     */
    public Plane calPlane(MeasureHelper measureHelper, ByteBuffer byteBuffer, boolean isSavePlane) {
        return this.calPlane(measureHelper, Constants.WIDTH, Constants.HEIGHT, byteBuffer, isSavePlane);
    }

    /**
     * 计算平面
     *
     * @param measureHelper
     * @param width
     * @param height
     * @param byteBuffer
     */
    public Plane calPlane(MeasureHelper measureHelper, int width, int height, ByteBuffer byteBuffer) {
        return this.calPlane(measureHelper, width, height, byteBuffer, false);
    }


    /**
     * 计算平面
     *
     * @param measureHelper
     * @param width
     * @param height
     * @param byteBuffer
     * @param isSavePlane
     */
    public Plane calPlane(MeasureHelper measureHelper, int width, int height, ByteBuffer byteBuffer, boolean isSavePlane) {
        Plane plane = measureHelper.calculatorPlane(width, height, byteBuffer);
        if (isSavePlane) {
            setPlane(plane);
        }

        return plane;
    }

    public static PlaneHelper getInstance(Context context) {
        mContext=context;
        return PlaneHelperHolder.planeHelper;
    }

    private static class PlaneHelperHolder {
        private static final PlaneHelper planeHelper = new PlaneHelper();
    }
}
