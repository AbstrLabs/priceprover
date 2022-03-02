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

        return Utility.toLongArray(encodeArr, OUTPUT_SIZE);
    }
}
