package com.example.mymhchart.ui;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;

import com.example.mymhchart.R;
import com.example.mymhchart.model.OneTimeData;
import com.example.mymhchart.model.dataDocker;
import com.example.mymhchart.ui.TimeSharingStaticBaseView;

import org.jetbrains.annotations.Nullable;
import java.util.ArrayList;

public class TimeSharingView extends TimeSharingStaticBaseView {

    private static final String TAG = "timesharingview";
    private static final int UNITPRICELOCATION = 0;
    private static final int EVERAGEPRICELOCATION = 1;
    private static final int STRENGTHLOCATION = 2;


    private Paint mDetailTimePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mDetailValuePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mLineChartPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mHistogramPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
    private Paint mCoordinatePaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    //展示的数据
    private String mDetailTime;
    private String mCurrentPrice;
    private String mAveragePrice;
    private String mUpAndDown;
    private String mAmountOfInCrease;
    private String mSize;
    private String mMoney;
    private float mYestodaysClose;
    private ArrayList<OneTimeData> mOneTimeData;



    private float ChartWidth;
    private float biggerChartHeigth;
    private float smallerChartHeigth;

    private float maxPercentNumber;
    private float maxCoordinatePrice;
    private float maxStrength;//柱状图的高度范围
    private float moreSpaceRatio;



    public TimeSharingView(Context context) {
        super(context);
        init();
    }


