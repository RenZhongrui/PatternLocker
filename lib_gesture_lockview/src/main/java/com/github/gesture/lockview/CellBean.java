package com.github.gesture.lockview;

/**
 * id 表示该cell的编号，9个cell的编号如下：
 *
 * 0 1 2
 * 3 4 5
 * 6 7 8
 *
 * x 表示该cell的x坐标（点坐标）
 * y 表示该cell的y坐标（点坐标）
 * x,y 点坐标编号如下：
 * (0,0) (1,0) (2,0)
 * (0,1) (1,1) (2,1)
 * (0,2) (1,2) (2,2)
 *
 * centerX 表示该cell的圆心x坐标（相对坐标）
 * centerY 表示该cell的圆心y坐标（相对坐标）
 * centerX, centerY 圆心坐标如下：
 * (radius, radius)  (4radius, radius)  (7radius, radius)
 * (radius, 4radius) (4radius, 4radius) (7radius, 4radius)
 * (radius, 7radius) (4radius, 7radius) (7radius, 7radius)
 *
 * radius 表示该cell的半径
 * isHit 表示该cell是否被设置的标记
 */
public class CellBean {
    private int id;
    private int x;
    private int y;
    private float centerX;
    private float centerY;
    private float radius;
    private boolean isHit;

    public CellBean(int id, int x, int y, float centerX, float centerY, float radius) {
        this.id = id;
        this.x = x;
        this.y = y;
        this.centerX = centerX;
        this.centerY = centerY;
        this.radius = radius;
    }


    /**
     * 是否触碰到该view
     */
    boolean of(float x, float y) {
        float dx = this.centerX - x;
        float dy = this.centerY - y;
        // 中心点距离点击点的距离小于等于圆半径，表示点击到的是圆内
        return Math.sqrt((dx * dx + dy * dy)) <= this.radius;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getX() {
        return x;
    }

    public void setX(int x) {
        this.x = x;
    }

    public int getY() {
        return y;
    }

    public void setY(int y) {
        this.y = y;
    }

    public float getCenterX() {
        return centerX;
    }

    public void setCenterX(float centerX) {
        this.centerX = centerX;
    }

    public float getCenterY() {
        return centerY;
    }

    public void setCenterY(float centerY) {
        this.centerY = centerY;
    }

    public float getRadius() {
        return radius;
    }

    public void setRadius(float radius) {
        this.radius = radius;
    }

    public boolean isHit() {
        return isHit;
    }

    public void setHit(boolean hit) {
        isHit = hit;
    }

    @Override
    public String toString() {
        return "CellBean{" +
                "id=" + id +
                ", x=" + x +
                ", y=" + y +
                ", centerX=" + centerX +
                ", centerY=" + centerY +
                ", radius=" + radius +
                ", isHit=" + isHit +
                '}';
    }
}
