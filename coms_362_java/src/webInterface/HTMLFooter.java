package webInterface;

/**
 * A class to define the footer for every page in the mp3tools site.
 * 
 * @author Justin Nelson
 * 
 */
public class HTMLFooter {

	/**
	 * The height of the footer in pixles
	 */
	public static final int FOOTER_HEIGHT = 25;
	private static String URL = "http://stackoverflow.com";
	private static String BUTTON_TEXT = "Programming Question?  Click Here!";

	private static final String[] authorNames = new String[] { "Justin Nelson",
			"Ben Petersen", "Shaun Brockhoff" };

	private static String pushDiv;
	private static String code;

	/*
	 * Static loading so it happens only once.
	 */
	static {
		pushDiv = "<div style=\"height: " + FOOTER_HEIGHT + "px\">";
		pushDiv += "\n</div>\n";
	}

	/*
	 * Static loading so it happens only once.
	 */
	static {
		StringBuilder builder = new StringBuilder();
		builder.append("<div class=\"footer center\" style=\"height: " + FOOTER_HEIGHT
				+ "px  \">\n");
		builder
				.append("\t<a href=\"" + URL + "\"  class=\"button\">" + BUTTON_TEXT
						+ "</a>\n");
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

	/**
	 * @return the html code that creates the footer. Relies on the correct stylesheet.
	 */
	public static String getFooter() {
		return code;
	}

	private HTMLFooter() {
	}
}
