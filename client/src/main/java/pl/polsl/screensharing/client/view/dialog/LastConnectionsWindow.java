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
import pl.polsl.screensharing.lib.gui.CellEditableModel;
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
    private final JPanel rightTopPanel;
    private final JScrollPane scrollPane;

    private final LastConnectionsController controller;

    private final JAppIconButton connectButton;
    private final JAppIconButton cancelButton;
    private final JAppIconButton removeRowButton;
    private final JAppIconButton removeAllRowsButton;

    private final String[] tableHeaders = { "Host", "Client", "Username", "Description" };

    private final JTable table;
    private final DefaultTableModel tableModel;

    public LastConnectionsWindow(ClientWindow clientWindow) {
        super(AppType.HOST, 850, 210, "Last connections", clientWindow, LastConnectionsWindow.class);
        clientState = clientWindow.getClientState();
        controller = new LastConnectionsController(clientWindow, this);

        rightPanel = new JPanel(new BorderLayout());
        rightTopPanel = new JPanel(new GridLayout(3, 1, 0, 5));

        tableModel = new CellEditableModel(new Object[][]{}, tableHeaders, 0, 1);
        table = new JTable(tableModel);

        scrollPane = new JScrollPane(table);

        connectButton = new JAppIconButton("Connect", ClientIcon.CONNECT_TO_REMOTE_SERVER);
        removeRowButton = new JAppIconButton("Remove", LibIcon.DELETE_CLAUSE);
        removeAllRowsButton = new JAppIconButton("Remove all", LibIcon.DELETE_TABLE);
        cancelButton = new JAppIconButton("Cancel", LibIcon.CANCEL);

        initObservables();

        connectButton.addActionListener(e -> controller.createConnection());
        cancelButton.addActionListener(e -> closeWindow());
        removeRowButton.addActionListener(e -> controller.removeSelectedRow());
        removeAllRowsButton.addActionListener(e -> controller.removeAllRows());

        AppCellEditor.setCellEditor(0, 23, "^\\s*(.*?):(\\d+)\\s*$", table);
        AppCellEditor.setCellEditor(1, 23, "^\\s*(.*?):(\\d+)\\s*$", table);
        AppCellEditor.setCellEditor(2, 40, "^[0-9a-zA-Z]+$", table);
        AppCellEditor.setCellEditor(3, 100, table);

        table.getSelectionModel().addListSelectionListener(e -> controller.markupSelectedRow());
        table.addPropertyChangeListener(controller::updateLastConnectionsData);
        table.getTableHeader().setReorderingAllowed(false);

        initDialogGui(false);
    }

    @Override
    protected void extendsDialog(JDialog dialog, JPanel rootPanel) {
        setColumnWidth(0, 150);
        setColumnWidth(1, 150);
        setColumnWidth(2, 100);

        connectButton.setEnabled(false);
        removeRowButton.setEnabled(false);

        rightTopPanel.add(connectButton);
        rightTopPanel.add(removeRowButton);
        rightTopPanel.add(removeAllRowsButton);

        rightPanel.add(rightTopPanel, BorderLayout.NORTH);
        rightPanel.add(cancelButton, BorderLayout.SOUTH);

        rootPanel.add(scrollPane, BorderLayout.CENTER);
        rootPanel.add(rightPanel, BorderLayout.EAST);
    }

    private void setColumnWidth(int index, int width) {
        table.getColumnModel().getColumn(index).setMinWidth(width);
    }

    private void initObservables() {
        clientState.wrapAsDisposable(clientState.getSavedConnections$(), savedConnections -> {
            final DefaultTableModel model = (DefaultTableModel) table.getModel();
            for (int i = 0; i < model.getRowCount(); i++) {
                model.removeRow(i);
            }
            for (final SavedConnection savedConnection : savedConnections) {
                model.addRow(new Object[]{
                    savedConnection.getHostIpAddress() + ":" + savedConnection.getHostPort(),
                    savedConnection.getClientIpAddress() + ":" + savedConnection.getClientPort(),
                    savedConnection.getUsername(),
                    savedConnection.getDescription()
                });
            }
        });
    }
}
