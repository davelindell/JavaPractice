package client.gui;

import java.awt.Dimension;

import javax.swing.BoxLayout;
import javax.swing.JEditorPane;
import javax.swing.JPanel;

import client.synchronizer.BatchState;
import client.synchronizer.BatchStateListenerAdapter;

@SuppressWarnings("serial")
public class FieldHelp extends JPanel {
	private BatchState batch_state;
	private JEditorPane text_field;
	
	public FieldHelp(BatchState batch_state) {
		this.batch_state = batch_state;
		batch_state.addListener(batch_state_listener);
		createComponents();
	}
	
	private void createComponents() {
		this.setPreferredSize(new Dimension(600, 200));
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		this.setVisible(true);
	}
	
	
	private BatchStateListenerAdapter batch_state_listener = new BatchStateListenerAdapter() {
		@Override
		public void fireChangeSelectedEntry(int row, int column) {
			// adjust to table coordinates
			if (column == 0)
				column = 1;
			FieldHelp.this.removeAll();
			String field_help = batch_state.getFieldHelps().get(column - 1);
			text_field = new JEditorPane("text/html",field_help);
			text_field.setPreferredSize(new Dimension(600, 200));
			text_field.setEditable(false);
			FieldHelp.this.add(text_field);
			FieldHelp.this.revalidate();
			repaint();

		}
		
		@Override
		public void fireSubmitBatch() {
			FieldHelp.this.removeAll();
			FieldHelp.this.revalidate();
			FieldHelp.this.repaint();
		}
		
	};

}
