package com.abstrlabs.priceprover;

import lombok.Data;

@Data
public class NotaryCheckInput {
    long[] ccwk;
    long[] cciv;
    long[] cswk;
    long[] csiv;
    long[] cpms;
    long[] crc;
    long[] ncwk;
    long[] nciv;
    long[] nswk;
    long[] nsiv;
    long[] npms;
    long[] nt;
    long[] epk;
    long[] epkvf;
    long[] epkvu;
    long[] ss;
    long[] esbn;
    long[] sr_padded;
    long[] np;
    long[] tbs1_padded;
    long[] exp_ct;
    long[] tcp;
    long[] exp_hash;
    int sr_padded_len;
    int crc_len;
}
