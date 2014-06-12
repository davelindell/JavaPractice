package client.gui;

import java.awt.Dimension;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JEditorPane;
import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

import shared.models.IndexedData;
import client.synchronizer.BatchState;
import client.synchronizer.BatchStateListenerAdapter;

public class TableModel extends AbstractTableModel {
	private BatchState batch_state;

	public TableModel(BatchState batch_state) {
		super();
		this.batch_state = batch_state;
	}
	
	@Override
	public int getRowCount() {
		return batch_state.getNumRecords();
	}

	@Override
	public int getColumnCount() {
		return batch_state.getNumFields() + 1;
	}
	
	@Override
	public boolean isCellEditable(int row, int column) {
		if (column == 0)
			return false;
		else
			return true;
	}

	@Override
	public Object getValueAt(int row_index, int column_index) {
		Object result = null;
		if (column_index == 0)
			result = row_index + 1;
		else
			result = batch_state.getRecords().get(row_index).get(column_index - 1).getRecord_value();
		
		return result;
	}
	
	@Override
	public String getColumnName(int column) {

		String result = null;

		if (column >= 0 && column < getColumnCount()) {

			if (column == 0)
				result = "Record Number";
			else 
				result = batch_state.getFields().get(column - 1).getField_title();
			
		} else {
			throw new IndexOutOfBoundsException();
		}

		return result;
	}
	
	@Override
	public void setValueAt(Object value, int row, int column) {
		
		if (row >= 0 && row < getRowCount() && column >= 0
				&& column < getColumnCount()) {
			batch_state.getRecords().get(row).get(column - 1).setRecord_value((String)value);
			
			this.fireTableCellUpdated(row, column);
			
		} else {
			throw new IndexOutOfBoundsException();
		}		
	}

	
}
