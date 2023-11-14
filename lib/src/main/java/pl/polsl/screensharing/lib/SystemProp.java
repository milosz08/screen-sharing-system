/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib;

import lombok.RequiredArgsConstructor;

import java.util.Objects;

@RequiredArgsConstructor
public enum SystemProp {
    JVM_VERSION("java.version"),
    OS_VERSION("os.name");

    private final String holder;

    public String getProp() {
        return Objects.requireNonNull(System.getProperty(holder), "unknow");
    }
}
