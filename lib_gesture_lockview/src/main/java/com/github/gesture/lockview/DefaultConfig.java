package com.github.gesture.lockview;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.TypedValue;

public class DefaultConfig {

    public static String DEFAULT_NORMAL_COLOR = "#2196F3"; // 默认正常颜色，#7D7E81
    public static String DEFAULT_HIT_COLOR = "#3F51B5"; // 默认点击颜色，#7D7E81
    public static String DEFAULT_ERROR_COLOR = "#F44336"; // 默认错误颜色
    public static String DEFAULT_FILL_COLOR = "#FFFFFF"; // 默认填充颜色
    public static int DEFAULT_LINE_WIDTH = 1; // 默认先宽度

    public static int defaultFreezeDuration = 1000;//ms
    public static boolean defaultEnableAutoClean = true;
    public static boolean defaultEnableHapticFeedback = false;
    public static boolean defaultEnableSkip = false;
    public static boolean defaultEnableLogger = false;
    public static int defaultNormalColor = Color.parseColor(DEFAULT_NORMAL_COLOR);
    public static int defaultHitColor = Color.parseColor(DEFAULT_HIT_COLOR);
    public static int defaultErrorColor = Color.parseColor(DEFAULT_ERROR_COLOR);
    public static int defaultFillColor = Color.parseColor(DEFAULT_FILL_COLOR);

    public static float getDefaultLineWidth(Context context) {

        return convertDpToPx(DEFAULT_LINE_WIDTH, context.getResources());
    }

    // 创建画笔
    public static Paint createPaint() {
        Paint paint = new Paint();
        paint.setDither(true);
        paint.setAntiAlias(true);
        paint.setStrokeCap(Paint.Cap.ROUND);
        paint.setStrokeJoin(Paint.Join.ROUND);
        return paint;
    }

    public static float convertDpToPx(float dpValue, Resources resources) {
        return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dpValue, resources.getDisplayMetrics());
    }
}
