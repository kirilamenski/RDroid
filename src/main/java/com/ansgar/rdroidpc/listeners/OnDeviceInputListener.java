package com.ansgar.rdroidpc.listeners;

import com.android.chimpchat.core.TouchPressType;

public interface OnDeviceInputListener {

    void touch(int x, int y, TouchPressType type);

    void press(String value, TouchPressType type);

    void type(String type);
}
