package infoExperts;

public class BlankFile implements CoLabFile {

	private String contents;
	
	public BlankFile(){
		contents = "";
	}
	
	@Override
	public String getContentsAsString() {
		return contents;
	}

	@Override
	public String getFileName() {
		return "blankFile";
	}

}
