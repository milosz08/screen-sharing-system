/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.net;

public interface ConnectionHandler {
    void onSuccess();
    void onFailure(Throwable throwable);
}
