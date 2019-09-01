package com.ansgar.rdroidpc.commands.tasks;

import com.ansgar.rdroidpc.entities.DumpsysModel;
import com.ansgar.rdroidpc.listeners.OnDumpsysListener;
import com.ansgar.rdroidpc.listeners.OnInputPackageListener;
import com.ansgar.rdroidpc.listeners.OnWindowResizedListener;
import com.ansgar.rdroidpc.ui.components.DumpsysReportComponent;
import com.ansgar.rdroidpc.ui.components.graphiccomponent.DumpsysGraphicComponent;
import com.ansgar.rdroidpc.ui.components.InputFieldComponent;
import com.ansgar.rdroidpc.ui.frames.BasePanel;
import com.ansgar.rdroidpc.utils.DumpsysCommandTask;

import java.awt.*;

public class DumpsysPanel extends BasePanel implements OnDumpsysListener,
        OnInputPackageListener, OnWindowResizedListener {

    private DumpsysCommandTask dumpsysCommandTask;
    private DumpsysGraphicComponent dumpsysGraphicComponent;
    private DumpsysReportComponent reportComponent;
    private InputFieldComponent inputFieldComponent;
    private String deviceId;

    public DumpsysPanel(String deviceId, Rectangle rectangle, String title) {
        super(rectangle, title, true);
        this.deviceId = deviceId;
        addPackageNameInputComponent();
        addReportComponent();
        addDumpsysGraphic();
        setWindowResizedListener(this);
    }

    @Override
    public void onCloseFrame() {
        super.onCloseFrame();
        stopDumpsys();
    }

    @Override
    public void getDumpsys(DumpsysModel dumpsysModel) {
        System.out.println("Window name: " + dumpsysModel.getWindow() + "size: " + dumpsysModel.getProfileData().size());
        if (dumpsysModel.getProfileData().size() > 0) dumpsysGraphicComponent.addItem(dumpsysModel);
    }

    private void setDumpsysTask() {
        if (dumpsysCommandTask == null) {
            dumpsysCommandTask = new DumpsysCommandTask();
            dumpsysCommandTask.setDeviceId(deviceId);
            dumpsysCommandTask.setListener(this);
        }
    }

    private void addPackageNameInputComponent() {
        inputFieldComponent = new InputFieldComponent();
        inputFieldComponent.setListener(this);
        add(inputFieldComponent);
    }

    private void addReportComponent() {
        reportComponent = new DumpsysReportComponent();
        add(reportComponent);
    }

    private void addDumpsysGraphic() {
        dumpsysGraphicComponent = new DumpsysGraphicComponent();
        add(dumpsysGraphicComponent);
    }

    @Override
    public void runDumpsys(String packageName) {
        setDumpsysTask();
        dumpsysCommandTask.setPackageName(packageName);
        dumpsysCommandTask.start(1000, 5000);
        dumpsysGraphicComponent.startAutoMoving();
    }

    @Override
    public void stopDumpsys() {
        if (dumpsysCommandTask != null) dumpsysCommandTask.stop();
        dumpsysCommandTask = null;
        if (dumpsysGraphicComponent != null) dumpsysGraphicComponent.destroy();
    }

    @Override
    public void windowResized(Rectangle rectangle) {
        int height = 40;
        inputFieldComponent.setBounds(5, 5, rectangle.width - 20, height);
        inputFieldComponent.updateComponent();
        int reportComponentXPos = (int) (rectangle.width * 0.6);
        reportComponent.setBounds(reportComponentXPos, height,
                rectangle.width - reportComponentXPos - 20, rectangle.height - 70 - height);
        dumpsysGraphicComponent.setBounds(5, height,
                reportComponentXPos - 20, rectangle.height - 70 - height);

    }
}
