package com.abstrlabs.priceprover;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import picocli.CommandLine;
import picocli.CommandLine.Command;
import picocli.CommandLine.Option;

import java.io.File;
import java.io.IOException;
import java.util.Base64;
import java.util.concurrent.Callable;


@Command(name = "notaryparser", description = "Parse notary json from pagesinger")
public class NotaryJsonParser implements Callable<Integer> {

    private final Logger log = LogManager.getLogger(this.getClass());

    public NotaryCheckInput notaryCheckInput = new NotaryCheckInput();

    @Option(names = {"-nf", "--notaryfile"}, defaultValue = "./out/notary.json", description = "decode and preprocess notary json.")
    String notaryFilePath;

    @Override
    public Integer call() throws IOException {
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
        notaryCheckInput.sr_padded = new long[] {0, 0, 0, 0, 0, 0, 0, 1, 124, 91, 228, 82, 57, 48, 237, 251, 217, 241, 170, 46, 113, 100, 150, 70, 52, 190, 213, 222, 102, 74, 128, 62, 193, 24, 94, 231, 191, 200, 43, 123, 60, 138, 215, 123, 169, 253, 122, 172, 206, 255, 107, 8, 165, 219, 102, 89, 132, 167, 58, 15, 54, 205, 168, 133, 128, 8, 123, 117, 42, 178, 147, 198, 38, 59, 222, 76, 191, 221, 167, 88, 254, 4, 220, 128, 164, 112, 233, 231, 233, 235, 134, 250, 194, 34, 112, 197, 23, 214, 104, 76, 215, 61, 171, 203, 73, 104, 143, 249, 157, 214, 7, 249, 207, 96, 55, 161, 138, 229, 90, 168, 204, 110, 178, 187, 175, 155, 145, 76, 226, 62, 249, 60, 194, 21, 17, 242, 84, 156, 66, 184, 81, 192, 135, 90, 215, 229, 228, 85, 111, 119, 247, 237, 3, 74, 42, 159, 230, 187, 163, 67, 116, 34, 216, 177, 114, 196, 132, 208, 194, 135, 60, 214, 114, 174, 54, 53, 88, 39, 119, 37, 133, 245, 50, 71, 201, 243, 153, 255, 134, 168, 21, 156, 60, 108, 129, 239, 198, 187, 112, 216, 60, 142, 131, 123, 54, 42, 69, 95, 239, 45, 100, 138, 193, 132, 173, 143, 42, 15, 96, 29, 88, 158, 122, 81, 180, 82, 151, 23, 170, 37, 37, 101, 247, 181, 171, 121, 26, 9, 223, 122, 164, 19, 125, 198, 148, 176, 57, 138, 201, 90, 201, 131, 96, 142, 228, 131, 82, 24, 199, 82, 134, 169, 84, 37, 128, 254, 31, 187, 207, 108, 164, 203, 158, 18, 252, 184, 111, 20, 149, 117, 231, 198, 129, 80, 209, 151, 149, 99, 183, 225, 184, 12, 150, 141, 66, 229, 59, 230, 113, 227, 226, 203, 250, 175, 63, 165, 147, 124, 42, 2, 189, 88, 98, 97, 97, 107, 183, 208, 124, 106, 37, 1, 58, 3, 208, 156, 27, 154, 231, 16, 123, 199, 25, 234, 191, 178, 113, 124, 209, 201, 100, 164, 57, 113, 154, 181, 144, 89, 14, 2, 88, 190, 248, 206, 191, 234, 201, 173, 147, 205, 73, 115, 186, 85, 111, 197, 95, 10, 33, 125, 152, 222, 26, 79, 90, 176, 172, 29, 18, 133, 13, 247, 9, 21, 102, 10, 52, 113, 78, 69, 224, 117, 144, 105, 198, 23, 123, 22, 159, 18, 250, 166, 114, 18, 99, 40, 101, 36, 156, 103, 5, 191, 147, 71, 15, 205, 151, 162, 175, 175, 255, 117, 75, 62, 81, 101, 217, 152, 27, 231, 41, 70, 146, 182, 221, 66, 191, 104, 218, 89, 191, 168, 142, 106, 159, 140, 13, 119, 250, 217, 55, 117, 5, 79, 45, 193, 251, 175, 66, 171, 109, 18, 12, 141, 181, 1, 247, 82, 110, 212, 107, 250, 177, 169, 179, 30, 32, 88, 142, 47, 88, 177, 192, 202, 15, 63, 33, 34, 250, 10, 172, 202, 251, 49, 41, 182, 240, 50, 28, 225, 134, 15, 87, 87, 17, 41, 238, 183, 225, 68, 227, 109, 238, 198, 96, 107, 96, 100, 12, 179, 14, 140, 30, 231, 41, 181, 131, 99, 56, 102, 4, 177, 140, 14, 243, 21, 103, 140, 89, 192, 139, 249, 55, 234, 110, 65, 217, 197, 158, 15, 171, 64, 201, 52, 38, 159, 124, 244, 255, 125, 151, 149, 30, 249, 74, 95, 58, 194, 168, 25, 132, 90, 212, 116, 60, 163, 188, 92, 103, 150, 104, 48, 187, 15, 140, 192, 231, 140, 12, 39, 208, 136, 187, 169, 91, 179, 252, 107, 198, 44, 31, 90, 236, 122, 154, 85, 126, 243, 206, 133, 78, 95, 28, 249, 32, 211, 165, 135, 194, 31, 244, 112, 54, 224, 65, 66, 57, 221, 82, 235, 65, 211, 150, 172, 213, 114, 194, 123, 43, 210, 23, 218, 243, 207, 142, 76, 184, 52, 184, 175, 21, 189, 15, 252, 4, 185, 73, 119, 234, 240, 42, 189, 196, 51, 159, 253, 141, 227, 161, 202, 126, 131, 62, 28, 119, 39, 107, 244, 90, 43, 225, 187, 121, 108, 122, 234, 254, 102, 120, 172, 201, 93, 11, 189, 195, 216, 115, 139, 188, 98, 213, 13, 142, 241, 92, 55, 244, 108, 239, 10, 33, 191, 233, 112, 199, 78, 18, 17, 98, 253, 134, 75, 112, 176, 14, 29, 226, 86, 134, 133, 32, 67, 22, 67, 2, 103, 203, 83, 10, 68, 144, 150, 209, 61, 149, 221, 115, 140, 96, 29, 10, 217, 28, 234, 62, 231, 2, 185, 34, 102, 146, 74, 162, 196, 43, 226, 152, 247, 204, 84, 117, 99, 83, 135, 241, 108, 121, 222, 225, 237, 114, 141, 193, 25, 13, 6, 27, 1, 31, 95, 38, 25, 125, 233, 152, 247, 150, 224, 147, 58, 1, 119, 13, 246, 181, 2, 189, 75, 90, 90, 84, 224, 234, 191, 159, 235, 187, 0, 0, 0, 0, 0, 0, 0, 2, 29, 228, 90, 94, 131, 185, 245, 32, 162, 167, 96, 32, 65, 109, 12, 62, 240, 71, 67, 39, 181, 128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 26, 112};
        notaryCheckInput.np = new long[] {4, 2, 157, 226, 0, 176, 161, 178, 63, 37, 58, 65, 41, 99, 81, 121, 5, 224, 140, 115, 39, 122, 219, 205, 176, 120, 55, 236, 53, 255, 37, 49, 136, 187, 196, 69, 85, 86, 173, 22, 250, 43, 54, 237, 192, 167, 29, 144, 28, 210, 183, 172, 100, 116, 221, 95, 130, 168, 74, 177, 96, 122, 24, 79, 112};
        notaryCheckInput.tbs1_padded = new long[] {78, 229, 69, 181, 197, 187, 112, 88, 195, 46, 153, 110, 137, 145, 27, 229, 83, 200, 16, 147, 76, 9, 19, 82, 100, 78, 117, 227, 182, 59, 16, 61, 83, 152, 214, 62, 216, 167, 14, 170, 240, 43, 95, 16, 47, 221, 33, 79, 235, 152, 27, 227, 17, 133, 59, 62, 61, 234, 79, 219, 215, 96, 228, 15, 6, 0, 197, 213, 222, 110, 152, 193, 193, 203, 117, 218, 90, 168, 201, 175, 101, 161, 45, 33, 232, 116, 140, 114, 238, 197, 61, 237, 245, 238, 129, 205, 0, 0, 0, 0, 0, 0, 0, 1, 23, 3, 3, 0, 124, 0, 0, 0, 39, 16, 230, 12, 35, 129, 42, 248, 127, 107, 13, 234, 149, 191, 133, 42, 130, 208, 246, 73, 195, 43, 214, 200, 243, 62, 173, 91, 44, 177, 118, 68, 149, 19, 117, 18, 232, 92, 125, 145, 136, 186, 170, 135, 247, 30, 80, 7, 218, 233, 0, 121, 19, 209, 213, 221, 197, 172, 189, 36, 143, 145, 123, 44, 28, 18, 106, 116, 206, 154, 24, 108, 118, 187, 162, 96, 179, 138, 117, 3, 65, 223, 116, 5, 11, 100, 56, 204, 102, 40, 132, 215, 181, 87, 132, 155, 82, 12, 18, 101, 65, 13, 121, 195, 175, 184, 240, 37, 190, 0, 115, 200, 57, 157, 184, 184, 69, 226, 38, 76, 250, 101, 127, 169, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 104, 0, 0, 0, 0, 0, 0, 3, 224, 4, 164, 203, 47, 187, 53, 92, 172, 243, 181, 58, 31, 142, 159, 18, 250, 191, 225, 4, 171, 209, 165, 163, 17, 246, 103, 179, 173, 126, 58, 229, 72, 93, 114, 45, 111, 137, 141, 27, 154, 132, 249, 241, 14, 237, 36, 223, 188, 231, 107, 249, 148, 191, 20, 54, 162, 144, 217, 228, 64, 31, 97, 186, 54, 81, 37, 82, 131, 170, 184, 138, 88, 135, 165, 222, 179, 130, 207, 88, 4, 72, 204, 98, 174, 55, 170, 44, 113, 181, 10, 194, 114, 5, 75, 203, 119, 201, 221, 45, 193, 122, 173, 194, 148, 87, 58, 84, 44, 101, 245, 190, 146, 109, 45, 39, 48, 93, 87, 248, 86, 137, 157, 18, 224, 94, 169, 206, 35, 138, 231, 117, 12, 83, 252, 46, 173, 33, 0, 0, 0, 0, 97, 191, 78, 78, 128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 12, 136};
        notaryCheckInput.exp_ct = new long[] {123, 10, 32, 32, 32, 32, 34, 71, 108, 111, 98, 97, 108, 32, 81, 117, 111, 116, 101, 34, 58, 32, 123, 10, 32, 32, 32, 32, 32, 32, 32, 32, 34, 48, 49, 46, 32, 115, 121, 109, 98, 111, 108, 34, 58, 32, 34, 73, 66, 77, 34, 44, 10, 32, 32, 32, 32, 32, 32, 32, 32, 34, 48, 50, 46, 32, 111, 112, 101, 110, 34, 58, 32, 34, 49, 50, 53, 46, 56, 55, 48, 48, 34, 44, 10, 32, 32, 32, 32, 32, 32, 32, 32, 34, 48, 51, 46, 32, 104, 105, 103, 104, 34, 58, 32, 34, 49, 50, 56, 46, 54, 52, 48, 48, 34, 44, 10, 32, 32, 32, 32, 32, 32, 32, 32, 34, 48, 52, 46, 32, 108, 111, 119, 34, 58, 32, 34, 49, 50, 53, 46, 50, 48, 57, 51, 34, 44, 10, 32, 32, 32, 32, 32, 32, 32, 32, 34, 48, 53, 46, 32, 112, 114, 105, 99, 101, 34, 58, 32, 34, 49, 50, 55, 46, 52, 48, 48, 48, 34, 44, 10, 32, 32, 32, 32, 32, 32, 32, 32, 34, 48, 54, 46, 32, 118, 111, 108, 117, 109, 101, 34, 58, 32, 34, 49, 48, 51, 56, 50, 54, 57, 51, 34, 44, 10, 32, 32, 32, 32, 32, 32, 32, 32, 34, 48, 55, 46, 32, 108, 97, 116, 101, 115, 116, 32, 116, 114, 97, 100, 105, 110, 103, 32, 100, 97, 121, 34, 58, 32, 34, 50, 48, 50, 49, 45, 49, 50, 45, 49, 55, 34, 44, 10, 32, 32, 32, 32, 32, 32, 32, 32, 34, 48, 56, 46, 32, 112, 114, 101, 118, 105, 111, 117, 115, 32, 99, 108, 111, 115, 101, 34, 58, 32, 34, 49, 50, 53, 46, 57, 51, 48, 48, 34, 44, 10, 32, 32, 32, 32, 32, 32, 32, 32, 34, 48, 57, 46, 32, 99, 104, 97, 110, 103, 101, 34, 58, 32, 34, 49, 46, 52, 55, 48, 48, 34, 44, 10, 32, 32, 32, 32, 32, 32, 32, 32, 34, 49, 48, 46, 32, 99, 104, 97, 110, 103, 101, 32, 112, 101, 114, 99, 101, 110, 116, 34, 58, 32, 34, 49, 46, 49, 54, 55, 51, 37, 34, 10, 32, 32, 32, 32, 125, 10, 125};
        notaryCheckInput.tcp = new long[] {0, 0, 0, 0, 97, 191, 78, 78, 123, 10, 32, 32, 32, 32, 34, 71, 108, 111, 98, 97, 108, 32, 81, 117, 111, 116, 101, 34, 58, 32, 123, 10, 32, 32, 32, 32, 32, 32, 32, 32, 34, 48, 49, 46, 32, 115, 121, 109, 98, 111, 108, 34, 58, 32, 34, 73, 66, 77, 34, 44, 10, 32, 32, 32, 32, 32, 32, 32, 32, 34, 48, 50, 46, 32, 111, 112, 101, 110, 34, 58, 32, 34, 49, 50, 53, 46, 56, 55, 48, 48, 34, 44, 10, 32, 32, 32, 32, 32, 32, 32, 32, 34, 48, 51, 46, 32, 104, 105, 103, 104, 34, 58, 32, 34, 49, 50, 56, 46, 54, 52, 48, 48, 34, 44, 10, 32, 32, 32, 32, 32, 32, 32, 32, 34, 48, 52, 46, 32, 108, 111, 119, 34, 58, 32, 34, 49, 50, 53, 46, 50, 48, 57, 51, 34, 44, 10, 32, 32, 32, 32, 32, 32, 32, 32, 34, 48, 53, 46, 32, 112, 114, 105, 99, 101, 34, 58, 32, 34, 49, 50, 55, 46, 52, 48, 48, 48, 34, 44, 10, 32, 32, 32, 32, 32, 32, 32, 32, 34, 48, 54, 46, 32, 118, 111, 108, 117, 109, 101, 34, 58, 32, 34, 49, 48, 51, 56, 50, 54, 57, 51, 34, 44, 10, 32, 32, 32, 32, 32, 32, 32, 32, 34, 48, 55, 46, 32, 108, 97, 116, 101, 115, 116, 32, 116, 114, 97, 100, 105, 110, 103, 32, 100, 97, 121, 34, 58, 32, 34, 50, 48, 50, 49, 45, 49, 50, 45, 49, 55, 34, 44, 10, 32, 32, 32, 32, 32, 32, 32, 32, 34, 48, 56, 46, 32, 112, 114, 101, 118, 105, 111, 117, 115, 32, 99, 108, 111, 115, 101, 34, 58, 32, 34, 49, 50, 53, 46, 57, 51, 48, 48, 34, 44, 10, 32, 32, 32, 32, 32, 32, 32, 32, 34, 48, 57, 46, 32, 99, 104, 97, 110, 103, 101, 34, 58, 32, 34, 49, 46, 52, 55, 48, 48, 34, 44, 10, 32, 32, 32, 32, 32, 32, 32, 32, 34, 49, 48, 46, 32, 99, 104, 97, 110, 103, 101, 32, 112, 101, 114, 99, 101, 110, 116, 34, 58, 32, 34, 49, 46, 49, 54, 55, 51, 37, 34, 10, 32, 32, 32, 32, 125, 10, 125, 128, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 12, 64};
        notaryCheckInput.exp_hash = new long[] {3451611916L, 778424106L, 1297035016L, 1945110051L, 589686400L, 2837115548L, 2980338592L, 631778133L};


//        long[] sr_padded = sr0 + sr1 + padding
//        long[] np = pubkeyPEM2raw
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
