package pl.polsl.screensharing.host.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import pl.polsl.screensharing.host.net.ConnectedClientInfo;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.host.view.dialog.ParticipantsDialogWindow;
import pl.polsl.screensharing.lib.net.SocketState;

import javax.swing.*;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Slf4j
@RequiredArgsConstructor
public class ParticipantsController {
    private final HostWindow hostWindow;
    private final ParticipantsDialogWindow participantsDialogWindow;

    public void removeSelectedParticipant() {
        final JTable table = participantsDialogWindow.getTable();
        final HostState hostState = hostWindow.getHostState();

        final int selectedRow = table.getSelectedRow();

        final String clientIp = getTableValue(1);
        final String clientUsername = getTableValue(2);

        final int result = JOptionPane.showConfirmDialog(participantsDialogWindow,
            String.format("Are you sure to kick user with IP: %s and username: %s?", clientIp, clientUsername),
            "Please confirm", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (result != JOptionPane.YES_OPTION) {
            return;
        }
        final ConcurrentMap<Long, ConnectedClientInfo> sessionParticipants = hostState.getLastEmittedConnectedClients();
        if (selectedRow < 0 || selectedRow >= sessionParticipants.size()) {
            return;
        }
        final Long threadId = (long) table.getModel().getValueAt(selectedRow, 0);
        final Optional<Long> removedOptional = sessionParticipants.keySet().stream()
            .filter(connectedClientInfo -> connectedClientInfo.equals(threadId))
            .findFirst();
        if (!removedOptional.isPresent()) {
            return;
        }
        final Long removed = removedOptional.get();
        hostWindow.getServerTcpSocket().sendSignalToClient(SocketState.KICK_FROM_SESSION, removed);

        sessionParticipants.remove(removed);
        hostState.updateConnectedClients(sessionParticipants);

        log.info("Removed last connection: {}.", removed);
        log.info("Reorganized rows: {}.", sessionParticipants);
    }

    public void removeAllParticipants() {
        final HostState clientState = hostWindow.getHostState();
        final int participantsSize = clientState.getLastEmittedConnectedClients().size();

        final int result = JOptionPane.showConfirmDialog(participantsDialogWindow,
            String.format("Are you sure to kick all %s participants?", participantsSize),
            "Please confirm", JOptionPane.YES_NO_CANCEL_OPTION, JOptionPane.QUESTION_MESSAGE);

        if (result != JOptionPane.YES_OPTION) {
            return;
        }
        hostWindow.getServerTcpSocket().sendSignalToAllClients(SocketState.KICK_FROM_SESSION);
        clientState.updateConnectedClients(new ConcurrentHashMap<>());
        log.info("Kicked all {} participants.", participantsSize);
    }

    public void markupSelectedRow() {
        final JTable table = participantsDialogWindow.getTable();
        final boolean isActive = table.getSelectedRow() != -1;
        participantsDialogWindow.getRemoveButton().setEnabled(isActive);
    }

    private String getTableValue(int col) {
        final JTable table = participantsDialogWindow.getTable();
        return table.getValueAt(table.getSelectedRow(), col).toString();
    }
}
