package com.abstrlabs.priceprover;

import com.abstrlabs.priceprover.util.Utility;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.security.NoSuchAlgorithmException;
import java.util.concurrent.Callable;

@Command(name = "notaryparser", description = "Parse notary json from pagesinger")
public class NotaryJsonParser implements Callable<Integer> {

    private final Logger log = LogManager.getLogger(this.getClass());

    public NotaryCheckInput notaryCheckInput = new NotaryCheckInput();

    private final String NOTARY_PUBKEY = "-----BEGIN PUBLIC KEY-----\n" +
            "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEAp3iALChsj8lOkEpY1F5BeCMcyd6\n" +
            "282weDfsNf8lMYi7xEVVVq0W+is27cCnHZAc0resZHTdX4KoSrFgehhPcA==\n" +
            "-----END PUBLIC KEY-----";

    @Option(names = {"-nf", "--notaryfile"}, defaultValue = "./out/notary.json", description = "decode and preprocess notary json.")
    String notaryFilePath;

    @Override
    public Integer call() throws Exception {
        File notaryFile = new File(notaryFilePath).getAbsoluteFile();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(notaryFile);
        long[] serverRecords0 = Utility.base64Decode(root.get("server response records").get("0").asText());
        long[] serverRecords1 = Utility.base64Decode(root.get("server response records").get("1").asText());

        notaryCheckInput.ccwk = Utility.base64Decode(root.get("client client_write_key share").asText());
        notaryCheckInput.cciv = Utility.base64Decode(root.get("client client_write_iv share").asText());
        notaryCheckInput.cswk = Utility.base64Decode(root.get("client server_write_key share").asText());
        notaryCheckInput.csiv = Utility.base64Decode(root.get("client server_write_iv share").asText());
        notaryCheckInput.cpms = Utility.base64Decode(root.get("client PMS share").asText());
        notaryCheckInput.crc = Utility.base64Decode(root.get("client request ciphertext").asText());
        notaryCheckInput.ncwk = Utility.base64Decode(root.get("notary client_write_key share").asText());
        notaryCheckInput.nciv = Utility.base64Decode(root.get("notary client_write_iv share").asText());
        notaryCheckInput.nswk = Utility.base64Decode(root.get("notary server_write_key share").asText());
        notaryCheckInput.nsiv = Utility.base64Decode(root.get("notary server_write_iv share").asText());
        notaryCheckInput.npms = Utility.base64Decode(root.get("notary PMS share").asText());
        notaryCheckInput.nt = Utility.base64Decode(root.get("notarization time").asText());
        notaryCheckInput.epk = Utility.base64Decode(root.get("ephemeral pubkey").asText());
        notaryCheckInput.epkvf = Utility.base64Decode(root.get("ephemeral valid from").asText());
        notaryCheckInput.epkvu = Utility.base64Decode(root.get("ephemeral valid until").asText());
        notaryCheckInput.ss = Utility.base64Decode(root.get("session signature").asText());
        notaryCheckInput.esbn = Utility.base64Decode(root.get("ephemeral signed by master key").asText());
        notaryCheckInput.crc_len = notaryCheckInput.crc.length;
        notaryCheckInput.sr_padded = Utility.padding(Utility.concat(serverRecords0, serverRecords1));
        notaryCheckInput.sr_padded_len = notaryCheckInput.sr_padded.length / 64;
        long[] serverEcPubkey = Utility.base64Decode(root.get("server pubkey for ECDHE").asText());
        notaryCheckInput.tbs1_padded = Utility.padding(getTbs1(serverRecords0, serverRecords1, notaryCheckInput.ccwk, notaryCheckInput.cciv, notaryCheckInput.cswk, notaryCheckInput.csiv,
                notaryCheckInput.cpms, notaryCheckInput.crc, serverEcPubkey, notaryCheckInput.npms, notaryCheckInput.ncwk, notaryCheckInput.nciv, notaryCheckInput.nswk,
                notaryCheckInput.nsiv, notaryCheckInput.nt));
        notaryCheckInput.np = Utility.pubkeyPEMToRaw(NOTARY_PUBKEY);
        notaryCheckInput.exp_ct = new AES128().aes128GcmDecrypt(serverRecords0, notaryCheckInput.cswk, notaryCheckInput.nswk, notaryCheckInput.csiv, notaryCheckInput.nsiv);
        notaryCheckInput.tcp = Utility.padding(Utility.concat(notaryCheckInput.nt, notaryCheckInput.exp_ct));
        notaryCheckInput.exp_hash = new SHA256().sha256Hash(Utility.concat(notaryCheckInput.nt, notaryCheckInput.exp_ct), 4);

        return 0;
    }

    private long[] getTbs1(long[] serverRecords0, long[] serverRecords1, long[] clientCwkShare, long[] clientCivShare, long[] clientSwkShare, long[] clientSivShare,
                           long[] clientPMSShare, long[] clientReqCipher, long[] serverEcPubkey, long[] notaryPMSShare, long[] notaryCwkShare, long[] notaryCivShare,
                           long[] notarySwkShare, long[] notarySivShare, long[] notaryTime) throws NoSuchAlgorithmException {
        long[] commitHash = new SHA256().sha256Hash(Utility.concat(serverRecords0, serverRecords1), 1);
        long[] keyShareHash = new SHA256().sha256Hash(Utility.concat(clientCwkShare, clientCivShare, clientSwkShare, clientSivShare), 1);
        long[] pmsShareHash = new SHA256().sha256Hash(clientPMSShare, 1);
        return Utility.concat(
              commitHash,
              keyShareHash,
              pmsShareHash,
              clientReqCipher,
              serverEcPubkey,
              notaryPMSShare,
              notaryCwkShare,
              notaryCivShare,
              notarySwkShare,
              notarySivShare,
              notaryTime);
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new NotaryJsonParser()).execute(args);
        System.exit(exitCode);
    }
}
