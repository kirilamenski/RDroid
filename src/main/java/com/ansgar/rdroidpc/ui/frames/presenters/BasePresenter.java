package com.ansgar.rdroidpc.ui.frames.presenters;

import com.ansgar.rdroidpc.ui.frames.views.BaseFrameView;

public class BasePresenter <T extends BaseFrameView> {

    protected T view;

    public BasePresenter(T view) {
        this.view = view;
    }

    public void destroy() {
    }
}
