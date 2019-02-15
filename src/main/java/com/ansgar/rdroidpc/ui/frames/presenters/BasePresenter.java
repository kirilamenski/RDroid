package com.ansgar.rdroidpc.ui.frames.presenters;

import com.ansgar.rdroidpc.ui.frames.views.BaseFrameView;

public class BasePresenter {

    private BaseFrameView view;

    public BasePresenter(BaseFrameView view) {
        this.view = view;
    }

    public void destroy() {
    }
}
