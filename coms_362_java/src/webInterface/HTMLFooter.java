package webInterface;

public class HTMLFooter {

	public static final int FOOTER_HEIGHT = 25;
	private static String URL = "http://stackoverflow.com";
	private static String BUTTON_TEXT = "Programming Question?  Click Here!";
	
	private static final String[] authorNames = new String[] { "Justin Nelson",
			"Ben Petersen", "Shaun SomeLastName" };

	private static String pushDiv;
	private static String code;

	static {
		pushDiv = "<div style=\"height: " + FOOTER_HEIGHT + "px\">";
		pushDiv += "\n</div>\n";
	}

	static {
		StringBuilder builder = new StringBuilder();
		builder.append("<div class=\"footer center whiteOnBlack\" style=\"height: "
				+ FOOTER_HEIGHT + "px  \">\n");
		builder
				.append("\t<a href=\"" + URL + "\"  class=\"button\" onmouseover=\"backgroundChangeIn(this)\" onmouseout=\"backgroundChangeOut(this)\">" + BUTTON_TEXT + "</a>\n");
		builder.append("\tby ");
		for (int i = 0; i < authorNames.length; i++) {
			builder.append(authorNames[i]);
			if (i < authorNames.length - 1)
				builder.append(", ");
			else
				builder.append(".");
		}
		builder.append("</div>");
		code = builder.toString();
	}

	public static String getFooter() {
		return code;
	}
	
	private HTMLFooter() {
	}
}
