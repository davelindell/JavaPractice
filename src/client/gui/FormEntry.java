package client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.GridBagConstraints;
import java.awt.GridBagLayout;
import java.awt.Insets;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.image.BufferedImage;
import java.util.ArrayList;
import java.util.List;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JSeparator;
import javax.swing.JSplitPane;
import javax.swing.JTable;
import javax.swing.JTextField;
import javax.swing.ListModel;
import javax.swing.ListSelectionModel;
import javax.swing.SwingConstants;
import javax.swing.UIDefaults;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import shared.communication.ValidateUser_Params;
import shared.communication.ValidateUser_Result;
import shared.models.IndexedData;
import client.ClientException;
import client.synchronizer.BatchState;
import client.synchronizer.BatchStateListenerAdapter;

public class FormEntry extends JPanel {
	private BatchState batch_state;
	private FormEntryModel form_model;
	private JList<Integer> row_list;
	private List<JLabel> field_labels;
	private List<JTextField> text_fields;
	private JPanel west_side;
	private JPanel east_side;
	private FormEntryCellRenderer form_renderer;
	
	public FormEntry(BatchState batch_state) {
		this.batch_state = batch_state;
		batch_state.addListener(batch_state_listener);
		form_model = new FormEntryModel(batch_state);
		form_renderer = new FormEntryCellRenderer(batch_state);
		
		this.setPreferredSize(new Dimension(600, 200));
		this.setLayout(new BoxLayout(this, BoxLayout.X_AXIS));
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		int cur_row = batch_state.getCurRow();
		if (!batch_state.getRecords().isEmpty()) {
			for (int i = 0; i < text_fields.size(); ++i) {
				IndexedData cur_entry = batch_state.getRecords().get(cur_row).get(i);
				text_fields.get(i).setText(cur_entry.getRecord_value());
			}
		}		
	}
	
	private BatchStateListenerAdapter batch_state_listener = new BatchStateListenerAdapter() {
		@Override
		public void fireDownloadBatch(BufferedImage batch_image) {
			FormEntry.this.removeAll();
			FormEntry.this.form_model = new FormEntryModel(batch_state);
			
			west_side = new JPanel();
			east_side = new JPanel();
			
			east_side.setLayout(new GridBagLayout());
			GridBagConstraints east_gbc = new GridBagConstraints();

			west_side.setLayout(new GridBagLayout());
			GridBagConstraints west_gbc = new GridBagConstraints();
			
			row_list = new JList<Integer>();
			row_list.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);  
			row_list.setCellRenderer(form_renderer);
			row_list.setModel(form_model);
			
			field_labels = new ArrayList<JLabel>();
			text_fields = new ArrayList<JTextField>();
			
			east_gbc.anchor = GridBagConstraints.NORTHWEST;
			east_gbc.weightx = 1;
			east_gbc.weighty = 1;
			
			for (int i = 0; i < batch_state.getNumFields(); ++i) {
				JLabel cur_field_label = new JLabel(batch_state.getFields().get(i).getField_title());
				cur_field_label.setHorizontalAlignment(SwingConstants.LEFT);
				JTextField cur_text_field = new JTextField(12);
				cur_text_field.setMinimumSize(cur_text_field.getPreferredSize());
				cur_text_field.addFocusListener(text_field_listener);
				
				field_labels.add(cur_field_label);
				text_fields.add(cur_text_field);
				
				east_gbc.gridx = 0;
				east_gbc.gridy = i;
				east_gbc.anchor = GridBagConstraints.WEST;
				east_gbc.insets = new Insets(10,10,10,10);
				east_side.add(cur_field_label, east_gbc);
				east_gbc.gridx = 1;
				east_gbc.gridy = i;
				east_gbc.insets = new Insets(10,10,10,10);
				east_side.add(cur_text_field, east_gbc);
			} 
			
			UIDefaults defaults = javax.swing.UIManager.getDefaults();
			west_gbc.anchor = GridBagConstraints.NORTHWEST;
			west_gbc.weightx = 1;
			west_gbc.weighty = 1;
			west_gbc.gridx = 0;
			west_gbc.gridy = 0;
			west_side.add(row_list, west_gbc);
			
			west_side.setPreferredSize(new Dimension(50, 200));
			west_side.setBackground(Color.white);
			west_side.setBorder(BorderFactory.createLineBorder(defaults.getColor("List.selectionBackground")));
			east_side.setPreferredSize(new Dimension(300, 200));
			east_side.setBorder(BorderFactory.createLineBorder(defaults.getColor("List.selectionBackground")));

			JScrollPane west_scroll_pane = new JScrollPane(west_side);
			JScrollPane east_scroll_pane = new JScrollPane(east_side);
			
			west_scroll_pane.setPreferredSize(new Dimension(50, 200));
			east_scroll_pane.setPreferredSize(new Dimension(450, 200));

			FormEntry.this.add(west_scroll_pane);
			FormEntry.this.add(east_scroll_pane);
			
			FormEntry.this.revalidate();
			FormEntry.this.repaint();
		}
		
		@Override
		public void fireChangeSelectedEntry(int row, int column) {
			row_list.setSelectedIndex(row);
			
			// adjust to table coordinates
			if (column == 0)
				column = 1;
			text_fields.get(column - 1).requestFocus();
			repaint();
		}
		
		@Override
		public void fireSubmitBatch() {
			west_side.setVisible(false);
			east_side.setVisible(false);
			FormEntry.this.repaint();
		}
	};
	
	private KeyListener text_field_key_listener = new KeyListener() {
		@Override
		public void keyTyped(KeyEvent e) {
	    }

		@Override
		public void keyPressed(KeyEvent e) {
		}

		@Override
		public void keyReleased(KeyEvent e) {
		}
	};
	
	
	private FocusListener text_field_listener = new FocusListener() {

		@Override
		public void focusGained(FocusEvent e) {
			JTextField cur_field = (JTextField)e.getComponent();
			for (int i = 0; i < text_fields.size(); ++i) {
				if (cur_field.equals(text_fields.get(i))) {
					batch_state.pushChangeSelectedEntry(batch_state.getCurRow(), i + 1);
				}
			}
		}

		@Override
		public void focusLost(FocusEvent e) {			
			JTextField cur_field = (JTextField)e.getComponent();
			String cur_text = cur_field.getText();
			int row = batch_state.getCurRow();
			int column = batch_state.getCurColumn();
			// adjust to table entry coordinates
			if (column == 0)
				column = 1;
			IndexedData cur_entry = batch_state.getRecords().get(row).get(column - 1);
			cur_entry.setRecord_value(cur_text);
		}
	};
}
