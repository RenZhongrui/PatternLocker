package com.github.gesture.lockview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.List;

public class DefaultIndicatorLinkedLineView implements IIndicatorLinkedLineView {

    private Paint paint;
    private DefaultStyleDecorator styleDecorator;

    public DefaultIndicatorLinkedLineView(DefaultStyleDecorator styleDecorator) {
        this.paint = DefaultConfig.createPaint();
        this.paint.setStyle(Paint.Style.STROKE);
        this.styleDecorator = styleDecorator;
    }

    @Override
    public void draw(Canvas canvas, List<Integer> hitIndexList, List<CellBean> cellBeanList, boolean isError) {
        if (hitIndexList.isEmpty() || cellBeanList.isEmpty()) {
            return;
        }
        int saveCount = canvas.save();
        Path path = new Path();
        boolean first = true;
        for (Integer it : hitIndexList) {
            if (0 <= it && it < cellBeanList.size()) {
                CellBean c = cellBeanList.get(it);
                if (first) {
                    path.moveTo(c.getCenterX(), c.getCenterY());
                    first = false;
                } else {
                    path.lineTo(c.getCenterX(), c.getCenterY());
                }
            }
        }
        this.paint.setColor(this.getColor(isError));
        this.paint.setStrokeWidth(this.styleDecorator.getLineWidth());
        canvas.drawPath(path, this.paint);
        canvas.restoreToCount(saveCount);
    }

    private int getColor(boolean isError) {
        if (isError) {
            return this.styleDecorator.getErrorColor();
        } else {
            return this.styleDecorator.getHitColor();
        }
    }
}
