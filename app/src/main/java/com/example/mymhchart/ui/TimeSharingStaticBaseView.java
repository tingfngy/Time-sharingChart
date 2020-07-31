package com.example.mymhchart.ui;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;


import com.example.mymhchart.R;

import org.jetbrains.annotations.Nullable;


public class TimeSharingStaticBaseView extends View {
    private static final String TAG = "hmchart";
    protected Paint mGridPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint mBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint mDetailRectBackgroundPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint mTimePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    protected Paint mDetailItemPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    //detail框中左上角坐标和框的长宽
    protected float detailRecvWidth;
    protected float detailRecvHeight;
    protected float detailLeftUpX;
    protected float detailLeftUpY;

    //分时图中的两个矩形的左上右下坐标
    protected float biggerLeftTopX;
    protected float biggerLeftTopY;
    protected float biggerRightButtomX;
    protected float biggerRightButtomY;

    protected float smallerLeftTopX;
    protected float smallerLeftTopY;
    protected float smallerRightButtomX;
    protected float smallerRightButtomY;


    public TimeSharingStaticBaseView(Context context) {
        super(context);
        init();
    }

    public TimeSharingStaticBaseView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TimeSharingStaticBaseView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }


    private void init() {
        mGridPaint.setColor(getContext().getColor(R.color.grid));
        mBackgroundPaint.setColor(getContext().getColor(R.color.background));
        mDetailRectBackgroundPaint.setColor(getContext().getColor(R.color.detailBackground));
        mTimePaint.setColor(getContext().getColor(R.color.black));
        mTimePaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.sp12));
        mDetailItemPaint.setColor(getContext().getColor(R.color.detailItem));
        mDetailItemPaint.setStrokeWidth(dp2px(2));
        mDetailItemPaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.sp16));
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        //初学先不进行模式判定，直接在布局中默认约定好了宽度为match_parent,高度为特定的，完善时要修改逻辑。
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(widthSize,heightSize);
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        int paddingLeft = getPaddingLeft();
        int paddingRight = getPaddingRight();
        int paddingTop = getPaddingTop();
        int paddingBottom = getPaddingBottom();

        int width = getWidth() - paddingLeft - paddingRight;
        int height = getHeight() - paddingTop - paddingBottom;
        canvas.drawRoundRect(new RectF(paddingLeft, paddingTop, width + paddingLeft, height + paddingTop),50,50,mBackgroundPaint);
        int detailRectpadding = 30;
        canvas.drawRoundRect(new RectF(paddingLeft + detailRectpadding, paddingTop + detailRectpadding,
                width + paddingLeft - detailRectpadding, height + paddingTop - 1250), 50, 50, mDetailRectBackgroundPaint);


        //画线
        biggerLeftTopX = paddingLeft+75;
        biggerLeftTopY = height + paddingTop-1200;
        biggerRightButtomX = width + paddingLeft-75;
        biggerRightButtomY = height + paddingTop-470;

        smallerLeftTopX = paddingLeft+75;
        smallerLeftTopY = height + paddingTop-400;
        smallerRightButtomX = width + paddingLeft-75;
        smallerRightButtomY = height + paddingTop-100;
        float[] pts = {
                //横线从上到下
                paddingLeft, height + paddingTop-1200, width + paddingLeft, height + paddingTop-1200,

                paddingLeft+75, height + paddingTop-470, width + paddingLeft-75, height + paddingTop-470,
                paddingLeft+75, height + paddingTop-400, width + paddingLeft-75, height + paddingTop-400,
                paddingLeft, height + paddingTop-100, width + paddingLeft, height + paddingTop-100,
                //竖线从左到右
                paddingLeft+75, height + paddingTop-1200, paddingLeft+75, height + paddingTop-100,

                paddingLeft+300, height + paddingTop-1200, paddingLeft+300,  height + paddingTop-470,
                paddingLeft+525, height + paddingTop-1200, paddingLeft+525,  height + paddingTop-470,
                paddingLeft+750, height + paddingTop-1200, paddingLeft + 750,  height + paddingTop-470,

                paddingLeft+300, height + paddingTop-400, paddingLeft+300,  height + paddingTop-100,
                paddingLeft+525, height + paddingTop-400, paddingLeft+525,  height + paddingTop-100,
                paddingLeft+750, height + paddingTop-400, paddingLeft+750,  height + paddingTop-100,

                width + paddingLeft-75, height + paddingTop-1200, width + paddingLeft-75, height + paddingTop-100,
        };
        canvas.drawLines(pts, mGridPaint);
        drawDottedLine(canvas, paddingLeft+75, height + paddingTop-835, width + paddingLeft-75, height + paddingTop-835, mGridPaint);
        // 写时间
        mTimePaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("10:30",paddingLeft+300,  height + paddingTop-420, mTimePaint);
        canvas.drawText("11:30/13:00",paddingLeft+525, height + paddingTop-420, mTimePaint);
        canvas.drawText("14:00",paddingLeft+750, height + paddingTop-420, mTimePaint);
        mTimePaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("9:30",paddingLeft+75, height + paddingTop-420, mTimePaint);
        mTimePaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText("15:00",paddingLeft+980, height + paddingTop-420, mTimePaint);

        // 写detail框里的静态内容
        detailRecvWidth = width - 2*detailRectpadding;
        detailRecvHeight = height - 1250 - detailRectpadding;
        detailLeftUpX = paddingLeft + detailRectpadding;
        detailLeftUpY = paddingTop + detailRectpadding;
        mDetailItemPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText("现价：",(float) (detailLeftUpX + detailRecvWidth * (0.3+0.125 * 0.7)), (float) (detailLeftUpY + detailRecvHeight * 0.25), mDetailItemPaint);
        canvas.drawText("均价：",(float) (detailLeftUpX + detailRecvWidth * (0.3+0.625 * 0.7)), (float) (detailLeftUpY + detailRecvHeight * 0.25), mDetailItemPaint);
        canvas.drawText("涨跌：",(float) (detailLeftUpX + detailRecvWidth * (0.3+0.125 * 0.7)), (float) (detailLeftUpY + detailRecvHeight * 0.5), mDetailItemPaint);
        canvas.drawText("涨幅：",(float) (detailLeftUpX + detailRecvWidth * (0.3+0.625 * 0.7)), (float) (detailLeftUpY + detailRecvHeight * 0.5), mDetailItemPaint);
        canvas.drawText("  量：",(float) (detailLeftUpX + detailRecvWidth * (0.3+0.125 * 0.7)), (float) (detailLeftUpY + detailRecvHeight * 0.75), mDetailItemPaint);
        canvas.drawText("  额：",(float) (detailLeftUpX + detailRecvWidth * (0.3+0.625 * 0.7)), (float) (detailLeftUpY + detailRecvHeight * 0.75), mDetailItemPaint);

    }

    private void drawDottedLine(Canvas canvas, int i, int i1, int i2, int i3, Paint mGridPaint) {
        int cycling = 20;
        int length = i2 - i;
        for(int j = 0; j < length/cycling; j++) {
            if (i + j*cycling + cycling/2 < i2) {
                canvas.drawLine(i + j * cycling, i1, i + j * cycling + cycling / 2, i3, mGridPaint);
            } else {
                canvas.drawLine(i + j * cycling, i1, i2, i3, mGridPaint);
            }
        }
    }

    protected int dp2px(float dp) {
        final float scale = getContext().getResources().getDisplayMetrics().density;
        return (int) (dp * scale + 0.5f);
    }
}
