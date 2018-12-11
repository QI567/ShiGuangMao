package com.yizhipin.view;

import android.content.Context;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.RectF;
import android.text.TextUtils;
import android.view.View;

import com.haibin.calendarview.Calendar;
import com.haibin.calendarview.MonthView;
import com.yizhipin.R;
import com.yizhipin.data.response.ScheduleItemBean;
import com.yizhipin.teacher.presenter.ScheduleCalendarPresenter;

/**
 * 演示一个变态需求的月视图
 * Created by huanghaibin on 2018/2/9.
 */

public class CustomMonthView extends MonthView {

    private int mRadius;

    /**
     * 自定义魅族标记的文本画笔
     */
    private Paint mTextPaint = new Paint();


    /**
     * 24节气画笔
     */
    private Paint mSolarTermTextPaint = new Paint();

    /**
     * 背景圆点
     */
    private Paint mPointPaint = new Paint();

    /**
     * 今天的背景色
     */
    private Paint mCurrentDayPaint = new Paint();

    /**
     * 圆点半径
     */
    private float mPointRadius;

    private int mPadding;

    private float mCircleRadius;
    /**
     * 自定义魅族标记的圆形背景
     */
    private Paint mSchemeBasicPaint = new Paint();

    private float mSchemeBaseLine;

    public CustomMonthView(Context context) {
        super(context);

        mTextPaint.setTextSize(dipToPx(context, 8));
        mTextPaint.setColor(0xffffffff);
        mTextPaint.setAntiAlias(true);
        mTextPaint.setFakeBoldText(true);


        mSolarTermTextPaint.setColor(0xff489dff);
        mSolarTermTextPaint.setAntiAlias(true);
        mSolarTermTextPaint.setTextAlign(Paint.Align.CENTER);

        mSchemeBasicPaint.setAntiAlias(true);
        mSchemeBasicPaint.setStyle(Paint.Style.FILL);
        mSchemeBasicPaint.setTextAlign(Paint.Align.CENTER);
        mSchemeBasicPaint.setFakeBoldText(true);
        mSchemeBasicPaint.setColor(Color.WHITE);


        mCurrentDayPaint.setAntiAlias(true);
        mCurrentDayPaint.setStyle(Paint.Style.FILL);
        mCurrentDayPaint.setColor(0xFFeaeaea);

        mPointPaint.setAntiAlias(true);
        mPointPaint.setStyle(Paint.Style.FILL);
        mPointPaint.setTextAlign(Paint.Align.CENTER);
        mPointPaint.setColor(Color.RED);

        mCircleRadius = dipToPx(getContext(), 7);

        mPadding = dipToPx(getContext(), 3);

        mPointRadius = dipToPx(context, 2);

        Paint.FontMetrics metrics = mSchemeBasicPaint.getFontMetrics();
        mSchemeBaseLine = mCircleRadius - metrics.descent + (metrics.bottom - metrics.top) / 2 + dipToPx(getContext(), 1);

        //兼容硬件加速无效的代码
        setLayerType(View.LAYER_TYPE_SOFTWARE, mSelectedPaint);
        //4.0以上硬件加速会导致无效
        mSelectedPaint.setMaskFilter(new BlurMaskFilter(28, BlurMaskFilter.Blur.SOLID));

        setLayerType(View.LAYER_TYPE_SOFTWARE, mSchemeBasicPaint);
        mSchemeBasicPaint.setMaskFilter(new BlurMaskFilter(28, BlurMaskFilter.Blur.SOLID));

    }

    @Override
    protected void onPreviewHook() {
        mSolarTermTextPaint.setTextSize(mCurMonthLunarTextPaint.getTextSize());
        mRadius = Math.min(mItemWidth, mItemHeight) / 11 * 5;
    }


    @Override
    protected boolean onDrawSelected(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme) {
//        int cx = x + mItemWidth / 2;
//        int cy = y + mItemHeight / 2;
        RectF rectF = new RectF(x, y, x + mItemWidth, y + mItemHeight);
        canvas.drawRoundRect(rectF, mRadius, mRadius, mCurrentDayPaint);
        return true;
    }

    @Override
    protected void onDrawScheme(Canvas canvas, Calendar calendar, int x, int y) {
    }

    @Override
    protected void onDrawText(Canvas canvas, Calendar calendar, int x, int y, boolean hasScheme, boolean isSelected) {
        int cx = x + mItemWidth / 2;
//        int cy = y + mItemHeight / 2;
        int top = y - mItemHeight / 6;

        if (calendar.isCurrentDay() && !isSelected) {
            RectF rectF = new RectF(x, y, x + mItemWidth, y + mItemHeight);
            canvas.drawRoundRect(rectF, mRadius, mRadius, mCurrentDayPaint);
        }
        mCurMonthTextPaint.setColor(0xff333333);
        mCurMonthLunarTextPaint.setColor(0xffCFCFCF);
        mSchemeLunarTextPaint.setColor(0xffCFCFCF);
        mOtherMonthTextPaint.setColor(0xFFe1e1e1);
        mOtherMonthLunarTextPaint.setColor(0xFFe1e1e1);
        String date = calendar.getYear() + "-" + calendar.getMonth() + "-" + calendar.getDay();
        ScheduleItemBean itemBean = ScheduleCalendarPresenter.Companion.getScheduleByDate(date);
        if (isSelected) {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    mSelectTextPaint);
            //画日程文本
            if (itemBean != null) {
                if (itemBean.getStatus() == 2) {
                    canvas.drawText(getResources().getStringArray(R.array.scheduleStatus)[2], cx, mTextBaseLine + y + mItemHeight / 10, mSelectedLunarTextPaint);
                } else {
                    canvas.drawText(itemBean.getId() + "", cx, mTextBaseLine + y + mItemHeight / 10, mSelectedLunarTextPaint);
                }
            }
        } else {
            canvas.drawText(String.valueOf(calendar.getDay()), cx, mTextBaseLine + top,
                    calendar.isCurrentDay() ? mCurDayTextPaint :
                            calendar.isCurrentMonth() ? mCurMonthTextPaint : mOtherMonthTextPaint);
            //画日程文本
            if (itemBean != null) {
                if (itemBean.getStatus() == 2) {
                    canvas.drawText(getResources().getStringArray(R.array.scheduleStatus)[2], cx, mTextBaseLine + y + mItemHeight / 10,
                            calendar.isCurrentDay() ? mCurDayLunarTextPaint :
                                    calendar.isCurrentMonth() ? !TextUtils.isEmpty(calendar.getSolarTerm()) ? mSolarTermTextPaint :
                                            mCurMonthLunarTextPaint : mOtherMonthLunarTextPaint);
                } else {
                    canvas.drawText(itemBean.getId() + "", cx, mTextBaseLine + y + mItemHeight / 10,
                            calendar.isCurrentDay() ? mCurDayLunarTextPaint :
                                    calendar.isCurrentMonth() ? !TextUtils.isEmpty(calendar.getSolarTerm()) ? mSolarTermTextPaint :
                                            mCurMonthLunarTextPaint : mOtherMonthLunarTextPaint);
                }
            }
        }
    }

    @Override
    public void onClick(View v) {
        super.onClick(v);
        Calendar calendar = getIndex();
        String date = calendar.getYear() + "-" + calendar.getMonth() + "-" + calendar.getDay();
        ScheduleItemBean itemBean = ScheduleCalendarPresenter.Companion.getScheduleByDate(date);
    }

    /**
     * dp转px
     *
     * @param context context
     * @param dpValue dp
     * @return px
     */
    private static int dipToPx(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
}