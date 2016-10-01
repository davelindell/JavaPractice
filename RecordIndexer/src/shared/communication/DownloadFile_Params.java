package shared.communication;
/**
 * Wrapper class for the downloadFile method parameter
 * @author lindell
 *
 */
public class DownloadFile_Params {
	private String url;
	public DownloadFile_Params(String url) {
		this.url = url;
	}
	
	public String getUrl() {
		return url;
	}
	
	public void setUrl(String url) {
		this.url = url;
	}
}
