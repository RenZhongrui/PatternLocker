package com.github.gesture.lockview;

import android.graphics.Canvas;
import android.graphics.Paint;

public class DefaultLockerNormalCellView implements INormalCellView {

    private Paint paint;
    private DefaultStyleDecorator styleDecorator;

    public DefaultLockerNormalCellView(DefaultStyleDecorator styleDecorator) {
        this.paint = DefaultConfig.createPaint();
        this.paint.setStyle(Paint.Style.FILL);
        this.styleDecorator = styleDecorator;
    }

    @Override
    public void draw(Canvas canvas, CellBean cellBean) {
        int saveCount = canvas.save();

        // draw outer circle
        this.paint.setColor(this.styleDecorator.getNormalColor());
        canvas.drawCircle(cellBean.getCenterX(), cellBean.getCenterY(), cellBean.getRadius(), this.paint);

        // draw fill circle
        this.paint.setColor(this.styleDecorator.getFillColor());
        canvas.drawCircle(cellBean.getCenterX(), cellBean.getCenterY(), cellBean.getRadius() - this.styleDecorator.getLineWidth(), this.paint);

        canvas.restoreToCount(saveCount);
    }
}
