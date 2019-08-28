package com.ansgar.rdroidpc.commands.tasks;

import com.ansgar.rdroidpc.entities.DumpsysModel;
import com.ansgar.rdroidpc.entities.ProfileData;
import com.ansgar.rdroidpc.listeners.OnDumpsysListener;
import com.ansgar.rdroidpc.listeners.OnInputPackageListener;
import com.ansgar.rdroidpc.listeners.OnWindowResizedListener;
import com.ansgar.rdroidpc.ui.components.DumpsysGraphicComponent;
import com.ansgar.rdroidpc.ui.components.InputFieldComponent;
import com.ansgar.rdroidpc.ui.frames.BasePanel;
import com.ansgar.rdroidpc.utils.DumpsysCommandTask;

import java.awt.*;

public class DumpsysPanel extends BasePanel implements OnDumpsysListener,
        OnInputPackageListener, OnWindowResizedListener {

    private DumpsysCommandTask dumpsysCommandTask;
    private DumpsysGraphicComponent dumpsysGraphicComponent;
    private InputFieldComponent inputFieldComponent;

    public DumpsysPanel(String deviceId, Rectangle rectangle, String title) {
        super(rectangle, title, true);
        setDumpsysTask(deviceId);
        addPackageNameInputComponent();
        addDumpsysGraphic();
        setWindowResizedListener(this);
    }

    @Override
    public void onCloseFrame() {
        super.onCloseFrame();
        if (dumpsysCommandTask != null) dumpsysCommandTask.stop();
    }

    @Override
    public void getDumpsys(DumpsysModel dumpsysModel) {
        if (dumpsysModel.getProfileData().size() > 0) dumpsysGraphicComponent.addItem(dumpsysModel);
    }

    private void setDumpsysTask(String deviceId) {
        dumpsysCommandTask = new DumpsysCommandTask();
        dumpsysCommandTask.setDeviceId(deviceId);
        dumpsysCommandTask.setListener(this);
    }

    private void addPackageNameInputComponent() {
        inputFieldComponent = new InputFieldComponent();
        inputFieldComponent.setListener(this);
        add(inputFieldComponent);
    }

    private void addDumpsysGraphic() {
        dumpsysGraphicComponent = new DumpsysGraphicComponent();
        add(dumpsysGraphicComponent);
    }

    @Override
    public void runDumpsys(String packageName) {
        dumpsysCommandTask.setPackageName(packageName);
        dumpsysCommandTask.start(1000, 5000);
    }

    @Override
    public void windowResized(Rectangle rectangle) {
        inputFieldComponent.setBounds(5, 5, rectangle.width - 20, 40);
        inputFieldComponent.updateComponent();
        dumpsysGraphicComponent.setBounds(5, inputFieldComponent.getHeight(),
                rectangle.width - 20, rectangle.height - 70 - inputFieldComponent.getHeight());

    }
}
