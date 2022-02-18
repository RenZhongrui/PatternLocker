package com.github.gesture.lockview;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;

import java.util.List;

public class DefaultLockerLinkedLineView implements ILockerLinkedLineView {

    private Paint paint;
    private DefaultStyleDecorator styleDecorator;

    public DefaultLockerLinkedLineView(DefaultStyleDecorator styleDecorator) {
        this.paint = DefaultConfig.createPaint();
        this.paint.setStyle(Paint.Style.STROKE);
        this.styleDecorator = styleDecorator;
    }

    @Override
    public void draw(Canvas canvas, List<Integer> hitIndexList, List<CellBean> cellBeanList, float endX, float endY, boolean isError) {
        if (hitIndexList.isEmpty() || cellBeanList.isEmpty()) {
            return;
        }
        int saveCount = canvas.save();
        Path path = new Path();
        boolean first = true;

        for (int i = 0; i < hitIndexList.size(); i++) {
            // 记录选中的点id
            Integer it = hitIndexList.get(i);
            if (0 <= it && it < cellBeanList.size()) {
                // 获取选中的点
                CellBean c = cellBeanList.get(it);
                if (first) {
                    path.reset();
                    // 如果是第一个，则先将起点移动到第一个点
                    path.moveTo(c.getCenterX(), c.getCenterY());
                    first = false;
                } else {
                    // 连接第一个点到第二个点
                    path.lineTo(c.getCenterX(), c.getCenterY());
                }
            }
        }

        if ((endX != 0f || endY != 0f) && hitIndexList.size() < 9) {
            path.lineTo(endX, endY);
        }

        this.paint.setColor(this.getColor(isError));
        this.paint.setStrokeWidth(this.styleDecorator.getLineWidth());
        canvas.drawPath(path, this.paint);
        canvas.restoreToCount(saveCount);
    }

    private int getColor(boolean isError) {
        if (isError)
            return this.styleDecorator.getErrorColor();
        else
            return this.styleDecorator.getHitColor();
    }
}
