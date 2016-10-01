package client.gui;

import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Image;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.awt.event.MouseWheelEvent;
import java.awt.geom.AffineTransform;
import java.awt.geom.NoninvertibleTransformException;
import java.awt.geom.Point2D;
import java.awt.geom.Rectangle2D;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.LookupOp;
import java.awt.image.ShortLookupTable;
import java.util.ArrayList;
import java.util.List;

import javax.swing.JComponent;

import shared.models.Field;
import client.synchronizer.BatchState;
import client.synchronizer.BatchStateListenerAdapter;

@SuppressWarnings("serial")
public class ImagePanel extends JComponent {
	private DrawingImage drawing_image;
	private DrawingRect drawing_rect;
	private BatchState batch_state;
	
	private boolean drag_image;

	private int w_dragStartX;
	private int w_dragStartY;
	private int w_dragStartOriginX;
	private int w_dragStartOriginY;
	
	private int highlight_x;
	private int highlight_y;
	private int highlight_height;
	private int highlight_width;
	
	private List<Integer> column_widths;
	private List<Integer> row_heights;
	
	private short[] invert_lookup_table;
	
	public ImagePanel(BatchState batch_state) {
		this.batch_state = batch_state;
		this.drawing_image = null;
		this.drawing_rect = null;
		column_widths = new ArrayList<Integer>();
		row_heights = new ArrayList<Integer>();
		
		highlight_x = 0;
		highlight_y = 0;
		highlight_height = 0;
		highlight_width = 0;
		
		batch_state.addListener(batch_state_listener);
		this.setBackground(new Color(120,120,120));
		this.setPreferredSize(new Dimension(1100, 500));		
		
		batch_state.setImagePosX(this.getWidth()/2);
		batch_state.setImagePosY(this.getHeight()/2);
		
		initDrag();
		
		this.addMouseListener(mouse_adapter);
		this.addMouseMotionListener(mouse_adapter);
		this.addMouseWheelListener(mouse_adapter);
		
		invert_lookup_table = new short[256];
		for (int i = 0; i < 256; i++) {
			invert_lookup_table[i] = (short) (255 - i);
		}
	}
	
	private void initDrag() {
	    drag_image = false;
		w_dragStartX = 0;
		w_dragStartY = 0;
		w_dragStartOriginX = 0;
		w_dragStartOriginY = 0;
	}
	
	public void setScale(double newScale) {
		batch_state.setZoomLevel(newScale);
		this.repaint();
	}
	
	public void setOrigin(int w_newCenterX, int w_newCenterY) {
		batch_state.setImagePosX(w_newCenterX);
		batch_state.setImagePosY(w_newCenterY);
		this.repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		Graphics2D g2 = (Graphics2D)g;
		g2.translate(ImagePanel.this.getWidth()/2, ImagePanel.this.getHeight()/2);
		g2.scale(batch_state.getZoomLevel(), batch_state.getZoomLevel());
		g2.translate(-batch_state.getImagePosX(), -batch_state.getImagePosY());
		
		if (drawing_image != null)
			drawing_image.draw(g2);	
		if (drawing_rect != null)
			drawing_rect.draw(g2);
	}
	
