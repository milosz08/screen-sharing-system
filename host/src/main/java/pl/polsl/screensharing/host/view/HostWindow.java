/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.host.view;

import lombok.Getter;
import lombok.Setter;
import pl.polsl.screensharing.host.controller.BottomInfobarController;
import pl.polsl.screensharing.host.net.ServerDatagramSocket;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.state.StreamingState;
import pl.polsl.screensharing.host.view.dialog.AboutDialogWindow;
import pl.polsl.screensharing.host.view.dialog.ConnectionSettingsDialogWindow;
import pl.polsl.screensharing.host.view.dialog.LicenseDialogWindow;
import pl.polsl.screensharing.host.view.dialog.ParticipantsDialogWindow;
import pl.polsl.screensharing.host.view.fragment.BottomInfobar;
import pl.polsl.screensharing.host.view.fragment.TopMenuBar;
import pl.polsl.screensharing.host.view.fragment.TopToolbar;
import pl.polsl.screensharing.host.view.fragment.VideoCanvas;
import pl.polsl.screensharing.host.view.tabbed.TabbedPaneWindow;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.gui.AbstractRootFrame;
import pl.polsl.screensharing.lib.gui.file.FileUtils;

import javax.swing.*;
import java.awt.*;
import java.util.Optional;

@Getter
public class HostWindow extends AbstractRootFrame {
    private final HostState hostState;
    private final Optional<Image> streamingImageIconOptional;

    private final TopMenuBar topMenuBar;
    private final TopToolbar topToolbar;
    private final TabbedPaneWindow tabbedPaneWindow;
    private final BottomInfobar bottomInfobar;

    private final AboutDialogWindow aboutDialogWindow;
    private final LicenseDialogWindow licenseDialogWindow;
    private final ConnectionSettingsDialogWindow connectionSettingsDialogWindow;
    private final ParticipantsDialogWindow participantsDialogWindow;

    @Setter
    private ServerDatagramSocket serverDatagramSocket;

    public HostWindow(HostState hostState) {
        super(AppType.HOST, hostState, HostWindow.class);
        this.hostState = hostState;
        streamingImageIconOptional = FileUtils.getImageFileFromResources(getClass(), "HostIconStreaming.png");

        topMenuBar = new TopMenuBar(this);
        topToolbar = new TopToolbar(this);
        tabbedPaneWindow = new TabbedPaneWindow(this);
        bottomInfobar = new BottomInfobar(this);

        aboutDialogWindow = new AboutDialogWindow(this);
        licenseDialogWindow = new LicenseDialogWindow(this);
        connectionSettingsDialogWindow = new ConnectionSettingsDialogWindow(this);
        participantsDialogWindow = new ParticipantsDialogWindow(this);


        initObservables();

        setResizable(false);
        setMaximumSize(AppType.HOST.getRootWindowSize());
    }

    @Override
    protected void extendsFrame(JFrame frame, JPanel rootPanel) {
        frame.setJMenuBar(topMenuBar);
        frame.add(topToolbar, BorderLayout.NORTH);
        frame.add(tabbedPaneWindow, BorderLayout.CENTER);
        frame.add(bottomInfobar, BorderLayout.SOUTH);
    }

    public void initObservables() {
        hostState.wrapAsDisposable(hostState.getStreamingState$(), streamingState -> {
            if (streamingState.equals(StreamingState.STREAMING)) {
                streamingImageIconOptional.ifPresent(this::setIconImage);
            } else {
                imageIconOptional.ifPresent(this::setIconImage);
            }
        });
    }

    public BottomInfobarController getBottomInfobarController() {
        return bottomInfobar.getBottomInfobarController();
    }

    public VideoCanvas getVideoCanvas() {
        return tabbedPaneWindow.getTabbedScreenFramePanel().getVideoCanvas();
    }
}
