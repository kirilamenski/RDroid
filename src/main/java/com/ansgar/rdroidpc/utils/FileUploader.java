package com.ansgar.rdroidpc.utils;

import com.ansgar.filemanager.FileManagerImpl;
import com.ansgar.rdroidpc.commands.CommandExecutor;
import com.ansgar.rdroidpc.constants.StringConst;
import com.ansgar.rdroidpc.entities.FilesEnum;
import com.ansgar.rdroidpc.enums.AdbCommandEnum;
import com.ansgar.rdroidpc.constants.SharedValuesKey;
import com.ansgar.rdroidpc.ui.components.OptionDialog;
import com.ansgar.rdroidpc.ui.components.OverlayComponent;

import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;
import java.util.*;
import java.util.List;
import java.util.Timer;
import java.util.concurrent.atomic.AtomicInteger;

public class FileUploader extends DropTarget {

    private String deviceId;
    private JPanel panel;

    public FileUploader(JPanel panel, String deviceId) {
        this.deviceId = deviceId;
        this.panel = panel;
        this.panel.setDropTarget(this);
    }

    @Override
    public synchronized void drop(DropTargetDropEvent dtde) {
        dtde.acceptDrop(DnDConstants.ACTION_COPY);
        try {
            List<File> droppedFiles = (List<File>) dtde.getTransferable()
                    .getTransferData(DataFlavor.javaFileListFlavor);

            uploadFiles(droppedFiles);
        } catch (UnsupportedFlavorException | IOException e) {
            e.printStackTrace();
        }
    }

    private void uploadFiles(List<File> droppedFiles) {
        CommandExecutor executor = new CommandExecutor();
        executor.setOnFinishExecuteListener(result -> showMessage());
        String uploadFolder = SharedValues.get(SharedValuesKey.UPLOAD_FOLDER, "/sdcard/Download");
        for (File file : droppedFiles) {
            String filePath = file.getAbsolutePath();
            String command = String.format(
                    Locale.ENGLISH,
                    AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.UPLOAD_FILES),
                    deviceId,
                    filePath,
                    uploadFolder
            );
            if (filePath.contains(" ")) {
                executor.execute(getCmds(filePath, uploadFolder));
            } else {
                executor.execute(command);
            }
            if (file.getName().endsWith(".apk")) {
                showInstallApkMessage(filePath);
            }
        }
    }

    // TODO refactor Hack for path with spaces
    private List<String> getCmds(String filePath, String uploadFolder) {
        List<String> cmd = new ArrayList<>();
        String adbPath = new FileManagerImpl(FilesEnum.CACHE.getValue())
                .get(FilesEnum.SETTINGS.getValue(), SharedValuesKey.ADB_PATH);
        if (!adbPath.isEmpty()) cmd.add(adbPath);
        else cmd.add("adb");
        cmd.add("-s");
        cmd.add(deviceId);
        cmd.add("push");
        cmd.add(filePath);
        cmd.add(uploadFolder);
        return cmd;
    }

    private void showMessage() {
        OverlayComponent component = getOverlayComponent();
        panel.add(component);
        component.updateTitle("File uploaded to \"sdcard/Download\" folder.");
        panel.repaint();

        AtomicInteger timeToDisappear = new AtomicInteger();
        timeToDisappear.set(10);
        Timer timer = new Timer();
        timer.schedule(new TimerTask() {
            @Override
            public void run() {
                timeToDisappear.set(timeToDisappear.get() - 1);
                if (timeToDisappear.get() <= 0) {
                    panel.remove(component);
                    timer.cancel();
                    panel.repaint();
                }
            }
        }, 0, 1000);
    }

    private OverlayComponent getOverlayComponent() {
        OverlayComponent component = new OverlayComponent();
        component.setBounds(new Rectangle(0, panel.getHeight() - 30, panel.getWidth(), 30));
        component.setImageWidth(30);
        component.setImageHeight(30);
        component.createComponent();
        return component;
    }

    private void showInstallApkMessage(String pathToApk) {
        int result = new OptionDialog()
                .setDialogTitle(StringConst.INSTALL_APK_MESSAGE)
                .setMainTitle("")
                .showDialog(panel, JOptionPane.YES_NO_OPTION, JOptionPane.QUESTION_MESSAGE);
        if (result == 0) {
            CommandExecutor executor = new CommandExecutor();
            executor.execute(String.format(AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.INSTALL_APK),
                    deviceId, pathToApk));
        }
    }

}
