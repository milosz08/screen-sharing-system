/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.view.dialog;

import lombok.Getter;
import pl.polsl.screensharing.client.controller.LastConnectionsController;
import pl.polsl.screensharing.client.model.SavedConnection;
import pl.polsl.screensharing.client.state.ClientState;
import pl.polsl.screensharing.client.view.ClientIcon;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.gui.AbstractPopupDialog;
import pl.polsl.screensharing.lib.gui.component.JAppIconButton;
import pl.polsl.screensharing.lib.gui.icon.LibIcon;
import pl.polsl.screensharing.lib.gui.input.AppCellEditor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

@Getter
public class LastConnectionsWindow extends AbstractPopupDialog {
    private final ClientState clientState;

    private final JPanel rightPanel;
    private final JScrollPane scrollPane;

    private final LastConnectionsController controller;

    private final JAppIconButton connectButton;
    private final JAppIconButton cancelButton;
    private final JAppIconButton removeRowButton;
    private final JAppIconButton removeAllRowsButton;

    private final String[] tableHeaders = { "IP address", "Port", "Username", "Description" };

    private final JTable table;
    private final DefaultTableModel tableModel;

    public LastConnectionsWindow(ClientWindow clientWindow) {
        super(AppType.HOST, 650, 210, "Last connections", clientWindow, LastConnectionsWindow.class);
        this.clientState = clientWindow.getClientState();
        this.controller = new LastConnectionsController(clientWindow, this);

        this.rightPanel = new JPanel(new GridLayout(5, 1, 5, 5));

        this.tableModel = new DefaultTableModel(new Object[][]{}, tableHeaders);
        this.table = new JTable(tableModel);

        this.scrollPane = new JScrollPane(table);

        this.connectButton = new JAppIconButton("Connect", ClientIcon.CONNECT_TO_REMOTE_SERVER);
        this.removeRowButton = new JAppIconButton("Remove", LibIcon.DELETE_CLAUSE);
        this.removeAllRowsButton = new JAppIconButton("Remove all", LibIcon.DELETE_TABLE);
        this.cancelButton = new JAppIconButton("Cancel", LibIcon.CANCEL);

        initObservables();

        this.connectButton.addActionListener(e -> controller.createConnection());
        this.cancelButton.addActionListener(e -> closeWindow());
        this.removeRowButton.addActionListener(e -> controller.removeSelectedRow());
        this.removeAllRowsButton.addActionListener(e -> controller.removeAllRows());

        AppCellEditor.setCellEditor(0, 15, "^[0-9.]+$", table);
        AppCellEditor.setCellEditor(1, 6, "^[0-9]+$", table);
        AppCellEditor.setCellEditor(2, 40, "^[0-9a-zA-Z]+$", table);
        AppCellEditor.setCellEditor(3, 100, table);

        this.table.getSelectionModel().addListSelectionListener(e -> controller.markupSelectedRow());
        this.table.addPropertyChangeListener(controller::updateLastConnectionsData);

        initDialogGui(false);
    }

    @Override
    protected void extendsDialog(JDialog dialog, JPanel rootPanel) {
        setColumnWidth(0, 100);
        setColumnWidth(1, 60);
        setColumnWidth(2, 100);

        connectButton.setEnabled(false);
        removeRowButton.setEnabled(false);

        rightPanel.add(connectButton);
        rightPanel.add(removeRowButton);
        rightPanel.add(removeAllRowsButton);
        rightPanel.add(new JPanel());
        rightPanel.add(cancelButton);

        rootPanel.add(scrollPane, BorderLayout.CENTER);
        rootPanel.add(rightPanel, BorderLayout.EAST);
    }

    private void setColumnWidth(int index, int width) {
        table.getColumnModel().getColumn(index).setMaxWidth(width);
    }

    private void initObservables() {
        clientState.wrapAsDisposable(clientState.getSavedConnections$(), savedConnections -> {
            final DefaultTableModel model = (DefaultTableModel) table.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                model.removeRow(i);
            }
            for (final SavedConnection savedConnection : savedConnections) {
                model.addRow(new Object[]{
                    savedConnection.getIpAddress(),
                    savedConnection.getPort(),
                    savedConnection.getUsername(),
                    savedConnection.getDescription()
                });
            }
        });
    }
}
