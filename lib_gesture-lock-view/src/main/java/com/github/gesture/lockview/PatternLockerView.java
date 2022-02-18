package com.github.gesture.lockview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.HapticFeedbackConstants;
import android.view.MotionEvent;
import android.view.View;

import java.util.ArrayList;
import java.util.List;



public class PatternLockerView extends View {

    /**
     * 绘制完后是否自动清除标志位，如果开启了该标志位，延时@freezeDuration毫秒后自动清除已绘制图案
     */
    private boolean enableAutoClean = false;


    public void setEnableAutoClean(boolean enableAutoClean) {
        this.enableAutoClean = enableAutoClean;
    }

    /**
     * 能否跳过中间点标志位，如果开启了该标志，则可以不用连续
     */
    boolean enableSkip = false;

    /**
     * 是否开启触碰反馈，如果开启了该标志，则每连接一个cell则会震动
     */
    boolean enableHapticFeedback = false;

    /**
     * 绘制完成后多久可以清除（单位ms），只有在@enableAutoClean = true 时有效
     */
    int freezeDuration = 0;

    /**
     * 绘制连接线
     */
    ILockerLinkedLineView linkedLineView = null;

    /**
     * 绘制未操作时的cell样式
     */
    INormalCellView normalCellView = null;

    /**
     * 绘制操作时的cell样式
     */
    IHitCellView hitCellView = null;

    /**
     * 是否是错误的图案
     */
    private boolean isError = false;

    /**
     * 终点x坐标
     */
    private float endX = 0f;

    /**
     * 终点y坐标
     */
    private float endY = 0f;

    /**
     * 记录绘制多少个cell，用于判断是否调用OnPatternChangeListener
     */
    private int hitSize = 0;

    // 是否可以滑动图案，避免异步清理图案后，一直滑动图案
    private boolean isEnabled = true;

    /**
     * 监听器
     */
    private OnPatternChangeListener listener = null;

    public void setOnPatternChangedListener(OnPatternChangeListener listener) {
        this.listener = listener;
    }

    private List<Integer> hitIndexList = new ArrayList<>();

    private TimeRunnable timeRunnable;

    /**
     * 真正的cell数组
     */
    private List<CellBean> cellBeanList;

    private Context mContext;


    public PatternLockerView(Context context) {
        this(context, null);
    }

