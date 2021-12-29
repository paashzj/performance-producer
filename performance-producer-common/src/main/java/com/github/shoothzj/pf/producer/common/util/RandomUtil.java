package com.github.shoothzj.pf.producer.common.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.ThreadLocalRandom;

/**
 * @author hezhangjian
 */
@Slf4j
public class RandomUtil {

    public static String getRandomStr(int messageByte) {
        StringBuilder messageBuilder = new StringBuilder(messageByte);
        for (int i = 0; i < messageByte; i++) {
            messageBuilder.append('a' + ThreadLocalRandom.current().nextInt(26));
        }
        return messageBuilder.toString();
    }

    public static byte[] getRandomBytes(int messageByte) {
        byte[] array = new byte[messageByte];
        for (int i = 0; i < messageByte; i++) {
            array[i] = (byte) (97 + ThreadLocalRandom.current().nextInt(26));
        }
        return array;
    }

}