	private MouseAdapter mouse_adapter = new MouseAdapter() {

		@Override
		public void mouseDragged(MouseEvent e) {
			if (drag_image) {
				int d_X = e.getX();
				int d_Y = e.getY();
				
				AffineTransform transform = new AffineTransform();
				
				//transform.translate(ImagePanel.this.getWidth()/2, ImagePanel.this.getHeight()/2);
				transform.scale(batch_state.getZoomLevel(), batch_state.getZoomLevel());
				transform.translate(-w_dragStartOriginX, -w_dragStartOriginY);
				
				Point2D d_Pt = new Point2D.Double(d_X, d_Y);
				Point2D w_Pt = new Point2D.Double();
				try
				{
					transform.inverseTransform(d_Pt, w_Pt);
				}
				catch (NoninvertibleTransformException ex) {
					return;
				}
				int w_X = (int)w_Pt.getX();
				int w_Y = (int)w_Pt.getY();
				
				int w_deltaX = w_X - w_dragStartX;
				int w_deltaY = w_Y - w_dragStartY;
												
				batch_state.setImagePosX(w_dragStartOriginX - w_deltaX);
				batch_state.setImagePosY(w_dragStartOriginY - w_deltaY);
				
 				if (w_dragStartOriginX - w_deltaX < 300) {
 					batch_state.setImagePosX(300);
				}
						
				if (w_dragStartOriginX - w_deltaX > 1000) {
					batch_state.setImagePosX(1000);
				}
				
				if (w_dragStartOriginY - w_deltaY < -100) {
					batch_state.setImagePosY(-100);
				}
					
				if(w_dragStartOriginY - w_deltaY > 700) {
					batch_state.setImagePosY(700);
				}

				ImagePanel.this.repaint();
			}
		}
		
		@Override
		public void mouseClicked(MouseEvent e) {
			int d_X = e.getX();
			int d_Y = e.getY();
			
			AffineTransform transform = new AffineTransform();
			transform.translate(ImagePanel.this.getWidth()/2, ImagePanel.this.getHeight()/2);
			transform.scale(batch_state.getZoomLevel(), batch_state.getZoomLevel());
			transform.translate(-batch_state.getImagePosX(), -batch_state.getImagePosY());
			
			Point2D d_Pt = new Point2D.Double(d_X, d_Y);
			Point2D w_Pt = new Point2D.Double();
			try
			{
				transform.inverseTransform(d_Pt, w_Pt);
			}
			catch (NoninvertibleTransformException ex) {
				return;
			}
			int w_X = (int)w_Pt.getX();
			int w_Y = (int)w_Pt.getY();
			
			int first_x = batch_state.getFirstXCoord();
			int first_y = batch_state.getFirstYCoord();
			int last_x = column_widths.get(column_widths.size()-1);
			int last_y = row_heights.get(row_heights.size()-1);
			int img_x = w_X - first_x;
			int img_y = w_Y - first_y;
			
			if (img_x >= 0 && img_y >= 0 &&
					img_x <  last_x && img_y < last_y) {
				
				int column = 0;
				int row = 0;
				int i = 0;
				boolean found = false;
				
				while(i < row_heights.size() && !found) {
					if (img_y <= row_heights.get(i)) {
						row = i;
						found = true;
					}
					++i;
				}
				
				i = 0;
				found = false;
				while(i < column_widths.size() && !found) {
					if (img_x <= column_widths.get(i)) {
						column = i;
						found = true;
					}
					++i;
				}
				
				batch_state.pushChangeSelectedEntry(row, column + 1);
			}
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			int d_X = e.getX();
			int d_Y = e.getY();
			
			AffineTransform transform = new AffineTransform();
			transform.scale(batch_state.getZoomLevel(), batch_state.getZoomLevel());
			transform.translate(-batch_state.getImagePosX(), -batch_state.getImagePosY());
			
			Point2D d_Pt = new Point2D.Double(d_X, d_Y);
			Point2D w_Pt = new Point2D.Double();
			try
			{
				transform.inverseTransform(d_Pt, w_Pt);
			}
			catch (NoninvertibleTransformException ex) {
				return;
			}
			int w_X = (int)w_Pt.getX();
			int w_Y = (int)w_Pt.getY();
			
			drag_image = true;		
			w_dragStartX = w_X;
			w_dragStartY = w_Y;		
			w_dragStartOriginX = batch_state.getImagePosX();
			w_dragStartOriginY = batch_state.getImagePosY();
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			initDrag();
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			double scroll_amount = 0;
			scroll_amount = ((double)e.getWheelRotation()) / 6;
			if ((batch_state.getZoomLevel() < .25 && scroll_amount > 0) ||
				 (batch_state.getZoomLevel() > 2 && scroll_amount < 0)) {
				scroll_amount = 0;
			}
			setScale(batch_state.getZoomLevel() - scroll_amount);
		}	
	};

	private class DrawingImage {

		private Image image;
		private Rectangle2D rect;
		
		public DrawingImage(Image image, Rectangle2D rect) {
			this.image = image;
			this.rect = rect;
		}
		
		public void draw(Graphics2D g2) {
			Rectangle2D bounds = rect.getBounds2D();
			g2.drawImage(image, (int)bounds.getMinX(), (int)bounds.getMinY(), (int)bounds.getMaxX(), (int)bounds.getMaxY(),
							0, 0, image.getWidth(null), image.getHeight(null), null);
		}
		
		public Image getImage() {
			return this.image;
		}
		
		public void setImage(Image image) {
			this.image = image;
		}
	};
	
	private class DrawingRect {

		private Rectangle2D rect;
		private Color color;

		public DrawingRect(Rectangle2D rect, Color color) {
			this.rect = rect;
			this.color = color;
		}

		public void setRectDim(int x, int y, int w, int h) {
			rect.setFrame(x, y, w, h);
		}
		
		public Color getColor() {
			return this.color;
		}
		
		public void setColor(Color color) {
			this.color = color;
		}
		
		public void draw(Graphics2D g2) {
			g2.setColor(color);
			g2.fill(rect);
		}	
	}
	
