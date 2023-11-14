/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host;

import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.host.net.StartNet;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.gui.AbstractGUIThread;
import pl.polsl.screensharing.lib.gui.AbstractRootFrame;

@Slf4j
public class HostMain extends AbstractGUIThread {
    public HostMain(AbstractRootFrame frame) {
        super(frame);
    }

    public static void main(String[] args) {
        final HostState state = new HostState();
        final HostMain hostMain = new HostMain(new HostWindow(state));
        hostMain.init();

        // wątek serwer, do testów
        final Thread thread = new Thread(new StartNet());
        thread.start();
    }

    @Override
    protected void createThreadSaveRootFrame(AbstractRootFrame frame) {
        frame.guiInitAndShow();
    }
}
