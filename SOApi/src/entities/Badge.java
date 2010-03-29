package entities;

public class Badge {
	private int id;
	private int awardCount;
	private String name;
	private String description;

	public Badge(int id, String name, String description, int awardCount) {
		this.id = id;
		this.name = name;
		this.description = description;
	}

	public int id() {
		return id;
	}

	public int awardCount() {
		return awardCount;
	}

	public String name() {
		return name;
	}

	public String description() {
		return description;
	}

	@Override
	public String toString() {
		return "Name: " + name + ", Description: " + description;
	}

	public static enum Class {
		BRONZE("Bronze"), SILVER("Silver"), GOLD("Gold");
		private String name;

		private Class(String displayName) {
			this.name = displayName;
		}

		@Override
		public String toString() {
			return name;
		}
	}
}