	private BatchStateListenerAdapter batch_state_listener = new BatchStateListenerAdapter() {
		@Override
		public void fireDownloadBatch() {
			Image batch_image = batch_state.getImage();
			// Initialize Image Panel
			batch_state.setImagePosX(ImagePanel.this.getWidth()/2);
			batch_state.setImagePosY(ImagePanel.this.getHeight()/2);
			initDrag();
			drawing_image = new DrawingImage(batch_image, new Rectangle2D.Double(0,0, batch_image.getWidth(null), batch_image.getHeight(null)));
			
			// Initialize Highlighted Cell
			highlight_x = batch_state.getFirstXCoord();
			highlight_y = batch_state.getFirstYCoord();
			highlight_width = batch_state.getFields().get(0).getPixel_width();
			highlight_height = batch_state.getRecordHeight();
			Rectangle2D rect = new Rectangle2D.Double(highlight_x, highlight_y, highlight_width, highlight_height);
			Color color = new Color(172, 199, 230,175);
			drawing_rect = new DrawingRect(rect, color);
			
			// get column widths and row heights;
			int cur_height = 0;
			for (int i = 0; i < batch_state.getNumRecords(); ++i) {
				cur_height += batch_state.getRecordHeight();
				row_heights.add(cur_height);
			}
			
			int cur_width = 0;
			List<Field> fields = batch_state.getFields();
			for (int i = 0; i < batch_state.getNumFields(); ++i) {
				cur_width += fields.get(i).getPixel_width();
				column_widths.add(cur_width);
			}
			
			// repaint
			ImagePanel.this.repaint();
		}
		@Override
		public void fireZoomInButton() {	
			if (!(batch_state.getZoomLevel() > 2))
				setScale(batch_state.getZoomLevel() + .15);
		}
		@Override
		public void fireZoomOutButton() {
			if (!(batch_state.getZoomLevel() < .25))
				setScale(batch_state.getZoomLevel() - .15);
		}

		
		@Override 
		public void fireInvertImage() {			
			Image image = drawing_image.getImage();
			BufferedImage buf_image = new BufferedImage(image.getWidth(null), image.getHeight(null), BufferedImage.TYPE_INT_RGB);
			Graphics2D g2d = buf_image.createGraphics();
			g2d.drawImage(image, 0, 0, null);
			g2d.dispose();
			BufferedImage result = invertImage(buf_image);
			drawing_image.setImage(result);
			ImagePanel.this.repaint();
		}
		
		@Override 
		public void fireToggleHighlights() {			
			Color color = drawing_rect.getColor();
			int red = color.getRed();
			int blue = color.getBlue();
			int green = color.getGreen();
			if (color.getAlpha() != 0) {
				int alpha = 0;
				drawing_rect.setColor(new Color(red, green, blue, alpha));
			}
			else {
				int alpha = 175;
				drawing_rect.setColor(new Color(red, green, blue, alpha));
			}
			ImagePanel.this.repaint();
		}
		
		public void fireChangeSelectedEntry(int row, int column) {
			// adjust to table coordinates
			if (column == 0)
				column = 1;
			int first_y_coord = batch_state.getFirstYCoord();
			Field cur_field = batch_state.getFields().get(column - 1);
			int highlight_width = cur_field.getPixel_width();
			int highlight_height = batch_state.getRecordHeight();
			int highlight_x = cur_field.getX_coord();
			int highlight_y = first_y_coord + row * highlight_height;
			drawing_rect.setRectDim(highlight_x, highlight_y, highlight_width, highlight_height);
			ImagePanel.this.repaint();
		}
		
		@Override
		public void fireSubmitBatch() {
			ImagePanel.this.drawing_image = null;
			ImagePanel.this.drawing_rect = null;
			ImagePanel.this.repaint();
		}
		
		@Override
		public void fireLoad() {
			if (batch_state.getImage() != null) {
				Image batch_image = batch_state.getImage();
				// Initialize Image Panel
				
				initDrag();
				
				drawing_image = new DrawingImage(batch_image, new Rectangle2D.Double(0,0, batch_image.getWidth(null), batch_image.getHeight(null)));
				
				// Initialize Highlighted Cell
				highlight_x = batch_state.getFirstXCoord();
				highlight_y = batch_state.getFirstYCoord();
				highlight_width = batch_state.getFields().get(0).getPixel_width();
				highlight_height = batch_state.getRecordHeight();
				Rectangle2D rect = new Rectangle2D.Double(highlight_x, highlight_y, highlight_width, highlight_height);
				Color color = new Color(172, 199, 230,175);
				drawing_rect = new DrawingRect(rect, color);
				
				// get column widths and row heights;
				int cur_height = 0;
				for (int i = 0; i < batch_state.getNumRecords(); ++i) {
					cur_height += batch_state.getRecordHeight();
					row_heights.add(cur_height);
				}
				
				int cur_width = 0;
				List<Field> fields = batch_state.getFields();
				for (int i = 0; i < batch_state.getNumFields(); ++i) {
					cur_width += fields.get(i).getPixel_width();
					column_widths.add(cur_width);
				}
				
				if(batch_state.getHighlightsVisible()) {
					batch_state.pushToggleHighlights();
				}
				
				if(batch_state.getImageInverted()) {
					batch_state.pushInvertImage();
				}
				
				// repaint
				ImagePanel.this.repaint();
			}
		}
	};
	
	private BufferedImage invertImage(BufferedImage in) {
		int width = in.getWidth();
		int height = in.getHeight();
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		BufferedImageOp invert = new LookupOp(new ShortLookupTable(0, invert_lookup_table), null);
		return invert.filter(in, out);
	}

}


