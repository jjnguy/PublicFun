package infoExpert;

/**
 * Holds information about a song
 * 
 * @author Justin Nelson
 *
 */
public class SongData {
	private String title;
	private String album;
	private String performers[] = new String[3]; // up to 3 performers and comments
	private String comments[] = new String[3];
	private String trackNum;
	private String year;
	private String encodedBy;
	private String composer;
	private String fileName;
	private String pictureName;
	private String owner;

	public SongData() {
	};

	/**
	 * Constructor
	 * 
	 * @param t
	 * 		Song Title
	 * @param a
	 * 		Song's album
	 * @param p
	 * 		Song's performer(s)
	 * @param c
	 * 		Song's comments
	 * @param tr
	 * 		Song's track number
	 * @param y
	 * 		Song's year
	 * @param e
	 * 		Song's encoder
	 * @param co
	 * 		Song's composer
	 * @param fp
	 * 		Song's file name
	 * @param pp
	 * 		Song's picture name
	 */
	public SongData(String t, String a, String p[], String c[], String tr, String y, String e,
			String co, String fp, String pp) {
		title = t;
		album = a;
		for (int i = 0; i < 3; i++) {
			performers[i] = p[i];
			comments[i] = c[i];
		}
		trackNum = tr;
		year = y;
		encodedBy = e;
		composer = co;
		fileName = fp;
		pictureName = pp;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String t) {
		this.title = t;
	}

	public String getAlbum() {
		return album;
	}

	public void setAlbum(String a) {
		this.album = a;
	}

	public String getTrackNum() {
		return trackNum;
	}

	public void setTrackNum(String tr) {
		this.trackNum = tr;
	}

	public String getYear() {
		return year;
	}

	public void setYear(String y) {
		this.year = y;
	}

	public String getEncodedBy() {
		return encodedBy;
	}

	public void setEncodedBy(String e) {
		this.encodedBy = e;
	}

	public String getPathName() {
		return fileName;
	}

	public String getFileName() {
		return fileName.substring(fileName.lastIndexOf("/") + 1);
	}

	public void setFileName(String f) {
		this.fileName = f;
	}

	public String getPictureName() {
		return pictureName;
	}

	public void setPictureName(String p) {
		this.pictureName = p;
	}

	public String getComment(int i) {
		return comments[i];
	}

	public void setComment(int i, String c) {
		comments[i] = c;
	}

	public String getPerformer(int i) {
		return performers[i];
	}

	public void setPerformer(int i, String p) {
		performers[i] = p;
	}

	public String getComposer() {
		return composer;
	}

	public void setComposer(String c) {
		this.composer = c;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
}