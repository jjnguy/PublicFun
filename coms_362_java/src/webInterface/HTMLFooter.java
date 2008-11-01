package webInterface;

public class HTMLFooter {

	private static final String[] authorNames = new String[] { "Justin Nelson",
			"Ben Petersen", "Shaun SomeLastName" };

	private static String code;
	static {
		StringBuilder builder = new StringBuilder();
		builder.append("<div class=\"footer center whiteOnBlack\">\n");
		builder
				.append("\t<a href=\"http://www.google.com\"  class=\"button plainText\">Click to search</a>\n");
		// TODO this should use the author names
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
