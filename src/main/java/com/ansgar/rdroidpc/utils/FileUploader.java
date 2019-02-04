package com.ansgar.rdroidpc.utils;

import com.ansgar.rdroidpc.commands.CommandExecutor;
import com.ansgar.rdroidpc.enums.AdbCommandEnum;
import com.ansgar.rdroidpc.constants.SharedValuesKey;
import com.ansgar.rdroidpc.entities.Device;

import javax.swing.*;
import java.awt.datatransfer.DataFlavor;
import java.awt.datatransfer.UnsupportedFlavorException;
import java.awt.dnd.DnDConstants;
import java.awt.dnd.DropTarget;
import java.awt.dnd.DropTargetDropEvent;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class FileUploader extends DropTarget {

    private Device device;

    public FileUploader(JPanel panel, Device device) {
        this.device = device;
        panel.setDropTarget(this);
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
        executor.setOnExecuteNextListener(System.out::println);
        String uploadFolder = SharedValues.get(SharedValuesKey.UPLOAD_FOLDER, "/sdcard/Download");
        for (File file : droppedFiles) {
            String filePath = file.getAbsolutePath();
            String command = String.format(
                    Locale.ENGLISH,
                    AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.UPLOAD_FILES),
                    device.getDeviceId(),
                    filePath,
                    uploadFolder
            );
            if (filePath.contains(" ")) {
                executor.execute(getCmds(filePath, uploadFolder));
            } else {
                executor.execute(command);
            }
        }
    }

    // TODO refactor Hack for path with spaces
    private List<String> getCmds(String filePath, String uploadFolder) {
        List<String> cmd = new ArrayList<>();
        String adbPath = SharedValues.get(SharedValuesKey.ADB_PATH, "");
        if (!adbPath.isEmpty()) cmd.add(adbPath);
        else cmd.add("adb");
        cmd.add("-s");
        cmd.add(device.getDeviceId());
        cmd.add("push");
        cmd.add(filePath);
        cmd.add(uploadFolder);
        return cmd;
    }
}
