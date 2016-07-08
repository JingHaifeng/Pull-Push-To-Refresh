package in.srain.cube.views.ptr;

import android.view.View;

/**
 * @author: haifeng jing(haifeng_jing@kingdee.com)
 * @date: 2016-07-08
 * @time: 13:43
 */
public abstract class PtrDefHandler implements PtrHandler {

    @Override
    public boolean checkCanPullDown(PtrFrameLayout frame, View content) {
        if (frame.getPtrIndicator().getCurrentPosY() < 0) {
            return false;
        }
        return content.canScrollVertically(-1);
    }

    @Override
    public boolean checkCanPullUp(PtrFrameLayout frame, View content) {
        if (frame.getPtrIndicator().getCurrentPosY() > 0) {
            return false;
        }
        return content.canScrollVertically(1);
    }

    @Override
    public abstract void onRefreshBegin(PtrFrameLayout frame);
}
