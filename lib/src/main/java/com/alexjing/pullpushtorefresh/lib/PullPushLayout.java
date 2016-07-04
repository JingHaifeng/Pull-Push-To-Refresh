package com.alexjing.pullpushtorefresh.lib;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Scroller;

import com.alexjing.pullpushtorefresh.lib.annotation.PullState;

import static com.alexjing.pullpushtorefresh.lib.PullStateConstants.STATE_COMPLETE_DOWN;
import static com.alexjing.pullpushtorefresh.lib.PullStateConstants.STATE_COMPLETE_UP;
import static com.alexjing.pullpushtorefresh.lib.PullStateConstants.STATE_INIT;
import static com.alexjing.pullpushtorefresh.lib.PullStateConstants.STATE_LOADING_DOWN;
import static com.alexjing.pullpushtorefresh.lib.PullStateConstants.STATE_LOADING_UP;
import static com.alexjing.pullpushtorefresh.lib.PullStateConstants.STATE_PREPARE_DOWN;
import static com.alexjing.pullpushtorefresh.lib.PullStateConstants.STATE_PREPARE_UP;
import static com.alexjing.pullpushtorefresh.lib.PullStateConstants.STATE_PULL_DOWN;
import static com.alexjing.pullpushtorefresh.lib.PullStateConstants.STATE_PULL_UP;

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
    private int mFooterHeight;

    private TouchManager mTouchManager;

    private ScrollChecker mScrollChecker;

    public PullPushLayout(Context context) {
        this(context, null);
    }

    public PullPushLayout(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullPushLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPresenter = new PullPushPresenter(this);

        mTouchManager = new TouchManager();

        setVerticalFadingEdgeEnabled(false);

        mScrollChecker = new ScrollChecker();
    }

    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        // 回收 ScrollerChecker
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
            mContentView.setVerticalFadingEdgeEnabled(false);
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
                mTouchManager.down(ev);
                mScrollChecker.abortIfWorking();
                super.dispatchTouchEvent(ev);
                return true;
            case MotionEvent.ACTION_MOVE:
                if (mTouchManager.move(ev, mContentView.canScrollVertically(1),
                                       mContentView.canScrollVertically(-1))) {
                    int offset = mTouchManager.mCurrentOffset;
                    updatePosition(offset);
                    return true;
                }
                break;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                if (mTouchManager.up(ev)) {
//                    layoutChild();
                    mScrollChecker.tryToScrollTo(0,1000);
                }

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
            MarginLayoutParams lp = (MarginLayoutParams) mFooterView.getLayoutParams();
            mFooterHeight = mFooterView.getMeasuredHeight() + lp.topMargin + lp.bottomMargin;
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

    public void updatePosition(int offset) {
        mContentView.offsetTopAndBottom(offset);
        mHeaderView.offsetTopAndBottom(offset);
        mFooterView.offsetTopAndBottom(offset);
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

    private class TouchManager implements TouchHandler {
        @PullState
        private int mPullState = STATE_INIT;

        private int mDownY;

        private int mLastMoveY;

        private int mCurrentMoveY;

        private int mSumOffset;

        private float mOffsetPercent = 0f;

        private float mCurrentOffsetPercent;

        private int mCurrentOffset;

        // 最大的系数
        private float mMaxCoefficient = 1f;

        public TouchManager() {
        }

        @Override
        public boolean down(MotionEvent ev) {
            mDownY = (int) ev.getY();
            mCurrentMoveY = mDownY;
            return false;
        }

        @Override
        public boolean move(MotionEvent ev, boolean canScrollDown, boolean canScrollUp) {
            mLastMoveY = mCurrentMoveY;
            mCurrentMoveY = (int) ev.getY();

            int offset = mCurrentMoveY - mLastMoveY;

            // offset > 0 下拉状态
            // offset < 0 上滑状态
            // 通过能否上下滚动来确定状态

            if (offset > 0 && mSumOffset >= 0 && canScrollUp) {
                mPullState = STATE_INIT;
                return false;
            }

            if (offset < 0 && mSumOffset <= 0 && canScrollDown) {
                mPullState = STATE_INIT;
                return false;
            }

            if (mSumOffset > 0) {
                mPullState = STATE_PULL_DOWN;
            } else if (mSumOffset < 0) {
                mPullState = STATE_PULL_UP;
            } else {
                mPullState = STATE_INIT;
            }

            int lastSumOffset = mSumOffset;
            setOffset(offset);

            offset = mSumOffset - lastSumOffset;

            mCurrentOffset = offset;

            float lastOffsetPercent = mOffsetPercent;

            if (mCurrentOffset > 0) {
                mOffsetPercent = mSumOffset / (mHeaderHeight * mMaxCoefficient);
            } else {
                mOffsetPercent = mSumOffset / (mFooterHeight * mMaxCoefficient);
            }

            mCurrentOffsetPercent = mOffsetPercent - lastOffsetPercent;
            return true;
        }

        @Override
        public boolean up(MotionEvent ev) {
            if (mSumOffset == 0) {
                return false;
            }
            // 当header 或 footer 完全显示的情况下 触发刷新
            if (mSumOffset > 0 && mSumOffset >= mHeaderHeight) {
                mPullState = STATE_PREPARE_DOWN;
            } else if (mSumOffset < 0 && Math.abs(mSumOffset) >= mFooterHeight) {
                mPullState = STATE_PREPARE_UP;
            } else {
                mPullState = mSumOffset > 0 ? STATE_COMPLETE_DOWN : STATE_COMPLETE_UP;
            }

            // 实际判断是否能刷新

            if (canPerformLoading()) {
                if (mPullState == STATE_PREPARE_DOWN) {
                    mPullState = STATE_LOADING_DOWN;
                }

                if (mPullState == STATE_PREPARE_UP) {
                    mPullState = STATE_LOADING_UP;
                }
            } else {

                if (mPullState == STATE_PREPARE_DOWN) {
                    mPullState = STATE_COMPLETE_DOWN;
                }

                if (mPullState == STATE_PREPARE_UP) {
                    mPullState = STATE_COMPLETE_UP;
                }


            }

            if (mCurrentOffset > 0) {
                mOffsetPercent = mSumOffset / mHeaderHeight * mMaxCoefficient;
            } else {
                mOffsetPercent = mSumOffset / mFooterHeight * mMaxCoefficient;
            }
            return true;
        }

        private boolean canPerformLoading() {
            switch (mPullState) {
                case STATE_PREPARE_DOWN:
                    // TODO: 16-7-4
                    break;
                case STATE_PREPARE_UP:
                    break;
            }

            return false;
        }

        public boolean isAlreadyHere(int to) {
            return mSumOffset == to;
        }

        public void setOffset(int offset){

            if (mSumOffset > 0) {
                mSumOffset = Math.max(0, mSumOffset + offset);
            } else if (mSumOffset < 0) {
                mSumOffset = Math.min(0, mSumOffset + offset);
            } else {
                mSumOffset += offset;
            }

            // 限制头和脚被拉的距离
            if (mSumOffset > 0) {
                if (mSumOffset > mHeaderHeight * mMaxCoefficient) {
                    mSumOffset = (int) (mHeaderHeight * mMaxCoefficient);
                }
            } else {
                if (Math.abs(mSumOffset) > mFooterHeight * mMaxCoefficient) {
                    mSumOffset = (int) (-mHeaderHeight * mMaxCoefficient);
                }
            }

        }
    }

    class ScrollChecker implements Runnable {

        private Scroller mScroller;

        private int mStart;
        private int mTo;

        private int mLastY;

        private long mDuration;

        private boolean isRunning;

        public ScrollChecker() {
            mScroller = new Scroller(getContext());
        }

        @Override
        public void run() {
            boolean finish = !mScroller.computeScrollOffset() || mScroller.isFinished();
            int currentY = mScroller.getCurrY();
            int deltaY = currentY - mLastY;
            if (deltaY != 0) {
                Log.d(TAG,"currentY:"+currentY + "  deltaY:"+deltaY);
            }

            if (!finish) {
                mLastY = currentY;
                updatePosition(deltaY);
                mTouchManager.setOffset(deltaY);
                post(this);
            } else {
                finish();
            }
        }

        private void finish() {
            reset();
        }

        private void reset() {
            isRunning = false;
            mLastY = 0;
            removeCallbacks(this);
        }

        public void abortIfWorking() {
            if (isRunning) {
                if (!mScroller.isFinished()) {
                    mScroller.forceFinished(true);
                }
                reset();
            }
        }

        public void tryToScrollTo(int to, long duration) {
            if (mTouchManager.isAlreadyHere(to)) {
                return;
            }

            mDuration = duration;

            mStart = mTouchManager.mSumOffset;
            mTo = to;

            int distance = mTo - mStart;
            removeCallbacks(this);


            if (!mScroller.isFinished()) {
                mScroller.forceFinished(true);
            }

            mLastY = 0;

            mScroller.startScroll(0, 0, 0, distance, (int) mDuration);

            post(this);

            isRunning = true;
        }
    }
}
