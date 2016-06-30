package com.alexjing.pullpushtorefresh.lib;

import android.view.MotionEvent;

import com.alexjing.pullpushtorefresh.lib.base.BasePresenter;
import com.alexjing.pullpushtorefresh.lib.base.BaseView;

/**
 * @author: haifeng jing(haifeng_jing@kingdee.com)
 * @date: 2016-06-30
 * @time: 14:40
 */
public class PullPushConstants {

    interface Presenter extends BasePresenter {

        boolean dispatchTouchEvent(MotionEvent motionEvent);

    }

    interface View extends BaseView<Presenter> {


        void updateContentPosition(float vertical);
    }
}
