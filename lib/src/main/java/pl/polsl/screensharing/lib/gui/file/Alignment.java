/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.lib.gui.file;

public enum Alignment {
    LEFT("text-align: left;"),
    RIGHT("text-align: right;"),
    CENTER("text-align: center;");

    private final String html;

    Alignment(String html) {
        this.html = html;
    }

    public String getHtml() {
        return html;
    }
}
