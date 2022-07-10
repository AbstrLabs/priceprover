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
@Command(name = "notarize", description = "Call pagesigner-cli and notarize the stock price")
public class PageSignerCallBack implements Callable<Integer> {

    private static final String HEADERS = "headers";

    @Option(names = {"-as", "--asset"}, defaultValue = "aIBM", description = "the asset name used to obtain the price data jason and the notary file via PageSinger.")
    String asset;

    @Option(names = {"-op", "--outputPath"}, defaultValue = "./out", description = "output path for generated headers and notary files")
    String outputPath;

    @Override
    public Integer call(){
        String assetName = normalizeAssetName(asset);

        try {
            // generate header.txt
            String headersPath = String.valueOf(Paths.get(outputPath, HEADERS));
            String content = "GET /query?function=GLOBAL_QUOTE&symbol=" + assetName + "&apikey=demo HTTP/1.1\n" +
                    "Host: www.alphavantage.co";
            File headersFile = new File(headersPath);
            if (headersFile.getParentFile().mkdirs()) {
                log.info("Headers folder created successfully");
            }
            if (headersFile.createNewFile()) {
                log.info("Headers file created successfully");
            };
            FileWriter headersWriter = new FileWriter(headersFile);
            headersWriter.write(content);
            headersWriter.close();

            // call pagesigner-cli
            String[] pagesignerCommand = new String[]{"./depends/pagesigner-cli/pgsg-node.js", "notarize", "www.alphavantage.co", "--headers", headersPath, outputPath};
            CommandExecutor ce = new CommandExecutor();
            String missionName = "Call pagesigner-cli";
            if (ce.execute(missionName, pagesignerCommand)) {
                log.info("saved the notary file into path:" + outputPath);
                return 0;
            } else {
                return -1;
            }

        } catch (IOException e) {
            log.error(e);
            return -2;
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
        log.debug("The normalized asset name is " + asset);
        return asset;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new PageSignerCallBack()).execute(args);
        System.exit(exitCode);
    }
}
