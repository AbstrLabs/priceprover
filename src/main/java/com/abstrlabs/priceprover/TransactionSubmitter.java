package com.abstrlabs.priceprover;

import com.abstrlabs.priceprover.util.CommandExecutor;
import lombok.extern.log4j.Log4j2;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.concurrent.Callable;

@Log4j2
@Command(name = "submit", description = "Call pagesigner-cli and notarize the stock price")
public class TransactionSubmitter implements Callable<Integer> {

    private static final String HEADERS = "headers";

    @Option(names = {"-ai", "--appId"}, defaultValue = "2", description = "deployed verifier contract's APP ID")
    String appId;

    @Option(names = {"-op", "--outputPath"}, defaultValue = "./out", description = "output path from previous steps")
    String outputPath;

    @Override
    public Integer call(){
        try {
            // call pagesigner-cli
            String[] pagesignerCommand = new String[]{"node", "./node_modules/.bin/algob", "run", "./scripts/call-verify.js"};
            CommandExecutor ce = new CommandExecutor();
            String missionName = "Submit price and proof to chain";
            if (ce.execute(missionName, pagesignerCommand, new String[]{
                "ZKP_INPUT=" + "../../" + outputPath + "/primary.in.gnark",
                "ZKP_INPUT_BEFORE_HASH=" + "../../" + outputPath + "/before_hash.in",
                "ZKP_PROOF=" + "../../" + outputPath + "/proof.gnark",
                "ALGORAND_APP_ID=" + appId,
            }, new File("./depends/algorand_zkp_verifier"))) {
                log.info("Proof validated by zkp_verifier. Submit success");
                log.info(ce.lastOutput);
                return 0;
            } else {
                return -1;
            }
        } catch (Exception e) {
            log.error(e);
            return -2;
        }
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new TransactionSubmitter()).execute(args);
        System.exit(exitCode);
    }
}
