package com.alexjing.pullpushtorefresh.lib;

/**
 * @author: haifeng jing(haifeng_jing@kingdee.com)
 * @date: 2016-07-04
 * @time: 15:03
 */
public interface PullStateConstants {
    int STATE_INIT = 1 << 0;

    int STATE_PULL_DOWN = 1 << 1;

    int STATE_PULL_UP = 1 << 2;

    int STATE_PREPARE_DOWN = 1 << 3;

    int STATE_PREPARE_UP = 1 << 4;

    int STATE_LOADING_DOWN = 1 << 5;

    int STATE_LOADING_UP = 1 << 6;

    int STATE_COMPLETE_DOWN = 1 << 7;

    int STATE_COMPLETE_UP = 1 << 8;
}
