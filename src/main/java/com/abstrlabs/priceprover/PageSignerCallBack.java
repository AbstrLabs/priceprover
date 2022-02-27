package com.abstrlabs.priceprover;

import com.abstrlabs.priceprover.util.CommandExecutor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.concurrent.Callable;


@Command(name = "pagesigner", description = "Trigger pagesigner-cli and generate a stock price notray json")
public class PageSignerCallBack implements Callable<Integer> {

    private final Logger log = LogManager.getLogger(this.getClass());

    @Option(names = {"-as", "--asset"}, defaultValue = "aIBM", description = "the asset name used to obtain the price data jason and the notary file via PageSinger.")
    String asset;

    @Option(names = {"-op", "--outputPath"}, defaultValue = "./out", description = "output path for generated headers and notary files")
    String outputPath;

    @Override
    public Integer call(){
        String assetName = normalizeAssetName(asset);

        try {
            //generate header.txt
            String currentWorkingDir = System.getProperty("user.dir");
            String headersPath = currentWorkingDir + "/headers.txt";
            String content = "GET /query?function=GLOBAL_QUOTE&symbol=" + assetName + "&apikey=demo HTTP/1.1\n" +
                    "Host: www.alphavantage.co";
            File headersFile = new File(headersPath);
            if (headersFile.createNewFile()) {
                log.info("Headers file created successfully");
            };
            FileWriter headersWriter = new FileWriter(headersFile);
            headersWriter.write(content);
            headersWriter.close();

            //call pagesigner-cli
            String[] pagesignerCommand = new String[]{"./depends/pagesigner-cli/pgsg-node.js", "notarize", "www.alphavantage.co", "--headers", headersPath, outputPath};
            CommandExecutor ce = new CommandExecutor();
            if (ce.execute(pagesignerCommand)) {
                log.info("Triggered pagesigner-cli successfully and saved the notary file into path:" + outputPath);
                return 0;
            } else {
                log.warn("pagesigner callback failed");
                return 1;
            }

        } catch (IOException e) {
            e.printStackTrace();
            return 2;
        }
    }

    /**
     * input : asset name, eg. aIBM, aAAPL
     * output: normalized asset name, eg. IBM, AAPL
     */
    private String normalizeAssetName(String asset) {
        if (asset.length() > 1 && asset.charAt(0) == 'a') {
            asset =  asset.substring(1);
        }
        log.info("The normalized asset name is " + asset);
        return asset;

    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new PageSignerCallBack()).execute(args);
        System.exit(exitCode);
    }
}
