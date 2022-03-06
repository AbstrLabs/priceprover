package com.abstrlabs.priceprover;

import org.junit.Test;
import picocli.CommandLine;

import static org.junit.Assert.assertEquals;

public class PriceProverTest {
    @Test
    public void callPriceProverFirstTime() {
        CommandLine cmd = new CommandLine(new PriceProver());
        int exitCode = cmd.execute("-fi");
        assertEquals(exitCode, 0);
    }

    @Test
    public void callPriceProverNonFirstTime() {
        PriceProver app = new PriceProver();
        int exitCode = new CommandLine(app)
                .setExecutionStrategy(app::executionStrategy)
                .execute();
        assertEquals(exitCode, 0);
    }
}
