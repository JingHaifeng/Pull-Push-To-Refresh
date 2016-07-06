package in.srain.cube.views.ptr.indicator;

import android.graphics.PointF;

public class PtrIndicator {

    public final static int POS_START = 0;
    protected int mHeaderOffsetToRefresh = 0;
    protected int mFooterOffsetToRefresh = 0;
    private PointF mPtLastMove = new PointF();
    private float mOffsetX;
    private float mOffsetY;
    private int mCurrentPos = 0;
    private int mLastPos = 0;
    private int mHeaderHeight;
    private int mFooterHeight;
    private int mPressedPos = 0;

    private float mRatioOfHeaderHeightToRefresh = 1f;
    private float mRatioOfFooterHeightToRefresh = 1f;
    private float mMaxRatioToPull = 1f;
    private float mResistance = 1.7f;
    private boolean mIsUnderTouch = false;
    private int mOffsetToKeepHeaderWhileLoading = -1;
    // record the refresh complete position
    private int mRefreshCompleteY = 0;

    public boolean isUnderTouch() {
        return mIsUnderTouch;
    }

    public float getResistance() {
        return mResistance;
    }

    public void setResistance(float resistance) {
        mResistance = resistance;
    }

    public void onRelease() {
        mIsUnderTouch = false;
    }

    public void onUIRefreshComplete() {
        mRefreshCompleteY = mCurrentPos;
    }

    public boolean goDownCrossFinishPosition() {
        return mCurrentPos != mRefreshCompleteY;
    }

    protected void processOnMove(float currentX, float currentY, float offsetX, float offsetY) {
        setOffset(offsetX, offsetY / mResistance);
    }

    public void setRatioOfHeaderHeightToRefresh(float ratio) {
        mRatioOfHeaderHeightToRefresh = ratio;
        mHeaderOffsetToRefresh = (int) (mHeaderHeight * ratio);
    }

    public void setRatioOfFooterHeightToRefresh(float ratio) {
        mRatioOfFooterHeightToRefresh = ratio;
        mFooterOffsetToRefresh = (int) (mFooterHeight * ratio);
    }

    public float getRatioOfHeaderToHeightRefresh() {
        return mRatioOfHeaderHeightToRefresh;
    }

    public float getRatioOfFooterHeightToRefresh() {
        return mRatioOfFooterHeightToRefresh;
    }

    public int getHeaderOffsetToRefresh() {
        return mHeaderOffsetToRefresh;
    }

    public int getFooterOffsetToRefresh() {
        return mFooterOffsetToRefresh;
    }

    public void setHeaderOffsetToRefresh(int offset) {
        mRatioOfHeaderHeightToRefresh = mHeaderHeight * 1f / offset;
        mHeaderOffsetToRefresh = offset;
    }

    public void setFooterOffsetToRefresh(int offset) {
        mRatioOfFooterHeightToRefresh = mHeaderHeight * 1f / offset;
        mFooterOffsetToRefresh = offset;
    }

    public void onPressDown(float x, float y) {
        mIsUnderTouch = true;
        mPressedPos = mCurrentPos;
        mPtLastMove.set(x, y);
    }

    public final void onMove(float x, float y) {
        float offsetX = x - mPtLastMove.x;
        float offsetY = (y - mPtLastMove.y);
        processOnMove(x, y, offsetX, offsetY);
        mPtLastMove.set(x, y);
    }

    protected void setOffset(float x, float y) {
        mOffsetX = x;
        mOffsetY = y;
    }

    public float getOffsetX() {
        return mOffsetX;
    }

    public float getOffsetY() {
        return mOffsetY;
    }

    public int getLastPosY() {
        return mLastPos;
    }

    public int getCurrentPosY() {
        return mCurrentPos;
    }

    /**
     * Update current position before update the UI
     */
    public final void setCurrentPos(int current) {
        mLastPos = mCurrentPos;
//        if (Math.abs(current) >= Math.abs(getMaxPullHeight())) {
//            current = getMaxPullHeight();
//        }
        mCurrentPos = current;
        onUpdatePos(current, mLastPos);
    }

    protected void onUpdatePos(int current, int last) {

    }

    public int getHeaderHeight() {
        return mHeaderHeight;
    }

    public void setHeaderHeight(int height) {
        mHeaderHeight = height;
        updateHeight();
    }

    public int getFooterHeight() {
        return mFooterHeight;
    }

    public void setFooterHeight(int footerHeight) {
        mFooterHeight = footerHeight;
        updateHeight();
    }

