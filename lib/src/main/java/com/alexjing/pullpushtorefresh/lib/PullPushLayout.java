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
    private int mHeaderHeight;

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

        if (mContentView != null) {
            mContentView.bringToFront();
        }

        if (mFooterView != null) super.onFinishInflate();
    }

    private float mDownY;
    private float mLastMoveY;
    private float mCurrentMoveY;

    private float mOffsetTotal = 0;

    // 分发事件
    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        if (!isEnabled() || mContentView == null || (mHeaderView == null && mFooterView == null)) {
            return super.dispatchTouchEvent(ev);
        }
        final int action = ev.getAction();
        switch (action) {
            case MotionEvent.ACTION_DOWN:
                mDownY = ev.getY();
                mCurrentMoveY = mDownY;
                mOffsetTotal = 0;
                break;
            case MotionEvent.ACTION_MOVE:
                mLastMoveY = mCurrentMoveY;
                mCurrentMoveY = ev.getY();
                float offset = mCurrentMoveY - mLastMoveY;
                boolean moveDown = offset < 0;
                if (moveDown && !mContentView.canScrollVertically(1)) {
                    mContentView.offsetTopAndBottom((int) offset);
                    mHeaderView.offsetTopAndBottom((int) offset);
                    mFooterView.offsetTopAndBottom((int) offset);

                    mOffsetTotal += offset;
                    return true;
                } else if (!moveDown && !mContentView.canScrollVertically(-1)) {
                    mContentView.offsetTopAndBottom((int) offset);
                    mHeaderView.offsetTopAndBottom((int) offset);
                    mFooterView.offsetTopAndBottom((int) offset);

                    mOffsetTotal += offset;
                    return true;
                }

//                if (Math.signum(mOffsetTotal) != Math.signum(offset)) {
//                    mContentView.offsetTopAndBottom((int) offset);
//                    mHeaderView.offsetTopAndBottom((int) offset);
//                    mFooterView.offsetTopAndBottom((int) offset);
//                    return true;
//                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                mDownY = 0;
                mCurrentMoveY = 0;
                mLastMoveY = 0;
                resetChild();
                break;
        }

        return super.dispatchTouchEvent(ev);
    }

    // 测量、布局
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        if (mHeaderView != null) {
            measureChildWithMargins(mHeaderView, widthMeasureSpec, 0, heightMeasureSpec, 0);
            MarginLayoutParams lp = (MarginLayoutParams) mHeaderView.getLayoutParams();
            mHeaderHeight = mHeaderView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
        }

        if (mContentView != null) {
            measureContentView(mContentView, widthMeasureSpec, heightMeasureSpec);
        }

        if (mFooterView != null) {
            measureChildWithMargins(mFooterView, widthMeasureSpec, 0, heightMeasureSpec, 0);
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

        if (mHeaderView != null) {
            MarginLayoutParams lp = (MarginLayoutParams) mHeaderView.getLayoutParams();
            final int left = lp.leftMargin + paddingLeft;
            final int top = getTop() - mHeaderHeight;
            final int right = left + mHeaderView.getMeasuredWidth();
            final int bottom = top + mHeaderView.getMeasuredHeight();

            mHeaderView.layout(left, top, right, bottom);
        }

        if (mContentView != null) {
            MarginLayoutParams lp = (MarginLayoutParams) mContentView.getLayoutParams();
            final int left = lp.leftMargin + paddingLeft;
            final int top = lp.topMargin + paddingTop;
            final int right = left + mContentView.getMeasuredWidth();
            final int bottom = top + mContentView.getMeasuredHeight();

            mContentView.layout(left, top, right, bottom);
        }


        if (mFooterView != null) {
            MarginLayoutParams lp = (MarginLayoutParams) mFooterView.getLayoutParams();
            final int left = lp.leftMargin + paddingLeft;
            final int top = getBottom() - getPaddingBottom();
            final int right = left + mFooterView.getMeasuredWidth();
            final int bottom = top + mFooterView.getMeasuredHeight();

            mFooterView.layout(left, top, right, bottom);
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
    public boolean canVerticalScroll(int direction) {
        if (mContentView == null) {
            return true;
        }
        return mContentView.canScrollVertically(direction);
    }

    @Override
    public void resetChild() {
        layoutChild();
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
