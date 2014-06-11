package client.synchronizer;

import java.awt.image.BufferedImage;

public interface BatchStateListener {	
	
	public void fireLogoutButton();
	
	public void fireDownloadBatch(BufferedImage batch_image);
	
	public void fireSubmitBatch();
	
	public void fireZoomInButton();
	
	public void fireZoomOutButton();

	public void fireInvertImage();
	
	public void fireChangeSelectedEntry(int row, int column);
}