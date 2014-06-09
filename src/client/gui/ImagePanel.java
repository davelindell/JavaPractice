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

import javax.swing.JComponent;

import client.synchronizer.BatchState;
import client.synchronizer.BatchStateListenerAdapter;

public class ImagePanel extends JComponent {
	private DrawingImage drawing_image;
	private BatchState batch_state;
	
	private int w_centerX;
	private int w_centerY;
	private double scale;
	private boolean drag_image;

	private int w_dragStartX;
	private int w_dragStartY;
	private int w_dragStartOriginX;
	private int w_dragStartOriginY;
	
	private short[] invert_lookup_table;
	
	public ImagePanel(BatchState batch_state) {
		this.batch_state = batch_state;
		this.drawing_image = null;
		scale = 1.0;

		batch_state.addListener(batch_state_listener);
		this.setBackground(new Color(120,120,120));
		this.setPreferredSize(new Dimension(1100, 500));		
		
		this.w_centerX = this.getWidth()/2;
		this.w_centerY = this.getHeight()/2;
		
		initDrag();
		
		this.addMouseListener(mouseAdapter);
		this.addMouseMotionListener(mouseAdapter);
		this.addMouseWheelListener(mouseAdapter);
		
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
		scale = newScale;
		this.repaint();
	}
	
	public void setOrigin(int w_newCenterX, int w_newCenterY) {
		w_centerX = w_newCenterX;
		w_centerY = w_newCenterY;
		this.repaint();
	}
	
	@Override
	protected void paintComponent(Graphics g) {
		g.setColor(getBackground());
		g.fillRect(0, 0, getWidth(), getHeight());
		Graphics2D g2 = (Graphics2D)g;
		g2.translate(ImagePanel.this.getWidth()/2, ImagePanel.this.getHeight()/2);
		g2.scale(scale, scale);
		g2.translate(-w_centerX, -w_centerY);
		if (drawing_image != null)
			drawing_image.draw(g2);	
	}
	
	private MouseAdapter mouseAdapter = new MouseAdapter() {

		@Override
		public void mouseDragged(MouseEvent e) {
			if (drag_image) {
				int d_X = e.getX();
				int d_Y = e.getY();
				
				AffineTransform transform = new AffineTransform();
				transform.scale(scale, scale);
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
								
				if (!(w_centerX - w_deltaX < -400) &&
						!(w_centerX - w_deltaX > ImagePanel.this.getWidth() + 400)){
					w_centerX = w_dragStartOriginX - w_deltaX;
				}
				
				if (!(w_centerY - w_deltaY < -400) &&
						!(w_centerY - w_deltaY > 400)) {
					w_centerY = w_dragStartOriginY - w_deltaY;
				}
				
				
				ImagePanel.this.repaint();
			}
		}
		
		@Override
		public void mousePressed(MouseEvent e) {
			int d_X = e.getX();
			int d_Y = e.getY();
			
			AffineTransform transform = new AffineTransform();
			transform.scale(scale, scale);
			transform.translate(-w_centerX, -w_centerY);
			
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
			w_dragStartOriginX = w_centerX;
			w_dragStartOriginY = w_centerY;
			
		}

		@Override
		public void mouseReleased(MouseEvent e) {
			initDrag();
		}

		@Override
		public void mouseWheelMoved(MouseWheelEvent e) {
			double scroll_amount = 0;
			scroll_amount = ((double)e.getWheelRotation()) / 6;
			if ((scale < .5 && scroll_amount > 0) ||
				 (scale > 2 && scroll_amount < 0)) {
				scroll_amount = 0;
			}
			setScale(scale - scroll_amount);
		}	
	};

	private class DrawingImage {

		private Image image;
		private Rectangle2D rect;
		
		public DrawingImage(Image image, Rectangle2D rect) {
			this.image = image;
			this.rect = rect;
		}

		public boolean contains(Graphics2D g2, double x, double y) {
			return rect.contains(x, y);
		}
		
		public void draw(Graphics2D g2) {
			Rectangle2D bounds = rect.getBounds2D();
			g2.drawImage(image, (int)bounds.getMinX(), (int)bounds.getMinY(), (int)bounds.getMaxX(), (int)bounds.getMaxY(),
							0, 0, image.getWidth(null), image.getHeight(null), null);
		}
		
		public Rectangle2D getRect() {
			return this.rect;
		}
		
		public Image getImage() {
			return this.image;
		}
		
		public void setImage(Image image) {
			this.image = image;
		}
	};
	
	private BatchStateListenerAdapter batch_state_listener = new BatchStateListenerAdapter() {
		@Override
		public void fireDownloadedBatch(BufferedImage batch_image) {
			ImagePanel.this.w_centerX = ImagePanel.this.getWidth()/2;
			ImagePanel.this.w_centerY = ImagePanel.this.getHeight()/2;
			initDrag();
			drawing_image = new DrawingImage(batch_image, new Rectangle2D.Double(0,0, batch_image.getWidth(null), batch_image.getHeight(null)));
			ImagePanel.this.repaint();
		}
		@Override
		public void fireZoomInButton() {	
			if (!(scale > 2))
				setScale(scale + .15);
		}
		@Override
		public void fireZoomOutButton() {
			if (!(scale < .5))
				setScale(scale - .15);
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
			
	};
	
	private BufferedImage invertImage(BufferedImage in) {
		int width = in.getWidth();
		int height = in.getHeight();
		BufferedImage out = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
		BufferedImageOp invert = new LookupOp(new ShortLookupTable(0, invert_lookup_table), null);
		return invert.filter(in, out);
		
	}

}


