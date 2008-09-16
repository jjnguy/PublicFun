package piece;

import java.awt.Color;
import java.awt.Image;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

@SuppressWarnings("serial")
public class CheckerPiece extends GenericGamePiece {

	private CheckerPiece(Color c1, Color c2) {
		super(c1, c2);
	}

	// TODO implement graphics for king checkers

	public static CheckerPiece RED() {
		return new CheckerPiece(new Color(128, 0, 0), Color.BLACK);
	}

	public static CheckerPiece RED_KING() {
		// InputStream in = getClass().getClassLoader().getResourceAsStream(
		// "CrownCheckerPattern2.JPG");
		Image i = null;
		try {
			i = ImageIO.read(new File("CrownCheckerPattern2.JPG"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new CheckerPiece(new Color(128, 0, 0), Color.BLUE);
	}

	public static CheckerPiece BLACK() {
		return new CheckerPiece(Color.BLACK, new Color(128, 0, 0));
	}

	public static CheckerPiece BLACK_KING() {
		InputStream in = new String().getClass().getClassLoader().getResourceAsStream(
				"CrownCheckerPattern2.jpg");
		Image i = null;
		try {
			i = ImageIO.read(in);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return new CheckerPiece(Color.BLACK, new Color(128, 0, 0));
	}

	@Override
	public String toString() {
		if (innerColor().equals(Color.BLACK))
			return "Black Piece";
		else
			return "Red Piece";
	}
}
