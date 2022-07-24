package com.abstrlabs.priceprover;

import org.junit.Test;
import picocli.CommandLine;

import static org.junit.Assert.assertEquals;

public class GnarkCallBackTest {
    @Test
    public void callGnarkFirstTime() {
        CommandLine cmd = new CommandLine(new GnarkCallBack());
        int exitCode = cmd.execute("-fi");
        assertEquals(exitCode, 0);
    }

    @Test
    public void callGnarkNonFirstTime() {
        CommandLine cmd = new CommandLine(new GnarkCallBack());
        int exitCode = cmd.execute();
        assertEquals(exitCode, 0);
    }
}
