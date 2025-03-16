package pl.polsl.screensharing.lib.gui;

import javax.swing.table.DefaultTableModel;

public class CellEditableModel extends DefaultTableModel {
    private final int colStartDisabled;
    private final int colEndDisabled;

    public CellEditableModel(Object[][] data, Object[] columnNames, int colStartDisabled, int colEndDisabled) {
        super(data, columnNames);
        this.colStartDisabled = colStartDisabled;
        this.colEndDisabled = colEndDisabled;
    }

    @Override
    public boolean isCellEditable(int row, int column) {
        return !(column >= colStartDisabled && column <= colEndDisabled);
    }
}
