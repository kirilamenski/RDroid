package com.ansgar.rdroidpc.ui.frames.views;

import com.android.chimpchat.core.TouchPressType;
import com.ansgar.rdroidpc.enums.OrientationEnum;

public interface VideoFrameView extends BaseFrameView {

    void startNewThread();

    void initChimpDevice();

    void changeOrientation(OrientationEnum orientation);

    OrientationEnum getCurrentOrientation();

    void press(String value, TouchPressType type);

    void stop(boolean closeFrame);

}
