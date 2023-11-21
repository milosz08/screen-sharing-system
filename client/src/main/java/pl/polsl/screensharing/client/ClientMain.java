/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client;

import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.client.state.ClientState;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.lib.UnoperableException;
import pl.polsl.screensharing.lib.gui.AbstractGUIThread;
import pl.polsl.screensharing.lib.gui.AbstractRootFrame;

@Slf4j
public class ClientMain extends AbstractGUIThread<ClientState> {
    public ClientMain(ClientState state) {
        super(state);
    }

    public static void main(String[] args) {
        try {
            final ClientState state = new ClientState();
            final ClientMain clientMain = new ClientMain(state);
            clientMain.init();
        } catch (UnoperableException ex) {
            log.error(ex.getMessage());
        }
    }

    @Override
    protected void createThreadSaveRootFrame(ClientState state) {
        final AbstractRootFrame frame = new ClientWindow(state);
        frame.guiInitAndShow();
    }
}
