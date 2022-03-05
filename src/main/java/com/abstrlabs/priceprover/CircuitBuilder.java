package com.abstrlabs.priceprover;

import com.abstrlabs.priceprover.util.Utility;
import com.abstrlabs.priceprover.util.Crypto;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.SneakyThrows;
import lombok.extern.log4j.Log4j2;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.util.Arrays;
import java.util.concurrent.Callable;

@Log4j2
@Command(name = "preprocess", description = "Parse the notary json from pagesinger, do the preprocess, and build the circuit by xjsnark")
public class CircuitBuilder implements Callable<Integer> {

    public NotaryCheckInput notaryCheckInput = new NotaryCheckInput();

    private final String NOTARY_PUBKEY = "-----BEGIN PUBLIC KEY-----\n" +
            "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEAp3iALChsj8lOkEpY1F5BeCMcyd6\n" +
            "282weDfsNf8lMYi7xEVVVq0W+is27cCnHZAc0resZHTdX4KoSrFgehhPcA==\n" +
            "-----END PUBLIC KEY-----";
    // recent notary pubkey (22-3-5), keep the old pubkey for test
    // "MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEdxg4idfkVEnjIwW8OwNtg9rG0EmajcIR3P6dmq10ZZBFxkTUCPX5BkrFT0cIFAWp2FU9Jt30pl4W4E7UEaDX+g==";

    @Option(names = {"-nf", "--notaryfile"}, defaultValue = "./out/notary.json", description = "decode and preprocess notary json.")
    String notaryFilePath;

    @Option(names = {"-op", "--outputPath"}, defaultValue = "./out", description = "output path for generated headers and notary files")
    String outputPath;

    @Override
    public Integer call() throws Exception {
        // parse notary json
        parseNotaryJson(notaryFilePath);

        // build circuit
        new TLSNotaryCheck(notaryCheckInput, outputPath);

        return 0;
    }

    @SneakyThrows
    public void parseNotaryJson(String notaryFilePath){
        File notaryFile = new File(notaryFilePath).getAbsoluteFile();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(notaryFile);
        long[] serverRecords0 = Crypto.base64Decode(root.get("server response records").get("0").asText());
        long[] serverRecords1 = Crypto.base64Decode(root.get("server response records").get("1").asText());
        long[] serverEcPubkey = Crypto.base64Decode(root.get("server pubkey for ECDHE").asText());
        notaryCheckInput.ccwk = Crypto.base64Decode(root.get("client client_write_key share").asText());
        notaryCheckInput.cciv = Crypto.base64Decode(root.get("client client_write_iv share").asText());
        notaryCheckInput.cswk = Crypto.base64Decode(root.get("client server_write_key share").asText());
        notaryCheckInput.csiv = Crypto.base64Decode(root.get("client server_write_iv share").asText());
        notaryCheckInput.cpms = Crypto.base64Decode(root.get("client PMS share").asText());
        notaryCheckInput.crc = Crypto.base64Decode(root.get("client request ciphertext").asText());
        notaryCheckInput.ncwk = Crypto.base64Decode(root.get("notary client_write_key share").asText());
        notaryCheckInput.nciv = Crypto.base64Decode(root.get("notary client_write_iv share").asText());
        notaryCheckInput.nswk = Crypto.base64Decode(root.get("notary server_write_key share").asText());
        notaryCheckInput.nsiv = Crypto.base64Decode(root.get("notary server_write_iv share").asText());
        notaryCheckInput.npms = Crypto.base64Decode(root.get("notary PMS share").asText());
        notaryCheckInput.nt = Crypto.base64Decode(root.get("notarization time").asText());
        notaryCheckInput.epk = Crypto.base64Decode(root.get("ephemeral pubkey").asText());
        notaryCheckInput.epkvf = Crypto.base64Decode(root.get("ephemeral valid from").asText());
        notaryCheckInput.epkvu = Crypto.base64Decode(root.get("ephemeral valid until").asText());
        notaryCheckInput.ss = Crypto.base64Decode(root.get("session signature").asText());
        notaryCheckInput.esbn = Crypto.base64Decode(root.get("ephemeral signed by master key").asText());
        notaryCheckInput.crc_len = notaryCheckInput.crc.length;
        notaryCheckInput.sr_padded = Utility.padding(Utility.concat(serverRecords0, serverRecords1));
        notaryCheckInput.tbs1_padded = Utility.padding(getTbs1(serverRecords0, serverRecords1, notaryCheckInput.ccwk, notaryCheckInput.cciv, notaryCheckInput.cswk, notaryCheckInput.csiv,
                notaryCheckInput.cpms, notaryCheckInput.crc, serverEcPubkey, notaryCheckInput.npms, notaryCheckInput.ncwk, notaryCheckInput.nciv, notaryCheckInput.nswk,
                notaryCheckInput.nsiv, notaryCheckInput.nt));
        notaryCheckInput.np = Utility.pubkeyPEMToRaw(NOTARY_PUBKEY);
        notaryCheckInput.exp_ct = getExpectedContent(serverRecords0, notaryCheckInput.cswk, notaryCheckInput.nswk, notaryCheckInput.csiv, notaryCheckInput.nsiv);
        notaryCheckInput.tcp = Utility.padding(Utility.concat(notaryCheckInput.nt, notaryCheckInput.exp_ct));
        notaryCheckInput.exp_hash = Crypto.sha256Hash(Utility.concat(notaryCheckInput.nt, notaryCheckInput.exp_ct), 4);
    }

    private long[] getTbs1(long[] serverRecords0, long[] serverRecords1, long[] clientCwkShare, long[] clientCivShare, long[] clientSwkShare, long[] clientSivShare,
                           long[] clientPMSShare, long[] clientReqCipher, long[] serverEcPubkey, long[] notaryPMSShare, long[] notaryCwkShare, long[] notaryCivShare,
                           long[] notarySwkShare, long[] notarySivShare, long[] notaryTime){
        long[] commitHash = Crypto.sha256Hash(Utility.concat(serverRecords0, serverRecords1), 1);
        long[] keyShareHash = Crypto.sha256Hash(Utility.concat(clientCwkShare, clientCivShare, clientSwkShare, clientSivShare), 1);
        long[] pmsShareHash = Crypto.sha256Hash(clientPMSShare, 1);
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

    public long[] getExpectedContent(long[] sr_padded, long[] cswk, long[] nswk, long[] csiv, long[] nsiv){

        byte[] server_records0 = Utility.toByteArray(sr_padded);
        byte[] client_swk_share = Utility.toByteArray(cswk);
        byte[] notary_swk_share = Utility.toByteArray(nswk);
        byte[] client_siv_share = Utility.toByteArray(csiv);
        byte[] notary_siv_share = Utility.toByteArray(nsiv);
        byte[] iv = Utility.xor(client_siv_share, notary_siv_share);
        byte[] key = Utility.xor(client_swk_share, notary_swk_share);
        byte[] server_records_head = Arrays.copyOfRange(server_records0, 0, 8);
        byte[] tmp1 = new byte[]{(byte) 0x17, (byte) 0x03, (byte) 0x03};
        byte[] tmp2 = Utility.toByteArray(server_records0.length - 8 - 16);
        byte[] nonce = Utility.concat(iv, server_records_head);
        byte[] aad = Utility.concat(server_records_head, tmp1, tmp2);
        byte[] data = Arrays.copyOfRange(server_records0, 8, server_records0.length);
        return Crypto.aes128GcmDecrypt(key, nonce, aad, data);
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new CircuitBuilder()).execute(args);
        System.exit(exitCode);
    }
}
