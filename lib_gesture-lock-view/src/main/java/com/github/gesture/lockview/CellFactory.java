package com.github.gesture.lockview;

import java.util.ArrayList;
import java.util.List;

public class CellFactory {

    public static List<CellBean> buildCells(int width, int height) {
        List result = new ArrayList<CellBean>();
        float pWidth = width / 8f;
        float pHeight = height / 8f;

        for (int i = 0; i < 9; i++) {
            CellBean cellBean = new CellBean(i, i % 3, i / 3, (i % 3 * 3 + 1) * pWidth,
                    (i / 3 * 3 + 1) * pHeight, pWidth);
            result.add(cellBean);
        }
        return result;
    }
}
