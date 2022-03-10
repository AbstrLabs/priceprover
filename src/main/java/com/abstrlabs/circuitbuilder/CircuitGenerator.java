package com.abstrlabs.circuitbuilder;

import backend.auxTypes.*;
import backend.config.Config;
import backend.eval.CircuitEvaluator;
import backend.eval.Instruction;
import backend.eval.SampleRun;
import backend.operations.WireLabelInstruction;
import backend.operations.primitive.AssertBasicOp;
import backend.operations.primitive.BasicOp;
import backend.operations.primitive.MulBasicOp;
import backend.optimizer.CircuitOptimizer;
import backend.structure.ConstantWire;
import backend.structure.VariableBitWire;
import backend.structure.VariableWire;
import backend.structure.Wire;
import lombok.SneakyThrows;
import util.Util;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.PrintWriter;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.concurrent.ConcurrentHashMap;


public abstract class CircuitGenerator extends backend.structure.CircuitGenerator{
    private static ConcurrentHashMap<Long, CircuitGenerator> __activeCircuitGenerators = new ConcurrentHashMap<>();

    private static CircuitGenerator __instance;

    protected int __currentWireId;

    private LinkedHashMap<Instruction, Instruction> __evaluationQueue;

    private LinkedHashMap<Instruction, Instruction> __nonOptimizedEvaluationQueue;

    private int __nonOptimalWireCount;

    protected Wire __zeroWire;

    protected Wire __oneWire;

    protected ArrayList<Wire> __inWires;

    protected ArrayList<Wire> __outWires;

    protected ArrayList<Wire> __proverWitnessWires;

    protected ArrayList<IAuxType> __inputAux;

    protected ArrayList<IAuxType> __proverAux;

    protected ArrayList<IAuxType> __verifiedProverAux;

    protected String __circuitName;

    protected HashMap<BigInteger, Wire> __knownConstantWires;

    private int __numOfConstraints;
    private int __phase;
    private int __stateCounter;
    private int __stateCounterPhase1;
    private int __conditionCounter;
    private ArrayList<SmartMemory<?>> __memoryList;
    private HashMap<Integer, VariableState> __varVariableStateTable;
    private ArrayList<Boolean> __conditionalStateList;
    private HashMap<Integer, SmartMemory.MemoryState> __memoryStateTable;
    private ArrayList<Class<? extends RuntimeStruct>> __rumtimeClassesList;
    private boolean __untrackedStateObjects = false;
    private CircuitEvaluator __circuitEvaluator;
    private Instruction __lastInstructionAdded;
    private CircuitOptimizer __circuitOptimizer;

    public CircuitGenerator(String circuitName) {
        super(circuitName);
        this.__circuitName = super.__circuitName;

        __instance = this;
        this.__inWires = super.__inWires;
        this.__outWires = super.__outWires;
        this.__proverWitnessWires = super.__proverWitnessWires;
        this.__evaluationQueue = super.__getEvaluationQueue();
        this.__nonOptimizedEvaluationQueue = this.__evaluationQueue;
        this.__knownConstantWires = super.__knownConstantWires;
        this.__currentWireId = 0;
        this.__numOfConstraints = 0;
        this.__inputAux = super.__inputAux;
        this.__proverAux = super.__proverAux;
        this.__verifiedProverAux = super.__verifiedProverAux;
        this.__varVariableStateTable = new HashMap<>();
        this.__conditionalStateList = new ArrayList<>();
        this.__memoryStateTable = new HashMap<>();
        this.__memoryList = new ArrayList<>();
        this.__rumtimeClassesList = new ArrayList<>();
        this.__stateCounter = 0;
        this.__conditionCounter = 0;
    }


    public static CircuitGenerator __getActiveCircuitGenerator() {
        return __instance;
    }


    protected void outsource() {
    }


