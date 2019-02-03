package com.ansgar.rdroidpc.commands;

import com.ansgar.rdroidpc.entities.Device;

import java.util.ArrayList;
import java.util.List;

public class ResponseParserUtil {

    public List<Device> getDevices(StringBuilder stringBuilder) {
        List<Device> devices = new ArrayList<>();

        String[] lines = stringBuilder.toString().split("\n");

        for (String line : lines) {
            String[] deviceInf = line.split("\t");
            if (deviceInf.length == 2) {
                devices.add(new Device(deviceInf[0], deviceInf[1], null, 0, 0, null));
            }
        }

        return devices;
    }

    public Device setDeviceName(Device device, String command) {
        CommandExecutor executor = new CommandExecutor();
        executor.setOnFinishExecuteListener(result -> {
            String[] lines = result.toString().split("\n");
            if (lines.length > 0) {
                String[] modelNameProp = lines[0].split(": ");
                String name = modelNameProp[modelNameProp.length - 1]
                        .replace("[", "")
                        .replace("]", "");
                device.setDeviceName(name);
            }
        });
        executor.execute(command);
        return device;
    }

    public Device setDeviceSize(Device device, String command) {
        CommandExecutor executor = new CommandExecutor();
        executor.setOnFinishExecuteListener(result -> {
            String[] splitedResult = result.toString().split(" ");
            if (splitedResult.length > 0) {
                String[] sizes = splitedResult[splitedResult.length - 1]
                        .split("x");
                if (sizes.length > 1) {
                    device.setWidth(Integer.parseInt(sizes[0].trim()));
                    device.setHeight(Integer.parseInt(sizes[1].trim()));
                }
            }
        });
        executor.execute(command);
        return device;
    }

}
