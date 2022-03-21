package com.abstrlabs.priceprover.util;

import com.abstrlabs.priceprover.Configs;
import com.abstrlabs.priceprover.circuits.AES128;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class CircuitGeneratorTest {

    @Test
    public void aes128CircuitGeneratorWriteCircuit(){
        Configs.writeCircuits = true;
        Configs.circuitPath = "./circuit/AES128.arith";
        File circuitFile = new File(Configs.circuitPath);
        if (circuitFile.exists()) {
            circuitFile.delete();
        }
        new AES128();
        assertTrue(circuitFile.exists());
        assertTrue(circuitFile.length() > 0);
    }

    @Test
    public void aes128CircuitGeneratorWriteInput(){
        Configs.writeInputs = true;
        Configs.circuitPath = "./circuit/AES128.arith";
        Configs.setPath("aes128");
        File circuitFile = new File(Configs.circuitPath);
        File inputFile = new File(Configs.getInputPath());
        Configs.writeCircuits = !circuitFile.exists();
        new AES128();
        assertTrue(inputFile.exists());
        assertTrue(inputFile.length() > 0);
        assertTrue(circuitFile.exists());
    }

    @Test
    public void aes128CircuitGenerateInputsOnly(){
        Configs.writeCircuits = false;
        Configs.writeInputs = true;
        Configs.circuitPath = "./circuit/AES128.arith";
        Configs.setPath("aes128");
        // will generate and set the output path to: ./out/aes128-timestamp/
        File circuitFile = new File(Configs.circuitPath);
        File inputFile = new File(Configs.getInputPath());
        if (circuitFile.exists()) {
            circuitFile.delete();
        }
        new AES128();
        assertTrue(inputFile.exists());
        assertTrue(inputFile.length() > 0);
        assertFalse(circuitFile.exists());
    }
}
