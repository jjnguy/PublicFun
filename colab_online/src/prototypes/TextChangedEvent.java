package prototypes;

public class TextChangedEvent {

	private int cursorLocation;
	private String textAdded;
	private int deleteEndIndex;
	private TextChangeType type;

	public int getCursorPosition() {
		return cursorLocation;
	}

	public String getTextAdded() {
		if (type == TextChangeType.TEXT_ADDED)
			return textAdded;
		else
			return "";
	}

	/**
	 * 
	 * @return the index (exclusive) where the delete ended
	 */
	public int getDeletedEnd() {
		if (type == TextChangeType.TEXT_DELETED)
			return deleteEndIndex;
		else
			return -1;
	}
}
