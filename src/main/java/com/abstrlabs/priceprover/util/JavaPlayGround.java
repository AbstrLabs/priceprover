package com.abstrlabs.priceprover.util;


import com.abstrlabs.priceprover.NotaryCheckInput;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.text.SimpleDateFormat;
import java.util.Date;

public class JavaPlayGround {
    public static void main(final String[] args) throws InterruptedException {
        // log level, change it in src/main/resouces/log4j2.xml
        Logger log = LogManager.getLogger(JavaPlayGround.class);
        log.debug("Debug Message Logged !!!");
        log.info("Info Message Logged !!!");

        // get current time
        Date dNow = new Date( );
        SimpleDateFormat ft = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        log.info("current time: " + ft.format(dNow));

        // convert byte to long
        byte a = -3;
        long b = (long) a & 0xff;
        log.info(b);

    }
}
