package com.abstrlabs.priceprover;

import backend.config.Config;
import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Configs saves configuration for priceprover.
 * backend.config.Config saves configuration for backend usage.
 */
@Log4j2
public class Configs{

    public static boolean writeCircuits = false;

    public static boolean writeInputs = true;

    public static String outputPath = "./out";

    public static String circuitPath = "./circuit/TLSNotaryCheck.arith";

    public static String assetTimePath = "./out/asset-yyyy-MM-dd-HH-mm-ss";

    public static String inputName = "input.in";

    public static String primaryInputName = "pri.in";

    public static String nizkInputName = "nizk.in";

    public static void setConfig(String asset, boolean firstTime, int verbosity) {
        setPath(asset);
        setFirstTime(firstTime);
        setLoggers(verbosity);
    }

    public static void setPath(String asset) {
        String timeStamp = new SimpleDateFormat("yyyy-MM-dd-HH-mm-ss").format(new Date());
        String assetTime = asset + "-" + timeStamp;
        assetTimePath = String.valueOf(Paths.get(outputPath, assetTime));

        try {
            Files.createDirectories(Path.of(assetTimePath));
            log.debug("assetTimePath created successfully");
        } catch (IOException e) {
            log.error(e);
        }
    }

    public static void setFirstTime(boolean firstTime) {
        writeCircuits = firstTime;
    }

    public static void setLoggers(int verbosity) {
        boolean verbose = verbosity >= 2;
        Config.outputVerbose = verbose;
        Config.debugVerbose = verbose;
        Config.inputVerbose = verbose;

        if (verbosity >= 3) {
            Configurator.setAllLevels(LogManager.getRootLogger().getName(), Level.TRACE);
        } else if (verbosity == 2) {
            Configurator.setAllLevels(LogManager.getRootLogger().getName(), Level.DEBUG);
        } else if (verbosity == 1) {
            Configurator.setAllLevels(LogManager.getRootLogger().getName(), Level.INFO);
        } else {
            Configurator.setAllLevels(LogManager.getRootLogger().getName(), Level.WARN);
        }
    }

    public static String getInputPath() {
        return String.valueOf(Paths.get(assetTimePath, inputName));
    }

    public static String getPriInputPath() {
        return String.valueOf(Paths.get(assetTimePath, primaryInputName));
    }

    public static String getNizkInputPath() {
        return String.valueOf(Paths.get(assetTimePath, nizkInputName));
    }
}
