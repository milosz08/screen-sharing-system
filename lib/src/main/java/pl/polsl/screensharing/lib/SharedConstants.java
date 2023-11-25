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
}
