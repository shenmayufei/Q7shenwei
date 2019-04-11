package com.spd.qsevendemo.measure.view;

import android.annotation.SuppressLint;
import android.content.Context;
import android.opengl.GLES20;
import android.opengl.GLSurfaceView;
import android.util.AttributeSet;
import android.view.MotionEvent;

import com.spd.qsevendemo.measure.utils.GLGraphics;

import java.nio.ByteBuffer;

import javax.microedition.khronos.egl.EGLConfig;
import javax.microedition.khronos.opengles.GL10;

/**
 * GLSurfaceView封装类
 */
@SuppressLint("NewApi")
public class GLPanel extends GLSurfaceView {
    private GLGraphics mGLGraphics;
    private ByteBuffer mBufferImage;

    private boolean bWork = true;
    private int mFrameWidth;
    private int mFrameHeight;
    private GlPanelListener mGlPanelListener;

    @SuppressLint("NewApi")
    public GLPanel(Context context) {
        super(context);
        init(context);
    }

    public GLPanel(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        setEGLContextClientVersion(2);
        setRenderer(new Scene());
        setRenderMode(GLSurfaceView.RENDERMODE_WHEN_DIRTY);
    }


    public void paint(float[] vertices, ByteBuffer buffer, int width, int height) {
        if (mGLGraphics == null) {
            return;
        }
        mFrameWidth = width;
        mFrameHeight = height;
        mGLGraphics.setVertexData(vertices);

        if (bWork) {
            mBufferImage = buffer;
            requestRender();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        bWork = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        bWork = true;
    }

    @SuppressLint("NewApi")
    private class Scene implements Renderer {
        @Override
        public void onDrawFrame(GL10 gl) {
            GLES20.glClear(GLES20.GL_DEPTH_BUFFER_BIT | GLES20.GL_COLOR_BUFFER_BIT);
            if (mBufferImage != null) {
                mBufferImage.position(0);
                mGLGraphics.buildTextures(mBufferImage, mFrameWidth, mFrameHeight);
            }
            mGLGraphics.draw();
        }

        @Override
        public void onSurfaceChanged(GL10 gl, int width, int height) {
            GLES20.glViewport(0, 0, width, height);
        }

        @Override
        public void onSurfaceCreated(GL10 gl, EGLConfig config) {
            GLES20.glClearColor(0.0f, 0.0f, 0.0f, 1.0f);
            mGLGraphics = new GLGraphics();
            if (!mGLGraphics.isProgramBuilt()) {
                mGLGraphics.buildProgram();
            }
            GLES20.glEnable(GLES20.GL_DEPTH_TEST);
            GLES20.glEnable(GLES20.GL_CULL_FACE);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_UP:
                if (mGlPanelListener != null) {
                    mGlPanelListener.onTouchUp(event.getX(), event.getY());
                }
                break;
            default:
                break;
        }
        return true;
    }

    public void setGlPanelListener(GlPanelListener glPanelListener) {
        this.mGlPanelListener = glPanelListener;
    }

    public interface GlPanelListener {
        void onTouchUp(float x, float y);
    }
}
