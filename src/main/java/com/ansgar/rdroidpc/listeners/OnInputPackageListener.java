package com.ansgar.rdroidpc.listeners;

public interface OnInputPackageListener {

    void runDumpsys(String packageName);

    void stopDumpsys();

}
