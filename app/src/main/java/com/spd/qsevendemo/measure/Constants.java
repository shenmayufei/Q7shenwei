package com.spd.qsevendemo.measure;

import android.support.annotation.IntDef;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 作者:jtl
 * 日期:Created in 2019/3/1 18:19
 * 描述:
 * 更改:
 */
public class Constants {
    public static final int WIDTH=640;
    public static final int HEIGHT=480;

    public static final int UPDATE_UI =1;
    public static final int TOAST=101;
    @Retention(RetentionPolicy.RUNTIME)
    @Target(ElementType.FIELD)
    @IntDef({UPDATE_UI,TOAST})
    public @interface HANDLER_TYPE{}

    public static final int CALIBRATE_CAL_RESULT = 1;
    public static final int FAILED_CAL_RESULT = 2;
    public static final int SUCCESS_CAL_RESULT = 3;
    @Retention(RetentionPolicy.SOURCE)
    @Target({ElementType.FIELD,ElementType.PARAMETER})
    @IntDef({CALIBRATE_CAL_RESULT, FAILED_CAL_RESULT, SUCCESS_CAL_RESULT})
    public @interface CAL_TYPE {}
}
