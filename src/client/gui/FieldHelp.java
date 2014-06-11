package client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

import javax.swing.JComponent;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;

import client.communication.ClientCommunicator;
import client.synchronizer.BatchState;
import client.synchronizer.BatchStateListenerAdapter;

public class FieldHelp extends JPanel {
	private BatchState batch_state;
	private JLabel text_field;
	
	public FieldHelp(BatchState batch_state) {
		this.batch_state = batch_state;
		batch_state.addListener(batch_state_listener);
		createComponents();
	}
	
	private void createComponents() {
		this.setPreferredSize(new Dimension(600, 200));
		this.setLayout(new FlowLayout());
		this.setBackground(Color.WHITE);
		text_field = new JLabel();
		FieldHelp.this.add(text_field);
	}
	
	
	private BatchStateListenerAdapter batch_state_listener = new BatchStateListenerAdapter() {
//		@Override
//		public void fireDownloadBatch(BufferedImage batch_image) {
//			JLabel text_field = new JLabel("A label");
//			label.setFont(new Font("Serif", Font.PLAIN, 14));
//			label.setForeground(new Color(0xffffdd));
//		}
		
		@Override
		public void fireChangeSelectedEntry(int row, int column) {
			FieldHelp.this.removeAll();
			if (column != 0) {
				String text_body = batch_state.getFieldHelps().get(column - 1);
				JLabel text_field = new JLabel(text_body);
				text_field.setBackground(Color.white);
				text_field.setFont(new Font("Serif", Font.PLAIN, 14));
				FieldHelp.this.add(text_field);
				FieldHelp.this.setVisible(true);
				repaint();
			}
		}
		
	};

}
