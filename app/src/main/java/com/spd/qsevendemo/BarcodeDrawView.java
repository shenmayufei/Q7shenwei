package com.spd.qsevendemo;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import com.spd.qsevendemo.utils.ToastUtils;

import java.util.List;


/**
 * @author xuyan
 */
public class BarcodeDrawView extends View {
    private List<BarcodeBounds> barcodeBoundsList;

    public BarcodeDrawView(Context context) {
        super(context);
    }

    public BarcodeDrawView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public BarcodeDrawView(Context context, List<BarcodeBounds> barcodeBoundsList) {
        super(context);
        this.barcodeBoundsList = barcodeBoundsList;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (barcodeBoundsList == null) {
            return;
        }
        ToastUtils.showShortToastSafe("barcodeBoundsList != null");
        for (int i = 0; i < barcodeBoundsList.size(); i++) {
            Point TopLeft = barcodeBoundsList.get(i).getTopLeft();
            Point TopRight = barcodeBoundsList.get(i).getTopRight();
            Point BottomLeft = barcodeBoundsList.get(i).getBottomLeft();
            Point BottomRight = barcodeBoundsList.get(i).getBottomRight();
            @SuppressLint("DrawAllocation") Paint p = new Paint();
            p.setStrokeWidth(3); //设置线宽
            p.setColor(Color.GREEN);// 设置绿色
            p.setStyle(Paint.Style.STROKE);
            int width = Integer.parseInt(String.valueOf(this.getWidth()), 10);
            int height = Integer.parseInt(String.valueOf(this.getHeight()), 10);
            float a = (float) 2560 / (float) height;
            float b = (float) 1920 / (float) width;
            // 绘制路径
            @SuppressLint("DrawAllocation") Path path = new Path();
            path.moveTo((float) width - ((float) TopLeft.y / b), ((float) TopLeft.x / a));
            path.lineTo((float) width - ((float) TopRight.y / b), ((float) TopRight.x / a));
            path.lineTo((float) width - ((float) BottomRight.y / b), ((float) BottomRight.x / a));
            path.lineTo((float) width - ((float) BottomLeft.y / b), ((float) BottomLeft.x / a));
            Log.i("point", a + "   之前: " + ((float) (TopRight.y)) + "$$$$" + ((float) (TopRight.x)));
            Log.i("point", b + "   过后: " + ((float) width - (float) ((float) TopRight.y / a) + "$$$$" + ((float) ((float) TopRight.x / b))));
            path.close();// 封闭或者path.lineTo(x, y);即开始的位置
            canvas.drawPath(path, p);
        }

    }

}
