package com.ansgar.rdroidpc.ui.frames.views;

import com.ansgar.rdroidpc.enums.OrientationEnum;
import com.ansgar.rdroidpc.enums.VideoMenuItemsEnum;

public interface VideoFrameView extends BaseFrameView {

    void startNewThread();

    void start(String command);

    void stop(boolean closeFrame);

    void disposeChimpDevice();

    void stopFrameGrabber();

    void stopThread();

    void stopFrame();

    void initChimpDevice();

    void changeOrientation(OrientationEnum orientation);

    OrientationEnum getCurrentOrientation();

    boolean isScreenEmpty();

    void openPanel(VideoMenuItemsEnum menuItem);

}
