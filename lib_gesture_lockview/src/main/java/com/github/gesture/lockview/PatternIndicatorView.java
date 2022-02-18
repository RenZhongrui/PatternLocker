package com.github.gesture.lockview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.View;

import java.util.ArrayList;
import java.util.List;


public class PatternIndicatorView extends View {

    private Context mContext;
    private DefaultIndicatorNormalCellView normalCellView;
    private DefaultIndicatorHitCellView hitCellView;
    private DefaultIndicatorLinkedLineView linkedLineView;

    private boolean isError = false;
    private List hitIndexList = new ArrayList<Integer>();
    private List<CellBean> cellBeanList;

    private Paint paintS;

    public PatternIndicatorView(Context context) {
        this(context, null);
    }

    public PatternIndicatorView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PatternIndicatorView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        //this.cellBeanList = cellBeanList();
        init(attrs, defStyleAttr);

        paintS = new Paint(Paint.ANTI_ALIAS_FLAG);
        paintS.setStyle(Paint.Style.FILL);
        paintS.setStrokeWidth(2f);
        paintS.setColor(Color.BLUE);
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        final TypedArray ta = mContext.obtainStyledAttributes(
                attrs, R.styleable.PatternIndicatorView, defStyleAttr, 0);
        int normalColor = ta.getColor(R.styleable.PatternIndicatorView_piv_color, DefaultConfig.defaultNormalColor);
        int fillColor = ta.getColor(R.styleable.PatternIndicatorView_piv_fillColor, DefaultConfig.defaultFillColor);
        int hitColor = ta.getColor(R.styleable.PatternIndicatorView_piv_hitColor, DefaultConfig.defaultHitColor);
        int errorColor = ta.getColor(R.styleable.PatternIndicatorView_piv_errorColor, DefaultConfig.defaultErrorColor);
        float lineWidth = ta.getDimension(R.styleable.PatternIndicatorView_piv_lineWidth, DefaultConfig.getDefaultLineWidth(mContext));
        ta.recycle();

        DefaultStyleDecorator decorator = new DefaultStyleDecorator(normalColor, fillColor, hitColor, errorColor, lineWidth);
        this.normalCellView = new DefaultIndicatorNormalCellView(decorator); // 正常布局样式
        this.hitCellView = new DefaultIndicatorHitCellView(decorator); // 点击后布局样式
        this.linkedLineView = new DefaultIndicatorLinkedLineView(decorator);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        initCellBeanList();

        this.updateHitState();
        this.drawLinkedLine(canvas);
        this.drawCells(canvas);
    }

    private void initCellBeanList() {
        if (this.cellBeanList == null) {
            int w = this.getWidth() - this.getPaddingLeft() - this.getPaddingRight();
            int h = this.getHeight() - this.getPaddingTop() - this.getPaddingBottom();
            this.cellBeanList = CellFactory.buildCells(w, h);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int a = Math.min(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(a, a);
    }

    public void updateState(List<Integer> hitIndexList, boolean isError) {
        this.hitIndexList = hitIndexList;
        this.isError = isError;
        invalidate();
    }

    private void updateHitState() {
        //1. clear pre state
        for (CellBean cellBean : this.cellBeanList) {
            cellBean.setHit(false);
        }
        for (int i = 0; i < this.hitIndexList.size(); i++) {
            //2. update hit state
            Integer it = (Integer) this.hitIndexList.get(i);
            if (0 <= it && it < this.cellBeanList.size()) {
                this.cellBeanList.get(it).setHit(true);
            }

        }

    }

    private void drawLinkedLine(Canvas canvas) {
        if (this.hitIndexList.size() > 0) {
            this.linkedLineView.draw(canvas,
                    this.hitIndexList,
                    this.cellBeanList,
                    this.isError);
        }
    }

    private void drawCells(Canvas canvas) {
        for (CellBean cellBean : this.cellBeanList) {
            if (cellBean.isHit()) {
                this.hitCellView.draw(canvas, cellBean, this.isError);
            } else {
                this.normalCellView.draw(canvas, cellBean);
            }
        }
    }
}
