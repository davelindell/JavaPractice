package shared.communication;
/**
 * Wrapper class for the return value of downloadFile()
 * this returns a byte array that contains the downloaded file.
 * @author lindell
 *
 */
public class DownloadFile_Result {
	private byte[] file_download;
	
	public DownloadFile_Result(byte[] file_download){
		this.file_download = file_download;
	}

	public byte[] getFile_download() {
		return file_download;
	}

	public void setFile_download(byte[] file_download) {
		this.file_download = file_download;
	}
	
	
}
