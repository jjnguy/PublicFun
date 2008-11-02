package webInterface;

public class HTMLFooter {

	public static final int FOOTER_HEIGHT = 25;
	public static final String WRAPPER_FOOTER_STYLE;
	
	private static final String[] authorNames = new String[] { "Justin Nelson",
			"Ben Petersen", "Shaun SomeLastName" };

	private static String pushDiv;
	private static String code;

	static {
		WRAPPER_FOOTER_STYLE = "style=\"margin: 0 auto " + (-1 * FOOTER_HEIGHT) + "px \"";
	}
	
	static {
		pushDiv = "<div style=\"height: " + FOOTER_HEIGHT + "px\">";
		pushDiv += "\n</div>\n";
	}

	static {
		StringBuilder builder = new StringBuilder();
		builder.append("<div class=\"footer center whiteOnBlack\" style=\"height: "
				+ FOOTER_HEIGHT + "px  \">\n");
		builder
				.append("\t<a href=\"http://www.google.com\"  class=\"button plainText\">Click to search</a>\n");
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

	private HTMLFooter() {
	}

	public static String getFooter() {
		return code;
	}
}
