package webInterface;

public class HTMLFooter {

	private static final String[] authorNames = new String[] { "Justin Nelson",
			"Ben Petersen", "Shaun SomeLastName" };

	private static String code;
	static {
		StringBuilder builder = new StringBuilder();
		builder.append("<div class=\"footer center whiteOnBlack\">\n");
		builder.append("\t<a href=\"http://www.google.com\"  class=\"button plainText\">Click to search</a>\n");
		builder.append("\tby Justin Nelson, Ben Petersen, and Shaun SomeLastname (sorry Shaun)\n");
		builder.append("</div>");
		code = builder.toString();
	}

	private HTMLFooter() {
	}

	public static String getFooter(){
		return code;
	}
}
