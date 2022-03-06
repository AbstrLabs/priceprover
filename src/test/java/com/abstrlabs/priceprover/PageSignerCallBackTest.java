package com.abstrlabs.priceprover;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import picocli.CommandLine;

public class PageSignerCallBackTest {
    @Test
    public void callPageSignerWithAsset() {
//      black box testing
        CommandLine cmd = new CommandLine(new PageSignerCallBack());
        int exitCode = cmd.execute("-as", "aIBM");
        assertEquals(exitCode, 0);
    }
}
