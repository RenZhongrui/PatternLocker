package com.github.gesture.lockview.demo;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;

public class LineView extends View {

    private Paint mPaint;
    private Path mMovePath;

    private Paint paintS;
    private float touchX;
    private float touchY;
    private List<Path> multiPath = new ArrayList<>();

    private Path pathNew;

    public LineView(Context context) {
        this(context, null);

    }

    public LineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        mPaint.setColor(Color.BLUE);
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStrokeWidth(1);

        paintS = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintS.setStyle(Paint.Style.STROKE);
        paintS.setStrokeWidth(2f);
        paintS.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        canvas.drawText("I Love You",200,60,mPaint);
        RectF rect = new RectF(180, 10, 180 + 320, 80);
        canvas.drawRoundRect(rect,51f,51f, paintS);

        drawMultiPath(canvas);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        touchX = event.getX();
        touchY = event.getY();
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            Log.e("onTouchEvent", "创建pathNew");
            pathNew = new Path();
            pathNew.reset();
            pathNew.moveTo(touchX, touchY);
            multiPath.add(pathNew);
        }
        if (Math.abs(touchX + touchY) > 20) {
            pathNew.lineTo(touchX, touchY);
        }
        invalidate();
        return true;
    }

    private void drawMultiPath(Canvas canvas) {
        for (Path path : multiPath) {
            canvas.drawPath(path, paintS);
        }
    }
}
