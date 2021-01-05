import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.FontMetrics;
import java.awt.Graphics;
import java.awt.Graphics2D;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import javax.swing.JComponent;
import javax.swing.JFrame;

public class GameGUI extends JComponent implements Runnable {
	
	private static final long serialVersionUID = 1L; 
	private static final int CELL_DIMENSION = 25;
	private static int highScore = 0;
	private JFrame frame;
	private SnakeGame game;
	private Thread gameThread;
	private volatile boolean paused;
	
	public GameGUI(JFrame frame) {
		paused = false;
		game = new SnakeGame();
		this.frame = frame;
		
		setPreferredSize(new Dimension((game.getCols() + 2) * CELL_DIMENSION,
						 (game.getRows()) * CELL_DIMENSION));
		
		addMouseListener(new MouseAdapter() {
            @Override
            public void mousePressed(MouseEvent e) {
                if (gameThread == null) {
                	startNewGame();
                	repaint();
                }
            }
         });
		
		addKeyListener(new KeyAdapter() {
        	@Override
     		public void keyPressed(KeyEvent e) {
        		int direction = game.getDirection();
        		/*
        		 * Only changes the direction if the new direction is
        		 * not in the opposite direction of the old direction
        		 */
     			switch(e.getKeyCode()) {
     				case KeyEvent.VK_DOWN:
     					if (direction != SnakeGame.UP) {
     						direction = SnakeGame.DOWN;
     					}
     	                break;
     	            case KeyEvent.VK_RIGHT:
     	            	if (direction != SnakeGame.LEFT) {
     	                	direction = SnakeGame.RIGHT;
     	                }
     	                break;
     	            case KeyEvent.VK_LEFT:
     	                if (direction != SnakeGame.RIGHT) {
     	                	direction = SnakeGame.LEFT;
     	                }
     	                break;
     	            case KeyEvent.VK_UP:
     	                if (direction != SnakeGame.DOWN) {
     	                	direction = SnakeGame.UP;
     	                }
     	                break;
     	            case KeyEvent.VK_SPACE:
     	            	if (gameThread != null) {
     	            		paused = !paused;
     	            	}
     	            	break;
     			}
     			game.setDirection(direction);
     			// Only moves the snake if the direction has changed
     			if (e.getKeyCode() - 36 != direction) {
         			game.moveSnake();
     			}
     			repaint();
            }
         });
	}
	
	public void run() {
		 while (Thread.currentThread() == gameThread) {
			 try {
	        	 Thread.sleep(Math.max(175 - 5 * game.getScore(), 75));
	         } catch (InterruptedException e) {
	        	 return;
	         }
	 
	         if (game.isGameOver()) {
	        	 endGame();
	         } else if (!paused) {
	        	 game.moveSnake();
	         }
	         repaint();
	     }
	}
	
	protected void paintComponent(Graphics g) {
		Graphics2D g2d = (Graphics2D) g;

		// Sets the background
		g2d.setColor(Color.GRAY);
		g2d.fillRect(0, 0, getWidth(), getHeight());

		if (gameThread == null) {
			drawStartScreen(g2d);
		} else if (paused) {
			drawPausedScreen(g2d);
		} else {
			drawBoard(g2d);
			drawScore(g2d);
		}
	}
	
	private void drawStartScreen(Graphics2D g) {
		Font font = getFont().deriveFont(Font.BOLD, 50);
		FontMetrics metrics = g.getFontMetrics(font);
		String text = "Snake Game";
		int x = (frame.getWidth() - metrics.stringWidth(text)) / 2;
		int y = ((frame.getHeight() - metrics.getHeight()) / 2)
				+ metrics.getAscent() - 70;
		g.setColor(Color.GREEN.darker());
	 	g.setFont(font);
	 	g.drawString(text, x, y);
	 	
	 	font = getFont().deriveFont(Font.BOLD, 18);
	 	metrics = g.getFontMetrics(font);
	 	text = "Click to play";
		x = (frame.getWidth() - metrics.stringWidth(text)) / 2;
		y += 30;
		g.setColor(Color.BLACK);
		g.setFont(font);
		g.drawString(text, x, y);
		
	 	text = "Press space to pause";
		x = (frame.getWidth() - metrics.stringWidth(text)) / 2;
		y += 30;
		g.setColor(Color.BLACK);
		g.setFont(font);
		g.drawString(text, x, y);
	 }
	
	private void drawPausedScreen(Graphics2D g) {
		Font font = getFont().deriveFont(Font.BOLD, 50);
		FontMetrics metrics = g.getFontMetrics(font);
		String text = "PAUSED";
		int x = (frame.getWidth() - metrics.stringWidth(text)) / 2;
		int y = ((frame.getHeight() - metrics.getHeight()) / 2) + 
				metrics.getAscent() - 20;
		g.setColor(Color.RED);
	 	g.setFont(font);
	 	g.drawString(text, x, y);	
	 	
		font = getFont().deriveFont(Font.BOLD, 18);
	 	metrics = g.getFontMetrics(font);
	 	text = "Press space to resume";
		x = (frame.getWidth() - metrics.stringWidth(text)) / 2;
		y += 30;
		g.setColor(Color.BLACK);
		g.setFont(font);
		g.drawString(text, x, y);
	}
	
	private void drawScore(Graphics2D g) {
        int height = getHeight();
        g.setFont(getFont().deriveFont(Font.BOLD, 18));
        g.setColor(getForeground());
    	if (game.getScore() > highScore) {
			highScore = game.getScore();
		}
        String scoreText = "High score: " + highScore + "     Score: "
        				   + game.getScore();
        g.drawString(scoreText, 25, height - 7);
    }
	
	private void drawBoard(Graphics2D g) {
		Graphics g2d = (Graphics2D) g;
		
		BoardCell[][] board = getBoard(game);
		for (int row = 0; row < board.length; row++) {
			for (int col = 0; col < board[row].length; col++) {
				g2d.setColor(board[row][col].getColor());
				g2d.fill3DRect((col + 1) * CELL_DIMENSION, (row + 1) *
					CELL_DIMENSION, CELL_DIMENSION, CELL_DIMENSION, true);
			}
		}
	}
	
	private BoardCell[][] getBoard(SnakeGame game) {
		int rows = game.getRows();
		int cols = game.getCols();
		BoardCell[][] board = new BoardCell[rows][cols];
		
		for (int row=0; row < rows; row++) {
			for (int col=0; col < cols; col++) {
				board[row][col] = game.getBoardCell(row, col);
			}
		}
		return board;
	}
	
	private void endGame() {
		if (gameThread != null) {
	         Thread temp = gameThread;
	         gameThread = null;
	         temp.interrupt();
	    }
	}
	
 	private void startNewGame() {		
		/* 
		 * Creates a new game thread with the same GameGUI but sets the
		 * game variable to a new SnakeGame
		 */
		this.game = new SnakeGame();
		gameThread = new Thread(this);
		gameThread.start();
	}

}
