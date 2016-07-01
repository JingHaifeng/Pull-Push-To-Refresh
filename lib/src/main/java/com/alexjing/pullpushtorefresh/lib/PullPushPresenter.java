package com.alexjing.pullpushtorefresh.lib;

import android.view.MotionEvent;

/**
 * @author: haifeng jing(haifeng_jing@kingdee.com)
 * @date: 2016-06-30
 * @time: 15:57
 */
public class PullPushPresenter implements PullPushConstants.Presenter {

    private PullPushConstants.View mView;


    private float mDownY;
    private float mLastMoveY;
    private float mCurrentMoveY;
    private float mEndY;
    private float mOffset;

    // 方向
    private int mDirection = 0;

    public PullPushPresenter(PullPushConstants.View view) {
        mView = view;
        mView.setPresenter(this);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent motionEvent) {
        // action Down 开始输入
        // action Move 计算输入并反馈
        // action Up/Cancel 结算状态并反馈

        final int action = motionEvent.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownY = motionEvent.getY();
                mCurrentMoveY = mDownY;
                break;
            case MotionEvent.ACTION_MOVE:
                mLastMoveY = mCurrentMoveY;
                mCurrentMoveY = motionEvent.getY();

                mDirection = mCurrentMoveY - mDirection > 0 ? 1 :
                        mCurrentMoveY - mDirection < 0 ? -1 : 0;

                if (!mView.canVerticalScroll(-mDirection)) {
                    mOffset = mCurrentMoveY - mLastMoveY;
                }
                mView.updateContentPosition(mOffset);
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mOffset != 0) {
                    mView.updateContentPosition(-mOffset);
                }
                break;
        }

        return true;
    }

    @Override
    public void subscribe() {

    }

    @Override
    public void unsubscribe() {

    }
}
