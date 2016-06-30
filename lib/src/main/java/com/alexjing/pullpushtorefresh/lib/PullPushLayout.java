package com.alexjing.pullpushtorefresh.lib;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;

/**
 * @author: haifeng jing(haifeng_jing@kingdee.com)
 * @date: 2016-06-30
 * @time: 14:37
 */
public class PullPushLayout extends ViewGroup implements PullPushConstants.View {
    private static final String TAG = "PullPushLayout";
    private View mHeaderView;
    private View mFooterView;
    private View mContentView;

    private int mHeaderId;
    private int mFooterId;
    private int mContentId;

    private PullPushConstants.Presenter mPresenter;

    public PullPushLayout(Context context) {
        this(context, null);
    }

    public PullPushLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullPushLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPresenter = new PullPushPresenter(this);
    }

    @Override
    protected void onFinishInflate() {
        final int childCount = getChildCount();
        if (childCount == 0) {

        } else if (childCount == 1) {
            // 默认没有 header & footer
            mContentView = getChildAt(0);
        } else if (childCount == 2) {
            // 默认没有 footer
            // TODO: 16-6-30 加入 id 判定

            mHeaderView = getChildAt(0);
            mContentView = getChildAt(1);
        } else if (childCount == 3) {
            // 分别为 footer & context & footer
            mHeaderView = getChildAt(0);
            mContentView = getChildAt(1);
            mFooterView = getChildAt(2);
        }
        super.onFinishInflate();
    }

    // 分发事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isEnabled() || (mContentView == null //||(mHeaderView == null && mFooterView == null)
        )) {
            return super.dispatchTouchEvent(ev);
        }

        return mPresenter.dispatchTouchEvent(ev);
    }

    // 测量、布局
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mContentView != null) {
            measureContentView(mContentView, widthMeasureSpec, heightMeasureSpec);
        }
    }

    private void measureContentView(View child, int parentWidthMeasureSpec,
                                    int parentHeightMeasureSpec) {
        final MarginLayoutParams lp = (MarginLayoutParams) child.getLayoutParams();

        final int childWidthMeasureSpec = getChildMeasureSpec(parentWidthMeasureSpec,
                                                              getPaddingLeft() + getPaddingRight() +
                                                              lp.leftMargin + lp.rightMargin,
                                                              lp.width);
        final int childHeightMeasureSpec = getChildMeasureSpec(parentHeightMeasureSpec,
                                                               getPaddingTop() +
                                                               getPaddingBottom() + lp.topMargin,
                                                               lp.height);

        child.measure(childWidthMeasureSpec, childHeightMeasureSpec);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        layoutChild();
    }

    private void layoutChild() {
        int paddingLeft = getPaddingLeft();
        int paddingTop = getPaddingTop();
        if (mContentView != null) {
            MarginLayoutParams lp = (MarginLayoutParams) mContentView.getLayoutParams();
            final int left = lp.leftMargin + paddingLeft;
            final int top = lp.topMargin + paddingTop;
            final int right = left + mContentView.getMeasuredWidth();
            final int bottom = top + mContentView.getMeasuredHeight();

            mContentView.layout(left, top, right, bottom);
        }
    }


    @Override
    protected boolean checkLayoutParams(ViewGroup.LayoutParams p) {
        return p != null && p instanceof LayoutParams;
    }

    @Override
    protected ViewGroup.LayoutParams generateDefaultLayoutParams() {
        return new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    }

    @Override
    protected ViewGroup.LayoutParams generateLayoutParams(ViewGroup.LayoutParams p) {
        return new LayoutParams(p);
    }

    @Override
    public ViewGroup.LayoutParams generateLayoutParams(AttributeSet attrs) {
        return new LayoutParams(getContext(), attrs);
    }

    @Override
    public void updateContentPosition(float vertical) {
        Log.d(TAG, "vertical:" + vertical);
        mContentView.offsetTopAndBottom((int) vertical);
    }

    @Override
    public void setPresenter(PullPushConstants.Presenter presenter) {
        mPresenter = presenter;
    }

    // 创建统一 MarginLayoutParams
    public static class LayoutParams extends MarginLayoutParams {

        public LayoutParams(Context c, AttributeSet attrs) {
            super(c, attrs);
        }

        public LayoutParams(int width, int height) {
            super(width, height);
        }

        @SuppressWarnings({"unused"})
        public LayoutParams(MarginLayoutParams source) {
            super(source);
        }

        public LayoutParams(ViewGroup.LayoutParams source) {
            super(source);
        }
    }
}
