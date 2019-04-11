package com.spd.qsevendemo.measure.view;

import android.app.DialogFragment;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.socks.library.KLog;
import com.spd.qsevendemo.R;

/**
 * 作者:jtl
 * 日期:Created in 2018/11/16 10:21
 * 描述:
 * 更改:
 */

public class MessageDialog extends DialogFragment {
    private TextView mTitleText;
    private TextView mContentText;
    private Button mPositiveBtn;
    private Button mNegativeBtn;
    private String title;
    private String content;
    private String positive;
    private String negative;
    private ProgressBar mProgressBar;
    private View.OnClickListener positiveListener;
    private View.OnClickListener negativeListener;
    private boolean visible;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        KLog.e("Dialog:onCreateView");
        //每次show的时候 都会走一遍该方法
        View view=inflater.inflate(R.layout.view_message_dialog,container,false);
        mTitleText = view.findViewById(R.id.tv_dialog_title);
        mContentText = view.findViewById(R.id.tv_dialog_content);
        mPositiveBtn = view.findViewById(R.id.btn_dialog_positive);
        mNegativeBtn = view.findViewById(R.id.btn_dialog_negative);
        mProgressBar=view.findViewById(R.id.progress_message_loading);

        if (!TextUtils.isEmpty(title)){
            mTitleText.setText(title);
        }
        if (!TextUtils.isEmpty(content)){
            mContentText.setText(content);
        }
        if (!TextUtils.isEmpty(negative)){
            mNegativeBtn.setText(negative);
        }
        if (!TextUtils.isEmpty(positive)){
            mPositiveBtn.setText(positive);
        }

        if (positiveListener !=null){
            mPositiveBtn.setOnClickListener(positiveListener);
        }
        if (negativeListener!=null){
            mNegativeBtn.setOnClickListener(negativeListener);
        }
        if (visible){
            mContentText.setVisibility(View.GONE);
            mProgressBar.setVisibility(View.VISIBLE);
        }
        else{
            mContentText.setVisibility(View.VISIBLE);
            mProgressBar.setVisibility(View.GONE);
        }

        setCancelable(true);
        return view;
    }

    /**
     * @param title 标题
     */
    public MessageDialog setTitle(@NonNull String title){
        this.title=title;
        return this;
    }

    /**
     * @param content 内容
     */
    public MessageDialog setContent(@NonNull String content){
        this.content=content;
        return this;
    }

    /**
     * progressBar 显示隐藏
     * @param visible
     */
    public MessageDialog setProgressVisible(boolean visible){
        this.visible=visible;
        return this;
    }

    public MessageDialog setProgressBar(boolean visible){
        if (mProgressBar!=null&&mContentText!=null){
            if (visible){
                mProgressBar.setVisibility(View.VISIBLE);
                mContentText.setVisibility(View.GONE);
            }
            else{
                mProgressBar.setVisibility(View.GONE);
                mContentText.setVisibility(View.VISIBLE);
            }
        }

        return this;
    }
    public MessageDialog setContentText(String content){
        if (mContentText!=null){
            mContentText.setText(content);
        }

        return this;
    }

    public MessageDialog setTitleText(String content){
        if (mTitleText!=null){
            mTitleText.setText(content);
        }

        return this;
    }


    /**
     * 获取progressBar状态
     * @return
     */
    public  boolean getProgressVisible(){
       return visible;
    }

    /**
     * 积极按钮
     * @param positive 文字
     * @param positiveClick 点击事件
     */
    public MessageDialog setPositiveClick(@NonNull String positive, View.OnClickListener positiveClick){
        if (positiveClick!=null){
            positiveListener =positiveClick;
        }
        this.positive=positive;
        return this;
    }

    /**
     * 消极按钮
     * @param negative 文字
     * @param negativeClick 点击事件
     */
    public MessageDialog setNegativeClick(@NonNull String negative, View.OnClickListener negativeClick){
        if (negativeClick!=null){
            negativeListener=negativeClick;
        }

        this.negative=negative;
        return this;
    }

    public MessageDialog setNegative(@NonNull String negative){
        if (mNegativeBtn!=null){
            mNegativeBtn.setText(negative);
        }

        return this;
    }

    public MessageDialog setPositive(@NonNull String positive){
        if (mPositiveBtn!=null){
            mPositiveBtn.setText(positive);
        }

        return this;
    }

    /**
     * 取消
     */
    public void cancel(){
        setStatusNavigation();
        getDialog().cancel();
    }

    @Override
    public void dismiss() {
        super.dismiss();
        setStatusNavigation();
    }

    public void setStatusNavigation() {
        if (Build.VERSION.SDK_INT >= 19) {
            View decorView = getActivity().getWindow().getDecorView();
            decorView.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
        }
    }
}
