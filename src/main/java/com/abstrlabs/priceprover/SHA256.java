package com.abstrlabs.priceprover;

import com.abstrlabs.priceprover.util.Utility;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

public class SHA256 {

    public long[] sha256Hash(long[] tcp, int size) throws NoSuchAlgorithmException {
        byte[] byteArr = Utility.toByteArray(tcp);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodeArr = digest.digest(byteArr);
        return Utility.toLongArray(encodeArr, size);
    }
}
