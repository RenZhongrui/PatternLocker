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

public class TwoLineView extends View {

    private Paint mPaint;

    private Paint paintS;
    private float touchX;
    private float touchY;
    private List<Path> multiPath = new ArrayList<>();

    private Path pathNew;

    public TwoLineView(Context context) {
        this(context, null);

    }

    public TwoLineView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TwoLineView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        paintS = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintS.setStyle(Paint.Style.STROKE);
        paintS.setStrokeWidth(2f);
        paintS.setColor(Color.BLUE);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

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
        } else if (event.getAction() == MotionEvent.ACTION_MOVE) {
            if (Math.abs(touchX + touchY) > 20) {
                pathNew.lineTo(500, 500);
            }
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
