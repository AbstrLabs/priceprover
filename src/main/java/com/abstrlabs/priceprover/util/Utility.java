package com.abstrlabs.priceprover.util;

import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.Base64;

@Log4j2
@UtilityClass
public class Utility {

    public byte[] concat(byte[]... arrays) throws IOException {
        ByteArrayOutputStream byte_stream = new ByteArrayOutputStream();
        for (byte[] array : arrays) {
            byte_stream.write(array);
        }
        return byte_stream.toByteArray();
    }

    public long[] concat(long[]... arrays) {
        return Arrays.stream(arrays)
                .flatMapToLong(Arrays::stream)
                .toArray();
    }

    public byte[] xor(byte[] arr1, byte[] arr2) {
        byte[] result = new byte[arr1.length];

        int i = 0;
        for (byte b : arr1) {
            result[i] = (byte) (b ^ arr2[i++]);
        }

        return result;
    }

    public long[] toLongArray(byte[] arr) {
        long[] result = new long[arr.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = (long) arr[i] & 0xff;
        }
        return result;
    }

    public byte[] toByteArray(int num) {
        return BigInteger.valueOf(num).toByteArray();
    }

    public byte[] toByteArray(long[] arr) {
        byte[] result = new byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = (byte) arr[i];
        }
        return result;
    }

    public long[] toLongArray(byte[] byteArr, int size) {
        long[] result = new long[byteArr.length / size];
        int i = 0;
        while (i < byteArr.length) {
            result[i / size] = byteArr[i] & 0xff;
            i++;
            for (int j = 1; j < size; j++) {
                result[i / size] = (result[i / size] << 8) + (byteArr[i] & 0xff);
                i++;
            }
        }
        return result;
    }

    public long[] padding(long[] arr) {
        int len = arr.length;
        int n = len / 64;
        int mod = len % 64;
        int retLength = (mod == 63) ? ((n + 2) * 64) : ((n + 1) * 64);
        long[] ret = new long[retLength];
        System.arraycopy(arr, 0, ret, 0, len);
        ret[len] = 0x80;
        for (int i = len + 1; i < retLength - 4; i++) {
            ret[i] = 0;
        }
        for (int i = retLength - 4; i < retLength; i++) {
            ret[i] = ((len * 8L) >> ((retLength - i - 1) * 8)) & 0xff;
        }
        return ret;
    }

    public long[] base64Decode(String text) {
        byte[] bytes = Base64.getDecoder().decode(text);
        return toLongArray(bytes);
    }

    public long[] pubkeyPEMToRaw(String pkPEM) {
        int[] preasn1 = new int[]{0x30, 0x59, 0x30, 0x13, 0x06, 0x07, 0x2A, 0x86, 0x48, 0xCE, 0x3D, 0x02, 0x01, 0x06, 0x08, 0x2A, 0x86, 0x48, 0xCE, 0x3D, 0x03, 0x01, 0x07, 0x03, 0x42, 0x00};
        String[] lines = pkPEM.split(System.lineSeparator());
        log.debug(lines);
        StringBuilder encoded = new StringBuilder();
        for (String line : lines) {
            if (line.trim().length() > 0 &&
                    !line.contains("-BEGIN CERTIFICATE-") &&
                    !line.contains("-BEGIN PUBLIC KEY-") &&
                    !line.contains("-END PUBLIC KEY-") &&
                    !line.contains("-END CERTIFICATE-")) {
                encoded.append(line.trim());
            }
        }
        log.debug(encoded);
        long[] res = base64Decode(encoded.toString());
        return Arrays.copyOfRange(res, preasn1.length, res.length);
    }

    public String getByteBinaryString(byte b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 7; i >= 0; --i) {
            sb.append(b >>> i & 1);
        }
        return sb.toString();
    }

    public String getByteBinaryString(long b) {
        StringBuilder sb = new StringBuilder();
        for (int i = 31; i >= 0; --i) {
            sb.append(b >>> i & 1);
        }
        return sb.toString();
    }
}
