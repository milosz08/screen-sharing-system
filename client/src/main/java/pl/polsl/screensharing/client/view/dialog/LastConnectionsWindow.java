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

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;

public class LastConnectionsWindow extends AbstractPopupDialog {
    private final JPanel rightPanel;
    private final JScrollPane scrollPane;

    private final DefaultTableModel tableModel;
    private final JTable table;

    private final JAppIconButton estabilishedConnButton;
    private final JAppIconButton cancelButton;
    private final JAppIconButton removeRowButton;
    private final JAppIconButton removeAllRowsButton;

    private final LastConnectionsController controller;

    private final String[] tableHeaders = { "IP address", "Port", "Description" };
    private final Object[][] dane = {
        { "127.0.0.1", "9091", "This is a sample description" },
        { "127.0.0.1", "9094", "This is a another sample description" },
    };

    public LastConnectionsWindow(ClientWindow clientWindow) {
        super(AppType.HOST, 620, 210, "Last connections", clientWindow, LastConnectionsWindow.class);
        this.controller = new LastConnectionsController(this);

        this.rightPanel = new JPanel(new GridLayout(5, 1, 5, 5));

        this.tableModel = new DefaultTableModel(dane, tableHeaders);
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
        this.table.getSelectionModel().addListSelectionListener(e -> controller.markupSelectedRow());

        initDialogGui(false);
    }

    @Override
    protected void extendsDialog(JDialog dialog, JPanel rootPanel) {
        setColumnWidth(0, 100);
        setColumnWidth(1, 60);

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
