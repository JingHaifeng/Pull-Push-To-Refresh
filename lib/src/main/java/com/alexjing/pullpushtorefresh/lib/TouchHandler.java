package com.alexjing.pullpushtorefresh.lib;

import android.view.MotionEvent;

/**
 * @author: haifeng jing(haifeng_jing@kingdee.com)
 * @date: 2016-07-01
 * @time: 09:51
 */
public interface TouchHandler {

    boolean down(MotionEvent ev);

    boolean move(MotionEvent ev, boolean canScrollUp, boolean canScrollDown);

    boolean up(MotionEvent ev);
}
