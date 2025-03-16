package pl.polsl.screensharing.host.net;

public interface ConnectionHandler {
    void onSuccess();

    void onFailure(Throwable throwable);
}
