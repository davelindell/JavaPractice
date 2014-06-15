package client.synchronizer;

import java.awt.image.BufferedImage;

public interface BatchStateListener {	
	
	public void fireLogoutButton();
	
	public void fireDownloadBatch();
	
	public void fireSubmitBatch();
	
	public void fireZoomInButton();
	
	public void fireZoomOutButton();

	public void fireInvertImage();
	
	public void fireToggleHighlights();
	
	public void fireChangeSelectedEntry(int row, int column);
	
	public void fireEnteredData(int row, int column);
	
	public void fireLoad();
	
	public void fireSave();
}