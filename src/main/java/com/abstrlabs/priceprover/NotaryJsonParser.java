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
import java.util.Base64;
import java.util.concurrent.Callable;


@Command(name = "notaryparser", description = "Parse notary json from pagesinger")
public class NotaryJsonParser implements Callable<Integer> {

    private final Logger log = LogManager.getLogger(this.getClass());

    public NotaryCheckInput notaryCheckInput = new NotaryCheckInput();

    @Option(names = {"-nf", "--notaryfile"}, defaultValue = "./out/notary.json", description = "decode and preprocess notary json.")
    String notaryFilePath;

    @Override
    public Integer call() throws Exception {
        File notaryFile = new File(notaryFilePath).getAbsoluteFile();
        ObjectMapper mapper = new ObjectMapper();
        JsonNode root = mapper.readTree(notaryFile);
        long[] serverRecords0 = base64Decode(root.get("server response records").get("0").asText());
        long[] serverRecords1 = base64Decode(root.get("server response records").get("1").asText());

        notaryCheckInput.ccwk = base64Decode(root.get("client client_write_key share").asText());
        notaryCheckInput.cciv = base64Decode(root.get("client client_write_iv share").asText());
        notaryCheckInput.cswk = base64Decode(root.get("client server_write_key share").asText());
        notaryCheckInput.csiv = base64Decode(root.get("client server_write_iv share").asText());
        notaryCheckInput.cpms = base64Decode(root.get("client PMS share").asText());
        notaryCheckInput.crc = base64Decode(root.get("client request ciphertext").asText());
        notaryCheckInput.ncwk = base64Decode(root.get("notary client_write_key share").asText());
        notaryCheckInput.nciv = base64Decode(root.get("notary client_write_iv share").asText());
        notaryCheckInput.nswk = base64Decode(root.get("notary server_write_key share").asText());
        notaryCheckInput.nsiv = base64Decode(root.get("notary server_write_iv share").asText());
        notaryCheckInput.npms = base64Decode(root.get("notary PMS share").asText());
        notaryCheckInput.nt = base64Decode(root.get("notarization time").asText());
        notaryCheckInput.epk = base64Decode(root.get("ephemeral pubkey").asText());
        notaryCheckInput.epkvf = base64Decode(root.get("ephemeral valid from").asText());
        notaryCheckInput.epkvu = base64Decode(root.get("ephemeral valid until").asText());
        notaryCheckInput.ss = base64Decode(root.get("session signature").asText());
        notaryCheckInput.esbn = base64Decode(root.get("ephemeral signed by master key").asText());
//        long[] serverEcPubkey = base64Decode(root.get("server pubkey for ECDHE").asText());
        notaryCheckInput.sr_padded_len = 896 / 4 / 16;
        notaryCheckInput.crc_len = notaryCheckInput.crc.length;
        // todo: add the actual logic
        notaryCheckInput.sr_padded = Utility.padding(Utility.concat(serverRecords0, serverRecords1));
        // pubkeyPEM2raw(serverEcPubkey)
        notaryCheckInput.np = new long[] {4, 2, 157, 226, 0, 176, 161, 178, 63, 37, 58, 65, 41, 99, 81, 121, 5, 224, 140, 115, 39, 122, 219, 205, 176, 120, 55, 236, 53, 255, 37, 49, 136, 187, 196, 69, 85, 86, 173, 22, 250, 43, 54, 237, 192, 167, 29, 144, 28, 210, 183, 172, 100, 116, 221, 95, 130, 168, 74, 177, 96, 122, 24, 79, 112};
        notaryCheckInput.tbs1_padded = new long[] {78, 229, 69, 181, 197, 187, 112, 88, 195, 46, 153, 110, 137, 145, 27, 229, 83, 200, 16, 147, 76, 9, 19, 82, 100, 78, 117, 227, 182, 59, 16, 61, 83, 152, 214, 62, 216, 167, 14, 170, 240, 43, 95, 16, 47, 221, 33, 79, 235, 152, 27, 227, 17, 133, 59, 62, 61, 234, 79, 219, 215, 96, 228, 15, 6, 0, 197, 213, 222, 110, 152, 193, 193, 203, 117, 218, 90, 168, 201, 175, 101, 161, 45, 33, 232, 116, 140, 114, 238, 197, 61, 237, 245, 238, 129, 205, 0, 0, 0, 0, 0, 0, 0, 1, 23, 3, 3, 0, 124, 0, 0, 0, 39, 16, 230, 12, 35, 129, 42, 248, 127, 107, 13, 234, 149, 191, 133, 42, 130, 208, 246, 73, 195, 43, 214, 200, 243, 62, 173, 91, 44, 177, 118, 68, 149, 19, 117, 18, 232, 92, 125, 145, 136, 186, 170, 135, 247, 30, 80, 7, 218, 233, 0, 121, 19, 209, 213, 221, 197, 172, 189, 36, 143, 145, 123, 44, 28, 18, 106, 116, 206, 154, 24, 108, 118, 187, 162, 96, 179, 138, 117, 3, 65, 223, 116, 5, 11, 100, 56, 204, 102, 40, 132, 215, 181, 87, 132, 155, 82, 12, 18, 101, 65, 13, 121, 195, 175, 184, 240, 37, 190, 0, 115, 200, 57, 157, 184, 184, 69, 226, 38, 76, 250, 101, 127, 169, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 104, 0, 0, 0, 0, 0, 0, 3, 224, 4, 164, 203, 47, 187, 53, 92, 172, 243, 181, 58, 31, 142, 159, 18, 250, 191, 225, 4, 171, 209, 165, 163, 17, 246, 103, 179, 173, 126, 58, 229, 72, 93, 114, 45, 111, 137, 141, 27, 154, 132, 249, 241, 14, 237, 36, 223, 188, 231, 107, 249, 148, 191, 20, 54, 162, 144, 217, 228, 64, 31, 97, 186, 54, 81, 37, 82, 131, 170, 184, 138, 88, 135, 165, 222, 179, 130, 207, 88, 4, 72, 204, 98, 174, 55, 170, 44, 113, 181, 10, 194, 114, 5, 75, 203, 119, 201, 221, 45, 193, 122, 173, 194, 148, 87, 58, 84, 44, 101, 245, 190, 146, 109, 45, 39, 48, 93, 87, 248, 86, 137, 157, 18, 224, 94, 169, 206, 35, 138, 231, 117, 12, 83, 252, 46, 173, 33, 0, 0, 0, 0, 97, 191, 78, 78, 128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 12, 136};
        notaryCheckInput.exp_ct = new AES128().aes128GcmDecrypt(serverRecords0, notaryCheckInput.cswk, notaryCheckInput.nswk, notaryCheckInput.csiv, notaryCheckInput.nsiv);
        notaryCheckInput.tcp = Utility.padding(Utility.concat(notaryCheckInput.nt, notaryCheckInput.exp_ct));
        // sha256(tcp)
        notaryCheckInput.exp_hash = new long[] {3451611916L, 778424106L, 1297035016L, 1945110051L, 589686400L, 2837115548L, 2980338592L, 631778133L};


//        long[] sr_padded = sr0 + sr1 + padding
//        long[] np = pubkeyPEM2raw(serverEcPubkey)
//(`-----BEGIN PUBLIC KEY-----
//MFkwEwYHKoZIzj0CAQYIKoZIzj0DAQcDQgAEAp3iALChsj8lOkEpY1F5BeCMcyd6
//282weDfsNf8lMYi7xEVVVq0W+is27cCnHZAc0resZHTdX4KoSrFgehhPcA==
//-----END PUBLIC KEY-----
//        long[] tbs1_padded = concat([
            //  commitHash,
            //  keyShareHash,
            //  pmsShareHash,
            //  clientReqCipher,
            //  serverEcPubkey,
            //  notaryPMSShare,
            //  notaryCwkShare,
            //  notaryCivShare,
            //  notarySwkShare,
            //  notarySivShare,
            //  notaryTime
//        ]);
//        long[] exp_ct = AES of sr
//        long[] tcp = nt + exp_ct + padding
//        long[] exp_hash = sha256 of tcp

        return 0;
    }

    public long[] base64Decode(String text) {
        byte[] bytes = Base64.getDecoder().decode(text);
        long[] res = new long[bytes.length];
        for (int i = 0; i < res.length; i++) {
            res[i] = (long) bytes[i] & 0xff;
            log.debug(i + " : " + res[i]);
        }
        return res;
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new NotaryJsonParser()).execute(args);
        System.exit(exitCode);
    }
}
