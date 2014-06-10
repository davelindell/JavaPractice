package client.gui;

import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.event.TableModelEvent;
import javax.swing.table.AbstractTableModel;

import shared.models.IndexedData;
import client.synchronizer.BatchState;
import client.synchronizer.BatchStateListenerAdapter;
import client.synchronizer.TableModelListener;

public class TableModel extends AbstractTableModel {
	private BatchState batch_state;

	public TableModel(BatchState batch_state) {
		super();
		this.batch_state = batch_state;
		batch_state.addListener(batch_state_listener);
	}
	
	@Override
	public int getRowCount() {
		return batch_state.getNumRecords() + 1;
	}

	@Override
	public int getColumnCount() {
		return batch_state.getNumFields() + 1;
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
	
	private BatchStateListenerAdapter batch_state_listener = new BatchStateListenerAdapter() {
		@Override
		public void fireDownloadBatch(BufferedImage batch_image) {
			TableModelEvent e = new TableModelEvent(TableModel.this);
			TableModel.this.fireTableChanged(e);
			
		}

	};
	
}
