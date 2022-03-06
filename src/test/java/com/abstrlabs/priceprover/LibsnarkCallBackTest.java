package com.abstrlabs.priceprover;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import picocli.CommandLine;

public class LibsnarkCallBackTest {
    @Test
    public void callLibsnarkFirstTime() {
        CommandLine cmd = new CommandLine(new LibsnarkCallBack());
        int exitCode = cmd.execute("-fi");
        assertEquals(exitCode, 0);
    }

    @Test
    public void callLibsnarkNonFirstTime() {
        CommandLine cmd = new CommandLine(new LibsnarkCallBack());
        int exitCode = cmd.execute();
        assertEquals(exitCode, 0);
    }
}
