package in.srain.cube.views.ptr;

import android.view.View;

public interface PtrHandler {

    boolean checkCanPullDown(final PtrFrameLayout frame, View content);

    boolean checkCanPullUp(final PtrFrameLayout frame, View content);

    /**
     * When refresh begin
     *
     * @param frame
     */
    public void onRefreshBegin(final PtrFrameLayout frame);
}