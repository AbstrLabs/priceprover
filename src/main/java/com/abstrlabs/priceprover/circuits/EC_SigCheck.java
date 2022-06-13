package com.abstrlabs.priceprover.circuits;

/*Generated by MPS */

import com.abstrlabs.priceprover.util.CircuitGenerator;
import backend.config.Config;
import backend.eval.SampleRun;
import java.math.BigInteger;
import backend.auxTypes.FieldElement;
import util.Util;
import backend.auxTypes.PermutationVerifier;
import backend.eval.CircuitEvaluator;
import backend.auxTypes.UnsignedInteger;
import backend.auxTypes.Bit;
import backend.auxTypes.ConditionalScopeTracker;



public class EC_SigCheck extends CircuitGenerator {



  public static void main(String[] args) {
    // This is the java main method. Its purpose is to make the Progam runnable in the environment 
    // This method can be left empty, or used to set Configuration params (see examples) 
    Config.hexOutputEnabled = true;

    // In the above example with a 256-bit exponent, we don't need to check against a 0/0 case. 
    // Even if sk was set to be the order, we would get a non-zero divided by zero case in the last iteration, 
    //  which is unsatisfiable.  
    // The checks are a bit costly for non-native fields when they are out of range, as we have to check if  
    //  the modulus divides the denominator, so we disable the checks here.  
    Config.enforceInternalDivisionNonZeroChecks = false;

    Config.writeCircuits = true;
    Config.outputFilesPath = "/home/bo/";

    new EC_SigCheck();
  }

