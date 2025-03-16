package pl.polsl.screensharing.host.view.dialog;

import lombok.Getter;
import pl.polsl.screensharing.host.controller.ParticipantsController;
import pl.polsl.screensharing.host.net.ConnectedClientInfo;
import pl.polsl.screensharing.host.state.HostState;
import pl.polsl.screensharing.host.view.HostIcon;
import pl.polsl.screensharing.host.view.HostWindow;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.gui.AbstractPopupDialog;
import pl.polsl.screensharing.lib.gui.CellEditableModel;
import pl.polsl.screensharing.lib.gui.component.JAppIconButton;
import pl.polsl.screensharing.lib.icon.LibIcon;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.util.Map;

@Getter
public class ParticipantsDialogWindow extends AbstractPopupDialog {
    private final HostState hostState;

    private final JPanel tablePanel;
    private final JPanel rightPanel;
    private final JPanel rightTopButtonsPanel;
    private final JScrollPane scrollPane;

    private final JAppIconButton removeButton;
    private final JAppIconButton removeAllButton;
    private final JAppIconButton cancelButton;

    private final String[] tableHeaders = {"Thread ID", "Client IP", "Username"};

    private final JTable table;
    private final DefaultTableModel tableModel;

    private final ParticipantsController controller;

    public ParticipantsDialogWindow(HostWindow hostWindow) {
        super(AppType.HOST, 430, 200, "Participants", hostWindow, ParticipantsDialogWindow.class);
        hostState = hostWindow.getHostState();
        controller = new ParticipantsController(hostWindow, this);

        tablePanel = new JPanel();
        rightPanel = new JPanel(new BorderLayout());
        rightTopButtonsPanel = new JPanel(new GridLayout(2, 1, 0, 5));

        removeButton = new JAppIconButton("Remove", HostIcon.IN_USER_BY_OTHER_USER);
        removeAllButton = new JAppIconButton("Remove all", HostIcon.TEAM_PROJECT_COLLECTION_OFFLINE);
        cancelButton = new JAppIconButton("Cancel", LibIcon.CANCEL);

        tableModel = new CellEditableModel(new Object[][]{}, tableHeaders, 0, 2);
        table = new JTable(tableModel);

        scrollPane = new JScrollPane(table);

        initObservables();

        removeButton.addActionListener(e -> controller.removeSelectedParticipant());
        removeAllButton.addActionListener(e -> controller.removeAllParticipants());
        cancelButton.addActionListener(e -> closeWindow());

        table.getSelectionModel().addListSelectionListener(e -> controller.markupSelectedRow());
        table.getTableHeader().setReorderingAllowed(false);

        initDialogGui(true);
    }

    @Override
    protected void extendsDialog(JDialog dialog, JPanel rootPanel) {
        tablePanel.setLayout(new BoxLayout(tablePanel, BoxLayout.Y_AXIS));
        tablePanel.add(scrollPane);

        setColumnWidth(0, 20);
        setColumnWidth(1, 150);

        removeButton.setEnabled(false);

        rightTopButtonsPanel.add(removeButton);
        rightTopButtonsPanel.add(removeAllButton);

        rightPanel.add(rightTopButtonsPanel, BorderLayout.NORTH);
        rightPanel.add(cancelButton, BorderLayout.SOUTH);

        rootPanel.add(tablePanel, BorderLayout.CENTER);
        rootPanel.add(rightPanel, BorderLayout.EAST);
    }

    private void setColumnWidth(int index, int width) {
        table.getColumnModel().getColumn(index).setMinWidth(width);
    }

    private void initObservables() {
        hostState.wrapAsDisposable(hostState.getConnectedClientsInfo$(), clientsInfo -> {
            final DefaultTableModel model = (DefaultTableModel) table.getModel();
            model.setRowCount(0);
            for (final Map.Entry<Long, ConnectedClientInfo> clientInfoEntry : clientsInfo.entrySet()) {
                final ConnectedClientInfo clientInfo = clientInfoEntry.getValue();
                model.addRow(new Object[]{
                    clientInfoEntry.getKey(),
                    clientInfo.getIpAddress() + ":" + clientInfo.getUdpPort(),
                    clientInfo.getUsername()
                });
            }
            removeAllButton.setEnabled(!clientsInfo.isEmpty());
        });
    }
}
