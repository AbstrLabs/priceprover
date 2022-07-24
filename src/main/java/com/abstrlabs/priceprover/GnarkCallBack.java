package com.abstrlabs.priceprover;

import com.abstrlabs.priceprover.util.CommandExecutor;
import lombok.extern.log4j.Log4j2;
import picocli.CommandLine;

import java.nio.file.Paths;
import java.util.concurrent.Callable;

@Log4j2
@CommandLine.Command(name = "prove", mixinStandardHelpOptions = true, description = "Trigger gnark and generate the proof")
public class GnarkCallBack implements Callable<Integer> {

    private static final String CIRCUIT_NAME = "priceProver.circuit";
    private static final String PROVING_KEY = "proving.key";
    private static final String VERIFICATION_KEY = "verification.key";
    private static final String PROOF = "proof";
    private static final String COMPILE = "compile";
    private static final String KEYGEN = "keygen";
    private static final String PROVE = "prove";
    private static final String XJSNARK_GNARK_PROVER = "./depends/xjsnark-gnark-prover/xjsnark-gnark-prover";

    @CommandLine.Option(names = {"-op", "--outputPath"}, defaultValue = "./out", description = "output path for generated headers and notary files")
    String outputPath;

    @CommandLine.Option(names = {"-xc", "--xjsnarkCircuit"}, defaultValue = "./out/TLSNotaryCheck.arith", description = "the xjsnark generated circuit")
    String xjsnarkCircuit;

    @CommandLine.Option(names = {"-xi", "--xjsnarkInput"}, defaultValue = "./out/TLSNotaryCheck_Sample_Run1.in", description = "the xjsnark generated input")
    String xjsnarkInput;

    @CommandLine.Option(names = {"-fi", "--firstTime"}, description = "if it is first time run")
    boolean firstTime = Configs.initializeRequired;

    @Override
    public Integer call() {
        CommandExecutor ce = new CommandExecutor();
        String missionName;
        String[] commands;

        if (firstTime) {
            missionName = "compile xjsnark circuit to gnark backend";
            commands = new String[]{XJSNARK_GNARK_PROVER, COMPILE, xjsnarkCircuit, getPath(CIRCUIT_NAME)};
            if (ce.execute(missionName, commands)) {
                missionName = "generate proving key and verification key";
                commands = new String[]{XJSNARK_GNARK_PROVER, KEYGEN, getPath(CIRCUIT_NAME),
                        getPath(PROVING_KEY), getPath(VERIFICATION_KEY)};
                if (ce.execute(missionName, commands)) {
                    missionName = "generate proof";
                    commands = new String[]{XJSNARK_GNARK_PROVER, PROVE, getPath(CIRCUIT_NAME),
                            getPath(PROVING_KEY), xjsnarkInput, getPath(PROOF)};
                    return ce.execute(missionName, commands) ? 0 : -1;
                }
            }
        } else {
            /* If it's not the first time, follow below assumptions
            * Already have the gnark-backend circuit, prooving.key and verification.key
            */
            missionName = "generate proof";
            commands = new String[]{XJSNARK_GNARK_PROVER, PROVE, getPath(CIRCUIT_NAME),
                    getPath(PROVING_KEY), xjsnarkInput, getPath(PROOF)};
            return ce.execute(missionName, commands) ? 0 : -1;
        }
        return -1;
    }

    private String getPath(String fileName) {
        return String.valueOf(Paths.get(outputPath, fileName));
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new GnarkCallBack()).execute(args);
        System.exit(exitCode);
    }
}