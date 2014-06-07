package client.synchronizer;

public interface BatchStateListener {	
	public void fireLogoutButton();
	
	public void fireDownloadedBatch();
	
	public void fireSubmittedBatch();
}