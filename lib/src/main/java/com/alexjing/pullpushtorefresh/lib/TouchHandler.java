package com.alexjing.pullpushtorefresh.lib;

import android.view.MotionEvent;
import android.view.View;

/**
 * @author: haifeng jing(haifeng_jing@kingdee.com)
 * @date: 2016-07-01
 * @time: 09:51
 */
public class TouchHandler {
    // 方向：-1 向下 ，1 向上 ，0 不变
    private int mDirect = 0;

    private float mDownY;

    private float mLastMoveY;

    private float mCurrentMoveY;

    private float mFinishY;

    private float mOffset;

    public void calculateMotion(MotionEvent ev) {
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
                mCurrentMoveY = mDownY;
                break;
            case MotionEvent.ACTION_MOVE:
                mLastMoveY = mCurrentMoveY;
                mCurrentMoveY = ev.getY();

                mDirect = mCurrentMoveY - mLastMoveY > 0 ? -1 :
                        mCurrentMoveY - mLastMoveY < 0 ? 1 : 0;

                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mFinishY = ev.getY();
                break;
        }
    }

    private float getOffset() {
        return getOffset();
    }

}
