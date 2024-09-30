package com.finzly.bbc.utils;

import lombok.experimental.UtilityClass;

import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@UtilityClass
public class OtpUtil {

    public static String generateOtpCode (Integer length) {
        if (length <= 0) {
            throw new IllegalArgumentException ("Length must be a positive integer");
        }

        return ThreadLocalRandom.current ()
                .ints (length, 0, 10)
                .mapToObj (Integer::toString)
                .collect (Collectors.joining ());
    }
}
