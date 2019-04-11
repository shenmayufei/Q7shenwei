package com.spd.qsevendemo.measure;

import android.content.Context;
import android.support.annotation.IntDef;
import android.support.annotation.NonNull;

import com.hjimi.api.iminect.ImiDevice;
import com.hjimi.api.iminect.Utils;
import com.imi.sdk.volume.BoxSize;
import com.imi.sdk.volume.Plane;
import com.socks.library.KLog;
import com.spd.qsevendemo.measure.helper.MeasureHelper;
import com.spd.qsevendemo.measure.helper.PlaneHelper;
import com.spd.qsevendemo.measure.view.GLPanel;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.nio.ByteBuffer;

/**
 * 作者:jtl
 * 日期:Created in 2018/9/4 18:20
 * 描述:获取彩色图深度图子线程
 * 更改:
 */

public class CalThread extends Thread {
    private GLPanel mGLCal;
    private GLPanel mGLResult;
    private boolean isRun;
    private ByteBuffer mRgbBuffer;
    private ByteBuffer mDepthBuffer;
    private ImiDevice mImiDevice;
    private boolean isInitSuccess = false;
    private MeasureListener<BoxSize> mMeasureListener;
    private Plane mPlane;

    @IntDef({DATATYPE.RGB, DATATYPE.DEPTH})
    @Retention(RetentionPolicy.SOURCE)
    public @interface DATATYPE {
        int RGB=0;
        int DEPTH=1;
    }

    private int dataType = DATATYPE.RGB;
    private MeasureHelper mMeasureHelper;
    private BoxSize mBoxSize;
    private int i=0;
    private Context mContext;

    public CalThread(Context context,ImiDevice imiDevice, @NonNull MeasureHelper measureHelper) {
        this.mContext=context;
        this.mMeasureHelper = measureHelper;
        mImiDevice = imiDevice;
        mRgbBuffer = ByteBuffer.allocateDirect(640 * 480 * 3);
        mDepthBuffer = ByteBuffer.allocateDirect(640 * 480 * 2);

        mImiDevice.startStream(ImiDevice.ImiStreamType.DEPTH.toNative() | ImiDevice.ImiStreamType.COLOR.toNative());
        mPlane = PlaneHelper.getInstance(mContext).getPlane();
    }

    @Override
    public void run() {
        super.run();
        while (isRun) {
            ImiDevice.ImiFrame colorFrame = mImiDevice.readNextFrame(ImiDevice.ImiStreamType.COLOR, 30);
            ImiDevice.ImiFrame depthFrame = mImiDevice.readNextFrame(ImiDevice.ImiStreamType.DEPTH, 30);
            if (colorFrame != null) {
                if (dataType == DATATYPE.RGB) {
                    drawColor(colorFrame.getData(), mGLCal);
                }
                mRgbBuffer = colorFrame.getData();
            }

            if (depthFrame != null) {
                if (dataType == DATATYPE.DEPTH) {
                    drawDepth(depthFrame, mGLCal);
                }
                mDepthBuffer = depthFrame.getData();
            }
            mPlane=PlaneHelper.getInstance(mContext).getPlane();
            if (mPlane == null) {
                mPlane = PlaneHelper.getInstance(mContext).calPlane(mMeasureHelper, mDepthBuffer, true);
            }else{
                try {
                    mBoxSize = mMeasureHelper.calculatorBox(mRgbBuffer, mDepthBuffer, mPlane);
                }
                catch (Exception e){
                    KLog.e("CalThread:Exception:"+e.toString());
                }

                if (mBoxSize != null && mMeasureListener != null) {
                    mMeasureListener.upDateListener(mBoxSize);
                }
            }

            if (!isInitSuccess) {
                isInitSuccess = true;
            }
        }
    }

    /**
     * 渲染彩色图
     *
     * @param buffer
     * @param glPanel
     */
    private void drawColor(ByteBuffer buffer, GLPanel glPanel) {
        if (glPanel != null) {
            glPanel.paint(null, buffer, 640, 480);
        }
    }

    /**
     * 渲染深度图
     *
     * @param nextFrame
     * @param glPanel
     */
    private void drawDepth(ImiDevice.ImiFrame nextFrame, GLPanel glPanel) {
        ByteBuffer frameData = nextFrame.getData();
        int width = nextFrame.getWidth();
        int height = nextFrame.getHeight();

        frameData = Utils.depth2RGB888(nextFrame, true, false);
        glPanel.paint(null, frameData, width, height);
    }

    public void onStart() {
        if (!isRun) {
            isRun = true;

            //start read thread
            this.start();
        }
    }

    public void onResume() {
        if (mGLCal != null) {
            mGLCal.onResume();
        }

        if (!isRun) {
            isRun = true;
        }
    }

    public void onPause() {
        if (mGLCal != null) {
            mGLCal.onPause();
        }

        isRun = false;
    }

    public void onDestroy() {
        isRun = false;
        if (mGLCal != null) {
            mGLCal.onPause();
        }
    }

    public GLPanel getGLResult() {
        return mGLResult;
    }

    public void setGLResult(GLPanel GLResult) {
        mGLResult = GLResult;
    }

    /**
     * 设置Listener
     *
     * @param measureListener
     */
    public void setMeasureListener(MeasureListener<BoxSize> measureListener) {
        mMeasureListener = measureListener;
    }

    /**
     * 设置GLPanel(自己封装的GLSurface类)
     *
     * @param glPanel
     */
    public void setGLCal(@NonNull GLPanel glPanel) {
        mGLCal = glPanel;
    }

    /**
     * 获取彩色图MAT
     *
     * @return
     */
    public ByteBuffer getRGBBuffer() {
        synchronized (mRgbBuffer) {
            return mRgbBuffer;
        }
    }

    /**
     * 获取深度图MAT
     *
     * @return
     */
    public ByteBuffer getDepthBuffer() {
        synchronized (mDepthBuffer) {
            return mDepthBuffer;
        }
    }

    /**
     * 设置渲染彩色图和深度图切换
     *
     * @param dataType
     */
    public void setDataType(@DATATYPE int dataType) {
        this.dataType = dataType;
    }

    /**
     * 彩色图和深度图是否获取过数据
     *
     * @return
     */
    public boolean getIsInitSuccess() {
        return isInitSuccess;
    }

}
