/*
 * Copyright (c) 2023 by MULTIPLE AUTHORS
 * Part of the CS study course project.
 */
package pl.polsl.screensharing.client.view.dialog;

import pl.polsl.screensharing.client.controller.LastConnectionsController;
import pl.polsl.screensharing.client.view.ClientWindow;
import pl.polsl.screensharing.lib.AppIcon;
import pl.polsl.screensharing.lib.AppType;
import pl.polsl.screensharing.lib.gui.AbstractPopupDialog;
import pl.polsl.screensharing.lib.gui.component.JAppIconButton;
import pl.polsl.screensharing.lib.gui.input.AppCellEditor;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LastConnectionsWindow extends AbstractPopupDialog {
    private final JPanel rightPanel;
    private final JScrollPane scrollPane;

    private final LastConnectionsController controller;

    private final JAppIconButton estabilishedConnButton;
    private final JAppIconButton cancelButton;
    private final JAppIconButton removeRowButton;
    private final JAppIconButton removeAllRowsButton;

    private final String[] tableHeaders = { "IP address", "Port", "Username", "Description" };

    private final JTable table;
    private final DefaultTableModel tableModel;
    private final Object[][] tableData;

    public LastConnectionsWindow(ClientWindow clientWindow) {
        super(AppType.HOST, 650, 210, "Last connections", clientWindow, LastConnectionsWindow.class);
        this.controller = new LastConnectionsController(clientWindow, this);

        this.rightPanel = new JPanel(new GridLayout(5, 1, 5, 5));

        this.tableData = clientWindow.getCurrentState().getParsedLastConnectionsList();
        this.tableModel = new DefaultTableModel(tableData, tableHeaders);
        this.table = new JTable(tableModel);

        this.scrollPane = new JScrollPane(table);

        this.estabilishedConnButton = new JAppIconButton("Connect", AppIcon.CONNECT_TO_REMOTE_SERVER);
        this.removeRowButton = new JAppIconButton("Remove", AppIcon.DELETE_CLAUSE);
        this.removeAllRowsButton = new JAppIconButton("Remove all", AppIcon.DELETE_TABLE);
        this.cancelButton = new JAppIconButton("Cancel", AppIcon.CANCEL);

        this.estabilishedConnButton.addActionListener(e -> controller.estabilishedConnection());
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

        estabilishedConnButton.setEnabled(false);
        removeRowButton.setEnabled(false);

        rightPanel.add(estabilishedConnButton);
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

    public JTable getTable() {
        return table;
    }

    public JButton getEstabilishedConnButton() {
        return estabilishedConnButton;
    }

    public JButton getRemoveRowButton() {
        return removeRowButton;
    }
}
