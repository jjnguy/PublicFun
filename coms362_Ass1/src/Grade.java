public enum Grade {

	A("A"), A_M("A-"), A_P("A+"), B("B"), B_M("B-"), B_P("B+"), C("C"), C_M("C-"), C_P("C+"), D(
			"D"), D_M("D-"), D_P("D+"), F("F");

	private String str;

	private Grade(String str) {
		this.str = str;
	}

	public String toString() {
		return str;
	}

	public static Grade stringToGrade(String gradeStr) {
		if (gradeStr.equalsIgnoreCase("A")) {
			return Grade.A;
		}
		if (gradeStr.equalsIgnoreCase("A-")) {
			return Grade.A_M;
		}
		if (gradeStr.equalsIgnoreCase("A+")) {
			return Grade.A_P;
		}
		if (gradeStr.equalsIgnoreCase("B")) {
			return Grade.B;
		}
		if (gradeStr.equalsIgnoreCase("B-")) {
			return Grade.B_M;
		}
		if (gradeStr.equalsIgnoreCase("B+")) {
			return Grade.B_P;
		}
		if (gradeStr.equalsIgnoreCase("C")) {
			return Grade.C;
		}
		if (gradeStr.equalsIgnoreCase("C-")) {
			return Grade.C_M;
		}
		if (gradeStr.equalsIgnoreCase("C+")) {
			return Grade.C_P;
		}
		if (gradeStr.equalsIgnoreCase("D")) {
			return Grade.D;
		}
		if (gradeStr.equalsIgnoreCase("D-")) {
			return Grade.D_M;
		}
		if (gradeStr.equalsIgnoreCase("D+")) {
			return Grade.D_P;
		}
		if (gradeStr.equalsIgnoreCase("F")) {
			return Grade.F;
		}
		throw new IllegalArgumentException("The String: '" + gradeStr
				+ "' is not recognized as a grade.");
	}
}
