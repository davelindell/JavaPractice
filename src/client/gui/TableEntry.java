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

import javax.swing.BoxLayout;
import javax.swing.JColorChooser;
import javax.swing.JComponent;
import javax.swing.JDialog;
import javax.swing.JMenuItem;
import javax.swing.JPanel;
import javax.swing.JPopupMenu;
import javax.swing.JTabbedPane;
import javax.swing.JTable;
import javax.swing.JTextField;
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
	
	private JPopupMenu popup_menu;
	private JMenuItem menu_item;
	private int suggestion_row;
	private int suggestion_column;
	
	public TableEntry(BatchState batch_state) {
		table_model = new TableModel(batch_state);
		table_model.addTableModelListener(this);

		popup_menu = new JPopupMenu();
		menu_item = new JMenuItem("See Suggestions");
		popup_menu.add(menu_item);
		menu_item.addActionListener(suggestions_listener);

		suggestion_row = 0;
		suggestion_column = 0;
		
		this.addMouseListener(mouse_adapter);
		this.batch_state = batch_state;
		
		batch_state.addListener(batch_state_listener);
		createComponents();
	}
	
	private void createComponents() {
		this.setPreferredSize(new Dimension(600, 200));
		this.setLayout(new BoxLayout(this, BoxLayout.LINE_AXIS));
		
	}
	
	private MouseAdapter mouse_adapter = new MouseAdapter() {

		@Override
		public void mousePressed(MouseEvent e) {

			if (e.isPopupTrigger()) {
				
				final int row = table.rowAtPoint(e.getPoint());
				final int column = table.columnAtPoint(e.getPoint());
				
				if (row >= 0 && row < table_model.getRowCount() &&
						column >= 1 && column < table_model.getColumnCount()) {
					if (!batch_state.isQuality(row, column)) {
						suggestion_row = row;
						suggestion_column = column;
						popup_menu.show(e.getComponent(), e.getX(), e.getY());
					}
				}
			}
		}
		
	};
	
	private ActionListener suggestions_listener = new ActionListener() {
		@Override
		public void actionPerformed(ActionEvent e) {
			
			SuggestionsDialog suggestions_dialog = 
					new SuggestionsDialog(batch_state, suggestion_row, suggestion_column);
			suggestions_dialog.setVisible(true);
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
		
		@Override
		public void fireChangeSelectedEntry(int row, int column) {
				table.changeSelection(row, column, false, false);
		}
		
		@Override
		public void fireEnteredData(int row, int column) {
			table.tableChanged(new TableModelEvent(table.getModel(), row, column));
		}
		
		@Override
		public void fireSubmitBatch() {
			TableEntry.this.removeAll();
			TableEntry.this.revalidate();
			TableEntry.this.repaint();
		}
		
	};
	
}
