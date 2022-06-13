package com.abstrlabs.priceprover.util;

import com.abstrlabs.priceprover.Configs;
import com.abstrlabs.priceprover.circuits.AES128;
import com.abstrlabs.priceprover.circuits.Sudoku9x9;
import com.abstrlabs.priceprover.circuits.Multiplier;
import com.abstrlabs.priceprover.circuits.SHA256;
import com.abstrlabs.priceprover.circuits.EC_SigCheck;
import com.abstrlabs.priceprover.circuits.XorTest;
import com.abstrlabs.priceprover.circuits.XorTest2;
import lombok.extern.log4j.Log4j2;
import org.junit.Test;

import java.io.File;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

@Log4j2
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
    public void sudukoCircuitGeneratorWriteCircuit(){
        Configs.writeCircuits = true;
        Configs.circuitPath = "./circuit/sudoku.arith";
        File circuitFile = new File(Configs.circuitPath);
        if (circuitFile.exists()) {
            circuitFile.delete();
        }
        new Sudoku9x9();
        assertTrue(circuitFile.exists());
        assertTrue(circuitFile.length() > 0);
    }

    @Test
    public void multiplierCircuitGeneratorWriteCircuit(){
        Configs.writeCircuits = true;
        Configs.circuitPath = "./circuit/multiplier.arith";
        File circuitFile = new File(Configs.circuitPath);
        if (circuitFile.exists()) {
            circuitFile.delete();
        }
        new Multiplier();
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
        log.info("Input file generated: " + inputFile.getAbsolutePath());
        assertTrue(circuitFile.exists());
    }

    @Test
    public void sudokuCircuitGeneratorWriteInput(){
        Configs.writeCircuits = true;
        Configs.circuitPath = "./circuit/sudoku.arith";
        Configs.setPath("sudoku");
        File circuitFile = new File(Configs.circuitPath);
        File inputFile = new File(Configs.getInputPath());
        new Sudoku9x9();
        assertTrue(inputFile.exists());
        assertTrue(inputFile.length() > 0);
        log.info("Input file generated: " + inputFile.getAbsolutePath());
        assertTrue(circuitFile.exists());
    }

    @Test
    public void multiplierCircuitGeneratorWriteInput(){
        Configs.writeCircuits = true;
        Configs.circuitPath = "./circuit/multiplier.arith";
        Configs.setPath("multiplier");
        File circuitFile = new File(Configs.circuitPath);
        File inputFile = new File(Configs.getInputPath());
        new Multiplier();
        assertTrue(inputFile.exists());
        assertTrue(inputFile.length() > 0);
        log.info("Input file generated: " + inputFile.getAbsolutePath());
        assertTrue(circuitFile.exists());
    }

    @Test
    public void sha256CircuitGeneratorWriteInput(){
        Configs.writeInputs = true;
        Configs.circuitPath = "./circuit/SHA256.arith";
        Configs.setPath("sha256");
        File circuitFile = new File(Configs.circuitPath);
        File inputFile = new File(Configs.getInputPath());
        Configs.writeCircuits = !circuitFile.exists();
        new SHA256();
        assertTrue(inputFile.exists());
        assertTrue(inputFile.length() > 0);
        log.info("Input file generated: " + inputFile.getAbsolutePath());
        assertTrue(circuitFile.exists());
    }

    @Test
    public void ecSigCheckCircuitGeneratorWriteInput(){
        Configs.writeInputs = true;
        Configs.circuitPath = "./circuit/EC_SigCheck.arith";
        Configs.setPath("ecsigcheck");
        File circuitFile = new File(Configs.circuitPath);
        File inputFile = new File(Configs.getInputPath());
        Configs.writeCircuits = !circuitFile.exists();
        new EC_SigCheck();
        assertTrue(inputFile.exists());
        assertTrue(inputFile.length() > 0);
        log.info("Input file generated: " + inputFile.getAbsolutePath());
        assertTrue(circuitFile.exists());
    }

    @Test
    public void xorTestCircuitGeneratorWriteInput(){
        Configs.writeInputs = true;
        Configs.circuitPath = "./circuit/XorTest.arith";
        Configs.setPath("XorTest");
        File circuitFile = new File(Configs.circuitPath);
        File inputFile = new File(Configs.getInputPath());
        Configs.writeCircuits = !circuitFile.exists();
        new XorTest();
        assertTrue(inputFile.exists());
        assertTrue(inputFile.length() > 0);
        log.info("Input file generated: " + inputFile.getAbsolutePath());
        assertTrue(circuitFile.exists());
    }

    @Test
    public void xorTest2CircuitGeneratorWriteInput(){
        Configs.writeInputs = true;
        Configs.circuitPath = "./circuit/XorTest2.arith";
        Configs.setPath("XorTest2");
        File circuitFile = new File(Configs.circuitPath);
        File inputFile = new File(Configs.getInputPath());
        Configs.writeCircuits = !circuitFile.exists();
        new XorTest2();
        assertTrue(inputFile.exists());
        assertTrue(inputFile.length() > 0);
        log.info("Input file generated: " + inputFile.getAbsolutePath());
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
        log.info("Input file generated: " + inputFile.getAbsolutePath());
        assertFalse(circuitFile.exists());
    }
}
