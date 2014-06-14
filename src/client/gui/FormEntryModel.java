package client.gui;

import javax.swing.AbstractListModel;
import javax.swing.ListModel;
import javax.swing.event.ListDataListener;

import client.synchronizer.BatchState;

public class FormEntryModel extends AbstractListModel<Integer> {
	private BatchState batch_state;
	
	FormEntryModel(BatchState batch_state) {
		this.batch_state = batch_state;
	}

	@Override
	public Integer getElementAt(int arg0) {
		return arg0 + 1;
	}

	@Override
	public int getSize() {
		return batch_state.getNumRecords();
	}


}