  public EC_SigCheck() {
    super("EC_SigCheck");
    generateCircuit();
    this.evaluateSampleRun(new SampleRun("Sample_Run1", true) {
      public void pre() {
        pk_x.mapValue(new BigInteger("029de200b0a1b23f253a412963517905e08c73277adbcdb07837ec35ff253188", 16), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
        pk_y.mapValue(new BigInteger("bbc4455556ad16fa2b36edc0a71d901cd2b7ac6474dd5f82a84ab1607a184f70", 16), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
        r.mapValue(new BigInteger("a254572cc47c2981d46d786944fb47fbbec821b748f1ecda13313793edae22c7", 16), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
        s.mapValue(new BigInteger("1cef4f59fec5f087c7e0f42255c1f9864f9b16ba2714a10f30672d2850e82c4a", 16), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
        h.mapValue(new BigInteger("68b8138094e7362e77580580053ca8654493b7c9c9a08ffaf79f353b17f12480", 16), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
      }
      public void post() {
        //  No outputs to print. No failed assertions are expected 
        System.out.println("If no failed assertions appear, everything looks to be OK for this circuit.");
      }

    });

  }



  public void __init() {
    pk_x = new FieldElement(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), new BigInteger("0"));
    pk_y = new FieldElement(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), new BigInteger("0"));
    r = new FieldElement(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), new BigInteger("0"));
    s = new FieldElement(new BigInteger("115792089210356248762697446949407573529996955224135760342422259061068512044369"), new BigInteger("0"));
    h = new FieldElement(new BigInteger("115792089210356248762697446949407573529996955224135760342422259061068512044369"), new BigInteger("0"));
  }

  public FieldElement pk_x;
  public FieldElement pk_y;
  public FieldElement r;
  public FieldElement s;
  public FieldElement h;

  @Override
  public void __defineInputs() {
    super.__defineInputs();
    pk_x = FieldElement.createInput(this, new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"));
    pk_y = FieldElement.createInput(this, new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"));
    r = FieldElement.createInput(this, new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"));
    s = FieldElement.createInput(this, new BigInteger("115792089210356248762697446949407573529996955224135760342422259061068512044369"));
    h = FieldElement.createInput(this, new BigInteger("115792089210356248762697446949407573529996955224135760342422259061068512044369"));















  }
  @Override
  public void __defineOutputs() {
    super.__defineOutputs();









  }
  @Override
  public void __defineWitnesses() {
    super.__defineWitnesses();

















  }
  public void outsource() {
    // EC generator point 
    CircuitGenerator.__getActiveCircuitGenerator().__addDebugInstruction(pk_x, "pk_x");
    CircuitGenerator.__getActiveCircuitGenerator().__addDebugInstruction(pk_y, "pk_y");
    CircuitGenerator.__getActiveCircuitGenerator().__addDebugInstruction(r, "r");
    CircuitGenerator.__getActiveCircuitGenerator().__addDebugInstruction(s, "s");
    CircuitGenerator.__getActiveCircuitGenerator().__addDebugInstruction(h, "h");
    FieldElement base_x = FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), "48439561293906451759052585252797914202762949526041747995844080717082404635286").copy();
    FieldElement base_y = FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), "36134250956749795798585127919587881956611106672985015071877198253568414405109").copy();

    FieldElement invS = FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573529996955224135760342422259061068512044369"), s).inv().copy();

    FieldElement u1 = FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573529996955224135760342422259061068512044369"), h).mul(invS).copy();
    FieldElement u2 = FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573529996955224135760342422259061068512044369"), r).mul(invS).copy();

    FieldElement[][] table = (FieldElement[][]) FieldElement.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{256, 2}, new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"));
    table[0][0].assign(base_x);
    table[0][1].assign(base_y);

    // The next loop is for preprocessing, and will be operating on constants in the circuit 
    // This won't add any constraints, but might take time initially as BigInteger operations are expensive due to  
    // constants being heavily splitted into multiple wires in the circuit. 
    // This can be replaced by independent faster native java code computing constants, and just doing assignments  
    // in the end but this was left for testing purposes. 
    System.out.println("Precomputing constants (might take time in this version -- see note in the code).. ");
    for (int i = 1; i < 256; i++) {
      table[i] = doublePoint(table[i - 1][0].copy(), table[i - 1][1].copy());
    }

    FieldElement[][] table2 = (FieldElement[][]) FieldElement.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{256, 2}, new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"));
    table2[0][0].assign(pk_x);
    table2[0][1].assign(pk_y);
    for (int i = 1; i < 256; i++) {
      FieldElement[] t1 = doublePoint(table2[i - 1][0].copy(), table2[i - 1][1].copy());
      // if F_p256 multiply is on witness instead of constant, it doesn't modulo pl (not sure if it's a feature or bug) 
      //  therefore we make it modulo p by convert it to uint_256 and to F_p256. Code below use this hack too 
      table2[i][0].assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), t1[0]));
      table2[i][1].assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), t1[1]));
    }

    // boolean to keep track when it's safe to apply the addition formula of affine points 
    Bit init1 = Bit.instantiateFrom(false).copy();
    Bit init2 = Bit.instantiateFrom(false).copy();

    FieldElement p1_x = new FieldElement(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), new BigInteger("0"));
    FieldElement p1_y = new FieldElement(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), new BigInteger("0"));
    FieldElement p2_x = new FieldElement(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), new BigInteger("0"));
    FieldElement p2_y = new FieldElement(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), new BigInteger("0"));

    for (int i = 0; i < 256; i++) {
      {
        Bit bit_a0ob0u = u1.getBitElements()[i].copy();
        boolean c_a0ob0u = CircuitGenerator.__getActiveCircuitGenerator().__checkConstantState(bit_a0ob0u);
        if (c_a0ob0u) {
          if (bit_a0ob0u.getConstantValue()) {
            {
              Bit bit_a0a0a2a0a04a02 = init1.copy();
              boolean c_a0a0a2a0a04a02 = CircuitGenerator.__getActiveCircuitGenerator().__checkConstantState(bit_a0a0a2a0a04a02);
              if (c_a0a0a2a0a04a02) {
                if (bit_a0a0a2a0a04a02.getConstantValue()) {
                  FieldElement[] rr = addPoints(p1_x.copy(), p1_y.copy(), table[i][0].copy(), table[i][1].copy());
                  p1_x.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), rr[0]));
                  p1_y.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), rr[1]));
                } else {
                  init1.assign(Bit.instantiateFrom(true));
                  p1_x.assign(table[i][0]);
                  p1_y.assign(table[i][1]);

                }
              } else {
                ConditionalScopeTracker.pushMain();
                ConditionalScopeTracker.push(bit_a0a0a2a0a04a02);
                FieldElement[] rr = addPoints(p1_x.copy(), p1_y.copy(), table[i][0].copy(), table[i][1].copy());
                p1_x.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), rr[0]));
                p1_y.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), rr[1]));

                ConditionalScopeTracker.pop();

                ConditionalScopeTracker.push(new Bit(true));

                init1.assign(Bit.instantiateFrom(true));
                p1_x.assign(table[i][0]);
                p1_y.assign(table[i][1]);
                ConditionalScopeTracker.pop();
                ConditionalScopeTracker.popMain();
              }

            }
          } else {

          }
        } else {
          ConditionalScopeTracker.pushMain();
          ConditionalScopeTracker.push(bit_a0ob0u);
          {
            Bit bit_a0a0ob0u = init1.copy();
            boolean c_a0a0ob0u = CircuitGenerator.__getActiveCircuitGenerator().__checkConstantState(bit_a0a0ob0u);
            if (c_a0a0ob0u) {
              if (bit_a0a0ob0u.getConstantValue()) {
                FieldElement[] rr = addPoints(p1_x.copy(), p1_y.copy(), table[i][0].copy(), table[i][1].copy());
                p1_x.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), rr[0]));
                p1_y.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), rr[1]));
              } else {
                init1.assign(Bit.instantiateFrom(true));
                p1_x.assign(table[i][0]);
                p1_y.assign(table[i][1]);

              }
            } else {
              ConditionalScopeTracker.pushMain();
              ConditionalScopeTracker.push(bit_a0a0ob0u);
              FieldElement[] rr = addPoints(p1_x.copy(), p1_y.copy(), table[i][0].copy(), table[i][1].copy());
              p1_x.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), rr[0]));
              p1_y.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), rr[1]));

              ConditionalScopeTracker.pop();

              ConditionalScopeTracker.push(new Bit(true));

              init1.assign(Bit.instantiateFrom(true));
              p1_x.assign(table[i][0]);
              p1_y.assign(table[i][1]);
              ConditionalScopeTracker.pop();
              ConditionalScopeTracker.popMain();
            }

          }

          ConditionalScopeTracker.pop();

          ConditionalScopeTracker.push(new Bit(true));

          ConditionalScopeTracker.pop();
          ConditionalScopeTracker.popMain();
        }

      }

      {
        Bit bit_c0ob0u = u2.getBitElements()[i].copy();
        boolean c_c0ob0u = CircuitGenerator.__getActiveCircuitGenerator().__checkConstantState(bit_c0ob0u);
        if (c_c0ob0u) {
          if (bit_c0ob0u.getConstantValue()) {
            {
              Bit bit_a0a0a2a2a04a02 = init2.copy();
              boolean c_a0a0a2a2a04a02 = CircuitGenerator.__getActiveCircuitGenerator().__checkConstantState(bit_a0a0a2a2a04a02);
              if (c_a0a0a2a2a04a02) {
                if (bit_a0a0a2a2a04a02.getConstantValue()) {
                  FieldElement[] rr = addPoints(p2_x.copy(), p2_y.copy(), table2[i][0].copy(), table2[i][1].copy());
                  p2_x.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), rr[0]));
                  p2_y.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), rr[1]));
                } else {
                  init2.assign(Bit.instantiateFrom(true));
                  p2_x.assign(table2[i][0]);
                  p2_y.assign(table2[i][1]);

                }
              } else {
                ConditionalScopeTracker.pushMain();
                ConditionalScopeTracker.push(bit_a0a0a2a2a04a02);
                FieldElement[] rr = addPoints(p2_x.copy(), p2_y.copy(), table2[i][0].copy(), table2[i][1].copy());
                p2_x.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), rr[0]));
                p2_y.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), rr[1]));

                ConditionalScopeTracker.pop();

                ConditionalScopeTracker.push(new Bit(true));

                init2.assign(Bit.instantiateFrom(true));
                p2_x.assign(table2[i][0]);
                p2_y.assign(table2[i][1]);
                ConditionalScopeTracker.pop();
                ConditionalScopeTracker.popMain();
              }

            }
          } else {

          }
        } else {
          ConditionalScopeTracker.pushMain();
          ConditionalScopeTracker.push(bit_c0ob0u);
          {
            Bit bit_a0c0ob0u = init2.copy();
            boolean c_a0c0ob0u = CircuitGenerator.__getActiveCircuitGenerator().__checkConstantState(bit_a0c0ob0u);
            if (c_a0c0ob0u) {
              if (bit_a0c0ob0u.getConstantValue()) {
                FieldElement[] rr = addPoints(p2_x.copy(), p2_y.copy(), table2[i][0].copy(), table2[i][1].copy());
                p2_x.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), rr[0]));
                p2_y.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), rr[1]));
              } else {
                init2.assign(Bit.instantiateFrom(true));
                p2_x.assign(table2[i][0]);
                p2_y.assign(table2[i][1]);

              }
            } else {
              ConditionalScopeTracker.pushMain();
              ConditionalScopeTracker.push(bit_a0c0ob0u);
              FieldElement[] rr = addPoints(p2_x.copy(), p2_y.copy(), table2[i][0].copy(), table2[i][1].copy());
              p2_x.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), rr[0]));
              p2_y.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), rr[1]));

              ConditionalScopeTracker.pop();

              ConditionalScopeTracker.push(new Bit(true));

              init2.assign(Bit.instantiateFrom(true));
              p2_x.assign(table2[i][0]);
              p2_y.assign(table2[i][1]);
              ConditionalScopeTracker.pop();
              ConditionalScopeTracker.popMain();
            }

          }

          ConditionalScopeTracker.pop();

          ConditionalScopeTracker.push(new Bit(true));

          ConditionalScopeTracker.pop();
          ConditionalScopeTracker.popMain();
        }

      }
    }

    FieldElement[] p = addPoints(p1_x.copy(), p1_y.copy(), p2_x.copy(), p2_y.copy());
    FieldElement p0 = FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), p[0]).copy();

    System.out.println("done");
    CircuitGenerator.__getActiveCircuitGenerator().__addDebugInstruction(p0, "p0");
    p0.forceEqual(r);
  }
  private FieldElement[] addPoints(FieldElement x1, FieldElement y1, FieldElement x2, FieldElement y2) {
    FieldElement lambda = (y2.subtract(y1)).div((x2.subtract(x1))).copy();
    FieldElement xr = lambda.mul(lambda).subtract(x1).subtract(x2).copy();
    FieldElement yr = lambda.mul((x1.subtract(xr))).subtract(y1).copy();
    return new FieldElement[]{xr.copy(), yr.copy()};
  }
  private FieldElement[] doublePoint(FieldElement x1, FieldElement y1) {
    FieldElement lambda = (FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), 3).mul(x1).mul(x1).add(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), -3))).div((FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), 2).mul(y1))).copy();
    FieldElement xr = lambda.mul(lambda).subtract(x1.mul(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), 2))).copy();
    FieldElement yr = lambda.mul((x1.subtract(xr))).subtract(y1).copy();
    return new FieldElement[]{xr.copy(), yr.copy()};
  }

  public void __generateSampleInput(CircuitEvaluator evaluator) {
    __generateRandomInput(evaluator);
  }

}
