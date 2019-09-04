package com.ansgar.rdroidpc.ui.components;

import com.ansgar.graphic.OnGraphicItemClicked;
import com.ansgar.rdroidpc.commands.tasks.DumpsysCommandTask;
import com.ansgar.rdroidpc.entities.DumpsysModel;
import com.ansgar.rdroidpc.listeners.OnDumpsysListener;
import com.ansgar.rdroidpc.listeners.OnInputPackageListener;
import com.ansgar.rdroidpc.listeners.OnWindowResizedListener;
import com.ansgar.rdroidpc.managers.AppPackagesManager;
import com.ansgar.rdroidpc.ui.components.graphiccomponent.DumpsysGraphicComponent;
import com.ansgar.rdroidpc.ui.frames.BasePanel;

import javax.annotation.Nullable;
import java.awt.*;
import java.util.List;

public class DumpsysPanel extends BasePanel implements OnDumpsysListener,
        OnInputPackageListener, OnWindowResizedListener, OnGraphicItemClicked {

    private DumpsysCommandTask dumpsysCommandTask;
    private DumpsysGraphicComponent dumpsysGraphicComponent;
    @Nullable
    private DumpsysReportComponent reportComponent;
    private InputFieldComponent inputFieldComponent;
    private String deviceId;

    public DumpsysPanel(String deviceId, Rectangle rectangle, String title) {
        super(rectangle, title, true);
        this.deviceId = deviceId;
        new SpinnerDialog(this) {
            @Override
            public void doInBack() {
                AppPackagesManager appPackagesManager = new AppPackagesManager(deviceId);
                addPackageNameInputComponent(appPackagesManager.getAllPackages());
                addDumpsysGraphic();
                setWindowResizedListener(DumpsysPanel.this);
                windowResized(getRectangle());
            }
        }.execute();
    }

    @Override
    public void onCloseFrame() {
        super.onCloseFrame();
        stopDumpsys();
    }

    @Override
    public void getDumpsys(DumpsysModel dumpsysModel) {
        if (dumpsysModel.getProfileData().size() > 0) dumpsysGraphicComponent.addItem(dumpsysModel);
    }

    private void setDumpsysTask() {
        if (dumpsysCommandTask == null) {
            dumpsysCommandTask = new DumpsysCommandTask();
            dumpsysCommandTask.setDeviceId(deviceId);
            dumpsysCommandTask.setListener(this);
        }
    }

    private void addPackageNameInputComponent(List<String> packages) {
        inputFieldComponent = new InputFieldComponent(packages);
        inputFieldComponent.setListener(this);
        add(inputFieldComponent);
    }

    private void showReportComponent(@Nullable DumpsysModel model) {
        if (model == null) {
            if (reportComponent != null) remove(reportComponent);
            reportComponent = null;
        } else {
            if (reportComponent == null) {
                reportComponent = new DumpsysReportComponent(model.getFullInformation().toString());
                add(reportComponent);
            } else {
                reportComponent.updatePanel(model.getFullInformation().toString());
            }
        }
    }

    private void addDumpsysGraphic() {
        dumpsysGraphicComponent = new DumpsysGraphicComponent();
        dumpsysGraphicComponent.setOnItemClickListener(this);
        add(dumpsysGraphicComponent);
    }

    @Override
    public void runDumpsys(String packageName) {
        setDumpsysTask();
        dumpsysCommandTask.setPackageName(packageName);
        dumpsysCommandTask.start(1000, 4000);
    }

    @Override
    public void stopDumpsys() {
        if (dumpsysCommandTask != null) dumpsysCommandTask.stop();
        dumpsysCommandTask = null;
    }

    @Override
    public void windowResized(Rectangle rectangle) {
        int height = 40;
        inputFieldComponent.setBounds(5, 5, rectangle.width - 20, height);
        inputFieldComponent.updateComponent();

        if (reportComponent != null) {
            reportComponent.setBounds(
                    (int) (rectangle.width * 0.5),
                    height,
                    rectangle.width - (int) (rectangle.width * 0.5) - 20,
                    rectangle.height - 70 - height
            );
        }
        int reportComponentXPos = reportComponent != null ? reportComponent.getX() : rectangle.width;
        dumpsysGraphicComponent.setBounds(5, height,
                reportComponentXPos - 20, rectangle.height - 70 - height);
    }

    @Override
    public void onItemClicked(int position) {
        showReportComponent(dumpsysGraphicComponent.getItem(position));
        windowResized(getRectangle());
    }
}
