package in.srain.cube.views.ptr;

import in.srain.cube.views.ptr.indicator.PtrIndicator;

/**
 * A single linked list to wrap PtrUIHandler
 */
class PtrUIHandlerHolder implements PtrUIHandler {

    private PtrUIHandler mHeaderHandler;
    private PtrUIHandler mFooterHandler;

    private boolean containsHeader(PtrUIHandler handler) {
        return mHeaderHandler != null && mHeaderHandler == handler;
    }

    private boolean containsFooter(PtrUIHandler handler) {
        return mFooterHandler != null && mFooterHandler == handler;
    }

    private PtrUIHandlerHolder() {

    }

    public boolean hasHeaderHandler() {
        return mHeaderHandler != null;
    }

    public boolean hasFooterHandler() {
        return mFooterHandler != null;
    }

    private PtrUIHandler getHeaderHandler() {
        return mHeaderHandler;
    }

    public PtrUIHandler getFooterHandler() {
        return mFooterHandler;
    }

    public static void addHeaderHandler(PtrUIHandlerHolder holder, PtrUIHandler handler) {

        if (null == handler) {
            return;
        }
        if (holder == null) {
            return;
        }
        if (null == holder.mHeaderHandler) {
            holder.mHeaderHandler = handler;
        }
    }


    public static void addFooterHandler(PtrUIHandlerHolder holder, PtrUIHandler handler) {

        if (null == handler) {
            return;
        }
        if (holder == null) {
            return;
        }
        if (null == holder.mFooterHandler) {
            holder.mFooterHandler = handler;
        }
    }



    public static PtrUIHandlerHolder create() {
        return new PtrUIHandlerHolder();
    }

    public static PtrUIHandlerHolder removeHeaderHandler(PtrUIHandlerHolder head, PtrUIHandler handler) {
        if (head == null || handler == null) {
            return head;
        }
        if (head.containsHeader(handler)) {
            head.mHeaderHandler = null;
        }
        return head;
    }
    public static PtrUIHandlerHolder removeFooterHandler(PtrUIHandlerHolder head, PtrUIHandler handler) {
        if (head == null || handler == null) {
            return head;
        }
        if (head.containsFooter(handler)) {
            head.mFooterHandler = null;
        }
        return head;
    }

    @Override
    public void onUIReset(PtrFrameLayout frame) {
        PtrUIHandlerHolder current = this;
        final PtrUIHandler headerHandler = current.getHeaderHandler();
        final PtrUIHandler footerHandler = current.getFooterHandler();
        if (isHeaderTask(frame.getStatus()))
        if (null != headerHandler) {
            headerHandler.onUIReset(frame);
        }
        if (null != footerHandler) {
            footerHandler.onUIReset(frame);
        }
    }

    @Override
    public void onUIRefreshPrepare(PtrFrameLayout frame) {
        if (!hasHeaderHandler() && !hasFooterHandler()) {
            return;
        }
        PtrUIHandlerHolder current = this;
        final PtrUIHandler headerHandler = current.getHeaderHandler();
        final PtrUIHandler footerHandler = current.getFooterHandler();
        if (isHeaderTask(frame.getStatus())) {
            if (null != headerHandler) {
                headerHandler.onUIRefreshPrepare(frame);
            }
        }else {
            if (null != footerHandler) {
                footerHandler.onUIRefreshPrepare(frame);
            }
        }
    }

    @Override
    public void onUIRefreshBegin(PtrFrameLayout frame) {
        PtrUIHandlerHolder current = this;
        final PtrUIHandler headerHandler = current.getHeaderHandler();
        final PtrUIHandler footerHandler = current.getFooterHandler();
        if (isHeaderTask(frame.getStatus())) {
            if (null != headerHandler) {
                headerHandler.onUIRefreshBegin(frame);
            }
        } else {
            if (null != footerHandler) {
                footerHandler.onUIRefreshBegin(frame);
            }
        }
    }

    @Override
    public void onUIRefreshComplete(PtrFrameLayout frame) {
        PtrUIHandlerHolder current = this;
        final PtrUIHandler headerHandler = current.getHeaderHandler();
        final PtrUIHandler footerHandler = current.getFooterHandler();
        if (isHeaderTask(frame.getStatus())) {
            if (null != headerHandler) {
                headerHandler.onUIRefreshComplete(frame);
            }
        } else {
            if (null != footerHandler) {
                footerHandler.onUIRefreshComplete(frame);
            }
        }
    }

    @Override
    public void onUIPositionChange(PtrFrameLayout frame, boolean isUnderTouch, byte status, PtrIndicator ptrIndicator) {
        PtrUIHandlerHolder current = this;
        final PtrUIHandler headerHandler = current.getHeaderHandler();
        final PtrUIHandler footerHandler = current.getFooterHandler();
        if (isHeaderTask(frame.getStatus())) {
            if (null != headerHandler) {
                headerHandler.onUIPositionChange(frame, isUnderTouch, status, ptrIndicator);
            }
        } else{
            if (null != footerHandler) {
                footerHandler.onUIPositionChange(frame, isUnderTouch, status, ptrIndicator);
            }
        }
    }

    private boolean isHeaderTask(int status) {
        return status == PtrFrameLayout.PTR_STATUS_COMPLETE_HEADER ||
               status == PtrFrameLayout.PTR_STATUS_PREPARE_HEADER ||
               status == PtrFrameLayout.PTR_STATUS_LOADING_HEADER;
    }

}
