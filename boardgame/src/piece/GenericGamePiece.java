package piece;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.Stroke;

import javax.swing.BorderFactory;
import javax.swing.JPanel;

import board.BoardSquare;

@SuppressWarnings("serial")
public class GenericGamePiece extends JPanel {

	private Color innerColor, outerColor;

	public GenericGamePiece() {
		this(Color.BLACK, Color.WHITE);
	}

	public GenericGamePiece(Color innercolorP, Color outerColorP) {
		innerColor = innercolorP;
		outerColor = outerColorP;
		setBorder(BorderFactory.createEmptyBorder());
		setPreferredSize(new Dimension(BoardSquare.CELL_WIDTH, BoardSquare.CELL_WIDTH));
		setOpaque(false);
	}

	public Color innerColor() {
		return innerColor;
	}

	@Override
	protected void paintComponent(Graphics g) {
		super.paintComponent(g);
		Color c = g.getColor();
		g.setColor(innerColor);
		g.fillOval(0, 0, BoardSquare.CELL_WIDTH, BoardSquare.CELL_WIDTH);
		g.setColor(outerColor);
		Graphics2D g2 = (Graphics2D) g;
		Stroke s = g2.getStroke();
		g2.setStroke(new BasicStroke(2));
		g.drawOval(2, 2, BoardSquare.CELL_WIDTH - 4, BoardSquare.CELL_WIDTH - 4);
		g.setColor(c);
		g2.setStroke(s);
	}

	public String toString(){
		return innerColor.toString() + outerColor.toString();
	}
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((innerColor == null) ? 0 : innerColor.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		GenericGamePiece other = (GenericGamePiece) obj;
		if (innerColor == null) {
			if (other.innerColor != null)
				return false;
		} else if (!innerColor.equals(other.innerColor))
			return false;
		if (outerColor == null) {
			if (other.outerColor != null)
				return false;
		} else if (!outerColor.equals(other.outerColor))
			return false;
		return true;
	}
}
