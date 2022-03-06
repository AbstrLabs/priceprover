package com.abstrlabs.priceprover;

import lombok.extern.log4j.Log4j2;
import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.core.config.Configurator;
import picocli.CommandLine;

import java.util.concurrent.Callable;

@Log4j2
@CommandLine.Command(name = "priceprover", mixinStandardHelpOptions = true, version = "0.0.2",
        description = "given the stock symbol, notarize the price and generate the proof", subcommands = {
        PageSignerCallBack.class,
        CircuitBuilder.class,
        LibsnarkCallBack.class})
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
     *   4. Generate verifier contract (todo)
     */
    public Integer call() {

        // PageSignerCallBack
        CommandLine cmd = new CommandLine(new PageSignerCallBack());
        if (cmd.execute("-as", asset, "-op", outputPath) == 0) {
            // CircuitBuilder
            if (firstTime) {
                cmd = new CommandLine(new CircuitBuilder());
                if (cmd.execute("-fi", "-op", outputPath) == 0) {
                    // LibsnarkCallBack
                    cmd = new CommandLine(new LibsnarkCallBack());
                    return cmd.execute("-fi");
                }
            } else {
                cmd = new CommandLine(new CircuitBuilder());
                if (cmd.execute("-op", outputPath) == 0) {
                    // LibsnarkCallBack
                    cmd = new CommandLine(new LibsnarkCallBack());
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
        configureLoggers();
        return new CommandLine.RunLast().execute(parseResult); // default execution strategy
    }

    private void configureLoggers() {
        if (verbosity.length >= 3) {
            Configurator.setAllLevels(LogManager.getRootLogger().getName(), Level.TRACE);
        } else if (verbosity.length == 2) {
            Configurator.setAllLevels(LogManager.getRootLogger().getName(), Level.DEBUG);
        } else if (verbosity.length == 1) {
            Configurator.setAllLevels(LogManager.getRootLogger().getName(), Level.INFO);
        } else {
            Configurator.setAllLevels(LogManager.getRootLogger().getName(), Level.WARN);
        }
    }

    public static void main(String[] args) {
        PriceProver app = new PriceProver();
        new CommandLine(app)
                .setExecutionStrategy(app::executionStrategy)
                .execute(args);
    }

}
