package client.gui;
import java.awt.Color;
import java.awt.Component;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JTable;
import javax.swing.border.Border;
import javax.swing.table.TableCellRenderer;

@SuppressWarnings("serial")
class TableEntryCellRenderer extends JLabel implements TableCellRenderer {

	private Border unselectedBorder = BorderFactory.createLineBorder(Color.BLACK, 1);
	private Border selectedBorder = BorderFactory.createLineBorder(Color.BLACK, 2);

	public TableEntryCellRenderer() {
		
		setOpaque(true);
		setFont(getFont().deriveFont(16.0f));
	}

	public Component getTableCellRendererComponent(JTable table,
			Object value, boolean isSelected, boolean hasFocus, int row,
			int column) {

		// Color c = ColorUtils.fromString((String)value);
		// this.setBackground(c);
		
		if (isSelected) {
			this.setBorder(selectedBorder);
		}
		else {
			this.setBorder(unselectedBorder);
		}
		
		this.setText((String)value);
		
		return this;
	}

}