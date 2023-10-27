/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib;

import java.util.Objects;

public enum SystemProp {
    JVM_VERSION("java.version"),
    OS_VERSION("os.name");

    private final String holder;

    SystemProp(String holder) {
        this.holder = holder;
    }

    public String getProp() {
        return Objects.requireNonNull(System.getProperty(holder), "unknow");
    }
}
