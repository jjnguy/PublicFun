package id3TagStuff.id3Data;

public interface ID3v2_XFrameData {

	/**
	 * @return the type of the data
	 */
	public String getType();

	/**
	 * Turns the data into an array of bytes.  Think serialization.
	 * 
	 * The bytes are then able to be saved into a ID3 tag.
	 * @param versionNumber the version number tag you would like to create
	 * @return the bytes representing the data.
	 */
	public int[] getByteRepresentation(int versionNumber);

}
