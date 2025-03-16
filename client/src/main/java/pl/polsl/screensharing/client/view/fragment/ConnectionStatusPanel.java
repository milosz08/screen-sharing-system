package pl.polsl.screensharing.client.view.fragment;

import pl.polsl.screensharing.client.state.ClientState;
import pl.polsl.screensharing.client.state.VisibilityState;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.lib.gui.AbstractTextInfoPanel;

public class ConnectionStatusPanel extends AbstractTextInfoPanel {
    private final ClientState clientState;

    public ConnectionStatusPanel(ClientWindow clientWindow) {
        clientState = clientWindow.getClientState();
        initObservables();
    }

    private void initObservables() {
        clientState.wrapAsDisposable(clientState.getVisibilityState$(), visibilityState -> {
            setVisible(!visibilityState.equals(VisibilityState.VISIBLE));
            setTextLabel(visibilityState.getState());
        });
    }
}
