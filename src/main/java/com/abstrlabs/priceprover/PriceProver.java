package com.abstrlabs.priceprover;

import lombok.extern.log4j.Log4j2;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@Log4j2
@CommandLine.Command(name = "priceprover", mixinStandardHelpOptions = true, version = "0.0.2",
        description = "given the stock symbol, notarize the price and generate the proof", subcommands = {
        PageSignerCallBack.class,
        CircuitBuilder.class,
        LibsnarkCallBack.class,
        TransactionSubmitter.class
    })
public class PriceProver implements Callable<Integer> {
    @CommandLine.Option(names = {"-as", "--asset"}, defaultValue = "aIBM", description = "the asset name used to obtain the price data.")
    String asset;

    @CommandLine.Option(names = {"-op", "--outputPath"}, defaultValue = "./out", description = "output path for generated files")
    String outputPath;

    @CommandLine.Option(names = {"-fi", "--firstTime"}, description = "if it is first time run")
    boolean firstTime;

    @CommandLine.Option(names = {"-v", "--verbose"}, description =
            {"Specify multiple -v options to increase verbosity.", "For example, `-v` - info, '-vv' - debug,'-vvv' - trace "})
    boolean[] verbosity = new boolean[1];

    /** wrap up all the steps:
     *   1. PageSignerCallBack (notary the price data)
     *   2. CircuitBuilder (parse notary json, build circuit and input)
     *   3. LibsnarkCallBack (generate proof)
     *   4. TransactionSubmitter (submit transactions contain price and proof to chain)
     */
    public Integer call() {

        // PageSignerCallBack
        CommandLine cmd = new CommandLine(new PageSignerCallBack());
        if (cmd.execute("-as", asset, "-op", outputPath) == 0) {
            // CircuitBuilder
            cmd = new CommandLine(new CircuitBuilder());
            if (cmd.execute("-op", outputPath) == 0) {
                // GnarkCallBack
                cmd = new CommandLine(new GnarkCallBack());
                if (cmd.execute() == 0) {
                    // TransactionSubmitter
                    cmd = new CommandLine(new TransactionSubmitter());
                    return cmd.execute();
                }
            }
        }
        return -1; // exit code
    }


    // A reference to this method can be used as a custom execution strategy
    // that first calls the init() method,
    // and then delegates to the default execution strategy.
    int executionStrategy(CommandLine.ParseResult parseResult) {
        Configs.setConfig(asset, firstTime, verbosity.length);
        return new CommandLine.RunLast().execute(parseResult); // default execution strategy
    }

    public static void main(String[] args) {
        PriceProver app = new PriceProver();
        new CommandLine(app)
                .setExecutionStrategy(app::executionStrategy)
                .execute(args);
    }

}
