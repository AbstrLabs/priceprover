package com.abstrlabs.priceprover;

import com.abstrlabs.priceprover.util.Utility;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javax.crypto.Cipher;
import javax.crypto.spec.GCMParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.util.ArrayList;
import java.util.Arrays;


public class AES128 {
    public static final int GCM_TAG_LENGTH = 16;
    private final Logger log = LogManager.getLogger(this.getClass());

    public long[] aes128GcmDecrypt(long[] sr_padded, long[] cswk, long[] nswk, long[] csiv, long[] nsiv) throws Exception {
        byte[] server_records0 = Utility.toByteArray(sr_padded);
        byte[] client_swk_share = Utility.toByteArray(cswk);
        byte[] notary_swk_share = Utility.toByteArray(nswk);
        byte[] client_siv_share = Utility.toByteArray(csiv);
        byte[] notary_siv_share = Utility.toByteArray(nsiv);
        byte[] iv = Utility.xor(client_siv_share, notary_siv_share);
        byte[] key = Utility.xor(client_swk_share, notary_swk_share);
        byte[] nonce = Utility.concat(new ArrayList<> (Arrays.asList(iv, Arrays.copyOfRange(server_records0, 0, 8))));
        byte[] tmp = new byte[] {(byte) 0x17, (byte) 0x03, (byte) 0x03};
        byte[] tmp2 = Utility.toByteArray(server_records0.length - 8 - 16);
        byte[] aad = Utility.concat(new ArrayList<> (Arrays.asList(Arrays.copyOfRange(server_records0, 0, 8), tmp, tmp2)));
        byte[] data = Arrays.copyOfRange(server_records0, 8, server_records0.length);

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

}


