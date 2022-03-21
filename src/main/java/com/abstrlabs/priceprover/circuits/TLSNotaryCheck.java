package com.abstrlabs.priceprover.circuits;

import backend.config.Config;
import backend.eval.SampleRun;

import java.math.BigInteger;

import backend.auxTypes.UnsignedInteger;
import backend.auxTypes.Bit;
import backend.auxTypes.SmartMemory;
import backend.auxTypes.ConditionalScopeTracker;
import backend.auxTypes.FieldElement;
import com.abstrlabs.priceprover.Configs;
import com.abstrlabs.priceprover.NotaryCheckInput;
import com.abstrlabs.priceprover.util.CircuitGenerator;
import lombok.extern.log4j.Log4j2;
import util.Util;
import backend.eval.CircuitEvaluator;

@Log4j2
public class TLSNotaryCheck extends CircuitGenerator {

    public TLSNotaryCheck(NotaryCheckInput notaryCheckInput) {
        super("TLSNotaryCheck");
        Config.enforceInternalDivisionNonZeroChecks = false;
        if (Configs.writeCircuits) {
            generateCircuit();
        } else {
            readCircuitFile();
        }
        this.evaluateSampleRun(new SampleRun("Sample_Run1", true) {
            public void pre() {
                long[] sr_padded = notaryCheckInput.getSr_padded();
                long[] ccwk = notaryCheckInput.getCcwk();
                long[] cciv = notaryCheckInput.getCciv();
                long[] cswk = notaryCheckInput.getCswk();
                long[] csiv = notaryCheckInput.getCsiv();
                long[] cpms = notaryCheckInput.getCpms();
                long[] crc = notaryCheckInput.getCrc();
                int crc_len = crc.length;
                long[] ncwk = notaryCheckInput.getNcwk();
                long[] nciv = notaryCheckInput.getNciv();
                long[] nswk = notaryCheckInput.getNswk();
                long[] nsiv = notaryCheckInput.getNsiv();
                long[] npms = notaryCheckInput.getNpms();
                long[] nt = notaryCheckInput.getNt();
                long[] epk = notaryCheckInput.getEpk();
                long[] epkvf = notaryCheckInput.getEpkvf();
                long[] epkvu = notaryCheckInput.getEpkvu();
                long[] np = notaryCheckInput.getNp();
                long[] ss = notaryCheckInput.getSs();
                long[] esbn = notaryCheckInput.getEsbn();
                long[] tbs1_padded = notaryCheckInput.getTbs1_padded();
                long[] exp_ct = notaryCheckInput.getExp_ct();
                long[] tcp = notaryCheckInput.getTcp();
                long[] exp_hash = notaryCheckInput.getExp_hash();
                int cts = notaryCheckInput.getCts();

                server_records_padded_u32_blocks.mapValue(BigInteger.valueOf(sr_padded.length / 64), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                for (int i = 0; i < sr_padded.length; i++) {
                    server_records[i].mapValue(BigInteger.valueOf(sr_padded[i]), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                }
                for (int i = sr_padded.length; i < server_records.length; i++) {
                    server_records[i].mapValue(BigInteger.valueOf(0), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                }

                for (int i = 0; i < 16; i++) {
                    client_cwk_share[i].mapValue(BigInteger.valueOf(ccwk[i]), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                }
                for (int i = 0; i < 4; i++) {
                    client_civ_share[i].mapValue(BigInteger.valueOf(cciv[i]), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                }
                for (int i = 0; i < 16; i++) {
                    client_swk_share[i].mapValue(BigInteger.valueOf(cswk[i]), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                }
                for (int i = 0; i < 4; i++) {
                    client_siv_share[i].mapValue(BigInteger.valueOf(csiv[i]), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                }
                for (int i = 0; i < 32; i++) {
                    client_pms_share[i].mapValue(BigInteger.valueOf(cpms[i]), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                }
                client_req_cipher_len.mapValue(BigInteger.valueOf(crc_len), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                for (int i = 0; i < crc_len; i++) {
                    client_req_cipher[i].mapValue(BigInteger.valueOf(crc[i]), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                }
                for (int i = crc_len; i < client_req_cipher.length; i++) {
                    client_req_cipher[i].mapValue(BigInteger.valueOf(0), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                }
                for (int i = 0; i < 16; i++) {
                    notary_cwk_share[i].mapValue(BigInteger.valueOf(ncwk[i]), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                }
                for (int i = 0; i < 4; i++) {
                    notary_civ_share[i].mapValue(BigInteger.valueOf(nciv[i]), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                }
                for (int i = 0; i < 16; i++) {
                    notary_swk_share[i].mapValue(BigInteger.valueOf(nswk[i]), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                }
                for (int i = 0; i < 4; i++) {
                    notary_siv_share[i].mapValue(BigInteger.valueOf(nsiv[i]), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                }
                for (int i = 0; i < 32; i++) {
                    notary_pms_share[i].mapValue(BigInteger.valueOf(npms[i]), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                }
                for (int i = 0; i < 8; i++) {
                    notary_time[i].mapValue(BigInteger.valueOf(nt[i]), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                }
                for (int i = 0; i < 65; i++) {
                    emphemeral_pubkey[i].mapValue(BigInteger.valueOf(epk[i]), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                }
                for (int i = 0; i < 4; i++) {
                    emphemeral_pubkey_valid_from[i].mapValue(BigInteger.valueOf(epkvf[i]), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                }
                for (int i = 0; i < 4; i++) {
                    emphemeral_pubkey_valid_until[i].mapValue(BigInteger.valueOf(epkvu[i]), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                }
                for (int i = 0; i < 65; i++) {
                    notary_pubkey[i].mapValue(BigInteger.valueOf(np[i]), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                }
                for (int i = 0; i < 64; i++) {
                    session_sig[i].mapValue(BigInteger.valueOf(ss[i]), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                }
                for (int i = 0; i < 64; i++) {
                    emphemeral_signed_by_notary[i].mapValue(BigInteger.valueOf(esbn[i]), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                }

                for (int i = 0; i < tbs1_padded.length; i++) {
                    tbs1[i].mapValue(BigInteger.valueOf(tbs1_padded[i]), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                }
                for (int i = tbs1_padded.length; i < tbs1.length; i++) {
                    tbs1[i].mapValue(BigInteger.valueOf(0), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                }
                tbs1_blocks.mapValue(BigInteger.valueOf(tbs1_padded.length / 64), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                content_len.mapValue(BigInteger.valueOf(exp_ct.length), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                for (int i = 0; i < exp_ct.length; i++) {
                    expected_content[i].mapValue(BigInteger.valueOf(exp_ct[i]), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                }
                for (int i = exp_ct.length; i < expected_content.length; i++) {
                    expected_content[i].mapValue(BigInteger.valueOf(0), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                }
                content_start.mapValue(BigInteger.valueOf(cts), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                for (int i = 0; i < time_content_hash.length; i++) {
                    time_content_hash[i].mapValue(BigInteger.valueOf(exp_hash[i]), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                }
                for (int i = 0; i < tcp.length; i++) {
                    time_content_padded[i].mapValue(BigInteger.valueOf(tcp[i]), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());
                }
                time_content_padded_u32_blocks.mapValue(BigInteger.valueOf(time_content_padded.length / 64), CircuitGenerator.__getActiveCircuitGenerator().__getCircuitEvaluator());

            }
            public void post() {
            }

        });
    }

    public void __init() {
        key = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{16}, 8);
        server_records = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{1024}, 8);
        server_records_padded_u32_blocks = new UnsignedInteger(32, new BigInteger("0"));
        client_cwk_share = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{16}, 8);
        client_civ_share = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{4}, 8);
        client_swk_share = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{16}, 8);
        client_siv_share = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{4}, 8);
        client_pms_share = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{32}, 8);
        client_req_cipher = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{200}, 8);
        client_req_cipher_len = new UnsignedInteger(32, new BigInteger("0"));
        notary_cwk_share = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{16}, 8);
        notary_civ_share = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{4}, 8);
        notary_swk_share = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{16}, 8);
        notary_siv_share = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{4}, 8);
        notary_pms_share = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{32}, 8);
        notary_time = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{8}, 8);
        emphemeral_pubkey = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{65}, 8);
        emphemeral_pubkey_valid_from = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{4}, 8);
        emphemeral_pubkey_valid_until = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{4}, 8);
        notary_pubkey = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{65}, 8);
        session_sig = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{64}, 8);
        emphemeral_signed_by_notary = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{64}, 8);
        tbs1 = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{512}, 8);
        tbs1_blocks = new UnsignedInteger(32, new BigInteger("0"));
        expected_content = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{431}, 8);
        time_content_padded = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{448}, 8);
        time_content_padded_u32_blocks = new UnsignedInteger(32, new BigInteger("0"));
        content_len = new UnsignedInteger(32, new BigInteger("0"));
        content_start = new UnsignedInteger(32, new BigInteger("0"));
        time_content_hash = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{8}, 32);
        server_pk_in_tbs1 = Bit.instantiateFrom(false);
    }

    private SmartMemory<UnsignedInteger> sBoxMem;
    private UnsignedInteger[] key;
    private UnsignedInteger[] expandedKey;
    private SmartMemory<UnsignedInteger> decryptedMem;
    private SmartMemory<UnsignedInteger> tbs1Mem;
    private SmartMemory<UnsignedInteger> paddedMem;
    public UnsignedInteger[] server_records;
    public UnsignedInteger server_records_padded_u32_blocks;
    public UnsignedInteger[] client_cwk_share;
    public UnsignedInteger[] client_civ_share;
    public UnsignedInteger[] client_swk_share;
    public UnsignedInteger[] client_siv_share;
    public UnsignedInteger[] client_pms_share;
    public UnsignedInteger[] client_req_cipher;
    public UnsignedInteger client_req_cipher_len;
    public UnsignedInteger[] notary_cwk_share;
    public UnsignedInteger[] notary_civ_share;
    public UnsignedInteger[] notary_swk_share;
    public UnsignedInteger[] notary_siv_share;
    public UnsignedInteger[] notary_pms_share;
    public UnsignedInteger[] notary_time;
    public UnsignedInteger[] emphemeral_pubkey;
    public UnsignedInteger[] emphemeral_pubkey_valid_from;
    public UnsignedInteger[] emphemeral_pubkey_valid_until;
    public UnsignedInteger[] notary_pubkey;
    public UnsignedInteger[] session_sig;
    public UnsignedInteger[] emphemeral_signed_by_notary;
    public UnsignedInteger[] tbs1;
    public UnsignedInteger tbs1_blocks;
    public UnsignedInteger[] expected_content;
    public UnsignedInteger[] time_content_padded;
    public UnsignedInteger time_content_padded_u32_blocks;
    public UnsignedInteger content_len;
    public UnsignedInteger content_start;
    public UnsignedInteger[] time_content_hash;
    private Bit server_pk_in_tbs1;

    public static final long[] K_CONST = {1116352408L, 1899447441L, 3049323471L, 3921009573L, 961987163L, 1508970993L, 2453635748L, 2870763221L, 3624381080L, 310598401L, 607225278L, 1426881987L, 1925078388L, 2162078206L, 2614888103L, 3248222580L, 3835390401L, 4022224774L, 264347078L, 604807628L, 770255983L, 1249150122L, 1555081692L, 1996064986L, 2554220882L, 2821834349L, 2952996808L, 3210313671L, 3336571891L, 3584528711L, 113926993L, 338241895L, 666307205L, 773529912L, 1294757372L, 1396182291L, 1695183700L, 1986661051L, 2177026350L, 2456956037L, 2730485921L, 2820302411L, 3259730800L, 3345764771L, 3516065817L, 3600352804L, 4094571909L, 275423344L, 430227734L, 506948616L, 659060556L, 883997877L, 958139571L, 1322822218L, 1537002063L, 1747873779L, 1955562222L, 2024104815L, 2227730452L, 2361852424L, 2428436474L, 2756734187L, 3204031479L, 3329325298L};
    public static final long[] H_CONST = {1779033703L, 3144134277L, 1013904242L, 2773480762L, 1359893119L, 2600822924L, 528734635L, 1541459225L};

    public static UnsignedInteger rotateRight(UnsignedInteger in, int r) {
        return (in.shiftRight(r)).orBitwise((in.shiftLeft((32 - r))));
    }

    public UnsignedInteger[] sha256(UnsignedInteger[] preimage) {
        // In this version,  uint_32 can also be used as a conversion function for both single vars and arrays
        // this is to make things easier when converting values from java native types
        UnsignedInteger[] H = UnsignedInteger.instantiateFrom(32, H_CONST);
        for (int i = 0; i < preimage.length / 16; i++) {
            UnsignedInteger[] words = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{64}, 32);
            UnsignedInteger a = H[0].copy(32);
            UnsignedInteger b = H[1].copy(32);
            UnsignedInteger c = H[2].copy(32);
            UnsignedInteger d = H[3].copy(32);
            UnsignedInteger e = H[4].copy(32);
            UnsignedInteger f = H[5].copy(32);
            UnsignedInteger g = H[6].copy(32);
            UnsignedInteger h = H[7].copy(32);

            for (int j = 0; j < 16; j++) {
                words[j].assign(preimage[j + i * 16], 32);
            }

            for (int j = 16; j < 64; j++) {
                UnsignedInteger s0 = rotateRight(words[j - 15].copy(32), 7).xorBitwise(rotateRight(words[j - 15].copy(32), 18)).xorBitwise((words[j - 15].shiftRight(3))).copy(32);
                UnsignedInteger s1 = rotateRight(words[j - 2].copy(32), 17).xorBitwise(rotateRight(words[j - 2].copy(32), 19)).xorBitwise((words[j - 2].shiftRight(10))).copy(32);
                words[j].assign(words[j - 16].add(s0).add(words[j - 7]).add(s1), 32);
            }

            for (int j = 0; j < 64; j++) {
                UnsignedInteger s0 = rotateRight(a.copy(32), 2).xorBitwise(rotateRight(a.copy(32), 13)).xorBitwise(rotateRight(a.copy(32), 22)).copy(32);
                UnsignedInteger maj = (a.andBitwise(b)).xorBitwise((a.andBitwise(c))).xorBitwise((b.andBitwise(c))).copy(32);
                UnsignedInteger t2 = s0.add(maj).copy(32);

                UnsignedInteger s1 = rotateRight(e.copy(32), 6).xorBitwise(rotateRight(e.copy(32), 11)).xorBitwise(rotateRight(e.copy(32), 25)).copy(32);
                UnsignedInteger ch = e.andBitwise(f).xorBitwise(e.invBits().andBitwise(g)).copy(32);
                // the uint_32(.) call is to convert from java type to xjsnark type
                UnsignedInteger t1 = h.add(s1).add(ch).add(UnsignedInteger.instantiateFrom(32, K_CONST[j])).add(words[j]).copy(32);

                h.assign(g, 32);
                g.assign(f, 32);
                f.assign(e, 32);
                e.assign(d.add(t1), 32);
                d.assign(c, 32);
                c.assign(b, 32);
                b.assign(a, 32);
                a.assign(t1.add(t2), 32);
            }

            H[0].assign(H[0].add(a), 32);
            H[1].assign(H[1].add(b), 32);
            H[2].assign(H[2].add(c), 32);
            H[3].assign(H[3].add(d), 32);
            H[4].assign(H[4].add(e), 32);
            H[5].assign(H[5].add(f), 32);
            H[6].assign(H[6].add(g), 32);
            H[7].assign(H[7].add(h), 32);
        }
        return H;
    }

    public UnsignedInteger[] sha256_len(UnsignedInteger[] preimage, UnsignedInteger len, int bound) {
        UnsignedInteger[] H = UnsignedInteger.instantiateFrom(32, H_CONST);

        int i = 0;
        {
            int loop_counter_d0g = 0;
            while (loop_counter_d0g < bound / 16 / 4) {
                {
                    Bit bit_a0b0d0g = UnsignedInteger.instantiateFrom(32, i).isLessThan(len).copy();
                    boolean c_a0b0d0g = CircuitGenerator.__getActiveCircuitGenerator().__checkConstantState(bit_a0b0d0g);
                    if (c_a0b0d0g) {
                        if (bit_a0b0d0g.getConstantValue()) {
                            UnsignedInteger[] words = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{64}, 32);
                            UnsignedInteger a = H[0].copy(32);
                            UnsignedInteger b = H[1].copy(32);
                            UnsignedInteger c = H[2].copy(32);
                            UnsignedInteger d = H[3].copy(32);
                            UnsignedInteger e = H[4].copy(32);
                            UnsignedInteger f = H[5].copy(32);
                            UnsignedInteger g = H[6].copy(32);
                            UnsignedInteger h = H[7].copy(32);

                            for (int j = 0; j < 16; j++) {
                                words[j].assign(preimage[j + i * 16], 32);
                            }

                            for (int j = 16; j < 64; j++) {
                                UnsignedInteger s0 = rotateRight(words[j - 15].copy(32), 7).xorBitwise(rotateRight(words[j - 15].copy(32), 18)).xorBitwise((words[j - 15].shiftRight(3))).copy(32);
                                UnsignedInteger s1 = rotateRight(words[j - 2].copy(32), 17).xorBitwise(rotateRight(words[j - 2].copy(32), 19)).xorBitwise((words[j - 2].shiftRight(10))).copy(32);
                                words[j].assign(words[j - 16].add(s0).add(words[j - 7]).add(s1), 32);
                            }

                            for (int j = 0; j < 64; j++) {
                                UnsignedInteger s0 = rotateRight(a.copy(32), 2).xorBitwise(rotateRight(a.copy(32), 13)).xorBitwise(rotateRight(a.copy(32), 22)).copy(32);
                                UnsignedInteger maj = (a.andBitwise(b)).xorBitwise((a.andBitwise(c))).xorBitwise((b.andBitwise(c))).copy(32);
                                UnsignedInteger t2 = s0.add(maj).copy(32);

                                UnsignedInteger s1 = rotateRight(e.copy(32), 6).xorBitwise(rotateRight(e.copy(32), 11)).xorBitwise(rotateRight(e.copy(32), 25)).copy(32);
                                UnsignedInteger ch = e.andBitwise(f).xorBitwise(e.invBits().andBitwise(g)).copy(32);
                                // the uint_32(.) call is to convert from java type to xjsnark type
                                UnsignedInteger t1 = h.add(s1).add(ch).add(UnsignedInteger.instantiateFrom(32, K_CONST[j])).add(words[j]).copy(32);

                                h.assign(g, 32);
                                g.assign(f, 32);
                                f.assign(e, 32);
                                e.assign(d.add(t1), 32);
                                d.assign(c, 32);
                                c.assign(b, 32);
                                b.assign(a, 32);
                                a.assign(t1.add(t2), 32);
                            }

                            H[0].assign(H[0].add(a), 32);
                            H[1].assign(H[1].add(b), 32);
                            H[2].assign(H[2].add(c), 32);
                            H[3].assign(H[3].add(d), 32);
                            H[4].assign(H[4].add(e), 32);
                            H[5].assign(H[5].add(f), 32);
                            H[6].assign(H[6].add(g), 32);
                            H[7].assign(H[7].add(h), 32);

                            i += 1;
                        } else {

                        }
                    } else {
                        ConditionalScopeTracker.pushMain();
                        ConditionalScopeTracker.push(bit_a0b0d0g);
                        UnsignedInteger[] words = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{64}, 32);
                        UnsignedInteger a = H[0].copy(32);
                        UnsignedInteger b = H[1].copy(32);
                        UnsignedInteger c = H[2].copy(32);
                        UnsignedInteger d = H[3].copy(32);
                        UnsignedInteger e = H[4].copy(32);
                        UnsignedInteger f = H[5].copy(32);
                        UnsignedInteger g = H[6].copy(32);
                        UnsignedInteger h = H[7].copy(32);

                        for (int j = 0; j < 16; j++) {
                            words[j].assign(preimage[j + i * 16], 32);
                        }

                        for (int j = 16; j < 64; j++) {
                            UnsignedInteger s0 = rotateRight(words[j - 15].copy(32), 7).xorBitwise(rotateRight(words[j - 15].copy(32), 18)).xorBitwise((words[j - 15].shiftRight(3))).copy(32);
                            UnsignedInteger s1 = rotateRight(words[j - 2].copy(32), 17).xorBitwise(rotateRight(words[j - 2].copy(32), 19)).xorBitwise((words[j - 2].shiftRight(10))).copy(32);
                            words[j].assign(words[j - 16].add(s0).add(words[j - 7]).add(s1), 32);
                        }

                        for (int j = 0; j < 64; j++) {
                            UnsignedInteger s0 = rotateRight(a.copy(32), 2).xorBitwise(rotateRight(a.copy(32), 13)).xorBitwise(rotateRight(a.copy(32), 22)).copy(32);
                            UnsignedInteger maj = (a.andBitwise(b)).xorBitwise((a.andBitwise(c))).xorBitwise((b.andBitwise(c))).copy(32);
                            UnsignedInteger t2 = s0.add(maj).copy(32);

                            UnsignedInteger s1 = rotateRight(e.copy(32), 6).xorBitwise(rotateRight(e.copy(32), 11)).xorBitwise(rotateRight(e.copy(32), 25)).copy(32);
                            UnsignedInteger ch = e.andBitwise(f).xorBitwise(e.invBits().andBitwise(g)).copy(32);
                            // the uint_32(.) call is to convert from java type to xjsnark type
                            UnsignedInteger t1 = h.add(s1).add(ch).add(UnsignedInteger.instantiateFrom(32, K_CONST[j])).add(words[j]).copy(32);

                            h.assign(g, 32);
                            g.assign(f, 32);
                            f.assign(e, 32);
                            e.assign(d.add(t1), 32);
                            d.assign(c, 32);
                            c.assign(b, 32);
                            b.assign(a, 32);
                            a.assign(t1.add(t2), 32);
                        }

                        H[0].assign(H[0].add(a), 32);
                        H[1].assign(H[1].add(b), 32);
                        H[2].assign(H[2].add(c), 32);
                        H[3].assign(H[3].add(d), 32);
                        H[4].assign(H[4].add(e), 32);
                        H[5].assign(H[5].add(f), 32);
                        H[6].assign(H[6].add(g), 32);
                        H[7].assign(H[7].add(h), 32);

                        i += 1;

                        ConditionalScopeTracker.pop();

                        ConditionalScopeTracker.push(new Bit(true));

                        ConditionalScopeTracker.pop();
                        ConditionalScopeTracker.popMain();
                    }

                }
                loop_counter_d0g = loop_counter_d0g + 1;
            }

        }
        return H;
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

    public void ec_sigcheck(FieldElement pk_x, FieldElement pk_y, UnsignedInteger r, UnsignedInteger s, UnsignedInteger h) {
        // EC generator point
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
        log.debug("Precomputing constants (might take time in this version -- see note in the code).. ");
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
            UnsignedInteger t10 = UnsignedInteger.instantiateFrom(256, t1[0]).copy(256);
            UnsignedInteger t11 = UnsignedInteger.instantiateFrom(256, t1[1]).copy(256);
            table2[i][0].assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), t10));
            table2[i][1].assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), t11));
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
                Bit bit_a0jb0n = u1.getBitElements()[i].copy();
                boolean c_a0jb0n = CircuitGenerator.__getActiveCircuitGenerator().__checkConstantState(bit_a0jb0n);
                if (c_a0jb0n) {
                    if (bit_a0jb0n.getConstantValue()) {
                        {
                            Bit bit_a0a0a2a0a53a31 = init1.copy();
                            boolean c_a0a0a2a0a53a31 = CircuitGenerator.__getActiveCircuitGenerator().__checkConstantState(bit_a0a0a2a0a53a31);
                            if (c_a0a0a2a0a53a31) {
                                if (bit_a0a0a2a0a53a31.getConstantValue()) {
                                    FieldElement[] rr = addPoints(p1_x.copy(), p1_y.copy(), table[i][0].copy(), table[i][1].copy());
                                    UnsignedInteger x = UnsignedInteger.instantiateFrom(256, rr[0]).copy(256);
                                    p1_x.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), x));
                                    UnsignedInteger y = UnsignedInteger.instantiateFrom(256, rr[1]).copy(256);
                                    p1_y.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), y));
                                } else {
                                    init1.assign(Bit.instantiateFrom(true));
                                    p1_x.assign(table[i][0]);
                                    p1_y.assign(table[i][1]);

                                }
                            } else {
                                ConditionalScopeTracker.pushMain();
                                ConditionalScopeTracker.push(bit_a0a0a2a0a53a31);
                                FieldElement[] rr = addPoints(p1_x.copy(), p1_y.copy(), table[i][0].copy(), table[i][1].copy());
                                UnsignedInteger x = UnsignedInteger.instantiateFrom(256, rr[0]).copy(256);
                                p1_x.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), x));
                                UnsignedInteger y = UnsignedInteger.instantiateFrom(256, rr[1]).copy(256);
                                p1_y.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), y));

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
                    ConditionalScopeTracker.push(bit_a0jb0n);
                    {
                        Bit bit_a0a0jb0n = init1.copy();
                        boolean c_a0a0jb0n = CircuitGenerator.__getActiveCircuitGenerator().__checkConstantState(bit_a0a0jb0n);
                        if (c_a0a0jb0n) {
                            if (bit_a0a0jb0n.getConstantValue()) {
                                FieldElement[] rr = addPoints(p1_x.copy(), p1_y.copy(), table[i][0].copy(), table[i][1].copy());
                                UnsignedInteger x = UnsignedInteger.instantiateFrom(256, rr[0]).copy(256);
                                p1_x.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), x));
                                UnsignedInteger y = UnsignedInteger.instantiateFrom(256, rr[1]).copy(256);
                                p1_y.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), y));
                            } else {
                                init1.assign(Bit.instantiateFrom(true));
                                p1_x.assign(table[i][0]);
                                p1_y.assign(table[i][1]);

                            }
                        } else {
                            ConditionalScopeTracker.pushMain();
                            ConditionalScopeTracker.push(bit_a0a0jb0n);
                            FieldElement[] rr = addPoints(p1_x.copy(), p1_y.copy(), table[i][0].copy(), table[i][1].copy());
                            UnsignedInteger x = UnsignedInteger.instantiateFrom(256, rr[0]).copy(256);
                            p1_x.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), x));
                            UnsignedInteger y = UnsignedInteger.instantiateFrom(256, rr[1]).copy(256);
                            p1_y.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), y));

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
                Bit bit_c0jb0n = u2.getBitElements()[i].copy();
                boolean c_c0jb0n = CircuitGenerator.__getActiveCircuitGenerator().__checkConstantState(bit_c0jb0n);
                if (c_c0jb0n) {
                    if (bit_c0jb0n.getConstantValue()) {
                        {
                            Bit bit_a0a0a2a2a53a31 = init2.copy();
                            boolean c_a0a0a2a2a53a31 = CircuitGenerator.__getActiveCircuitGenerator().__checkConstantState(bit_a0a0a2a2a53a31);
                            if (c_a0a0a2a2a53a31) {
                                if (bit_a0a0a2a2a53a31.getConstantValue()) {
                                    FieldElement[] rr = addPoints(p2_x.copy(), p2_y.copy(), table2[i][0].copy(), table2[i][1].copy());
                                    UnsignedInteger x = UnsignedInteger.instantiateFrom(256, rr[0]).copy(256);
                                    p2_x.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), x));
                                    UnsignedInteger y = UnsignedInteger.instantiateFrom(256, rr[1]).copy(256);
                                    p2_y.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), y));
                                } else {
                                    init2.assign(Bit.instantiateFrom(true));
                                    p2_x.assign(table2[i][0]);
                                    p2_y.assign(table2[i][1]);

                                }
                            } else {
                                ConditionalScopeTracker.pushMain();
                                ConditionalScopeTracker.push(bit_a0a0a2a2a53a31);
                                FieldElement[] rr = addPoints(p2_x.copy(), p2_y.copy(), table2[i][0].copy(), table2[i][1].copy());
                                UnsignedInteger x = UnsignedInteger.instantiateFrom(256, rr[0]).copy(256);
                                p2_x.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), x));
                                UnsignedInteger y = UnsignedInteger.instantiateFrom(256, rr[1]).copy(256);
                                p2_y.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), y));

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
                    ConditionalScopeTracker.push(bit_c0jb0n);
                    {
                        Bit bit_a0c0jb0n = init2.copy();
                        boolean c_a0c0jb0n = CircuitGenerator.__getActiveCircuitGenerator().__checkConstantState(bit_a0c0jb0n);
                        if (c_a0c0jb0n) {
                            if (bit_a0c0jb0n.getConstantValue()) {
                                FieldElement[] rr = addPoints(p2_x.copy(), p2_y.copy(), table2[i][0].copy(), table2[i][1].copy());
                                UnsignedInteger x = UnsignedInteger.instantiateFrom(256, rr[0]).copy(256);
                                p2_x.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), x));
                                UnsignedInteger y = UnsignedInteger.instantiateFrom(256, rr[1]).copy(256);
                                p2_y.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), y));
                            } else {
                                init2.assign(Bit.instantiateFrom(true));
                                p2_x.assign(table2[i][0]);
                                p2_y.assign(table2[i][1]);

                            }
                        } else {
                            ConditionalScopeTracker.pushMain();
                            ConditionalScopeTracker.push(bit_a0c0jb0n);
                            FieldElement[] rr = addPoints(p2_x.copy(), p2_y.copy(), table2[i][0].copy(), table2[i][1].copy());
                            UnsignedInteger x = UnsignedInteger.instantiateFrom(256, rr[0]).copy(256);
                            p2_x.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), x));
                            UnsignedInteger y = UnsignedInteger.instantiateFrom(256, rr[1]).copy(256);
                            p2_y.assign(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), y));

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
        UnsignedInteger x = UnsignedInteger.instantiateFrom(256, p[0]).copy(256);
        FieldElement p0 = FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), x).copy();

        p0.forceEqual(FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), r));
    }

    public static final int[] SBOX = {0x63, 0x7c, 0x77, 0x7b, 0xf2, 0x6b, 0x6f, 0xc5, 0x30, 0x01, 0x67, 0x2b, 0xfe, 0xd7, 0xab, 0x76, 0xca, 0x82, 0xc9, 0x7d, 0xfa, 0x59, 0x47, 0xf0, 0xad, 0xd4, 0xa2, 0xaf, 0x9c, 0xa4, 0x72, 0xc0, 0xb7, 0xfd, 0x93, 0x26, 0x36, 0x3f, 0xf7, 0xcc, 0x34, 0xa5, 0xe5, 0xf1, 0x71, 0xd8, 0x31, 0x15, 0x04, 0xc7, 0x23, 0xc3, 0x18, 0x96, 0x05, 0x9a, 0x07, 0x12, 0x80, 0xe2, 0xeb, 0x27, 0xb2, 0x75, 0x09, 0x83, 0x2c, 0x1a, 0x1b, 0x6e, 0x5a, 0xa0, 0x52, 0x3b, 0xd6, 0xb3, 0x29, 0xe3, 0x2f, 0x84, 0x53, 0xd1, 0x00, 0xed, 0x20, 0xfc, 0xb1, 0x5b, 0x6a, 0xcb, 0xbe, 0x39, 0x4a, 0x4c, 0x58, 0xcf, 0xd0, 0xef, 0xaa, 0xfb, 0x43, 0x4d, 0x33, 0x85, 0x45, 0xf9, 0x02, 0x7f, 0x50, 0x3c, 0x9f, 0xa8, 0x51, 0xa3, 0x40, 0x8f, 0x92, 0x9d, 0x38, 0xf5, 0xbc, 0xb6, 0xda, 0x21, 0x10, 0xff, 0xf3, 0xd2, 0xcd, 0x0c, 0x13, 0xec, 0x5f, 0x97, 0x44, 0x17, 0xc4, 0xa7, 0x7e, 0x3d, 0x64, 0x5d, 0x19, 0x73, 0x60, 0x81, 0x4f, 0xdc, 0x22, 0x2a, 0x90, 0x88, 0x46, 0xee, 0xb8, 0x14, 0xde, 0x5e, 0x0b, 0xdb, 0xe0, 0x32, 0x3a, 0x0a, 0x49, 0x06, 0x24, 0x5c, 0xc2, 0xd3, 0xac, 0x62, 0x91, 0x95, 0xe4, 0x79, 0xe7, 0xc8, 0x37, 0x6d, 0x8d, 0xd5, 0x4e, 0xa9, 0x6c, 0x56, 0xf4, 0xea, 0x65, 0x7a, 0xae, 0x08, 0xba, 0x78, 0x25, 0x2e, 0x1c, 0xa6, 0xb4, 0xc6, 0xe8, 0xdd, 0x74, 0x1f, 0x4b, 0xbd, 0x8b, 0x8a, 0x70, 0x3e, 0xb5, 0x66, 0x48, 0x03, 0xf6, 0x0e, 0x61, 0x35, 0x57, 0xb9, 0x86, 0xc1, 0x1d, 0x9e, 0xe1, 0xf8, 0x98, 0x11, 0x69, 0xd9, 0x8e, 0x94, 0x9b, 0x1e, 0x87, 0xe9, 0xce, 0x55, 0x28, 0xdf, 0x8c, 0xa1, 0x89, 0x0d, 0xbf, 0xe6, 0x42, 0x68, 0x41, 0x99, 0x2d, 0x0f, 0xb0, 0x54, 0xbb, 0x16};
    public static final int[] RCON = {0x8d, 0x01, 0x02, 0x04, 0x08, 0x10, 0x20, 0x40, 0x80, 0x1b, 0x36};
    private static int nb = 4;
    private static int nk = 4;

    private UnsignedInteger[] rotWord(UnsignedInteger[] w) {
        UnsignedInteger[] newW = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{w.length}, 8);
        for (int j = 0; j < w.length; j++) {
            newW[j].assign(w[(j + 1) % w.length], 8);
        }
        return newW;
    }

    private UnsignedInteger[] subWord(UnsignedInteger[] w) {
        UnsignedInteger[] newW = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{w.length}, 8);
        for (int j = 0; j < w.length; j++) {
            newW[j].assign(sBoxMem.read(w[j]), 8);
        }
        return newW;
    }

    private UnsignedInteger gal_mul_const(UnsignedInteger x, int c) {

        UnsignedInteger p = new UnsignedInteger(8, new BigInteger("0"));
        for (int counter = 0; counter < 8; counter += 1) {
            if ((c & 1) != 0) {
                p.assign(p.xorBitwise(x), 8);
            }
            c = c >> 1;
            // c is a java integer, we can use the == operator. Otherwise, the eq operator should be used
            // we use break; here as this is also a static java loop -- support for the runtime bWhile break; will be added.
            if (c == 0) {
                break;
            }
            Bit[] xBits = x.getBitElements();
            Bit hi = xBits[7].copy();
            x.assign(x.shiftLeft(1), 8);
            UnsignedInteger tmp = x.xorBitwise(new BigInteger("" + 0x1b)).copy(8);

            // this is a runtime circuit condition
            {
                Bit bit_l0c0gb = hi.copy();
                boolean c_l0c0gb = CircuitGenerator.__getActiveCircuitGenerator().__checkConstantState(bit_l0c0gb);
                if (c_l0c0gb) {
                    if (bit_l0c0gb.getConstantValue()) {
                        x.assign(tmp, 8);
                    } else {

                    }
                } else {
                    ConditionalScopeTracker.pushMain();
                    ConditionalScopeTracker.push(bit_l0c0gb);
                    x.assign(tmp, 8);

                    ConditionalScopeTracker.pop();

                    ConditionalScopeTracker.push(new Bit(true));

                    ConditionalScopeTracker.pop();
                    ConditionalScopeTracker.popMain();
                }

            }
        }
        return p;
    }

    private UnsignedInteger[][] shiftRows(UnsignedInteger[][] state) {
        UnsignedInteger[][] newState = (UnsignedInteger[][]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{4, 4}, 8);
        for (int j = 0; j < 4; j++) {
            newState[0][j].assign(state[0][j], 8);
            newState[1][j].assign(state[1][(j + 1) % 4], 8);
            newState[2][j].assign(state[2][(j + 2) % 4], 8);
            newState[3][j].assign(state[3][(j + 3) % 4], 8);
        }
        return newState;
    }

    private UnsignedInteger[][] mixColumns(UnsignedInteger[][] state) {

        UnsignedInteger[] a = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{4}, 8);
        UnsignedInteger[][] newState = (UnsignedInteger[][]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{4, 4}, 8);
        for (int c = 0; c < 4; c++) {
            for (int i = 0; i < 4; i++) {
                a[i].assign(state[i][c], 8);
            }
            newState[0][c].assign(gal_mul_const(a[0].copy(8), 2).xorBitwise(gal_mul_const(a[1].copy(8), 3)).xorBitwise(a[2]).xorBitwise(a[3]), 8);
            newState[1][c].assign(a[0].xorBitwise(gal_mul_const(a[1].copy(8), 2)).xorBitwise(gal_mul_const(a[2].copy(8), 3)).xorBitwise(a[3]), 8);
            newState[2][c].assign(a[0].xorBitwise(a[1]).xorBitwise(gal_mul_const(a[2].copy(8), 2)).xorBitwise(gal_mul_const(a[3].copy(8), 3)), 8);
            newState[3][c].assign(gal_mul_const(a[0].copy(8), 3).xorBitwise(a[1]).xorBitwise(a[2]).xorBitwise(gal_mul_const(a[3].copy(8), 2)), 8);
        }
        return newState;
    }

    private UnsignedInteger[] expandKey() {

        int nr = nk + 6;
        UnsignedInteger[] expandedKey = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{nb * (nr + 1) * 4}, 8);
        UnsignedInteger[][] w = (UnsignedInteger[][]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{nb * (nr + 1), 4}, 8);
        UnsignedInteger[] tmp;

        // using native java types for loops with bounds known at compilation time
        int i = 0;

        while (i < nk) {
            w[i] = new UnsignedInteger[]{key[4 * i].copy(8), key[4 * i + 1].copy(8), key[4 * i + 2].copy(8), key[4 * i + 3].copy(8)};
            i++;
        }
        while (i < nb * (nr + 1)) {
            tmp = w[i - 1];
            if (i % nk == 0) {
                tmp = rotWord(tmp);
                tmp = subWord(tmp);
                tmp[0].assign(tmp[0].xorBitwise(UnsignedInteger.instantiateFrom(8, RCON[i / 4])), 8);
            }
            for (int v = 0; v < 4; v++) {
                w[i][v].assign(w[i - nk][v].xorBitwise(tmp[v]), 8);
            }
            i++;
        }

        int idx = 0;
        for (int k = 0; k < nb * (nr + 1); k++) {
            for (int j = 0; j < 4; j++) {
                expandedKey[idx].assign(w[k][j], 8);
                idx++;
            }
        }
        return expandedKey;
    }

    private void initAes128() {
        UnsignedInteger[] sBox = UnsignedInteger.instantiateFrom(8, SBOX);
        sBoxMem = new SmartMemory(sBox, UnsignedInteger.__getClassRef(), new Object[]{"8"});
        for (int i = 0; i < 16; i++) {
            key[i].assign(client_swk_share[i].xorBitwise(notary_swk_share[i]), 8);
        }
        expandedKey = expandKey();
    }

    public UnsignedInteger[] aes128(UnsignedInteger[] plaintext) {
        UnsignedInteger[] ciphertext = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{16}, 8);
        UnsignedInteger[][] state = (UnsignedInteger[][]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{4, 4}, 8);
        int idx = 0;
        for (int j = 0; j < 4; j++) {
            for (int k = 0; k < 4; k++) {
                state[k][j].assign(plaintext[idx++], 8);

            }
        }
        state = addRoundkey(state, 0, 3);
        int nr = 6 + nk;
        for (int round = 1; round < nr; round++) {
            state = subState(state);
            state = shiftRows(state);
            state = mixColumns(state);
            state = addRoundkey(state, round * 4 * 4, (round + 1) * 4 * 4 - 1);
            // Note: the methods substate(), shiftRows(), mixColumns(), addRoundKey() could be also be
            // written in a way that updates the state array directly in place.
        }
        state = subState(state);
        state = shiftRows(state);
        state = addRoundkey(state, nr * nb * 4, (nr + 1) * nb * 4);

        idx = 0;
        for (int j = 0; j < 4; j++) {
            for (int k = 0; k < 4; k++) {
                ciphertext[idx].assign(state[k][j], 8);
                idx++;
            }
        }
        return ciphertext;
    }

    private UnsignedInteger[][] addRoundkey(UnsignedInteger[][] state, int from, int to) {
        UnsignedInteger[][] newState = (UnsignedInteger[][]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{4, 4}, 8);
        int idx = 0;
        for (int j = 0; j < 4; j++) {
            for (int i = 0; i < 4; i++) {
                newState[i][j].assign(state[i][j].xorBitwise(expandedKey[from + idx]), 8);

                idx++;
            }
        }
        return newState;
    }

    private UnsignedInteger[][] subState(UnsignedInteger[][] state) {
        UnsignedInteger[][] newState = (UnsignedInteger[][]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{4, 4}, 8);
        for (int i = 0; i < state.length; i++) {
            for (int j = 0; j < state[i].length; j++) {
                newState[i][j].assign(sBoxMem.read(state[i][j]), 8);
            }
        }
        return newState;
    }

    private UnsignedInteger[] aes128_ctr(UnsignedInteger[] msg, UnsignedInteger[] nonce, int counter_start_val) {
        UnsignedInteger[] ans = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{msg.length}, 8);
        int counter = counter_start_val;
        for (int i = 0; i < 1008 / 16; i++) {
            UnsignedInteger[] chunk = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{16}, 8);
            for (int j = 0; j < 16; j++) {
                chunk[j].assign(msg[i * 16 + j], 8);
            }
            UnsignedInteger[] chunk_nonce = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{16}, 8);
            for (int j = 0; j < 12; j++) {
                chunk_nonce[j].assign(nonce[j], 8);
            }
            chunk_nonce[12].assign(UnsignedInteger.instantiateFrom(8, 0), 8);
            chunk_nonce[13].assign(UnsignedInteger.instantiateFrom(8, 0), 8);
            chunk_nonce[14].assign(UnsignedInteger.instantiateFrom(8, 0), 8);
            chunk_nonce[15].assign(UnsignedInteger.instantiateFrom(8, counter), 8);
            UnsignedInteger[] encrypted_chunk_nonce = aes128(chunk_nonce);
            for (int j = 0; j < 16; j++) {
                ans[i * 16 + j].assign(encrypted_chunk_nonce[j].xorBitwise(chunk[j]), 8);
            }
            counter += 1;
        }
        return ans;
    }

    private UnsignedInteger[] aes128_gcm_decrypt(UnsignedInteger[] msg, UnsignedInteger[] nonce) {
        return aes128_ctr(msg, nonce, 2);
    }

    @Override
    public void __defineInputs() {
        super.__defineInputs();


        time_content_hash = (UnsignedInteger[]) UnsignedInteger.createInputArray(CircuitGenerator.__getActiveCircuitGenerator(), Util.getArrayDimensions(time_content_hash), 32);


    }

    @Override
    public void __defineVerifiedWitnesses() {
        super.__defineVerifiedWitnesses();

        server_records_padded_u32_blocks = UnsignedInteger.createVerifiedWitness(this, 32);
        client_req_cipher_len = UnsignedInteger.createVerifiedWitness(this, 32);
        tbs1_blocks = UnsignedInteger.createVerifiedWitness(this, 32);
        content_start = UnsignedInteger.createVerifiedWitness(this, 32);
        content_len = UnsignedInteger.createVerifiedWitness(this, 32);
        time_content_padded_u32_blocks = UnsignedInteger.createVerifiedWitness(this, 32);


        server_records = (UnsignedInteger[]) UnsignedInteger.createVerifiedWitnessArray(CircuitGenerator.__getActiveCircuitGenerator(), Util.getArrayDimensions(server_records), 8);
        client_cwk_share = (UnsignedInteger[]) UnsignedInteger.createVerifiedWitnessArray(CircuitGenerator.__getActiveCircuitGenerator(), Util.getArrayDimensions(client_cwk_share), 8);
        client_civ_share = (UnsignedInteger[]) UnsignedInteger.createVerifiedWitnessArray(CircuitGenerator.__getActiveCircuitGenerator(), Util.getArrayDimensions(client_civ_share), 8);
        client_swk_share = (UnsignedInteger[]) UnsignedInteger.createVerifiedWitnessArray(CircuitGenerator.__getActiveCircuitGenerator(), Util.getArrayDimensions(client_swk_share), 8);
        client_siv_share = (UnsignedInteger[]) UnsignedInteger.createVerifiedWitnessArray(CircuitGenerator.__getActiveCircuitGenerator(), Util.getArrayDimensions(client_siv_share), 8);
        client_pms_share = (UnsignedInteger[]) UnsignedInteger.createVerifiedWitnessArray(CircuitGenerator.__getActiveCircuitGenerator(), Util.getArrayDimensions(client_pms_share), 8);
        client_req_cipher = (UnsignedInteger[]) UnsignedInteger.createVerifiedWitnessArray(CircuitGenerator.__getActiveCircuitGenerator(), Util.getArrayDimensions(client_req_cipher), 8);
        notary_cwk_share = (UnsignedInteger[]) UnsignedInteger.createVerifiedWitnessArray(CircuitGenerator.__getActiveCircuitGenerator(), Util.getArrayDimensions(notary_cwk_share), 8);
        notary_civ_share = (UnsignedInteger[]) UnsignedInteger.createVerifiedWitnessArray(CircuitGenerator.__getActiveCircuitGenerator(), Util.getArrayDimensions(notary_civ_share), 8);
        notary_swk_share = (UnsignedInteger[]) UnsignedInteger.createVerifiedWitnessArray(CircuitGenerator.__getActiveCircuitGenerator(), Util.getArrayDimensions(notary_swk_share), 8);
        notary_siv_share = (UnsignedInteger[]) UnsignedInteger.createVerifiedWitnessArray(CircuitGenerator.__getActiveCircuitGenerator(), Util.getArrayDimensions(notary_siv_share), 8);
        notary_pms_share = (UnsignedInteger[]) UnsignedInteger.createVerifiedWitnessArray(CircuitGenerator.__getActiveCircuitGenerator(), Util.getArrayDimensions(notary_pms_share), 8);
        emphemeral_pubkey = (UnsignedInteger[]) UnsignedInteger.createVerifiedWitnessArray(CircuitGenerator.__getActiveCircuitGenerator(), Util.getArrayDimensions(emphemeral_pubkey), 8);
        emphemeral_pubkey_valid_from = (UnsignedInteger[]) UnsignedInteger.createVerifiedWitnessArray(CircuitGenerator.__getActiveCircuitGenerator(), Util.getArrayDimensions(emphemeral_pubkey_valid_from), 8);
        emphemeral_pubkey_valid_until = (UnsignedInteger[]) UnsignedInteger.createVerifiedWitnessArray(CircuitGenerator.__getActiveCircuitGenerator(), Util.getArrayDimensions(emphemeral_pubkey_valid_until), 8);
        notary_pubkey = (UnsignedInteger[]) UnsignedInteger.createVerifiedWitnessArray(CircuitGenerator.__getActiveCircuitGenerator(), Util.getArrayDimensions(notary_pubkey), 8);
        session_sig = (UnsignedInteger[]) UnsignedInteger.createVerifiedWitnessArray(CircuitGenerator.__getActiveCircuitGenerator(), Util.getArrayDimensions(session_sig), 8);
        emphemeral_signed_by_notary = (UnsignedInteger[]) UnsignedInteger.createVerifiedWitnessArray(CircuitGenerator.__getActiveCircuitGenerator(), Util.getArrayDimensions(emphemeral_signed_by_notary), 8);
        tbs1 = (UnsignedInteger[]) UnsignedInteger.createVerifiedWitnessArray(CircuitGenerator.__getActiveCircuitGenerator(), Util.getArrayDimensions(tbs1), 8);
        expected_content = (UnsignedInteger[]) UnsignedInteger.createVerifiedWitnessArray(CircuitGenerator.__getActiveCircuitGenerator(), Util.getArrayDimensions(expected_content), 8);
        notary_time = (UnsignedInteger[]) UnsignedInteger.createVerifiedWitnessArray(CircuitGenerator.__getActiveCircuitGenerator(), Util.getArrayDimensions(notary_time), 8);
        time_content_padded = (UnsignedInteger[]) UnsignedInteger.createVerifiedWitnessArray(CircuitGenerator.__getActiveCircuitGenerator(), Util.getArrayDimensions(time_content_padded), 8);


    }

    @Override
    public void __defineWitnesses() {
        super.__defineWitnesses();


    }

    public void outsource() {
        // Entry point for the circuit. Input and witness arrays/structs must be instantiated outside this method
        // verify session is signed by emphemeral public key

        UnsignedInteger hash1 = sessionHash().copy(256);

        FieldElement[] pk1 = pk_bytes_to_field(emphemeral_pubkey);
        UnsignedInteger[] sig1 = sig_bytes_to_u256(session_sig);
        ec_sigcheck(pk1[0].copy(), pk1[1].copy(), sig1[0].copy(256), sig1[1].copy(256), hash1.copy(256));

        // verify emphemeral is signed by notary public key
        UnsignedInteger[] e = emphemeral();
        UnsignedInteger[] e_padded = padding(e);
        UnsignedInteger[] e32 = u8_array_to_u32_array(e_padded);
        UnsignedInteger[] e_sha256 = sha256(e32);
        // notary pubkey is in whitelist, currently only one whitelist notary
        // todo
        //  1. update the whitelist
        //  2. uncommented
//        UnsignedInteger hash2 = u32_array_to_u256(e_sha256).copy(256);
//        FieldElement[] pk2 = pk_bytes_to_field(notary_pubkey);
//        UnsignedInteger[] sig2 = sig_bytes_to_u256(emphemeral_signed_by_notary);
//
//        FieldElement whitelist_pubkey0 = FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), "1183580499612595107347534150886196424982336515517045996405235863538343686536").copy();
//        FieldElement whitelist_pubkey1 = FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), "84929283230988835289687745630717661562113198563775866077273232656743255265136").copy();
//        pk2[0].forceEqual(whitelist_pubkey0);
//        pk2[1].forceEqual(whitelist_pubkey1);

//        ec_sigcheck(pk2[0], pk2[1], sig2[0].copy(256), sig2[1].copy(256), hash2.copy(256));


        UnsignedInteger[] nonce = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{16}, 8);
        for (int i = 0; i < 4; i++) {
            nonce[i].assign(client_siv_share[i].xorBitwise(notary_siv_share[i]), 8);
        }
        for (int i = 4; i < 16; i++) {
            nonce[i].assign(server_records[i - 4], 8);
        }
        initAes128();
        UnsignedInteger[] msg = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{1008}, 8);
        for (int i = 0; i < 1008; i++) {
            msg[i].assign(server_records[i + 8], 8);
        }
        UnsignedInteger[] decrypted = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{1008}, 8);
        decrypted = aes128_gcm_decrypt(msg, nonce);
        checkContent(decrypted);

        checkPaddedContent();
        UnsignedInteger[] tc = u8_array_to_u32_array(time_content_padded);
        UnsignedInteger[] tcsha256 = sha256_len(tc, time_content_padded_u32_blocks.copy(32), 448);
        for (int i = 0; i < 8; i++) {
            tcsha256[i].forceEqual(time_content_hash[i]);
        }
    }

    private UnsignedInteger u32_array_to_u256(UnsignedInteger[] arr) {
        UnsignedInteger ret = new UnsignedInteger(256, new BigInteger("0"));
        for (int i = 0; i < 8; i++) {
            int j = 7 - i;
            UnsignedInteger k = UnsignedInteger.instantiateFrom(256, arr[i]).shiftLeft((32 * j)).copy(256);
            ret.assign(ret.add(k), 256);
        }
        return ret;
    }

    private FieldElement[] pk_bytes_to_field(UnsignedInteger[] pk) {
        UnsignedInteger x = new UnsignedInteger(256, new BigInteger("0"));
        UnsignedInteger y = new UnsignedInteger(256, new BigInteger("0"));
        for (int i = 1; i < 33; i++) {
            int j = 32 - i;
            x.assign(x.add((UnsignedInteger.instantiateFrom(256, pk[i]).shiftLeft((8 * j)))), 256);
        }
        for (int i = 33; i < 65; i++) {
            int j = 64 - i;
            y.assign(y.add((UnsignedInteger.instantiateFrom(256, pk[i]).shiftLeft((8 * j)))), 256);
        }
        return new FieldElement[]{FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), x).copy(), FieldElement.instantiateFrom(new BigInteger("115792089210356248762697446949407573530086143415290314195533631308867097853951"), y).copy()};
    }

    private UnsignedInteger[] sig_bytes_to_u256(UnsignedInteger[] sig) {
        UnsignedInteger r = new UnsignedInteger(256, new BigInteger("0"));
        UnsignedInteger s = new UnsignedInteger(256, new BigInteger("0"));
        for (int i = 0; i < 32; i++) {
            int j = 31 - i;
            r.assign(r.add((UnsignedInteger.instantiateFrom(256, sig[i]).shiftLeft((8 * j)))), 256);
        }
        for (int i = 32; i < 64; i++) {
            int j = 63 - i;
            s.assign(s.add((UnsignedInteger.instantiateFrom(256, sig[i]).shiftLeft((8 * j)))), 256);
        }
        return new UnsignedInteger[]{r.copy(256), s.copy(256)};
    }

    private UnsignedInteger[] u8_array_to_u32_array(UnsignedInteger[] arr) {
        int length;
        if (arr.length % 4 == 0) {
            length = arr.length / 4;
        } else {
            new UnsignedInteger(1, new BigInteger("0")).forceEqual(new UnsignedInteger(1, new BigInteger("1")));
            length = 0;
        }
        UnsignedInteger[] ret = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{length}, 32);
        for (int i = 0; i < arr.length / 4; i++) {
            ret[i].assign(new UnsignedInteger(32, new BigInteger("0")), 32);
            for (int j = 0; j < 4; j++) {
                UnsignedInteger t = UnsignedInteger.instantiateFrom(32, arr[i * 4 + j]).shiftLeft(((3 - j) * 8)).copy(32);
                ret[i].assign(ret[i].add(t), 32);
            }
        }
        return ret;
    }

    private UnsignedInteger[] padding(UnsignedInteger[] a) {
        int length = a.length;
        int n = length / 64;
        int mod = length % 64;
        int ret_length;
        if (mod >= 56) {
            ret_length = (n + 2) * 64;
        } else {
            ret_length = (n + 1) * 64;
        }
        UnsignedInteger[] ret = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{ret_length}, 8);
        for (int i = 0; i < length; i++) {
            ret[i].assign(a[i], 8);
        }
        for (int i = length; i < length + 1; i++) {
            ret[i].assign(UnsignedInteger.instantiateFrom(8, 0x80), 8);
        }
        // bug, this doesn't work, have to use above form to work around
        for (int i = length + 1; i < ret_length - 8; i++) {
            ret[i].assign(UnsignedInteger.instantiateFrom(8, 0), 8);
        }
        for (int i = ret_length - 8; i < ret_length - 4; i++) {
            ret[i].assign(UnsignedInteger.instantiateFrom(8, 0), 8);
        }
        long l = length * 8;
        for (int i = ret_length - 4; i < ret_length; i++) {
            long j = ret_length - i - 1;
            j = (l >> (j * 8)) & 0xff;
            ret[i].assign(UnsignedInteger.instantiateFrom(8, j), 8);
        }
        return ret;
    }

    private UnsignedInteger[] emphemeral() {
        UnsignedInteger[] ret = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{emphemeral_pubkey.length + emphemeral_pubkey_valid_from.length + emphemeral_pubkey_valid_until.length}, 8);
        for (int i = 0; i < emphemeral_pubkey_valid_from.length; i++) {
            ret[i].assign(emphemeral_pubkey_valid_from[i], 8);
        }
        for (int i = 0; i < emphemeral_pubkey_valid_until.length; i++) {
            ret[i + emphemeral_pubkey_valid_from.length].assign(emphemeral_pubkey_valid_until[i], 8);
        }
        for (int i = 0; i < emphemeral_pubkey.length; i++) {
            ret[i + emphemeral_pubkey_valid_until.length + emphemeral_pubkey_valid_from.length].assign(emphemeral_pubkey[i], 8);
        }
        return ret;
    }

    private UnsignedInteger sessionHash() {

        UnsignedInteger[] srs = u8_array_to_u32_array(server_records);
        UnsignedInteger[] srshash = sha256_len(srs, server_records_padded_u32_blocks.copy(32), 1024);
        UnsignedInteger commitHash = u32_array_to_u256(srshash).copy(256);
        UnsignedInteger[] expectedCommitHashU32 = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{8}, 32);
        UnsignedInteger[] tbs1_u32 = u8_array_to_u32_array(tbs1);

        for (int i = 0; i < 8; i++) {
            expectedCommitHashU32[i].assign(tbs1_u32[i], 32);
        }
        UnsignedInteger expectedCommitHash = u32_array_to_u256(expectedCommitHashU32).copy(256);
        // This confirms server_records are not forged
        commitHash.forceEqual(expectedCommitHash);

        UnsignedInteger[] serverEcPubkey = (UnsignedInteger[]) UnsignedInteger.createZeroArray(CircuitGenerator.__getActiveCircuitGenerator(), new int[]{88}, 8);

        // verify server pubkey in tbs1 is a whitelist one
        UnsignedInteger[] whitelistServerPubkey = {new UnsignedInteger(8, new BigInteger("4")), new UnsignedInteger(8, new BigInteger("164")), new UnsignedInteger(8, new BigInteger("203")), new UnsignedInteger(8, new BigInteger("47")), new UnsignedInteger(8, new BigInteger("187")), new UnsignedInteger(8, new BigInteger("53")), new UnsignedInteger(8, new BigInteger("92")), new UnsignedInteger(8, new BigInteger("172")), new UnsignedInteger(8, new BigInteger("243")), new UnsignedInteger(8, new BigInteger("181")), new UnsignedInteger(8, new BigInteger("58")), new UnsignedInteger(8, new BigInteger("31")), new UnsignedInteger(8, new BigInteger("142")), new UnsignedInteger(8, new BigInteger("159")), new UnsignedInteger(8, new BigInteger("18")), new UnsignedInteger(8, new BigInteger("250")), new UnsignedInteger(8, new BigInteger("191")), new UnsignedInteger(8, new BigInteger("225")), new UnsignedInteger(8, new BigInteger("4")), new UnsignedInteger(8, new BigInteger("171")), new UnsignedInteger(8, new BigInteger("209")), new UnsignedInteger(8, new BigInteger("165")), new UnsignedInteger(8, new BigInteger("163")), new UnsignedInteger(8, new BigInteger("17")), new UnsignedInteger(8, new BigInteger("246")), new UnsignedInteger(8, new BigInteger("103")), new UnsignedInteger(8, new BigInteger("179")), new UnsignedInteger(8, new BigInteger("173")), new UnsignedInteger(8, new BigInteger("126")), new UnsignedInteger(8, new BigInteger("58")), new UnsignedInteger(8, new BigInteger("229")), new UnsignedInteger(8, new BigInteger("72")), new UnsignedInteger(8, new BigInteger("93")), new UnsignedInteger(8, new BigInteger("114")), new UnsignedInteger(8, new BigInteger("45")), new UnsignedInteger(8, new BigInteger("111")), new UnsignedInteger(8, new BigInteger("137")), new UnsignedInteger(8, new BigInteger("141")), new UnsignedInteger(8, new BigInteger("27")), new UnsignedInteger(8, new BigInteger("154")), new UnsignedInteger(8, new BigInteger("132")), new UnsignedInteger(8, new BigInteger("249")), new UnsignedInteger(8, new BigInteger("241")), new UnsignedInteger(8, new BigInteger("14")), new UnsignedInteger(8, new BigInteger("237")), new UnsignedInteger(8, new BigInteger("36")), new UnsignedInteger(8, new BigInteger("223")), new UnsignedInteger(8, new BigInteger("188")), new UnsignedInteger(8, new BigInteger("231")), new UnsignedInteger(8, new BigInteger("107")), new UnsignedInteger(8, new BigInteger("249")), new UnsignedInteger(8, new BigInteger("148")), new UnsignedInteger(8, new BigInteger("191")), new UnsignedInteger(8, new BigInteger("20")), new UnsignedInteger(8, new BigInteger("54")), new UnsignedInteger(8, new BigInteger("162")), new UnsignedInteger(8, new BigInteger("144")), new UnsignedInteger(8, new BigInteger("217")), new UnsignedInteger(8, new BigInteger("228")), new UnsignedInteger(8, new BigInteger("64")), new UnsignedInteger(8, new BigInteger("31")), new UnsignedInteger(8, new BigInteger("97")), new UnsignedInteger(8, new BigInteger("186")), new UnsignedInteger(8, new BigInteger("54")), new UnsignedInteger(8, new BigInteger("81"))};

        // server pk in tbs1 is after commitHash, keyShareHash, pmsShareHash and clientReqCipher
        UnsignedInteger pkStart = UnsignedInteger.instantiateFrom(32, 32 + 32 + 32).add(client_req_cipher_len).copy(32);
        server_pk_in_tbs1.assign(Bit.instantiateFrom(true));
        tbs1Mem = new SmartMemory(tbs1, UnsignedInteger.__getClassRef(), new Object[]{"8"});
        for (int i = 0; i < 65; i++) {
            Bit tmp = whitelistServerPubkey[i].isEqualTo(tbs1Mem.read(pkStart.add(UnsignedInteger.instantiateFrom(32, i)))).copy();
            server_pk_in_tbs1.assign(server_pk_in_tbs1.and(tmp));
        }
