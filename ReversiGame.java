import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.IOException;

public class ReversiGame {

	protected BufferedReader reader;
	protected ReversiBoard board;
	protected char currentTurn;
	protected char enemyTurn;

	public String getMove() {
		String move = "";
		try {
			move = reader.readLine();
		} catch (IOException e) {
			System.out.println(e.getMessage());
		}
		return move; 
	}

	// GAME LOGIC
	public void play() {
		String input;
		Point point;
		while (true) {
			board.printBoard();
			if (board.getLiveCells(currentTurn, enemyTurn).size() == 0) {
				System.out.printf("%s HAS WON\n", enemyTurn);
				return;
			}
			System.out.printf("CURRENT TURN: %s\n", currentTurn);
			input = getMove();
			point = new Point(input);
			if (board.canPlaceAt(currentTurn, enemyTurn, point)) {
				board.placeAt(currentTurn, point);
				System.out.printf("SUCCESSFULLY PLACED %s at %s\n", currentTurn, point);
				swapTurn();
			} else {
				System.out.printf("FAILED TO PLACED %s at %s\n", currentTurn, point);
			}
		}
	}

	public void swapTurn() {
		char temp = enemyTurn;
		enemyTurn = currentTurn;
		currentTurn = temp;
	}

	// CONSTRUCTOR
	public ReversiGame() {
		reader = new BufferedReader(new InputStreamReader(System.in));
		board = new ReversiBoard();
		currentTurn = 'B';
		enemyTurn = 'W';
	}

	// MAIN
	public static void main(String[] args) {
		ReversiGame game = new ReversiGame();
		game.play();
	}

}