    public TimeSharingView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init();
    }


    public TimeSharingView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        // 初始化笔的样式
        mDetailTimePaint.setColor(getContext().getColor(R.color.detailTime));
        mDetailTimePaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.sp48));
        mDetailTimePaint.setStrokeWidth(dp2px(8));
        mDetailTimePaint.setTextAlign(Paint.Align.CENTER);
        //mDetailValuePaint.setColor(getContext().getColor(R.color.detailItem));
        mDetailValuePaint.setStrokeWidth(dp2px(2));
        mDetailValuePaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.sp16));
        mDetailValuePaint.setTextAlign(Paint.Align.CENTER);
        mHistogramPaint.setStrokeWidth(dp2px(1));
        mCoordinatePaint.setTextSize(getResources().getDimensionPixelSize(R.dimen.sp8));
        initData(new dataDocker("----", "----", "----", "0.00", "----", "----", "----", 0f, new ArrayList<>()));
    }

    private void initData(dataDocker data) {
        moreSpaceRatio = 1.10f;
        maxStrength = 0;

        mDetailTime = data.getDetailTime();
        mCurrentPrice = data.getCurrentPrice();
        mAveragePrice = data.getAveragePrice();
        mUpAndDown = data.getUpAndDown();
        mAmountOfInCrease = data.getAmountOfInCrease();
        mSize = data.getSize();
        mMoney = data.getMoney();
        mYestodaysClose = data.getYestodaysClose();//要求保留小数点后两位
        mOneTimeData = data.getOneTimeData();

        maxStrength = maxWithLocation(mOneTimeData,STRENGTHLOCATION) * moreSpaceRatio;
    }

    public void refreshView(dataDocker data) {
        initData(data);
        invalidate();
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        ChartWidth = biggerRightButtomX - biggerLeftTopX;
        biggerChartHeigth = biggerRightButtomY - biggerLeftTopY;
        smallerChartHeigth = smallerRightButtomY - smallerLeftTopY;
        //detail框中的变化数据
        canvas.drawText(mDetailTime.substring(0,2), (detailLeftUpX + 0.15f * detailRecvWidth), (detailLeftUpY + 0.4f * detailRecvHeight), mDetailTimePaint);
        canvas.drawText(mDetailTime.substring(2), detailLeftUpX + 0.15f * detailRecvWidth, detailLeftUpY + 0.75f * detailRecvHeight, mDetailTimePaint);


        if (Double.parseDouble(mUpAndDown) < 0) {
            mDetailValuePaint.setColor(getContext().getColor(R.color.green));
        } else {
            mDetailValuePaint.setColor(getContext().getColor(R.color.red));
        }
        canvas.drawText(mCurrentPrice,(float) (detailLeftUpX + detailRecvWidth * (0.3+0.33 * 0.7)), (float) (detailLeftUpY + detailRecvHeight * 0.25), mDetailValuePaint);
        canvas.drawText(mAveragePrice,(float) (detailLeftUpX + detailRecvWidth * (0.3+0.83 * 0.7)), (float) (detailLeftUpY + detailRecvHeight * 0.25), mDetailValuePaint);
        canvas.drawText(mUpAndDown,(float) (detailLeftUpX + detailRecvWidth * (0.3+0.33 * 0.7)), (float) (detailLeftUpY + detailRecvHeight * 0.5), mDetailValuePaint);
        canvas.drawText(mAmountOfInCrease,(float) (detailLeftUpX + detailRecvWidth * (0.3+0.83 * 0.7)), (float) (detailLeftUpY + detailRecvHeight * 0.5), mDetailValuePaint);
        canvas.drawText(mSize,(float) (detailLeftUpX + detailRecvWidth * (0.3+0.33 * 0.7)), (float) (detailLeftUpY + detailRecvHeight * 0.75), mDetailValuePaint);
        canvas.drawText(mMoney,(float) (detailLeftUpX + detailRecvWidth * (0.3+0.83 * 0.7)), (float) (detailLeftUpY + detailRecvHeight * 0.75), mDetailValuePaint);

        float maxUnitPrice = maxWithLocation(mOneTimeData, UNITPRICELOCATION);
        float minUnitPrice = minWithLocation(mOneTimeData, UNITPRICELOCATION);
        maxPercentNumber = keepPointAfterTwo(moreSpaceRatio * 100 * Math.max(Math.abs(maxUnitPrice-mYestodaysClose),Math.abs(mYestodaysClose - minUnitPrice))/mYestodaysClose);
        maxCoordinatePrice = keepPointAfterTwo(maxPercentNumber * mYestodaysClose * 0.01 + mYestodaysClose);

        mCoordinatePaint.setColor(getContext().getColor(R.color.red));
        mCoordinatePaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.valueOf(maxCoordinatePrice), biggerLeftTopX, biggerLeftTopY+30,mCoordinatePaint);
        canvas.drawText(String.valueOf(keepPointAfterTwo((maxCoordinatePrice + mYestodaysClose)/2)), biggerLeftTopX, biggerLeftTopY + (biggerRightButtomY - biggerLeftTopY)/4,mCoordinatePaint);
        mCoordinatePaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText(String.valueOf(maxPercentNumber)+"%", biggerRightButtomX, biggerLeftTopY+30,mCoordinatePaint);
        canvas.drawText(String.valueOf(keepPointAfterTwo(maxPercentNumber/2))+"%", biggerRightButtomX, biggerLeftTopY + (biggerRightButtomY - biggerLeftTopY)/4,mCoordinatePaint);

        mCoordinatePaint.setColor(getContext().getColor(R.color.black));
        mCoordinatePaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.valueOf(mYestodaysClose), biggerLeftTopX, biggerLeftTopY + (biggerRightButtomY - biggerLeftTopY)/2,mCoordinatePaint);
        mCoordinatePaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("0.00%", biggerRightButtomX, biggerLeftTopY + (biggerRightButtomY - biggerLeftTopY)/2,mCoordinatePaint);

        mCoordinatePaint.setColor(getContext().getColor(R.color.green));
        mCoordinatePaint.setTextAlign(Paint.Align.RIGHT);
        canvas.drawText(String.valueOf(keepPointAfterTwo(2*mYestodaysClose-maxCoordinatePrice)), biggerLeftTopX, biggerRightButtomY,mCoordinatePaint);
        canvas.drawText(String.valueOf(keepPointAfterTwo(2*mYestodaysClose - (maxCoordinatePrice + mYestodaysClose)/2 )), biggerLeftTopX, biggerLeftTopY + (biggerRightButtomY - biggerLeftTopY)*3/4,mCoordinatePaint);
        mCoordinatePaint.setTextAlign(Paint.Align.LEFT);
        canvas.drawText("-"+String.valueOf(maxPercentNumber)+"%", biggerRightButtomX, biggerRightButtomY,mCoordinatePaint);
        canvas.drawText("-"+String.valueOf(keepPointAfterTwo(maxPercentNumber/2))+"%", biggerRightButtomX, biggerLeftTopY + (biggerRightButtomY - biggerLeftTopY)*3/4,mCoordinatePaint);

        //写小矩形的坐标轴
        canvas.drawText("0万", biggerRightButtomX+5, smallerRightButtomY,mCoordinatePaint);
        canvas.drawText(String.valueOf(Math.round(maxStrength/2))+"万", biggerRightButtomX, (smallerLeftTopY + smallerRightButtomY)/2+5,mCoordinatePaint);
        canvas.drawText(String.valueOf(Math.round(maxStrength))+"万", biggerRightButtomX, smallerLeftTopY+10,mCoordinatePaint);
        //画折线
        mLineChartPaint.setColor(getContext().getColor(R.color.blue));
        for (int i = 0; i < mOneTimeData.size()-1; i++) {
            canvas.drawLine(getXCoordinate(i), getYCoordinatePrice(i),getXCoordinate(i+1), getYCoordinatePrice(i+1), mLineChartPaint);
        }
        mLineChartPaint.setColor(getContext().getColor(R.color.yellow));
        for (int i = 0; i < mOneTimeData.size()-1; i++) {
            canvas.drawLine(getXCoordinate(i), getYCoordinateAveragePrice(i),getXCoordinate(i+1), getYCoordinateAveragePrice(i+1), mLineChartPaint);
        }
        for (int i = 0; i < mOneTimeData.size(); i++) {
            if (i == 0) {
                mHistogramPaint.setColor(getContext().getColor(R.color.white));
            }else if (mOneTimeData.get(i).getmPrice() - mOneTimeData.get(i-1).getmPrice() < 0) {
                mHistogramPaint.setColor(getContext().getColor(R.color.green));
            }else {
                mHistogramPaint.setColor(getContext().getColor(R.color.red));
            }
            canvas.drawLine(getXCoordinate(i), smallerRightButtomY, getXCoordinate(i), getYCoordinateHistogram(i), mHistogramPaint);
        }
    }

    private float minWithLocation(ArrayList<OneTimeData> mOneTimeData, int i) {
        if(mOneTimeData.isEmpty()) {
            return 0;
        }
        float temp = mOneTimeData.get(0).findByLocation(i);
        for (int j = 1; j <mOneTimeData.size(); j++) {
            if (temp > mOneTimeData.get(j).findByLocation(i)) {
                temp = mOneTimeData.get(j).findByLocation(i);
            }
        }
        return temp;
    }

    private float maxWithLocation(ArrayList<OneTimeData> mOneTimeData, int i) {
        if(mOneTimeData.isEmpty()) {
            return 0;
        }
        float temp = mOneTimeData.get(0).findByLocation(i);
        for (int j = 1; j <mOneTimeData.size(); j++) {
            if (temp < mOneTimeData.get(j).findByLocation(i)) {
                temp = mOneTimeData.get(j).findByLocation(i);
            }
        }
        return temp;
    }

    private float getYCoordinateHistogram(int i) {
        return smallerRightButtomY - smallerChartHeigth*mOneTimeData.get(i).getmStrength()/maxStrength;
    }

    private float getYCoordinateAveragePrice(int i) {
        return (biggerLeftTopY+biggerChartHeigth/2) - (mOneTimeData.get(i).getmEveragePrice()-mYestodaysClose)/(maxCoordinatePrice-mYestodaysClose)*biggerChartHeigth/2;
    }

    private float getYCoordinatePrice(int i) {
        return (biggerLeftTopY+biggerChartHeigth/2) - (mOneTimeData.get(i).getmPrice()-mYestodaysClose)/(maxCoordinatePrice-mYestodaysClose)*biggerChartHeigth/2;
    }

    private float getXCoordinate(int i) {
        float aPartXAxis = ChartWidth/(4*60);
        return (biggerLeftTopX + i*aPartXAxis);
    }

    private float keepPointAfterTwo(double n) {
        return Math.round(n*100)/100f;
    }
}
