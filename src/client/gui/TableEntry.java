package client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.image.BufferedImage;

import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.event.TableModelEvent;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import client.synchronizer.BatchState;
import client.synchronizer.BatchStateListenerAdapter;

public class TableEntry extends JPanel implements javax.swing.event.TableModelListener {
	private TableModel table_model;
	private JTable table;
	private BatchState batch_state;
	
	public TableEntry(BatchState batch_state) {
		table_model = new TableModel(batch_state);
		this.batch_state = batch_state;
		table_model.addTableModelListener(this);
		batch_state.addListener(batch_state_listener);
		createComponents();
	}
	
	private void createComponents() {
		this.setPreferredSize(new Dimension(600, 200));

		
	}

	
	

	
	
	private MouseAdapter mouse_adapter = new MouseAdapter() {

		@Override
		public void mouseReleased(MouseEvent e) {

			if (e.isPopupTrigger()) {
				
				final int row = table.rowAtPoint(e.getPoint());
				final int column = table.columnAtPoint(e.getPoint());
				
				if (row >= 0 && row < table_model.getRowCount() &&
						column >= 1 && column < table_model.getColumnCount()) {
										
				}
			}
		}
		
	};

	@Override
	public void tableChanged(TableModelEvent e) {		
		TableColumnModel columnModel = table.getColumnModel();		
		
		for (int i = 0; i < table_model.getColumnCount(); ++i) {
			TableColumn column = columnModel.getColumn(i);
			column.setPreferredWidth(100);
		}		
		for (int i = 0; i < table_model.getColumnCount(); ++i) {
			TableColumn column = columnModel.getColumn(i);
			column.setCellRenderer(new TableEntryCellRenderer(batch_state));
		}	
	}
	
	private BatchStateListenerAdapter batch_state_listener = new BatchStateListenerAdapter() {
		@Override
		public void fireDownloadBatch(BufferedImage batch_image) {
			TableEntry.this.removeAll();
			table = new JTable(table_model);
			table.setRowHeight(15);
			table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
			table.setCellSelectionEnabled(true);
			table.getTableHeader().setReorderingAllowed(false);
			table.addMouseListener(mouse_adapter);
			table.setIntercellSpacing(new Dimension(0,0));
			table.setGridColor(Color.BLACK);
			table.setFont(table.getFont().deriveFont(Font.PLAIN));
			
			TableColumnModel columnModel = table.getColumnModel();		
			
			for (int i = 0; i < table_model.getColumnCount(); ++i) {
				TableColumn column = columnModel.getColumn(i);
				column.setPreferredWidth(100);
			}		
			for (int i = 0; i < table_model.getColumnCount(); ++i) {
				TableColumn column = columnModel.getColumn(i);
				column.setCellRenderer(new TableEntryCellRenderer(batch_state));
				// column.setCellEditor(new TableEntryCellRenderer());
			}	
			
			JPanel root_panel = new JPanel(new BorderLayout());
			root_panel.add(table.getTableHeader(), BorderLayout.NORTH);
			root_panel.add(table, BorderLayout.CENTER);

			TableEntry.this.add(root_panel);
		}

	};
	
}
