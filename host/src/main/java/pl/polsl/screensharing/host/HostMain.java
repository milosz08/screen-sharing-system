package pl.polsl.screensharing.host;

import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.UnoperableException;
import pl.polsl.screensharing.lib.gui.AbstractGUIThread;
import pl.polsl.screensharing.lib.gui.AbstractRootFrame;

@Slf4j
public class HostMain extends AbstractGUIThread<HostState> {
    public HostMain(HostState state) {
        super(state);
    }

    public static void main(String[] args) {
        try {
            final HostState state = new HostState();
            final HostMain hostMain = new HostMain(state);
            hostMain.init();
        } catch (UnoperableException ex) {
            log.error(ex.getMessage());
        }
    }

    @Override
    protected void createThreadSaveRootFrame(HostState state) {
        final AbstractRootFrame frame = new HostWindow(state);
        frame.guiInitAndShow();
    }
}
