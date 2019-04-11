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
public class WorklistShow extends LinearLayout {

    private TextView mTotalName;
    private TextView mShow;
    private TextView mUnit;
    private LinearLayout mlayout;

    public WorklistShow(Context context) {
        this(context, null);

    }

    public WorklistShow(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public WorklistShow(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {

        LayoutInflater.from(context).inflate(R.layout.view_work_repairlist_item, this, true);
        mTotalName = findViewById(R.id.worklist_one);
        mShow = findViewById(R.id.worklist_two);
        mUnit = findViewById(R.id.worklist_three);

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
