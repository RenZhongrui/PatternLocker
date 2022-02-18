package com.github.gesture.lockview;

import android.graphics.Canvas;
import android.graphics.Paint;

public class DefaultIndicatorHitCellView implements IHitCellView {

    private Paint paint;
    private DefaultStyleDecorator styleDecorator;

    public DefaultIndicatorHitCellView(DefaultStyleDecorator styleDecorator) {
        this.paint = DefaultConfig.createPaint();
        this.paint.setStyle(Paint.Style.FILL);
        this.styleDecorator = styleDecorator;
    }

    @Override
    public void draw(Canvas canvas, CellBean cellBean, boolean isError) {
        int saveCount = canvas.save();
        this.paint.setColor(this.getColor(isError));
        canvas.drawCircle(cellBean.getCenterX(), cellBean.getCenterY(), cellBean.getRadius(), this.paint);
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
