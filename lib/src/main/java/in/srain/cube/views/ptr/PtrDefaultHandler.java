package in.srain.cube.views.ptr;

import android.view.View;
import android.widget.AbsListView;

public abstract class PtrDefaultHandler implements PtrHandler {

    public static boolean canChildScrollUp(View view) {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (view instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) view;
                return absListView.getChildCount() > 0
                        && (absListView.getFirstVisiblePosition() > 0 || absListView.getChildAt(0)
                        .getTop() < absListView.getPaddingTop());
            } else {
                return view.getScrollY() > 0;
            }
        } else {
            return view.canScrollVertically(-1);
        }
    }
    public static boolean canChildScrollDown(View view) {
        if (android.os.Build.VERSION.SDK_INT < 14) {
            if (view instanceof AbsListView) {
                final AbsListView absListView = (AbsListView) view;
                final int count = absListView.getCount();
                final int childCount = absListView.getChildCount();
                return count > 0
                        && (absListView.getLastVisiblePosition() < count-1 || absListView.getChildAt(childCount-1)
                        .getBottom() > absListView.getBottom());
            } else {
                return view.getScrollY() > 0;
            }
        } else {
            return view.canScrollVertically(1);
        }
    }

    /**
     * Default implement for check can perform pull to refresh
     *
     * @param frame
     * @param content
     * @param target
     * @return
     */
    public static boolean checkContentCanBePull(PtrFrameLayout frame, View content, View target) {
        if (frame.isRefreshing()) {
            return false;
        }
        if (frame.getHeaderView() != null && frame.getHeaderView() == target) {
            return !canChildScrollUp(content);
        } else if (frame.getFooterView() != null && frame.getFooterView() == target){
            return !canChildScrollDown(content);
        }
        return false;
    }

    @Override
    public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View target) {
        return checkContentCanBePull(frame, content, target);
    }
}