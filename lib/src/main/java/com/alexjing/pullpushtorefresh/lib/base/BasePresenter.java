package com.alexjing.pullpushtorefresh.lib.base;

/**
 * 用于注册与反注册
 *
 * @author: haifeng jing(haifeng_jing@kingdee.com)
 * @date: 2016-05-06
 * @time: 14:42
 */
public interface BasePresenter {

    void subscribe();

    void unsubscribe();
}
