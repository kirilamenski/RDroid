package com.ansgar.rdroidpc.commands.tasks;

import com.ansgar.rdroidpc.listeners.OnDumpsysListener;
import com.ansgar.rdroidpc.ui.frames.BasePanel;
import com.ansgar.rdroidpc.ui.frames.presenters.BasePresenter;
import com.ansgar.rdroidpc.utils.DumpsysCommandTask;

import java.awt.*;

public class DumpsysPanel extends BasePanel implements OnDumpsysListener {

    private DumpsysCommandTask dumpsysCommandTask;

    public DumpsysPanel(String deviceId, Rectangle rectangle, String title) {
        super(rectangle, title);
        setDumpsysTask(deviceId);
    }

    @Override
    public void onCloseFrame() {
        super.onCloseFrame();
        if (dumpsysCommandTask != null) dumpsysCommandTask.stop();
    }

    @Override
    public void getDumpsys(StringBuilder result) {
        System.out.println(result.toString());
    }

    private void setDumpsysTask(String deviceId) {
        dumpsysCommandTask = new DumpsysCommandTask();
        dumpsysCommandTask.setDeviceId(deviceId);
        dumpsysCommandTask.setListener(this);
        dumpsysCommandTask.start(10000, 5000);
    }

}
