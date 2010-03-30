package entities;

public class Tag {

	private String name;
	private int count;

	public Tag(String name, int count) {
		this.name = name;
		this.count = count;
	}

	public String getName() {
		return name;
	}

	public int getCount() {
		return count;
	}

	public static Tag[] fromTagString(String tags){
		String[] split = tags.split("\\s+");
		Tag[] ret = new Tag[split.length];
		for (int i = 0; i < ret.length; i++){
			ret[i] = new Tag(split[i], -1);
		}
		return ret;
	}
	
	@Override
	public String toString() {
		return name;
	}
}
