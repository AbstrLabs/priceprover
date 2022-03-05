package com.abstrlabs.priceprover.util;

import lombok.SneakyThrows;
import lombok.experimental.UtilityClass;
import lombok.extern.log4j.Log4j2;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.Base64;

@Log4j2
@UtilityClass
public class Crypto {

    @SneakyThrows
    public long[] sha256Hash(long[] data, int size) {
        byte[] byteArr = Utility.toByteArray(data);
        MessageDigest digest = MessageDigest.getInstance("SHA-256");
        byte[] encodeArr = digest.digest(byteArr);
        return Utility.toLongArray(encodeArr, size);
    }

    @SneakyThrows
    public long[] aes128GcmDecrypt(byte[] key, byte[] nonce, byte[] aad, byte[] data){
        final int GCM_TAG_LENGTH = 16;
        // Get Cipher Instance
        Cipher cipher = Cipher.getInstance("AES/GCM/NoPadding");
        // Create SecretKeySpec
        SecretKeySpec keySpec = new SecretKeySpec(key, "AES");
        // Create GCMParameterSpec
        GCMParameterSpec gcmParameterSpec = new GCMParameterSpec(GCM_TAG_LENGTH * 8, nonce);
        // Initialize Cipher for DECRYPT_MODE
        cipher.init(Cipher.DECRYPT_MODE, keySpec, gcmParameterSpec);
        cipher.updateAAD(aad);
        // Perform Decryption
        byte[] plainTextArr = cipher.doFinal(data);
        String plainText = new String(plainTextArr);
        log.debug(plainText);
        int content_start = plainText.indexOf("{");
        int content_end = plainText.lastIndexOf("}");
        byte[] content = Arrays.copyOfRange(plainTextArr, content_start, content_end + 1);
        return Utility.toLongArray(content);
    }

    public long[] base64Decode(String text) {
        byte[] bytes = Base64.getDecoder().decode(text);
        return Utility.toLongArray(bytes);
    }
}