    public void generateCircuit() {
        System.out.println("[1st Phase] Running Initial Circuit Analysis for < " + this.__circuitName + " >");
        __phase1();
        System.out.println("[2nd Phase] Running Circuit Generator for < " + this.__circuitName + " >");
        __phase2();


        if (Config.multivariateExpressionMinimization) {
            System.out.println("Initial Circuit Generation Done for < " + this.__circuitName + " >  \n \t Current total number of constraints :  " + __getNumOfConstraints() + "\n");
            System.out.println("Now: attempting to apply multivariate expression minimization (might take time/require memory depending on how large the circuit is)");
            if (!Config.arithOptimizerIncrementalMode) {
                System.out.println("** Note: If the size of memory is a bottleneck, e.g., the circuit size is very large, enabling Config.arithOptimizerIncrementalMode could help.");
            }
        } else {

            System.out.println("Circuit Generation Done for < " + this.__circuitName + " >  \n \t Total Number of Constraints :  " + __getNumOfConstraints() + "\n");
        }

        this.__nonOptimalWireCount = this.__currentWireId;
        if (Config.multivariateExpressionMinimization) {
            this.__evaluationQueue = __copyEvalSeq(this.__evaluationQueue);

            this.__circuitOptimizer = new CircuitOptimizer(this);
        }


        if (Config.writeCircuits) {
            __writeCircuitFile(Config.multivariateExpressionMinimization ? "_optimized" : "");
        }
    }


