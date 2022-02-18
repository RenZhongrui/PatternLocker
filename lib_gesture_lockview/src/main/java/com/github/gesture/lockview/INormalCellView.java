package com.github.gesture.lockview;

import android.graphics.Canvas;

public interface INormalCellView {

    /**
     * 绘制正常情况下（即未设置的）每个图案的样式
     *
     * @param canvas
     * @param cellBean the target cell view
     */
    void draw(Canvas canvas, CellBean cellBean);
}
