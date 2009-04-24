package edu.cs319.client.customcomponents.syntaxhighlighting;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import javax.swing.JEditorPane;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AbstractDocument;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Element;
import javax.swing.text.Segment;
import javax.swing.text.StringContent;

public class SyntaxHighlightingPane extends JEditorPane {

	public SyntaxHighlightingPane() throws BadLocationException {
		setDocument(new SyntaxHighlightingDoc(new StringContent()));
	}

	private class SyntaxHighlightingDoc extends AbstractDocument {

		private List<Element> elements;
		private int length;

		protected SyntaxHighlightingDoc(Content c) throws BadLocationException {
			super(c);
			String s = c.getString(0, c.length());
			elements = new ArrayList<Element>();
			length = s.length();
			// TODO Auto-generated constructor stub
		}

		private String[] keyWords = { "new", "class", "interface", "enum", "extends", "implements",
				"void", "null", "true", "false", "public", "protected", "private", "synchronized",
				"volitile", "int", "double", "byte", "float", "short", "char", "long", "throw",
				"catch", "try", "goto", "do", "while", "for", "const", "strictfp", "break",
				"continue", "this", "final", "boolean", "abstract", "native", "import", "package",
				"super", "instanceof", "throws", "finally", "return" };

		private String keyWordsToBigOr() {
			String ret = "(";
			for (String string : keyWords) {
				ret += " " + string + " ";
			}
			ret += " new )";
			return ret;
		}

		private void updateElements(Content c) throws BadLocationException {
			elements.clear();
			String s = c.getString(0, c.length());
			Pattern p = Pattern.compile(keyWordsToBigOr());
			Matcher m = p.matcher(s);
			while (m.find()) {
				KeywordElement e = new KeywordElement(this, m.start(), m.end());
				elements.add(e);
			}
		}

		@Override
		public Element getDefaultRootElement() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getLength() {
			return length;
		}

		@Override
		public Element[] getRootElements() {
			return elements.toArray(new Element[elements.size()]);
		}

		@Override
		public String getText(int offset, int length) throws BadLocationException {
			String ret = "";
			for (Element e : elements) {
				if (e.getStartOffset() >= offset) {
					// ret +=
				}
			}
			return null;
		}

		@Override
		public void getText(int offset, int length, Segment txt) throws BadLocationException {
			// TODO Auto-generated method stub

		}

		@Override
		public void insertString(int offset, String str, AttributeSet a)
				throws BadLocationException {
			// TODO Auto-generated method stub

		}

		@Override
		public void remove(int offs, int len) throws BadLocationException {
			// TODO Auto-generated method stub

		}

		@Override
		public void removeDocumentListener(DocumentListener listener) {
			// TODO Auto-generated method stub

		}

		@Override
		public void removeUndoableEditListener(UndoableEditListener listener) {
			// TODO Auto-generated method stub

		}

		@Override
		public void render(Runnable r) {
			// TODO Auto-generated method stub

		}

		@Override
		public Element getParagraphElement(int pos) {
			// TODO Auto-generated method stub
			return null;
		}

	}
}
