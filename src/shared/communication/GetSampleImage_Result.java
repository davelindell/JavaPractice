package shared.communication;

/**
 * Wrapper class for the result that getSampleImage returns
 * @author lindell
 *
 */
public class GetSampleImage_Result {
	private String image_url;
	
	public GetSampleImage_Result(String image_url) {
		this.image_url = image_url;
	}

	public String getImage_url() {
		return image_url;
	}

	public void setImage_url(String image_url) {
		this.image_url = image_url;
	}
	
}
