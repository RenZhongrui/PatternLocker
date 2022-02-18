package com.github.gesture.lockview;

import android.graphics.Canvas;
import android.graphics.Paint;

public class DefaultLockerHitCellView implements IHitCellView {

    private Paint paint;
    private DefaultStyleDecorator styleDecorator;

    public DefaultLockerHitCellView(DefaultStyleDecorator styleDecorator) {
        this.paint = DefaultConfig.createPaint();
        this.paint.setStyle(Paint.Style.FILL);
        this.styleDecorator = styleDecorator;
    }

    @Override
    public void draw(Canvas canvas, CellBean cellBean, boolean isError) {
        int saveCount = canvas.save();

        // draw outer circle 画外圆
        this.paint.setColor(this.getColor(isError));
        canvas.drawCircle(cellBean.getCenterX(), cellBean.getCenterY(), cellBean.getRadius(), this.paint);

        // draw fill circle 画填充圆
        this.paint.setColor(this.styleDecorator.getFillColor());
        canvas.drawCircle(cellBean.getCenterX(), cellBean.getCenterY(), cellBean.getRadius() - this.styleDecorator.getLineWidth(), this.paint);

        // draw inner circle 画内部圆
        this.paint.setColor(this.getColor(isError));
        canvas.drawCircle(cellBean.getCenterX(), cellBean.getCenterY(), cellBean.getRadius() / 5f, this.paint);

        canvas.restoreToCount(saveCount);
    }

    private int getColor(boolean isError) {
        if (isError)
            return this.styleDecorator.getErrorColor();
        else
            return this.styleDecorator.getHitColor();
    }
}