    private void __phase2() {
        __declareGenericConstants();
        __init();
        __defineInputs();
        __defineVerifiedWitnesses();
        __defineWitnesses();

        for (Class<? extends RuntimeStruct> c : this.__rumtimeClassesList) {
            try {
                Method m = c.getMethod("____reset", new Class[0]);
                m.invoke(null, (Object[]) new String[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        outsource();
        __checkWitnesses();

        __defineOutputs();
        for (SmartMemory<?> mem : this.__memoryList) {
            mem.finalize();
        }

        if (this.__stateCounter != this.__stateCounterPhase1) {
            System.err.println("Internal Inconsistency Detected! -- Inconsistent State Counters [" + this.__stateCounterPhase1 + "," +
                    this.__stateCounter + "]");
            throw new RuntimeException("Inconsistent state counters.");
        }
    }


    private void __checkWitnesses() {
        for (IAuxType t : this.__verifiedProverAux) {
            t.verifyRange();
        }
    }

    private void __phase1() {
        this.__phase = 0;
        __declareGenericConstants();
        __init();
        __defineInputs();
        __defineVerifiedWitnesses();
        __defineWitnesses();
        outsource();
        __checkWitnesses();
        __defineOutputs();

        for (SmartMemory<?> mem : this.__memoryList) {
            mem.analyzeWorkload();
        }


        this.__stateCounterPhase1 = this.__stateCounter;
        __clear();
        System.out.println("Phase 1: Analysis Completed!");

        this.__phase++;
    }


    private LinkedHashMap<Instruction, Instruction> __copyEvalSeq(LinkedHashMap<Instruction, Instruction> evaluationQueue) {
        LinkedHashMap<Instruction, Instruction> c = new LinkedHashMap<>();


        this.__oneWire = this.__oneWire.copy();

        Wire[] wireList = new Wire[__getCurrentWireId()];
        wireList[0] = this.__oneWire;
        for (Instruction i : evaluationQueue.keySet()) {

            Instruction copiedInstruction = i.copy(wireList);
            if (copiedInstruction != null) {

                c.put(copiedInstruction, copiedInstruction);
            }
        }


        this.__zeroWire = wireList[1];
        this.__knownConstantWires.clear();
        this.__knownConstantWires.put(BigInteger.ONE, this.__oneWire);
        this.__knownConstantWires.put(BigInteger.ZERO, this.__zeroWire);

        return c;
    }


    private void __clear() {
        this.__inWires.clear();
        this.__outWires.clear();
        this.__proverWitnessWires.clear();
        this.__evaluationQueue.clear();
        this.__nonOptimizedEvaluationQueue.clear();
        this.__knownConstantWires.clear();

        this.__inputAux.clear();
        this.__proverAux.clear();
        this.__verifiedProverAux.clear();
        this.__currentWireId = 0;
        this.__stateCounter = 0;
        this.__conditionCounter = 0;
        this.__numOfConstraints = 0;
        this.__memoryList.clear();
        SmartMemory.globalMemoryCounter = 0;
    }

    public String __getName() {
        return this.__circuitName;
    }


    public void __generateSampleInput(CircuitEvaluator evaluator) {
    }


    public Wire __createInputWire(String... desc) {
        Wire newInputWire = new VariableWire(this.__currentWireId++);
        __addToEvaluationQueue(new WireLabelInstruction(WireLabelInstruction.LabelType.input, newInputWire, desc));
        this.__inWires.add(newInputWire);
        return newInputWire;
    }

    public Wire[] __createInputWireArray(int n, String... desc) {
        Wire[] list = new Wire[n];
        for (int i = 0; i < n; i++) {
            if (desc.length == 0) {
                list[i] = __createInputWire(new String[]{""});
            } else {
                list[i] = __createInputWire(new String[]{String.valueOf(desc[0]) + " " + i});
            }
        }
        return list;
    }


    public Wire __createProverWitnessWire(String... desc) {
        Wire wire = new VariableWire(this.__currentWireId++);
        __addToEvaluationQueue(new WireLabelInstruction(WireLabelInstruction.LabelType.nizkinput, wire, desc));
        this.__proverWitnessWires.add(wire);
        return wire;
    }


    public Wire[] __createProverWitnessWireArray(int n, String... desc) {
        Wire[] ws = new Wire[n];
        for (int k = 0; k < n; k++) {
            if (desc.length == 0) {
                ws[k] = __createProverWitnessWire(new String[]{""});
            } else {
                ws[k] = __createProverWitnessWire(new String[]{String.valueOf(desc[0]) + " " + k});
            }
        }
        return ws;
    }

    public Wire[] __generateZeroWireArray(int n) {
        ConstantWire[] arrayOfConstantWire = new ConstantWire[n];
        Arrays.fill((Object[]) arrayOfConstantWire, this.__zeroWire);
        return (Wire[]) arrayOfConstantWire;
    }

    public Wire[] __generateOneWireArray(int n) {
        ConstantWire[] arrayOfConstantWire = new ConstantWire[n];
        Arrays.fill((Object[]) arrayOfConstantWire, this.__oneWire);
        return (Wire[]) arrayOfConstantWire;
    }

    public Wire __makeOutput(Wire wire, String... desc) {
        Wire outputWire = wire;
        if ((!(wire instanceof VariableWire) && !(wire instanceof VariableBitWire)) || this.__inWires.contains(wire)) {
            packIfNeeded(wire, desc);
            outputWire = __makeVariable(wire, desc);
        } else if (this.__inWires.contains(wire) || this.__proverWitnessWires.contains(wire)) {
            outputWire = __makeVariable(wire, desc);
        } else {
            packIfNeeded(wire, new String[0]);
        }

        this.__outWires.add(outputWire);
        __addToEvaluationQueue(new WireLabelInstruction(WireLabelInstruction.LabelType.output, outputWire, desc));
        return outputWire;
    }


    protected Wire __makeVariable(Wire wire, String... desc) {
        Wire outputWire = new VariableWire(this.__currentWireId++);
        Instruction op = new MulBasicOp(wire, this.__oneWire, outputWire, desc);
        Wire[] cachedOutputs = __addToEvaluationQueue(op);
        if (cachedOutputs == null) {
            return outputWire;
        }

        this.__currentWireId--;
        return cachedOutputs[0];
    }


    public Wire[] __makeOutputArray(Wire[] wires, String... desc) {
        Wire[] outs = new Wire[wires.length];
        for (int i = 0; i < wires.length; i++) {
            if (desc.length == 0) {
                outs[i] = __makeOutput(wires[i], new String[]{""});
            } else {
                outs[i] = __makeOutput(wires[i], new String[]{String.valueOf(desc[0]) + "[" + i + "]"});
            }
        }
        return outs;
    }

    public void __addDebugInstruction(Wire w, String... desc) {
        packIfNeeded(w, new String[0]);
        __addToEvaluationQueue(new WireLabelInstruction(WireLabelInstruction.LabelType.debug, w, desc));
    }


    public void __addDebugInstruction(IAuxType t, String... desc) {
        __addToEvaluationQueue(new WireLabelInstruction(WireLabelInstruction.LabelType.debug, t.copy(), desc));
    }

    public void __addDebugInstruction(PackedValue v, String... desc) {
        __addToEvaluationQueue(new WireLabelInstruction(WireLabelInstruction.LabelType.debug, v, desc));
    }


    public void __addDebugInstruction(Wire[] wires, String... desc) {
        for (int i = 0; i < wires.length; i++) {
            packIfNeeded(wires[i], new String[0]);

            __addToEvaluationQueue(
                    new WireLabelInstruction(WireLabelInstruction.LabelType.debug, wires[i], new String[]{(desc.length > 0) ? (String.valueOf(desc[0]) + " - " + i) : ""}));
        }
    }

    public void __writeCircuitFile(String arg) {
        try {
            PrintWriter printWriter = new PrintWriter(new BufferedWriter(new FileWriter(String.valueOf(Config.outputFilesPath) + (
                    Config.outputFilesPath.isEmpty() ? "" : File.separator) + __getName() + arg + ".arith")));

            printWriter.println("total " + this.__currentWireId);
            for (Instruction e : this.__evaluationQueue.keySet()) {
                if (e.doneWithinCircuit()) {
                    printWriter.print(e + "\n");
                }
            }
            printWriter.close();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void __printCircuit() {
        for (Instruction e : this.__evaluationQueue.keySet()) {
            if (e.doneWithinCircuit()) {
                System.out.println(e);
            }
        }
    }


    private void __declareGenericConstants() {
        this.__oneWire = new ConstantWire(this.__currentWireId++, BigInteger.ONE);
        this.__knownConstantWires.put(BigInteger.ONE, this.__oneWire);
        __addToEvaluationQueue(new WireLabelInstruction(WireLabelInstruction.LabelType.input, this.__oneWire, new String[]{"The one-input wire."}));
        this.__inWires.add(this.__oneWire);
        this.__zeroWire = this.__oneWire.mul(0L, new String[0]);
        super.__zeroWire = this.__zeroWire;
        super.__oneWire = this.__oneWire;
        super.__inWires = this.__inWires;
        super.__knownConstantWires = this.__knownConstantWires;
    }


    public void __init() {
    }


    public void evaluateSampleRun(SampleRun sampleRun) {
        if (!sampleRun.isEnabled()) {
            return;
        }
        System.out.println("Running Sample Run: " + sampleRun.getName());

        this.__knownConstantWires.clear();
        this.__knownConstantWires.put(BigInteger.ONE, this.__oneWire);
        this.__circuitEvaluator = new CircuitEvaluator(this.__nonOptimalWireCount);

        sampleRun.pre();

        System.out.println("Evaluating Input on the circuit " + (Config.multivariateExpressionMinimization ? "without multivariate optimizations attempts" : ""));
        this.__circuitEvaluator.evaluate(this.__nonOptimizedEvaluationQueue);
        sampleRun.post();


        System.out.println("Evaluation Done ");

        if (Config.multivariateExpressionMinimization) {
            System.out.println("Evaluating Input on the circuit after multivariate optimizations attempt");
            this.__knownConstantWires.clear();
            this.__knownConstantWires.put(BigInteger.ONE, this.__oneWire);
            this.__circuitEvaluator = this.__circuitOptimizer.mapFromOldEvaluationSeq(this.__circuitEvaluator);


            System.out.println("Evaluation Done");
            System.out.println("[Pass] Output values after multivariate optimizations match the previous output of the circuit.");
        }


        System.out.println("Sample Run: " + sampleRun.getName() + " finished!");

        if (Config.writeCircuits) {
            __prepInputFile(String.valueOf(sampleRun.getName()) + (Config.multivariateExpressionMinimization ? "_optimized" : ""));
        }
    }


    public Wire __createConstantWire(BigInteger x, String... desc) {
        return this.__oneWire.mul(x, desc);
    }

    public Wire[] __createConstantWireArray(BigInteger[] a, String... desc) {
        Wire[] w = new Wire[a.length];
        for (int i = 0; i < a.length; i++) {
            w[i] = __createConstantWire(a[i], desc);
        }
        return w;
    }

    public Wire __createConstantWire(long x, String... desc) {
        return this.__oneWire.mul(x, desc);
    }

    public Wire[] __createConstantWireArray(long[] a, String... desc) {
        Wire[] w = new Wire[a.length];
        for (int i = 0; i < a.length; i++) {
            w[i] = __createConstantWire(a[i], desc);
        }
        return w;
    }

    public Wire __createNegConstantWire(BigInteger x, String... desc) {
        return this.__oneWire.mul(x.negate(), desc);
    }

    public Wire __createNegConstantWire(long x, String... desc) {
        return this.__oneWire.mul(-x, desc);
    }


    public void __specifyProverWitnessComputation(Instruction instruction) {
        __addToEvaluationQueue(instruction);
    }

//    @Override
//    public Wire __getZeroWire() {
//        return this.__zeroWire;
//    }
//
//    @Override
//    public Wire __getOneWire() {
//        return this.__oneWire;
//    }

    public LinkedHashMap<Instruction, Instruction> __getEvaluationQueue() {
        return this.__evaluationQueue;
    }

    public int __getNumWires() {
        return this.__currentWireId;
    }


    public Wire[] __addToEvaluationQueue(Instruction e) {
        this.__lastInstructionAdded = e;
        if (this.__evaluationQueue.containsKey(e) &&
                e instanceof BasicOp) {
            return ((BasicOp) this.__evaluationQueue.get(e)).getOutputs();
        }

        if (e instanceof BasicOp) {

            this.__numOfConstraints += ((BasicOp) e).getNumMulGates();
            ((BasicOp) e).getNumMulGates();
        }


        this.__evaluationQueue.put(e, e);


        return null;
    }

    public void __printState(String message) {
        System.out.println("\nGenerator State @ " + message);
        System.out.println("\tCurrent Number of Multiplication Gates  :: " + this.__numOfConstraints + "\n");
    }

    public int __getNumOfConstraints() {
        return this.__numOfConstraints;
    }

    public ArrayList<Wire> __getInWires() {
        return this.__inWires;
    }

    public ArrayList<Wire> __getOutWires() {
        return this.__outWires;
    }

    public ArrayList<Wire> __getProverWitnessWires() {
        return this.__proverWitnessWires;
    }


    public void __addAssertion(Wire w1, Wire w2, Wire w3, String... desc) {
        if (w1 instanceof ConstantWire && w2 instanceof ConstantWire && w3 instanceof ConstantWire) {
            BigInteger const1 = ((ConstantWire) w1).getConstant();
            BigInteger const2 = ((ConstantWire) w2).getConstant();
            BigInteger const3 = ((ConstantWire) w3).getConstant();
            if (!const3.equals(const1.multiply(const2).mod(Config.getFiniteFieldModulus()))) {
                throw new RuntimeException("This assertion can never work on the provided constant wires .. ");
            }
        } else {
            packIfNeeded(w1, new String[0]);
            packIfNeeded(w2, new String[0]);
            packIfNeeded(w3, new String[0]);

            if (ConditionalScopeTracker.getCurrentScopeId() > 0) {
                Wire active = ConditionalScopeTracker.getAccumActiveBit().getWire();

                if (w1 instanceof ConstantWire) {
                    Instruction op = new AssertBasicOp(w1.mul(active, new String[0]), w2, w3.mul(active, new String[0]), desc);
                    __addToEvaluationQueue(op);
                } else {
                    Instruction op = new AssertBasicOp(w1, w2.mul(active, new String[0]), w3.mul(active, new String[0]), desc);
                    __addToEvaluationQueue(op);
                }

            } else {

                Instruction op = new AssertBasicOp(w1, w2, w3, desc);
                __addToEvaluationQueue(op);
            }
        }
    }

    public void __forceNativeConstraint(FieldElement a, FieldElement b, FieldElement c, String... desc) {
        if (!a.isNativeSnarkField() || !b.isNativeSnarkField() || !c.isNativeSnarkField()) {
            throw new IllegalArgumentException("Verifying native constraints works only on native field types.");
        }

        __addAssertion(a.getPackedWire().getArray()[0], b.getPackedWire().getArray()[0], c.getPackedWire().getArray()[0], desc);
    }


    public void __addZeroAssertion(Wire w, String... desc) {
        __addAssertion(w, this.__oneWire, this.__zeroWire, desc);
    }

    public void __addOneAssertion(Wire w, String... desc) {
        __addAssertion(w, this.__oneWire, this.__oneWire, desc);
    }

    public void __addBinaryAssertion(Wire w, String... desc) {
        Wire inv = w.invAsBit(desc);
        __addAssertion(w, inv, this.__zeroWire, desc);
    }

    public void __addEqualityAssertion(Wire w1, Wire w2, String... desc) {
        __addAssertion(w1, this.__oneWire, w2, desc);
    }

    public void __addEqualityAssertion(Wire w1, BigInteger b, String... desc) {
        __addAssertion(w1, this.__oneWire, __createConstantWire(b, desc), desc);
    }


    public void __evalCircuit() {
        this.__knownConstantWires.clear();
        this.__knownConstantWires.put(BigInteger.ONE, this.__oneWire);
        this.__circuitEvaluator = new CircuitEvaluator(this.__nonOptimalWireCount);
        __generateSampleInput(this.__circuitEvaluator);

        this.__circuitEvaluator.evaluate(this.__nonOptimizedEvaluationQueue);
        if (Config.multivariateExpressionMinimization) {
            this.__knownConstantWires.clear();

            this.__circuitEvaluator = this.__circuitOptimizer.mapFromOldEvaluationSeq(this.__circuitEvaluator);
        }
    }


    public void __prepFiles() {
        __writeCircuitFile("");
        if (this.__circuitEvaluator == null) {
            throw new NullPointerException("evalCircuit() must be called before prepFiles()");
        }
        this.__circuitEvaluator.writeInputFile("");
    }

    public void __prepFiles(String arg) {
        __writeCircuitFile(arg);
        if (this.__circuitEvaluator == null) {
            throw new NullPointerException("evalCircuit() must be called before prepFiles()");
        }
        this.__circuitEvaluator.writeInputFile(arg);
    }

    public void __prepInputFile(String arg) {
        if (this.__circuitEvaluator == null) {
            throw new NullPointerException("evalCircuit() must be called before prepFiles()");
        }
        this.__circuitEvaluator.writeInputFile(arg);
    }


    public CircuitEvaluator __getCircuitEvaluator() {
        if (this.__circuitEvaluator == null) {
            throw new NullPointerException("evalCircuit() must be called before getCircuitEvaluator()");
        }
        return this.__circuitEvaluator;
    }


    public void __defineInputs() {
    }


    public void __defineWitnesses() {
    }


    public void __defineVerifiedWitnesses() {
    }


    public void __defineOutputs() {
    }


    public int __getPhase() {
        return this.__phase;
    }

    public VariableState __retrieveVariableState() {
        VariableState variableState;
        if (this.__phase == 0) {
            variableState = new VariableState();
            if (!this.__untrackedStateObjects) {
                this.__varVariableStateTable.put(Integer.valueOf(this.__stateCounter), variableState);
                variableState.setId(this.__stateCounter);
                this.__stateCounter++;
            }

            return variableState;
        }

        if (!this.__untrackedStateObjects) {
            variableState = this.__varVariableStateTable.get(Integer.valueOf(this.__stateCounter));
            this.__stateCounter++;
        } else {
            variableState = new VariableState();
        }

        return variableState;
    }


    public boolean __checkConstantState(Bit b) {
        if (this.__phase == 0) {
            boolean isConstant = b.isConstant();
            this.__conditionalStateList.add(Boolean.valueOf(isConstant));
            this.__conditionCounter++;
            return isConstant;
        }
        boolean recalledDecision = ((Boolean) this.__conditionalStateList.get(this.__conditionCounter)).booleanValue();
        this.__conditionCounter++;
        return recalledDecision;
    }


    public void __setUntrackedStateObjects(boolean untrackedStateObjects) {
        this.__untrackedStateObjects = untrackedStateObjects;
    }


    public int __getStateCounter() {
        return this.__stateCounter;
    }


    public void __setEvaluationQueue(LinkedHashMap<Instruction, Instruction> evaluationQueue) {
        this.__knownConstantWires.clear();
        this.__knownConstantWires.put(BigInteger.ONE, this.__oneWire);
        this.__evaluationQueue = evaluationQueue;
    }

    public int __getCurrentWireId() {
        return this.__currentWireId;
    }

    public void __setCurrentWireId(int newWireCount) {
        this.__currentWireId = newWireCount;
    }

    public Instruction __getLastInstructionAdded() {
        return this.__lastInstructionAdded;
    }

    public ArrayList<IAuxType> __getInputAux() {
        return this.__inputAux;
    }

    public ArrayList<IAuxType> __getProverAux() {
        return this.__proverAux;
    }

    public ArrayList<IAuxType> __getProverVerifiedAux() {
        return this.__verifiedProverAux;
    }

    public void __generateRandomInput(CircuitEvaluator evaluator) {
        for (IAuxType t : this.__inputAux) {

            t.mapRandomValue(evaluator);
        }
    }


    public PackedValue __createConstantPackedValue(BigInteger constant, int bitWidth) {
        if (bitWidth <= UnsignedInteger.BITWIDTH_LIMIT_SHORT) {
            return new PackedValue(__createConstantWire(constant, new String[0]), constant);
        }

        constant = constant.mod((new BigInteger("2")).pow(bitWidth));

        int numChunks = (int) Math.ceil(constant.bitLength() * 1.0D / UnsignedInteger.BITWIDTH_PER_CHUNK);

        BigInteger[] chunks = Util.split(constant, numChunks, UnsignedInteger.BITWIDTH_PER_CHUNK);
        Wire[] array = new Wire[numChunks];
        for (int i = 0; i < numChunks; i++) {
            array[i] = __createConstantWire(chunks[i], new String[0]);
        }

        return new PackedValue(array, chunks);
    }


    public PackedValue __createConstantPackedValue(BigInteger constant, BigInteger modulus) {
        if (modulus.bitLength() <= UnsignedInteger.BITWIDTH_LIMIT_SHORT || modulus.equals(Config.getFiniteFieldModulus())) {
            return new PackedValue(__createConstantWire(constant, new String[0]), constant);
        }

        constant = constant.mod(modulus);

        int numChunks = (int) Math.ceil(constant.bitLength() * 1.0D / UnsignedInteger.BITWIDTH_PER_CHUNK);

        BigInteger[] chunks = Util.split(constant, numChunks, UnsignedInteger.BITWIDTH_PER_CHUNK);
        Wire[] array = new Wire[numChunks];
        for (int i = 0; i < numChunks; i++) {
            array[i] = __createConstantWire(chunks[i], new String[0]);
        }


        return new PackedValue(array, chunks);
    }


    public ArrayList<SmartMemory<?>> __getMemoryList() {
        return this.__memoryList;
    }

    public HashMap<Integer, SmartMemory.MemoryState> __getMemoryStateTable() {
        return this.__memoryStateTable;
    }

    public ArrayList<Class<? extends RuntimeStruct>> __getRumtimeClassesList() {
        return this.__rumtimeClassesList;
    }

    public ArrayList<IAuxType> __getVerifiedProverAux() {
        return this.__verifiedProverAux;
    }

    @SneakyThrows
    private void packIfNeeded(Wire wire, String... desc) {
        if (wire.getWireId() == -1) {
            Method m = wire.getClass().getDeclaredMethod("packIfNeeded", String[].class);
            m.setAccessible(true);
            m.invoke(wire, desc);
        }
    }
}