//        server_pk_in_tbs1.forceEqual(Bit.instantiateFrom(true));
        Bit notary_time_in_tbs1 = Bit.instantiateFrom(true).copy();
        UnsignedInteger notary_time_start = pkStart.add(UnsignedInteger.instantiateFrom(32, 65 + 32 + 16 + 4 + 16 + 4)).copy(32);
        for (int i = 0; i < 8; i++) {
            Bit tmp = notary_time[i].isEqualTo(tbs1Mem.read(notary_time_start.add(UnsignedInteger.instantiateFrom(32, i)))).copy();
            notary_time_in_tbs1.assign(notary_time_in_tbs1.and(tmp));
        }
        notary_time_in_tbs1.forceEqual(Bit.instantiateFrom(true));

        UnsignedInteger[] ret = sha256_len(tbs1_u32, tbs1_blocks.copy(32), 512);
        return u32_array_to_u256(ret);
    }

    private void checkContent(UnsignedInteger[] decrypted) {
        decryptedMem = new SmartMemory(decrypted, UnsignedInteger.__getClassRef(), new Object[]{"8"});
        Bit content_in_decrypted = Bit.instantiateFrom(true).copy();
        for (int i = 0; i < 431; i++) {
            Bit tmp = UnsignedInteger.instantiateFrom(32, i).isGreaterThanOrEquals(content_len).copy();
            Bit tmp2 = expected_content[i].isEqualTo(decryptedMem.read(UnsignedInteger.instantiateFrom(32, i).add(content_start))).copy();
            Bit tmp3 = tmp.or(tmp2).copy();
            content_in_decrypted.assign(content_in_decrypted.and(tmp3));
        }
        content_in_decrypted.forceEqual(Bit.instantiateFrom(true));
    }

    private void checkPaddedContent() {
        paddedMem = new SmartMemory(time_content_padded, UnsignedInteger.__getClassRef(), new Object[]{"8"});
        for (int i = 0; i < 8; i++) {
            time_content_padded[i].forceEqual(notary_time[i]);
        }

        Bit time_content_correct = Bit.instantiateFrom(true).copy();
        for (int i = 8; i < 431 + 8; i++) {
            Bit tmp = UnsignedInteger.instantiateFrom(32, i).isGreaterThanOrEquals(UnsignedInteger.instantiateFrom(32, 8).add(content_len)).copy();
            Bit tmp2 = expected_content[i - 8].isEqualTo(paddedMem.read(UnsignedInteger.instantiateFrom(32, i))).copy();
            Bit tmp3 = tmp.or(tmp2).copy();
            time_content_correct.assign(time_content_correct.and(tmp3));
        }
        time_content_correct.forceEqual(Bit.instantiateFrom(true));
    }

    public void __generateSampleInput(CircuitEvaluator evaluator) {
        __generateRandomInput(evaluator);
    }

}
