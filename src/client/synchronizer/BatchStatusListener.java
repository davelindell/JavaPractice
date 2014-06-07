package client.synchronizer;

public interface BatchStatusListener {	
	public void fireLogoutButton();
	
	public void fireDownloadedBatch();
	
	public void fireSubmittedBatch();
}