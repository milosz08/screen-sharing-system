/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class SharedConstants {
    public static final String IPV4_REGEX = "^[0-9.]+$";
    public static final String PORT_REGEX = "^[0-9]+$";
    public static final String USERNAME_REGEX = "^[0-9a-z]+$";

    public static final int DEFAULT_PORT = 9092;
    public static final String DEFAULT_USERNAME = "user";
    public static final double DEFAULT_ASPECT_RATIO = 16.0 / 9.0;
    public static final String PASSWORD_REPLACEMENT = "*********";
    public static final int FRAME_SIZE = 49_152; // 48kb
    public static final int AES_KEY_SIZE = 128; // 128bit
    public static final int IV_SIZE = AES_KEY_SIZE / 8;
    public static final int PACKAGE_SIZE = 49_152 - IV_SIZE; // 48kb
    public static final int BILION = 1_000_000_000;
    public static final int MAX_FRAME_WIDTH = 1600;
    public static final int MAX_FRAME_HEIGHT = 900;
}
