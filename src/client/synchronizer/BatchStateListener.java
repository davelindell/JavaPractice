package client.synchronizer;

import java.awt.image.BufferedImage;

public interface BatchStateListener {	
	
	public void fireLogoutButton();
	
	public void fireDownloadedBatch(BufferedImage batch_image);
	
	public void fireSubmittedBatch();
	
	public void fireZoomInButton();
	
	public void fireZoomOutButton();

	public void fireInvertImage();
}