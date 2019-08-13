package com.ansgar.rdroidpc.ui.frames;

import com.ansgar.rdroidpc.listeners.OnDumpsysListener;
import com.ansgar.rdroidpc.utils.DumpsysUtil;

import java.awt.*;

public class DumpsysPanel extends BasePanel implements OnDumpsysListener {

    private DumpsysUtil dumpsysUtil;

    public DumpsysPanel(String deviceId, Rectangle rectangle, String title) {
        super(rectangle, title);
        this.dumpsysUtil = new DumpsysUtil(deviceId, this);
        runDumsys();
    }

    @Override
    public void onCloseFrame() {
        super.onCloseFrame();
        if (dumpsysUtil != null) dumpsysUtil.stop();
    }

    private void runDumsys() {
        dumpsysUtil.start(10000, 5000);
    }

    @Override
    public void getDumpsys(StringBuilder result) {
        System.out.println(result.toString());
    }
}
