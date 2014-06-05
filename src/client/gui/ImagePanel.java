package client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;

import javax.swing.JComponent;
import javax.swing.JPanel;

public class ImagePanel extends JComponent {
	
	public ImagePanel() {
		this.setBackground(new Color(120,120,120));
		this.setPreferredSize(new Dimension(1100, 500));
		//paintComponent(getGraphics());
		
	}
	@Override
	public void paintComponent(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());

	}

}
