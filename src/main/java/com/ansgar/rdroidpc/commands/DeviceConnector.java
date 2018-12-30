package com.ansgar.rdroidpc.commands;

import com.ansgar.rdoidpc.entities.Device;

import java.io.*;
import java.util.Locale;

// TODO Find a path to solve problem with send commands in one process stream
public class DeviceConnector {

    private Device device;
//    private Process process;
//    private BufferedWriter writer;
//    private BufferedReader reader;

    public DeviceConnector(Device device) {
        this.device = device;
//        try {
//            this.process = Runtime.getRuntime()
//                    .exec(String.format(Locale.ENGLISH, "adb -s %s shell", device.getDeviceId()));
//            outputStream = process.getOutputStream();
//            this.process = new ProcessBuilder()
//                    .command(String.format(Locale.ENGLISH, "adb -s %s shell", device.getDeviceId()))
//                    .start();
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }

    public void execute(String command) throws IOException {
        Process process = Runtime.getRuntime()
                .exec(String.format(Locale.ENGLISH, "adb -s %s shell", device.getDeviceId()));
        if (process != null) {
            BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(process.getOutputStream()));
            BufferedReader reader = new BufferedReader(new InputStreamReader(process.getInputStream()));

            try {
                writer.append(command);
                writer.flush();
                writer.close();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String line = null;
            while ((line = reader.readLine()) != null) {
                System.out.println(line);
            }
        }
    }

}
