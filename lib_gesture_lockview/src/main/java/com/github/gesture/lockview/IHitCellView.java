package com.github.gesture.lockview;

import android.graphics.Canvas;

public interface IHitCellView {

    /**
     * 绘制已设置的每个图案的样式
     *
     * @param canvas
     * @param cellBean
     * @param isError
     */
    void draw(Canvas canvas, CellBean cellBean, boolean isError);
}
