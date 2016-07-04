package com.alexjing.pullpushtorefresh.lib.annotation;

import android.support.annotation.IntDef;

import com.alexjing.pullpushtorefresh.lib.PullStateConstants;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

/**
 * @author: haifeng jing(haifeng_jing@kingdee.com)
 * @date: 2016-07-04
 * @time: 15:14
 */
@Retention(RetentionPolicy.SOURCE)
@IntDef({PullStateConstants.STATE_INIT, PullStateConstants.STATE_PULL_DOWN,
        PullStateConstants.STATE_PULL_UP, PullStateConstants.STATE_PREPARE_DOWN,
        PullStateConstants.STATE_PREPARE_UP, PullStateConstants.STATE_LOADING_DOWN,
        PullStateConstants.STATE_LOADING_UP, PullStateConstants.STATE_COMPLETE_DOWN,
        PullStateConstants.STATE_COMPLETE_UP,})
public @interface PullState {

}