    public PatternLockerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PatternLockerView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.mContext = context;
        initData();
        init(attrs, defStyleAttr);
    }

    private void initData() {
        this.hitIndexList.clear();
        this.timeRunnable = new TimeRunnable();
    }

    /**
     * 更改状态
     */
    public void updateStatus(boolean isError) {
        this.isError = isError;
        invalidate();
    }

    @Override
    protected void onDetachedFromWindow() {
        this.setOnPatternChangedListener(null);
        this.removeCallbacks(this.timeRunnable);
        super.onDetachedFromWindow();
    }

    private void updateHitState(MotionEvent event) {
        for (CellBean it : this.cellBeanList) {
            // 如果没有被点击过并且点击到了圆
            if (!it.isHit() && it.of(event.getX(), event.getY())) {
                if (!enableSkip && this.hitIndexList.size() > 0) {
                    CellBean last = this.cellBeanList.get(this.hitIndexList.get(this.hitIndexList.size() - 1));
                    int mayId = (last.getId() + it.getId()) / 2;
                    if (!this.hitIndexList.contains(mayId) && (Math.abs(last.getX() - it.getX()) % 2 == 0) && (Math.abs(last.getY() - it.getY()) % 2 == 0)) {
                        this.cellBeanList.get(mayId).setHit(true);
                        this.hitIndexList.add(mayId);
                    }
                }
                it.setHit(true);
                this.hitIndexList.add(it.getId());
                this.hapticFeedback();
            }
        }
    }

    private void hapticFeedback() {
        if (this.enableHapticFeedback) {
            this.performHapticFeedback(HapticFeedbackConstants.VIRTUAL_KEY,
                    HapticFeedbackConstants.FLAG_IGNORE_VIEW_SETTING);
        }
    }

    private void init(AttributeSet attrs, int defStyleAttr) {
        TypedArray ta = mContext.obtainStyledAttributes(attrs, R.styleable.PatternLockerView, defStyleAttr, 0);

        int normalColor = ta.getColor(R.styleable.PatternLockerView_plv_color, DefaultConfig.defaultNormalColor);
        int hitColor = ta.getColor(R.styleable.PatternLockerView_plv_hitColor, DefaultConfig.defaultHitColor);
        int errorColor = ta.getColor(R.styleable.PatternLockerView_plv_errorColor, DefaultConfig.defaultErrorColor);
        int fillColor = ta.getColor(R.styleable.PatternLockerView_plv_fillColor, DefaultConfig.defaultFillColor);
        float lineWidth = ta.getDimension(R.styleable.PatternLockerView_plv_lineWidth, DefaultConfig.getDefaultLineWidth(mContext));

        this.freezeDuration = ta.getInteger(R.styleable.PatternLockerView_plv_freezeDuration, DefaultConfig.defaultFreezeDuration);
        this.enableAutoClean = ta.getBoolean(R.styleable.PatternLockerView_plv_enableAutoClean, DefaultConfig.defaultEnableAutoClean);
        this.enableHapticFeedback = ta.getBoolean(R.styleable.PatternLockerView_plv_enableHapticFeedback, DefaultConfig.defaultEnableHapticFeedback);
        this.enableSkip = ta.getBoolean(R.styleable.PatternLockerView_plv_enableSkip, DefaultConfig.defaultEnableSkip);

        ta.recycle();

        // style
        DefaultStyleDecorator styleDecorator = new DefaultStyleDecorator(normalColor, fillColor, hitColor, errorColor, lineWidth);
        this.normalCellView = new DefaultLockerNormalCellView(styleDecorator);
        this.hitCellView = new DefaultLockerHitCellView(styleDecorator);
        this.linkedLineView = new DefaultLockerLinkedLineView(styleDecorator);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        // 取最小值，保证是正方形
        int a = Math.min(widthMeasureSpec, heightMeasureSpec);
        super.onMeasure(a, a);
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        this.initCellBeanList();
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

    private void drawLinkedLine(Canvas canvas) {
        if (this.hitIndexList.size() > 0) {
            this.linkedLineView.draw(canvas,
                    this.hitIndexList,
                    this.cellBeanList,
                    this.endX,
                    this.endY,
                    this.isError);
        }
    }

    private void drawCells(Canvas canvas) {
        for (CellBean cellBean : this.cellBeanList) {
            if (cellBean.isHit() && this.hitCellView != null) {
                this.hitCellView.draw(canvas, cellBean, this.isError);
            } else {
                this.normalCellView.draw(canvas, cellBean);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (!this.isEnabled) {
            return super.onTouchEvent(event);
        }
        boolean isHandle = false;
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                this.handleActionDown(event);
                isHandle = true;
                break;
            case MotionEvent.ACTION_MOVE:
                this.handleActionMove(event);
                isHandle = true;
                break;
            case MotionEvent.ACTION_UP:
                this.handleActionUp(event);
                isHandle = true;
                break;
        }
        invalidate();
        if (isHandle)
            return true;
        else
            return super.onTouchEvent(event);
    }

    private void handleActionDown(MotionEvent event) {
        //1. reset to default state
        this.clearHitData();

        //2. update hit state
        this.updateHitState(event);

        //3. notify listener
        if (this.listener != null) {
            this.listener.onStart(this);
        }
    }

    private void handleActionMove(MotionEvent event) {

        //1. update hit state
        this.updateHitState(event);

        //2. update end point
        this.endX = event.getX();
        this.endY = event.getY();

        //3. notify listener if needed
        int size = this.hitIndexList.size();
        if (this.hitSize != size) {
            this.hitSize = size;
            if (this.listener != null) {
                this.listener.onChange(this, this.hitIndexList);
            }
        }
    }

    private void handleActionUp(MotionEvent event) {
        //1. update hit state
        this.updateHitState(event);

        //2. update end point
        this.endX = 0f;
        this.endY = 0f;

        //3. notify listener
        if (this.listener != null) {
            this.listener.onComplete(this, this.hitIndexList);
        }

        //4. startTimer if needed
        if (this.enableAutoClean && this.hitIndexList.size() > 0) {
            this.startTimer();
        }
    }

    public class TimeRunnable implements Runnable {

        @Override
        public void run() {
            isEnabled = true;
            clearHitState();
        }
    }

    private void startTimer() {
        isEnabled = false;
        this.postDelayed(timeRunnable, this.freezeDuration);
    }

    /**
     * 清除已绘制图案
     */
    public void clearHitState() {
        this.clearHitData();
        this.isError = false;
        if (this.listener != null) {
            this.listener.onClear(this);
        }
        invalidate();
    }

    private void clearHitData() {
        if (this.hitIndexList.size() > 0) {
            this.hitIndexList.clear();
            this.hitSize = 0;
            for (CellBean cellBean : this.cellBeanList) {
                cellBean.setHit(false);
            }
        }
    }
}
