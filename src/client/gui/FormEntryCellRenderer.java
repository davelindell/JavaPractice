package client.gui;

import java.awt.Color;
import java.awt.Component;
import java.awt.Dimension;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JList;
import javax.swing.ListCellRenderer;
import javax.swing.SwingConstants;
import javax.swing.border.Border;

import client.synchronizer.BatchState;

public class FormEntryCellRenderer extends JLabel implements ListCellRenderer<Integer> {
	private BatchState batch_state;
	private Border unselectedBorder = BorderFactory.createMatteBorder(0,0,0,0, Color.WHITE);
	private Border selectedBorder = BorderFactory.createMatteBorder(1,1,1,1, Color.BLUE);
	
	public FormEntryCellRenderer(BatchState batch_state) {
		this.batch_state = batch_state;
		 setOpaque(true);  
	     setHorizontalAlignment(LEFT);  
	     setVerticalAlignment(TOP);  
	}
	
	@Override
	public Component getListCellRendererComponent(JList<? extends Integer> list,
			Integer value, int index, boolean isSelected, boolean cellHasFocus) {
		// update currently selected row
		if (isSelected && index != batch_state.getCurRow()) {
			batch_state.pushChangeSelectedEntry(index, batch_state.getCurColumn());
		}
		this.setPreferredSize(new Dimension(50,15));
		this.setBackground(Color.WHITE);
		this.setHorizontalAlignment(SwingConstants.LEFT);
		setText(value.toString());
		
		if (isSelected) {
			this.setBackground(new Color(172, 199, 230));
		}
		else {
			this.setBorder(unselectedBorder);
			this.setBackground(Color.WHITE);
		}
			
		return this;
	}




}
