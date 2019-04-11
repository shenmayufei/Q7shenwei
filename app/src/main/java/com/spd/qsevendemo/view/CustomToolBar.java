package com.spd.qsevendemo.view;

import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.spd.qsevendemo.R;


/**
 * @author xuyan  title栏
 */
public class CustomToolBar extends RelativeLayout implements View.OnClickListener {

    private TextView tvCamera;
    private TextView tvWeight;
    private TextView tvCount;
    private ImageView imageSetting;
    private BtnClickListener listener;

    public CustomToolBar(Context context) {
        super(context);
    }

    public CustomToolBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化组件
     *
     * @param context
     */
    private void init(Context context) {
        LayoutInflater.from(context).inflate(R.layout.titlebar, this);
        tvCamera = findViewById(R.id.title_camera_state);
        tvWeight = findViewById(R.id.title_weight_state);
        tvCount = findViewById(R.id.title_count);
        imageSetting = findViewById(R.id.title_settings);
        tvCamera.setOnClickListener(this);
        tvWeight.setOnClickListener(this);
        tvCount.setOnClickListener(this);
        imageSetting.setOnClickListener(this);
    }

    public void setTitleBarListener(BtnClickListener listener) {
        this.listener = listener;
    }

    /**
     * 按钮点击接口
     */
    public interface BtnClickListener {
//        void cameraClick();
//
//        void weightClick();
//
//        void countClick();

        void settingClick();
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
//            case R.id.title_camera_state:
//                listener.cameraClick();
//                break;
//            case R.id.title_weight_state:
//                listener.weightClick();
//                break;
//            case R.id.title_save_count:
//                listener.countClick();
//                break;

            case R.id.title_settings:
                listener.settingClick();
                break;
            default:
                break;
        }
    }

    public void setCameraState(int cameraState) {
        tvCamera.setText(cameraState);
    }

    public void setWeightState(int weightState) {
        tvWeight.setText(weightState);
    }

    public void setCount(int count) {
        tvCount.setText(count);
    }

    public void setTvExport(String count) {
        tvCount.setText(count);
    }

    public void setSetingBackground(int drawable) {

        imageSetting.setImageResource(drawable);
    }

    /**
     * 设置中间标题是否可见
     *
     * @param flag 是否可见
     */
    public void setTvExportVisable(boolean flag) {
        if (flag) {
            imageSetting.setVisibility(VISIBLE);
        } else {
            imageSetting.setVisibility(INVISIBLE);
        }
    }

}