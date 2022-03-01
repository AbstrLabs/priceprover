package com.abstrlabs.priceprover.util;

import java.math.BigInteger;
import java.nio.Buffer;
import java.util.Arrays;

public class Utility {
    public static byte[] concat(byte[] first, byte[] second) {
        byte[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static long[] concat(long[] first, long[] second) {
        long[] result = Arrays.copyOf(first, first.length + second.length);
        System.arraycopy(second, 0, result, first.length, second.length);
        return result;
    }

    public static byte[] xor(byte[] arr1, byte[] arr2) {
        byte[] result = new byte[arr1.length];

        int i = 0;
        for (byte b : arr1) {
            result[i] = (byte) (b ^ arr2[i++]);
        }

        return result;
    }

    public static long[] toLongArray(byte[] arr) {
        long[] result = new long[arr.length];
        for (int i = 0; i < result.length; i++) {
            result[i] = (long) arr[i] & 0xff;
        }
        return result;
    }

    public static byte[] toByteArray(int num) {
        return BigInteger.valueOf(num).toByteArray();
    }

    public static byte[] toByteArray(long[] arr) {
        byte[] result = new byte[arr.length];
        for (int i = 0; i < arr.length; i++) {
            result[i] = (byte) arr[i];
        }
        return result;
    }

    public static long[] toLongArray(byte[] byteArr, int size) {
        long[] result = new long[size];
        int i = 0;
        while (i < byteArr.length) {
            for (int j = 0; j < byteArr.length / size; j++) {
                result[i / size] += (long) byteArr[i] & 0xff;
                i++;
            }
        }
        return result;
    }

    public static long[] padding(long[] arr) {
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

}
