package com.abstrlabs.priceprover;

import com.abstrlabs.priceprover.util.CommandExecutor;
import lombok.extern.log4j.Log4j2;
import picocli.CommandLine;

import java.nio.file.Paths;
import java.util.concurrent.Callable;

@Log4j2
@CommandLine.Command(name = "prove", mixinStandardHelpOptions = true, description = "Trigger libsnark and generate the proof")
public class LibsnarkCallBack implements Callable<Integer> {

    private static final String CIRCUIT_NAME = "priceProver.circuit";
    private static final String PRIMARY_IN = "primary.in";
    private static final String AUXILIARY_IN = "auxiliary.in";
    private static final String PROVING_KEY = "proving.key";
    private static final String VERIFICATION_KEY = "verification.key";
    private static final String PROOF = "proof";
    private static final String TRANSLATE = "translate";
    private static final String TRANSLATE_INPUT = "translate_input";
    private static final String GENERATE = "generate";
    private static final String PROVE = "prove";
    private static final String CONVERT = "convert";
    private static final String RUN_PPZKSNARK = "./depends/libsnark/run_ppzksnark";
    private static final String PRIMARY_IN_GNARK = "primary.in.gnark";
    private static final String VERIFICATION_KEY_GNARK = "verification.key.gnark";
    private static final String PROOF_GNARK = "proof.gnark";

    @CommandLine.Option(names = {"-op", "--outputPath"}, defaultValue = "./out", description = "output path for generated headers and notary files")
    String outputPath;

    @CommandLine.Option(names = {"-xc", "--xjsnarkCircuit"}, defaultValue = "./out/TLSNotaryCheck.arith", description = "the xjsnark generated circuit")
    String xjsnarkCircuit;

    @CommandLine.Option(names = {"-xi", "--xjsnarkInput"}, defaultValue = "./out/TLSNotaryCheck_Sample_Run1.in", description = "the xjsnark generated input")
    String xjsnarkInput;

    @CommandLine.Option(names = {"-fi", "--firstTime"}, description = "if it is first time run")
    boolean firstTime = Configs.initializeRequired;

    private int convert() {
        CommandExecutor ce = new CommandExecutor();
        String missionName;
        String[] commands;

        missionName = "convert libsnark vk, primary input and proof to gnark format";
        commands = new String[]{RUN_PPZKSNARK, CONVERT, getPath(PROVING_KEY), getPath(VERIFICATION_KEY), getPath(PRIMARY_IN),
                getPath(PROOF), getPath(VERIFICATION_KEY_GNARK), getPath(PRIMARY_IN_GNARK), getPath(PROOF_GNARK)};
        if (ce.execute(missionName, commands)) {
            return 0;
        };
        return -1;
    }

    @Override
    public Integer call() {
        CommandExecutor ce = new CommandExecutor();
        String missionName;
        String[] commands;

        if (firstTime) {
            missionName = "translate xjsnark circuit and input to libsnark backend";
            commands = new String[]{RUN_PPZKSNARK, TRANSLATE, xjsnarkCircuit, xjsnarkInput,
                    getPath(CIRCUIT_NAME), getPath(PRIMARY_IN), getPath(AUXILIARY_IN)};
            if (ce.execute(missionName, commands)) {
                missionName = "generate proving key and verification key";
                commands = new String[]{RUN_PPZKSNARK, GENERATE, getPath(CIRCUIT_NAME),
                        getPath(PROVING_KEY), getPath(VERIFICATION_KEY)};
                if (ce.execute(missionName, commands)) {
                    missionName = "generate proof";
                    commands = new String[]{RUN_PPZKSNARK, PROVE, getPath(CIRCUIT_NAME),
                            getPath(PROVING_KEY), getPath(PRIMARY_IN), getPath(AUXILIARY_IN), getPath(PROOF)};
                    if (ce.execute(missionName, commands)) {
                        return convert();
                    };
                }
            }
        } else {
             /* if it's not the first time, necessary steps:
             *   1. translate input
             *   2. generate proof
             *   Assumptions: already have the circuit, prooving.key and verification.key
             */
            missionName = "translate xjsnark input to libsnark backend";
            commands = new String[]{RUN_PPZKSNARK, TRANSLATE_INPUT, xjsnarkCircuit, xjsnarkInput,
                    getPath(CIRCUIT_NAME), getPath(PRIMARY_IN), getPath(AUXILIARY_IN)};
            if (ce.execute(missionName, commands)) {
                missionName = "generate proof";
                commands = new String[]{RUN_PPZKSNARK, PROVE, getPath(CIRCUIT_NAME), getPath(PROVING_KEY),
                        getPath(PRIMARY_IN), getPath(AUXILIARY_IN), getPath(PROOF)};
                if (ce.execute(missionName, commands)) {
                    return convert();
                }
            }
        }
        return -1;
    }

    private String getPath(String fileName) {
        return String.valueOf(Paths.get(outputPath, fileName));
    }

    public static void main(String[] args) {
        int exitCode = new CommandLine(new LibsnarkCallBack()).execute(args);
        System.exit(exitCode);
    }
}