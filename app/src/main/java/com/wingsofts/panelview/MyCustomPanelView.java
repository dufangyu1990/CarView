package com.wingsofts.panelview;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by dufangyu on 2016/6/28.
 */
public class MyCustomPanelView extends View {
    private int mWidth;
    private int mHeight;

    private int mPercent;

    //刻度宽度
    private float mTikeWidth;

    //第二个弧的宽度
    private int mScendArcWidth;





    //文字的大小
    private int mTextSize;

    //设置文字颜色
    private int mTextColor;
    private int mArcColor;


    //刻度的个数
    private int mTikeCount;

    private Context mContext;
    private Paint mPaint;
    private Paint textPaint;

    public MyCustomPanelView(Context context) {
        this(context, null);
    }

    public MyCustomPanelView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public MyCustomPanelView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        mContext = context;
        init();
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.PanelView, defStyleAttr, 0);
        mArcColor = a.getColor(R.styleable.PanelView_arcColor,Color.parseColor("#87cefa"));
        mTikeCount = a.getInt(R.styleable.PanelView_tikeCount, 50);
        mTextSize = a.getDimensionPixelSize(PxUtils.spToPx(R.styleable.PanelView_android_textSize, mContext), 24);
        mScendArcWidth = 50;
        a.recycle();
    }


    private void init()
    {
        mPaint = new Paint();
        textPaint = new Paint();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize =  MeasureSpec.getSize(heightMeasureSpec);
        int heightMode =  MeasureSpec.getMode(heightMeasureSpec);

        if(widthMode==MeasureSpec.EXACTLY)
        {
            mWidth = widthSize;
        }else if(widthMode == MeasureSpec.AT_MOST)
        {
            mWidth = Math.min(PxUtils.dpToPx(300,mContext),widthSize);
        }

        if(heightMode ==MeasureSpec.EXACTLY)
        {
            mHeight = heightSize;
        }else if(heightMode ==MeasureSpec.AT_MOST)
        {
            mHeight = Math.min(PxUtils.dpToPx(300,mContext),heightSize);
        }
        setMeasuredDimension(mWidth, mHeight);
    }


    @Override
    protected void onDraw(Canvas canvas) {

        int strokeWidth = 30;
        mPaint.setStrokeWidth(strokeWidth);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setAntiAlias(true);
        textPaint.setStyle(Paint.Style.STROKE);
        textPaint.setAntiAlias(true);
        mPaint.setColor(mArcColor);

        //绘制里面的粗圆弧
        mPaint.setStrokeWidth(mScendArcWidth);
        RectF secondRectF = new RectF(strokeWidth+70,strokeWidth+70,mWidth-strokeWidth-70,mHeight-strokeWidth-70);

        RectF secondRectF2 = new RectF(strokeWidth+100,strokeWidth+100,mWidth-strokeWidth-100,mHeight-strokeWidth-100);
        float percent = mPercent/100f;
        //充满的圆弧的度数
        float fill = 250*percent;
        //空的圆弧的度数
        float empty = 250-fill;
        if(percent==0){
            mPaint.setColor(Color.WHITE);
        }

        //画粗弧突出部分左端
        canvas.drawArc(secondRectF,135,10,false,mPaint);

        //画圆弧充满的部分
        canvas.drawArc(secondRectF,145,fill,false,mPaint);
        mPaint.setColor(Color.WHITE);
        //画空白处
        canvas.drawArc(secondRectF, 145 + fill, empty, false, mPaint);
        //画粗弧突出部分右端
        if(percent == 1){
            mPaint.setColor(mArcColor);
        }
        canvas.drawArc(secondRectF, 145 + fill + empty, 10, false, mPaint);

        mPaint.setColor(mArcColor);


        mPaint.setStrokeWidth(mScendArcWidth);
        mPaint.setColor(Color.rgb(30,144,255));
        canvas.drawArc(secondRectF2, 135, 270, false, mPaint);


        //绘制刻度
        mPaint.setColor(mArcColor);
        //绘制顶部第一条刻度
        mTikeWidth=20;
        mPaint.setStrokeWidth(3);
        canvas.drawLine(mWidth / 2, mTikeWidth * 2, mWidth / 2, mTikeWidth * 3, mPaint);
        textPaint.setTextSize(30);
        mTextColor =Color.rgb(30,144,255);
        textPaint.setColor(mTextColor);
        canvas.drawText(50 + "", mWidth / 2, mTikeWidth, textPaint);
        //旋转的角度
        float rAngle = 250f/mTikeCount;
        canvas.save();
        //通过旋转画布 绘制右面的刻度
        for(int i =1;i<mTikeCount/2+1;i++)
        {
            canvas.rotate(rAngle, mWidth / 2, mHeight / 2);

            canvas.drawLine(mWidth / 2, mTikeWidth * 2, mWidth / 2, mTikeWidth * 3, mPaint);

                    if(i%5==0)
                    {
                        textPaint.setTextSize(mTextSize);
                        mTextColor =Color.rgb(30,144,255);
                        textPaint.setColor(mTextColor);
                        canvas.drawText(50 + (i/ 5) * 10 + "", mWidth / 2, mTikeWidth , textPaint);
                    }



        }
        //现在需要将将画布旋转回来
        canvas.restore();
        //通过旋转画布 绘制左面的刻度
        canvas.save();
        for(int i =1;i<mTikeCount/2+1;i++)
        {
            canvas.rotate(-rAngle, mWidth / 2, mHeight / 2);
            canvas.drawLine(mWidth / 2, mTikeWidth * 2, mWidth / 2, mTikeWidth * 3, mPaint);
            if(i%5==0)
            {
                textPaint.setTextSize(mTextSize);
                mTextColor =Color.rgb(30,144,255);
                textPaint.setColor(mTextColor);
                canvas.drawText(50 - (i/ 5) * 10 + "", mWidth / 2, mTikeWidth , textPaint);
            }


        }
        //现在需要将将画布旋转回来
        canvas.restore();



        //居中写文本信息
        Rect targetRect = new Rect(strokeWidth+100,strokeWidth+100,mWidth-strokeWidth-100,mHeight-strokeWidth-100);
        textPaint.setFlags(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStrokeWidth(0);
        textPaint.setColor(Color.TRANSPARENT);
        textPaint.setTextSize(PxUtils.spToPx(30, mContext));
        canvas.drawRect(targetRect, textPaint);
        mTextColor =Color.rgb(30,144,255);
        textPaint.setColor(mTextColor);
        Paint.FontMetricsInt fontMetrics = textPaint.getFontMetricsInt();
        int baseline = (targetRect.bottom + targetRect.top - fontMetrics.bottom - fontMetrics.top) / 2;
        textPaint.setTextAlign(Paint.Align.CENTER);
        canvas.drawText(mPercent+"", targetRect.centerX(), baseline, textPaint);



        super.onDraw(canvas);
    }



    /**
     * 设置百分比
     * @param percent
     */
    public void setPercent(int percent) {
        mPercent = percent;
        invalidate();
    }


    /**
     * 设置圆弧颜色
     * @param color
     */

    public void setArcColor(int color){
        mArcColor = color;

        invalidate();
    }



}
