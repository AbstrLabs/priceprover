package com.abstrlabs.priceprover.util;

import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;

import java.io.BufferedReader;
import java.io.File;
import java.io.InputStreamReader;

@Log4j2
public class CommandExecutor {
    public static String lastOutput = "";

    @SneakyThrows
    private boolean afterExecute(String missionName, Process proc) {
        boolean success = true;

        BufferedReader stdInput = new BufferedReader(new
                InputStreamReader(proc.getInputStream()));

        BufferedReader stdError = new BufferedReader(new
                InputStreamReader(proc.getErrorStream()));

        String s;
        lastOutput = "";
        // Read the output from the command
        while ((s = stdInput.readLine()) != null) {
            lastOutput += s + "\n";
            log.debug(s);
        }
        // Read any errors from the attempted command
        if ((s = stdError.readLine()) != null) {
            success = false;
            log.error(missionName + " failed");
            log.error(s);
        }
        while ((s = stdError.readLine()) != null) {
            log.error(s);
        }

        if (success) {
            log.info(missionName + " successfully");
        }
        return success;
    }

    @SneakyThrows
    public boolean execute(String missionName, String[] commands) {
        log.info(missionName + " start");
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(commands);
        return afterExecute(missionName, proc);
    }

    @SneakyThrows
    public boolean execute(String missionName, String[] commands, String[] env, File dir) {
        log.info(missionName + " start");
        Runtime rt = Runtime.getRuntime();
        Process proc = rt.exec(commands, env, dir);
        return afterExecute(missionName, proc);
    }
}
