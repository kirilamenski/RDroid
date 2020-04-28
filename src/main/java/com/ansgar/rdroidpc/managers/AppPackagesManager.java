package com.ansgar.rdroidpc.managers;

import com.ansgar.rdroidpc.commands.CommandExecutor;
import com.ansgar.rdroidpc.enums.AdbCommandEnum;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

public class AppPackagesManager {

    private String deviceId;

    public AppPackagesManager(String deviceId) {
        this.deviceId = deviceId;
    }

    public List<String> getAllPackages(String query) {
        List<String> packagesList = new ArrayList<>();
        CommandExecutor executor = new CommandExecutor();
        executor.setOnExecuteNextListener(line -> packagesList.add(line.replace("package:", "")));
        executor.execute(String.format(
                Locale.ENGLISH,
                AdbCommandEnum.Companion.getCommandValue(AdbCommandEnum.PACKAGES),
                deviceId,
                "'" + query + "'"
        ));
        executor.destroy();
        return packagesList;
    }

}
