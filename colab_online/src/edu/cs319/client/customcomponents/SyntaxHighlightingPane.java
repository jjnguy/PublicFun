package edu.cs319.client.customcomponents;

import javax.swing.JEditorPane;
import javax.swing.event.DocumentListener;
import javax.swing.event.UndoableEditListener;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.Element;
import javax.swing.text.Position;
import javax.swing.text.Segment;

public class SyntaxHighlightingPane extends JEditorPane {

	public SyntaxHighlightingPane() {

	}

	private class SyntaxHighlightingDoc implements Document {

		@Override
		public void addDocumentListener(DocumentListener arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public void addUndoableEditListener(UndoableEditListener arg0) {
			// TODO Auto-generated method stub

		}

		@Override
		public Position createPosition(int arg0) throws BadLocationException {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Element getDefaultRootElement() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Position getEndPosition() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public int getLength() {
			// TODO Auto-generated method stub
			return 0;
		}

		@Override
		public Object getProperty(Object arg0) {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Element[] getRootElements() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public Position getStartPosition() {
			// TODO Auto-generated method stub
			return null;
		}

		@Override
		public String getText(int offset, int length) throws BadLocationException {
			// TODO Auto-generated method stub
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
		public void putProperty(Object key, Object value) {
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

	}
}
