package genericComponents;
public class Difficulty {

	private int width, height, numMines;

	public Difficulty(int widhtP, int heightP, int numMinesP) {
		width = widhtP;
		height = heightP;
		numMines = numMinesP;
	}

	public int width() {
		return width;
	}

	public int height() {
		return height;
	}

	public int mines() {
		return numMines;
	}

	
	/* (non-Javadoc)
	 * @see java.lang.Object#hashCode()
	 */
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + height;
		result = prime * result + numMines;
		result = prime * result + width;
		return result;
	}

	/* (non-Javadoc)
	 * @see java.lang.Object#equals(java.lang.Object)
	 */
	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Difficulty other = (Difficulty) obj;
		if (height != other.height)
			return false;
		if (numMines != other.numMines)
			return false;
		if (width != other.width)
			return false;
		return true;
	}

	public static final Difficulty EASY = new Difficulty(9, 9, 10);
	public static final Difficulty INTERMEDIATE = new Difficulty(16, 16, 40);
	public static final Difficulty EXPERT = new Difficulty(32, 16, 99);
}
