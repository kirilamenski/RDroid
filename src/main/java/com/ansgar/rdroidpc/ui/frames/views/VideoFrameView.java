package com.ansgar.rdroidpc.ui.frames.views;

import com.android.chimpchat.core.TouchPressType;
import com.ansgar.rdroidpc.enums.OrientationEnum;

public interface VideoFrameView extends BaseFrameView {

    void changeOrientation(OrientationEnum orientation);

    OrientationEnum getCurrentOrientation();

    void press(String value, TouchPressType type);
}
