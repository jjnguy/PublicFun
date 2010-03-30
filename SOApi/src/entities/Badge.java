package entities;

public class Badge {
	private int badgeId;
	private int awardCount;
	private String name;
	private String description;

	public Badge(int badgeId, String name, String description, int awardCount) {
		this.badgeId = badgeId;
		this.name = name;
		this.description = description;
	}

	public int getAwardCount() {
		return awardCount;
	}

	public String getName() {
		return name;
	}

	public String getDescription() {
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
