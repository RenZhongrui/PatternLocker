package com.github.gesture.lockview;

import android.graphics.Canvas;

import java.util.List;

public interface IIndicatorLinkedLineView {

    /**
     * 绘制指示器连接线
     *
     * @param canvas
     * @param hitIndexList
     * @param cellBeanList
     * @param isError
     */
    void draw(Canvas canvas,
              List<Integer> hitIndexList,
              List<CellBean> cellBeanList,
              boolean isError);

}
