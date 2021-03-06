package client.gui;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

import client.synchronizer.BatchState;

@SuppressWarnings("serial")
class TableEntryCellRenderer extends JLabel implements TableCellRenderer {
	private BatchState batch_state;
	private Border unselectedBorder = BorderFactory.createMatteBorder(0,0,1,1, Color.black);
	private Border selectedBorder = BorderFactory.createMatteBorder(2,2,2,2, Color.BLUE);

	public TableEntryCellRenderer(BatchState batch_state) {
		this.batch_state = batch_state;
		setOpaque(true);
		setFont(getFont().deriveFont(12.0f));
	}

	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column) {
		
		// update currently selected row
		if (isSelected && 
				(row != batch_state.getCurRow() || 
					column != batch_state.getCurColumn())) {
			
			batch_state.pushChangeSelectedEntry(row, column);
		}
		
		boolean is_quality = true;
		if(column != 0)
			is_quality = batch_state.isQuality(row, column);
		


		if (isSelected && hasFocus && is_quality) {
			this.setBorder(selectedBorder);
			this.setBackground(new Color(172, 199, 230));
		}
		
		else if(isSelected && !hasFocus && is_quality) {
			this.setBorder(unselectedBorder);
			this.setBackground(new Color(172, 199, 230));
		}
		
		else if (!is_quality) {
			this.setBackground(Color.red);
			
			if (isSelected) 
				this.setBorder(selectedBorder);
			else
				this.setBorder(unselectedBorder);
		}
		
		else {
			this.setBackground(Color.WHITE);
			this.setBorder(unselectedBorder);
		}
		
		
		if (column == 0 && !isSelected) {
			this.setBorder(BorderFactory.createMatteBorder(0,1,1,1, Color.black));
		}
		
		if(column == 0) {
			this.setHorizontalAlignment(SwingConstants.RIGHT);
			this.setText(Integer.toString((Integer)value));
		}
			
		else {
			this.setHorizontalAlignment(SwingConstants.LEFT);
			this.setText((String)value);
		}
		
		return this;
	}

}