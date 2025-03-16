package pl.polsl.screensharing.host.view.fragment;

import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.gui.AbstractTextInfoPanel;

public class DisableScreenPanel extends AbstractTextInfoPanel {
    private final HostState hostState;

    public DisableScreenPanel(HostWindow hostWindow) {
        super("Showing screen is disabled");
        hostState = hostWindow.getHostState();
        initObservables();
    }

    private void initObservables() {
        hostState.wrapAsDisposable(hostState.isScreenIsShowForParticipants$(), isShowing -> {
            setVisible(!isShowing);
        });
    }
}
