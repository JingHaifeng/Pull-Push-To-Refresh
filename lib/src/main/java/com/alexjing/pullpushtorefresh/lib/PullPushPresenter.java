package com.alexjing.pullpushtorefresh.lib;

import android.view.MotionEvent;

/**
 * @author: haifeng jing(haifeng_jing@kingdee.com)
 * @date: 2016-06-30
 * @time: 15:57
 */
public class PullPushPresenter implements PullPushConstants.Presenter {

    private PullPushConstants.View mView;


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
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
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
