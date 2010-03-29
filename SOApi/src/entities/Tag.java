package entities;

public class Tag {

	private String name;
	private int count;

	public Tag(String name, int count) {
		this.name = name;
		this.count = count;
	}

	public String name() {
		return name;
	}

	public int count() {
		return count;
	}

	@Override
	public String toString() {
		return name;
	}
}
