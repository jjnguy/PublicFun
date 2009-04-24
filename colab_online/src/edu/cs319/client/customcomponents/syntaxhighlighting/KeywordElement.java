package edu.cs319.client.customcomponents.syntaxhighlighting;

import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;

public class KeywordElement implements Element {

	private Document doc;
	private AttributeSet atributes;
	private int start, end;

	public KeywordElement(Document par, int start, int end) {
		atributes = new KeywordAtributeSet();
		doc = par;
		this.end = end;
		this.start = start;
	}

	@Override
	public AttributeSet getAttributes() {
		return atributes;
	}

	@Override
	public Document getDocument() {
		return doc;
	}

	@Override
	public Element getElement(int arg0) {
		return null;
	}

	@Override
	public int getElementCount() {
		return 0;
	}

	@Override
	public int getElementIndex(int arg0) {
		return -1;
	}

	@Override
	public int getEndOffset() {
		return end;
	}

	@Override
	public String getName() {
		try {
			return "Keyword: " + doc.getText(start, end - start);
		} catch (BadLocationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}

	@Override
	public Element getParentElement() {
		return null;
	}

	@Override
	public int getStartOffset() {
		return start;
	}

	@Override
	public boolean isLeaf() {
		return true;
	}

}
