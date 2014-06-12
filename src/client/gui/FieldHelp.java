package client.gui;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.EventQueue;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import java.io.IOException;

import javax.swing.BoxLayout;
import javax.swing.JComponent;
import javax.swing.JEditorPane;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.ListSelectionModel;
import javax.swing.table.TableColumn;
import javax.swing.table.TableColumnModel;
import javax.swing.text.JTextComponent;

import client.communication.ClientCommunicator;
import client.synchronizer.BatchState;
import client.synchronizer.BatchStateListenerAdapter;

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
				FieldHelp.this.removeAll();
				String field_help = batch_state.getFieldHelps().get(column);
				text_field = new JEditorPane("text/html",field_help);
				text_field.setPreferredSize(new Dimension(600, 200));
				text_field.setEditable(false);
				FieldHelp.this.add(text_field);
				FieldHelp.this.revalidate();
				repaint();

		}
		
	};

}