    protected void updateHeight() {
        mHeaderOffsetToRefresh = (int) (mRatioOfHeaderHeightToRefresh * mHeaderHeight);
        mFooterOffsetToRefresh = (int) (mRatioOfFooterHeightToRefresh * mFooterHeight);
    }

    public void convertFrom(PtrIndicator ptrSlider) {
        mCurrentPos = ptrSlider.mCurrentPos;
        mLastPos = ptrSlider.mLastPos;
        mHeaderHeight = ptrSlider.mHeaderHeight;
    }

    public boolean hasLeftStartPosition() {
        return mCurrentPos != POS_START;
    }

    public boolean hasJustLeftStartPosition() {
        return mLastPos == POS_START && hasLeftStartPosition();
    }

    public boolean hasJustPullDown(){
        return hasJustLeftStartPosition() && mCurrentPos > 0;
    }

    public boolean hasJustPullUp() {
        return hasJustLeftStartPosition() && mCurrentPos < 0;
    }

    public boolean hasJustBackToStartPosition() {
        return mLastPos != POS_START && isInStartPosition();
    }

    public boolean isOverOffsetToRefresh() {
        if (mCurrentPos > 0) {
            if (mHeaderHeight == 0 || mHeaderOffsetToRefresh == 0) {
                return false;
            }
            return mCurrentPos >= getHeaderOffsetToRefresh();
        } else if (mCurrentPos < 0){
            if (mFooterHeight == 0 || mFooterOffsetToRefresh == 0) {
                return false;
            }
            return Math.abs(mCurrentPos) >= getFooterOffsetToRefresh();
        } else {
            return false;
        }
    }

    public boolean hasMovedAfterPressedDown() {
        return mCurrentPos != mPressedPos;
    }

    public boolean isInStartPosition() {
        return mCurrentPos == POS_START;
    }

    public boolean crossRefreshLine() {
        if (mCurrentPos > 0) {
            return mLastPos < getHeaderOffsetToRefresh() && mCurrentPos >= getHeaderOffsetToRefresh();
        } else if (mCurrentPos < 0){
            return Math.abs(mLastPos) < getFooterOffsetToRefresh() && Math.abs(mCurrentPos) >= getFooterOffsetToRefresh();
        } else {
            return false;
        }
    }

    public boolean hasJustReachedHeight() {
        if (mCurrentPos > 0) {
            return mLastPos < getHeaderOffsetToRefresh() && mCurrentPos >= mHeaderHeight;
        } else if (mCurrentPos < 0){
            return Math.abs(mLastPos) < getFooterOffsetToRefresh() && Math.abs(mCurrentPos) >= mFooterHeight;
        } else {
            return false;
        }
    }

    public boolean isOverOffsetToKeepHeaderWhileLoading() {
        return Math.abs(mCurrentPos) > Math.abs(getOffsetToKeepHeaderWhileLoading());
    }

    public void setOffsetToKeepHeaderWhileLoading(int offset) {
        mOffsetToKeepHeaderWhileLoading = offset;
    }

    public int getOffsetToKeepHeaderWhileLoading() {
        return mOffsetToKeepHeaderWhileLoading >= 0 ? mOffsetToKeepHeaderWhileLoading :
                mCurrentPos > 0 ?
                mHeaderHeight : -mFooterHeight;
    }

    public boolean isAlreadyHere(int to) {
        return mCurrentPos == to;
    }

    public float getLastPercent() {
        final float oldPercent = mHeaderHeight == 0 ? 0 : mLastPos * 1f / mHeaderHeight;
        return oldPercent;
    }

    public float getCurrentPercent() {
        final float currentPercent = mHeaderHeight == 0 ? 0 : mCurrentPos * 1f / mHeaderHeight;
        return currentPercent;
    }

    @Deprecated
    public boolean willOverTop(int to) {
        return to < POS_START;
    }

    public boolean isMoveDown() {
        return mOffsetY > 0;
    }

    public boolean isMoveUp() {
        return mOffsetY < 0;
    }

    public boolean isPullDown() {
        return mLastPos > 0 || mCurrentPos >= 0;
    }

    public float getMaxRatioToPull() {
        return mMaxRatioToPull;
    }

    public void setMaxRatioToPull(float maxRatioToPull) {
        mMaxRatioToPull = maxRatioToPull;
    }

    public int getMaxPullHeight(){
        if (mCurrentPos > 0) {
            return (int) (mMaxRatioToPull * mHeaderHeight);
        } else {
            return (int) (mMaxRatioToPull * -mFooterHeight);
        }
    }
}