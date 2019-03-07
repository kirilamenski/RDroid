package com.ansgar.rdroidpc.ui.frames.views;

import com.android.chimpchat.core.TouchPressType;
import com.ansgar.rdroidpc.enums.OrientationEnum;

public interface VideoFrameView extends BaseFrameView {

    void startNewThread();

    void start(String command);

    void stop(boolean closeFrame);

    void disposeDevice();

    void stopFrameGrabber();

    void stopThread();

    void stopFrame();

    void initChimpDevice();

    void changeOrientation(OrientationEnum orientation);

    void press(String value, TouchPressType type);

    OrientationEnum getCurrentOrientation();

    boolean isScreenEmpty();

}
