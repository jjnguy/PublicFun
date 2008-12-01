import javax.swing.JFrame;

public class GravityFrame extends JFrame {

	private GravityPane gravPane;

	public GravityFrame() {
		super();
		setDefaultCloseOperation(EXIT_ON_CLOSE);
		gravPane = new GravityPane();
		add(gravPane);
		gravPane.addObject(new GravitySphere(200, 200));
		setVisible(true);
		pack();
		gravPane.run();
	}

	public static void main(String[] args) {
		new GravityFrame();
	}
}
