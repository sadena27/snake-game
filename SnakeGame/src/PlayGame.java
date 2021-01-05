import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.SwingUtilities;

public class PlayGame {
	
	public static void main(String[] args) {
		// Uses a lambda expression to execute a thread to play the game
		SwingUtilities.invokeLater(() -> {
			JFrame frame = new JFrame("Snake Game");
			GameGUI gameGUI = new GameGUI(frame);

			frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
			frame.add(gameGUI, BorderLayout.CENTER);
			gameGUI.setFocusable(true);
			gameGUI.requestFocusInWindow();
			frame.pack();
			frame.setLocationRelativeTo(null);
			frame.setVisible(true);
		});
	}

}
