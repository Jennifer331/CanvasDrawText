package com.example.administrator.texttest;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.text.TextPaint;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import java.text.BreakIterator;

/**
 * Created by Lei Xiaoyue on 2015-12-01.
 */
public class TextView extends View {
    private static final String TAG = "TextView";
    private static final int TEXT_SIZE = 25;
    private static final int MARGING_RIGHT = 50;
    private static final int MARGING_LEFT = 50;
    private static final int MARGING_TOP = 50;
    private static final int MARGING_BOTTOM = 50;
    private static final int PADDING_RIGHT = 10;
    private static final int PADDING_LEFT = 10;
    private static final int PADDING_TOP = 10;
    private static final int PADDING_BOTTOM = 10;
    private static final int MIN_WIDTH = 50;
    private static final int MIN_HEIGHT = 50;
    private static final String TEST_TEXT = "When the bombs fell on our harbour and tyranny "
            + "threatened the world, she was there to witness a generation rise to greatness and"
            + " a democracy was saved. Yes, we can.";
    private static final String TEST_TEXT2 = "I LOVE YOU";

    private BreakIterator mBoundary;
    private TextPaint mTextPaint;
    private Paint mBoundPaint;
    private Rect mTextBound;
    private int mMinWidth;
    private int mMinHeight;
    private int mMaxWidth;
    private int mMaxHeight;
    private int mLineHeight;

    private int mLastX;
    private int mLastY;

    public TextView(Context context) {
        super(context);
        mBoundary = BreakIterator.getWordInstance();
        Log.v(TAG, "WORDS COUNT:" + mBoundary.DONE);
        mTextPaint = new TextPaint();
        mBoundPaint = new Paint();
        mMinWidth = MIN_WIDTH > PADDING_LEFT + PADDING_RIGHT ? MIN_WIDTH
                : PADDING_LEFT + PADDING_RIGHT;
        mMinHeight = MIN_HEIGHT > PADDING_TOP + PADDING_BOTTOM ? MIN_HEIGHT
                : PADDING_TOP + PADDING_BOTTOM;
        initPaint();
    }

    private void initPaint() {
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.BLACK);
        mTextPaint.setTextSize(TEXT_SIZE);
        mLineHeight = (int) (mTextPaint.descent() - mTextPaint.ascent());

        mBoundPaint.setAntiAlias(true);
        mBoundPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        int width = getWidth();
        int height = getHeight();
        mTextBound = new Rect(MARGING_LEFT, MARGING_TOP, width - MARGING_RIGHT,
                height - MARGING_BOTTOM);
        mMaxWidth = width - MARGING_LEFT - MARGING_RIGHT;
        mMaxHeight = height - MARGING_TOP - MARGING_BOTTOM;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(mTextBound, mBoundPaint);
        String leftString = TEST_TEXT;
        int x = mTextBound.left + PADDING_LEFT;
        int y = (int) (mTextBound.top + PADDING_TOP - mTextPaint.ascent());
        int width = mTextBound.width() - PADDING_LEFT - PADDING_RIGHT;
        while (leftString.length() > 0 && y < mTextBound.bottom - mTextPaint.ascent()) {
            mBoundary.setText(leftString);
            int measure =(int) mTextPaint.measureText(leftString);
            if(width > measure){
                int offset = (int)((width - measure)/2);
                canvas.drawText(leftString,x + offset,y,mTextPaint);
                break;
            }
            int number = mTextPaint.breakText(leftString, true, width, null);
            int end = mBoundary.preceding(number);
            if(0 == end){
                end = number;
            }
            String print = leftString.substring(0, end);
            int offset = (int)((width - mTextPaint.measureText(print))/2);
            canvas.save();
            canvas.clipRect(mTextBound);
            canvas.drawText(print, x + offset, y, mTextPaint);
            canvas.restore();
            y += mLineHeight;
            leftString = leftString.substring(end);
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN: {
                mLastX = (int) event.getX();
                mLastY = (int) event.getY();
                break;
            }
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_MOVE: {
                int x = (int) event.getX();
                int y = (int) event.getY();
                adjustBound(x - mLastX, y - mLastY);
                mLastX = x;
                mLastY = y;
            }
        }
        return true;
    }

    private void adjustBound(int deltaX, int deltaY) {
        int destWidth = mTextBound.width() + deltaX;
        int destHeight = mTextBound.height() + deltaY;
        if (destWidth > mMinWidth && destWidth < mMaxWidth) {
            int offsetX = deltaX / 2;
            mTextBound.left -= offsetX;
            mTextBound.right += offsetX;
        }
        if (destHeight > mMinHeight && destHeight < mMaxHeight) {
            int offsetY = deltaY / 2;
            mTextBound.top -= offsetY;
            mTextBound.bottom += offsetY;
        }
        invalidate();
    }
}
