package com.spd.qsevendemo.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.spd.qsevendemo.R;


/**
 * @author xu  右方显示3个重量体积条码结果的item
 */
public class JianshuShow extends LinearLayout {

    private TextView mTotalName;
    private TextView mShow;
    private TextView mUnit;
    private LinearLayout mlayout;

    public JianshuShow(Context context) {
        this(context, null);

    }

    public JianshuShow(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public JianshuShow(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {

        LayoutInflater.from(context).inflate(R.layout.view_jianshu_item, this, true);
        mTotalName = findViewById(R.id.worklist_one_jianshu);
        mShow = findViewById(R.id.worklist_two_jianshu);
        mUnit = findViewById(R.id.worklist_three_jianshu);

        mlayout = findViewById(R.id.bg_layout);
    }

    public void setTotalName(String count) { mTotalName.setText(count); }

    public void setShow(String count) { mShow.setText(count); }

    public String getShow() { return mShow.getText().toString(); }

    public void setUnit(String count) {
        mUnit.setText(count);
    }

    public void setBackground(boolean color){
        if (color) {
            mlayout.setBackground(getResources().getDrawable(R.drawable.bg_red));
        } else {
            mlayout.setBackground(getResources().getDrawable(R.drawable.bg_green));
        }
    }

}
