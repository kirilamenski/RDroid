package com.ansgar.rdroidpc.commands;

import com.ansgar.filemanager.FileManager;
import com.ansgar.filemanager.FileManagerImpl;
import com.ansgar.rdroidpc.entities.Device;
import com.ansgar.rdroidpc.entities.FilesEnum;
import com.ansgar.rdroidpc.entities.Option;

import java.util.ArrayList;
import java.util.List;

public class ResponseParserUtil {

    public List<Device> getDevices(StringBuilder stringBuilder) {
        List<Device> devices = new ArrayList<>();

        String[] lines = stringBuilder.toString().split("\n");

        for (String line : lines) {
            String[] deviceInf = line.split("\t");
            if (deviceInf.length == 2) {
                devices.add(new Device(deviceInf[0], deviceInf[1], null, 0, 0, new Option()));
            }
        }

        return devices;
    }

    public void setDeviceName(Device device, String command) {
        CommandExecutor executor = new CommandExecutor();
        executor.setOnFinishExecuteListener(result -> {
            String[] lines = result.toString().split("\n");
            if (lines.length > 0) {
                String[] modelNameProp = lines[0].split(": ");
                String name = modelNameProp[modelNameProp.length - 1]
                        .replace("[", "")
                        .replace("]", "");
                device.setDeviceName(name);
                executor.destroy();
            }
        });
        executor.execute(command);
    }

    public void setDeviceSize(Device device, String command) {
        CommandExecutor executor = new CommandExecutor();
        executor.setOnFinishExecuteListener(result -> {
            String[] splittedResult = result.toString().split(" ");
            if (splittedResult.length > 0) {
                String[] sizes = splittedResult[splittedResult.length - 1]
                        .split("x");
                if (sizes.length > 1) {
                    device.setWidth(Integer.parseInt(sizes[0].trim()));
                    device.setHeight(Integer.parseInt(sizes[1].trim()));
                }
            }
            executor.destroy();
        });
        executor.execute(command);
    }

    public void setDeviceOption(Device device) {
        FileManager fileManager = new FileManagerImpl(FilesEnum.CACHE.getValue());
        Device savedDevice = fileManager.getClass(FilesEnum.DEVICES.getValue(), device.getDeviceId(), Device.class);
        if (savedDevice != null) device.setOption(savedDevice.getOption());
    }

}
