/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client;

import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.client.net.StartNet;
import pl.polsl.screensharing.client.state.ClientState;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.lib.gui.AbstractGUIThread;
import pl.polsl.screensharing.lib.gui.AbstractRootFrame;

@Slf4j
public class ClientMain extends AbstractGUIThread {
    public ClientMain(AbstractRootFrame frame) {
        super(frame);
    }

    public static void main(String[] args) {
        final ClientState clientState = new ClientState();
        final ClientMain clientMain = new ClientMain(new ClientWindow(clientState));
        clientMain.init();

        // wątek klienta, do testów
        final Thread thread = new Thread(new StartNet());
        thread.start();
    }

    @Override
    protected void createThreadSaveRootFrame(AbstractRootFrame frame) {
        frame.guiInitAndShow();
    }
}
