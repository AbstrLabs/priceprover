package com.abstrlabs.priceprover;

import picocli.CommandLine;

import java.util.concurrent.Callable;

@CommandLine.Command(name = "priceprover", subcommands = {
        PageSignerCallBack.class,
        NotaryJsonParser.class})
public class CLI implements Callable<Integer> {
    public Integer call() {
        /* todo: wrap up all the steps:
        *   1. PageSignerCallBack
        *   2. NotaryJsonParser
        *   3. CircuitCompile (using xjsnark_backend)
        *   4. Prove (using libsnark)
        *   5. Generate verifier contract
        */
        return 0; // exit code
    }


    // A reference to this method can be used as a custom execution strategy
    // that first calls the init() method,
    // and then delegates to the default execution strategy.
    private int executionStrategy(CommandLine.ParseResult parseResult) {
        init(); // custom initialization to be done before executing any command or subcommand
        return new CommandLine.RunLast().execute(parseResult); // default execution strategy
    }


    private void init() {
        // ...
    }

    public static void call(String[] args) {
        CLI app = new CLI();
        new CommandLine(app)
                .setExecutionStrategy(app::executionStrategy)
                .execute(args);
    }

}
