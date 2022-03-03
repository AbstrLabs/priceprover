package com.abstrlabs.priceprover;

import com.abstrlabs.priceprover.util.Utility;
import lombok.extern.log4j.Log4j2;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Log4j2
public class SHA256 {

    public long[] sha256Hash(long[] tcp, int size) {
        byte[] byteArr = Utility.toByteArray(tcp);
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] encodeArr = digest.digest(byteArr);
            return Utility.toLongArray(encodeArr, size);
        } catch (NoSuchAlgorithmException e) {
            log.error(e);
            return null;
        }
    }
}
