import javax.swing.*;

public class Main {
	public static void main(String[] args) {
		JFrame fr = new JFrame();
		Screen sc = new Screen();
		fr.add(sc);
		fr.setDefaultCloseOperation(WindowConstants.EXIT_ON_CLOSE);
		fr.pack();
		fr.setLocationRelativeTo(null);
		fr.setVisible(true);
		sc.animate();
	}
}