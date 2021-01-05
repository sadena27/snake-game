import java.util.Random;

public class SnakeGame {
	
	protected static final int LEFT = 1;
	protected static final int UP = 2;
	protected static final int RIGHT = 3;
	protected static final int DOWN = 4;
	private static final int MAX_LENGTH = 529;
	private static final int rows = 23, cols = 23;
	
	private BoardCell[][] board;
	private Coordinate[] snakeCoordinates;
	private int score, length, direction;
	private Coordinate apple;
	private boolean hitTail;
	
	public SnakeGame() {
		board = new BoardCell[rows][cols];
		snakeCoordinates = new Coordinate[MAX_LENGTH];
		score = 0;
		direction = RIGHT;
		hitTail = false;
		
		// Initial apple location
		apple = new Coordinate(12, 17, 0);
		
		setBoardWithColor(BoardCell.BLACK);
		setApple();
		
		// Snake starting position with a length of 2
		snakeCoordinates[0] = new Coordinate(12, 5, RIGHT);
		snakeCoordinates[1] = new Coordinate(12, 4, RIGHT);
		length = 2;
		setSnake();
	}

	public int getRows() {
		return rows;
	}
	
	public int getCols() {
		return cols;
	}
	
		public int getScore() {
			return score;
	}
		
	public int getDirection() {
		return direction;
	}
	
	public BoardCell getBoardCell(int rowIndex, int colIndex) {
		return board[rowIndex][colIndex];
	}
	
	private void setBoardWithColor(BoardCell cell) {
		for (int row = 0; row < rows; row++) {
			for (int col = 0 ; col < cols; col++) {
				board[row][col] = cell;
			}
		}
	}
	
	public void setDirection(int direction) {
		this.direction = direction;
	}
	
	// Sets apple coordinate's board cell with the color red
	private void setApple() {
		board[apple.getRow()][apple.getCol()] = BoardCell.RED;
	}
	
	// Sets the snake coordinates' board cells with the color green
	private void setSnake() {
		for (int i = 0; i < length; i++) {
			Coordinate coordinate = snakeCoordinates[i];
			int row = coordinate.getRow();
			int col = coordinate.getCol();
			if (i == 0) {
				// The snake's head will be a lighter green than its body
				board[row][col] = BoardCell.GREEN;
			} else {
				board[row][col] = BoardCell.DARK_GREEN;
			}
		}
	}
	
	// Moves the snakes coordinates in the current direction
	public void moveSnake() {
		Coordinate tail = snakeCoordinates[length - 1];
		int tailRow = tail.getRow();
		int tailCol = tail.getCol();
		
		int row = snakeCoordinates[0].getRow();
		int col = snakeCoordinates[0].getCol();

		for (int i = length - 1; i >= 1; i--) {
			snakeCoordinates[i] = snakeCoordinates[i - 1];
		}

		switch(direction) {
			case LEFT:
				snakeCoordinates[0] = new Coordinate(row, col - 1, LEFT);
				break;
			case UP:
				snakeCoordinates[0] = new Coordinate(row - 1, col, UP);
				break;
			case RIGHT:
				snakeCoordinates[0] = new Coordinate(row, col + 1, RIGHT);
				break;
			case DOWN:
				snakeCoordinates[0] = new Coordinate(row + 1, col, DOWN);
				break;		
		}
		
		// If the new head was on the tail
		if (snakeCoordinates[0].equals(tail)) {
			hitTail = true;
		}
		
		/* 
		 * Only resets the board with the new snake coordinates if the game
		 * isn't over
		 */
		if (!isGameOver()) {
			board[tailRow][tailCol] = BoardCell.BLACK;
			checkIfAppleAndGrow();
			setSnake();
		}
	}
	
	// Checks if the head is on an apple and then grows the snake by a length of 1
	private void checkIfAppleAndGrow() {
		if (snakeCoordinates[0].equals(apple)) {
			Coordinate tail = snakeCoordinates[length - 1];
			int row = tail.getRow();
			int col = tail.getCol();
			int direction = tail.getDirection();
			
			/*
			 * Adds the tail coordinate in the appropriate spot depending on
			 * the direction the snake is moving in.
			 */
			switch(direction) {
				case LEFT:
					snakeCoordinates[length] = new Coordinate(row, col + 1, direction);
					break;
				case UP:
					snakeCoordinates[length] = new Coordinate(row + 1, col, direction);
					break;
				case RIGHT:
					snakeCoordinates[length] = new Coordinate(row, col - 1, direction);
					break;
				case DOWN:
					snakeCoordinates[length] = new Coordinate(row - 1, col, direction);
					break;		
			}

			score++;
			length++;
			spawnNewApple();
		}
	}
	
	// Spawns a new apple on the board
	private void spawnNewApple() {
		Random random = new Random();
		apple = new Coordinate(random.nextInt(rows), random.nextInt(cols), 0);
		// If the new apple coordinates are on the snake choose new coordinates
		while (appleInSnake()) {
			spawnNewApple();
		}
		setApple();
	}
	
	// Checks if the apple coordinates are on the snake
	private boolean appleInSnake() {
		for (int i = 0; i < length; i++) {
			if (snakeCoordinates[i].equals(apple)) {
				return true;
			}
		}
		return false;
	}
 	
	public boolean isGameOver() {
		return hitWall() || hitSnake();
	}
	
	// Returns whether the snake has hit the wall
	private boolean hitWall() {
		int row = snakeCoordinates[0].getRow();
		int col = snakeCoordinates[0].getCol();
		return row < 0 || row >= rows || col < 0 || col >= cols;
	}
	
	// Returns whether the snake has hit itself
	private boolean hitSnake() {
		for (int i = 1; i < length; i++) {
			if (snakeCoordinates[0].equals(snakeCoordinates[i])) {
				return true;
			}
		}
		return hitTail;
	}
	
}
