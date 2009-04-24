package edu.cs319.client.customcomponents.syntaxhighlighting;

import java.awt.Color;
import java.awt.Font;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import javax.swing.text.AttributeSet;

public class KeywordAtributeSet implements AttributeSet {

	private KeywordColorAtribute color;
	private KeywordFontAtribute font;

	private Map<String, Object> attributes;

	public KeywordAtributeSet() {
		attributes = new HashMap<String, Object>();
		attributes.put("color", color.getColor());
		attributes.put("font", font.getFont());
	}

	public static class KeywordColorAtribute implements AttributeSet.ColorAttribute {
		public Color getColor() {
			return Color.BLUE;
		}
	}

	public static class KeywordFontAtribute implements AttributeSet.FontAttribute {
		public Font getFont() {
			return new Font("Courier New", Font.BOLD, 12);
		}
	}

	@Override
	public boolean containsAttribute(Object name, Object value) {
		return attributes.containsKey(name);
	}

	@Override
	public boolean containsAttributes(AttributeSet attributes) {
		return attributes.equals(this);
	}

	@Override
	public AttributeSet copyAttributes() {
		return new KeywordAtributeSet();
	}

	@Override
	public Object getAttribute(Object key) {
		return attributes.get(key);
	}

	@Override
	public int getAttributeCount() {
		return attributes.size();
	}

	@Override
	public Enumeration<?> getAttributeNames() {
		return new AtSetEnum(attributes);
	}

	@Override
	public AttributeSet getResolveParent() {
		return null;
	}

	@Override
	public boolean isDefined(Object attrName) {
		return attributes.keySet().contains(attrName);
	}

	@Override
	public boolean isEqual(AttributeSet attr) {
		return this.equals(attr);
	}

	class AtSetEnum implements Enumeration<Object> {

		Iterator<Object> it;

		public AtSetEnum(Map<?, Object> stuff) {
			it = stuff.values().iterator();
		}

		@Override
		public boolean hasMoreElements() {
			return it.hasNext();
		}

		@Override
		public Object nextElement() {
			return it.next();
		}

	}
}
