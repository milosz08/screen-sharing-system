package pl.polsl.screensharing.client.net;

import pl.polsl.screensharing.client.model.ConnectionDetails;

public interface ConnectionHandler {
    void onSuccess(ConnectionDetails connectionDetails);

    void onFailure(ConnectionDetails connectionDetails, String message);
}
