
public class ReflectionTest {
	public static void main(String[] args) throws ClassNotFoundException {
		int i = 56789567;
		byte b1 = (byte) (i & 0x000f);
		byte b2 = (byte) (i >> 1 & 0x000f);
		byte b3 = (byte) (i >> 2 & 0x000f);
		byte b4 = (byte) (i >> 3 & 0x000f);
	}
}

class StaticIntClass {
	public static int i = 0;
	public static int j = 0;
	public static int k = 0;
	public static int l = 0;

	public StaticIntClass() {
	}

	public void method() {

	}
}