package com.spd.qsevendemo.measure.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.TextView;

import com.spd.qsevendemo.R;

import java.text.DecimalFormat;

/**
 * 作者:jtl
 * 日期:Created in 2018/7/5 10:57
 * 描述:titleView
 * 更改:
 * @author xuyan
 */

public class TitleView extends FrameLayout {
    private TextView volumeText;
    private TextView longText;
    private TextView heightText;
    private TextView widthText;

    public TitleView(Context context) {
        this(context, null);
    }

    public TitleView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init(context);
    }

    private void init(Context context) {
        View view = LayoutInflater.from(context).inflate(R.layout.layout_title_view, this);
        volumeText = view.findViewById(R.id.tv_title_volume);
        longText = view.findViewById(R.id.tv_title_long);
        heightText = view.findViewById(R.id.tv_title_height);
        widthText = view.findViewById(R.id.tv_title_width);
    }

    /**
     * 设置数据
     * @param volume 体积
     * @param longs  长
     * @param width 宽
     * @param height 高
     */
    public void setData(final double volume, final double longs, final double width, final double height) {
        volumeText.setText(new DecimalFormat("0.00").format(volume * 100 * 100 * 100) + "cm³");
        longText.setText(new DecimalFormat("0.0").format(longs * 100) + "cm");
        heightText.setText(new DecimalFormat("0.0").format(width * 100) + "cm");
        widthText.setText(new DecimalFormat("0.0").format(height * 100) + "cm");
    }
}
