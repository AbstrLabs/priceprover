package com.abstrlabs.priceprover;

import com.abstrlabs.priceprover.util.Utility;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {
    private static final int OUTPUT_SIZE = 8;

    public long[] sha256Hash(long[] tcp) throws NoSuchAlgorithmException {
        byte[] byteArr = Utility.toByteArray(tcp);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodeArr = digest.digest(byteArr);
//        byte[] test0 = new byte[] {encodeArr[0], encodeArr[1], encodeArr[2], encodeArr[3]};
//        for (byte i : test0) {
//            System.out.print(String.format("%8s", Integer.toBinaryString(i & 0xFF)).replace(' ', '0'));
//        }
//        long test = new BigInteger(test0).longValue();

        return Utility.toLongArray(encodeArr, OUTPUT_SIZE);
    }
}
