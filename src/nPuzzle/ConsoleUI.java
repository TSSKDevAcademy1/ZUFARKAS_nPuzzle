package nPuzzle;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class ConsoleUI {

	private Field field;

	private BufferedReader input = new BufferedReader(new InputStreamReader(System.in));

	public void run() {
		loadOrCreateGame();
		do {
			print();
			System.out.println();
			processInput();
		} while (!field.isSolved());
		print();
		System.out.println("Congratulation, You won!");

	}

	private void loadOrCreateGame() {
		try {
			field = Field.load();
		} catch (ClassNotFoundException | IOException e) {
			System.err.println("Nepodarilo sa nacitat hru");
			newGame();
			e.printStackTrace();
		}
	}

	private void newGame() {
		field = new Field(4, 4);
	}

	private void print() {
		int time = field.getPlayingSeconds();
		System.out.printf("Playing time is %d:%2d", time, time % 60);
		for (int r = 0; r < field.getRowCount(); r++) {
			System.out.println("\n");
			for (int c = 0; c < field.getColumnCount(); c++) {
				int tile = field.getTile(r, c);
				if (tile == 0) {
					System.out.printf("%5s", " ");
				} else {
					System.out.printf("%5d", tile);
				}
			}
		}
	}

	private void processInput() {

		System.out.println("Please, enter input. \n a|left, d|right, w|up, s|down, n|new, x|exit");

		String input = readLine().toLowerCase().trim();

		switch (input) {
		case "a":
		case "left":
			field.left();
			break;
		case "d":
		case "right":
			field.right();
			break;
		case "w":
		case "up":
			field.up();
			break;
		case "s":
		case "down":
			field.down();
			break;
		case "n":
		case "new":
			newGame();
			break;
		case "x":
		case "exit":
			saveGame();
			System.exit(0);
			break;
		default:
			System.err.println("Wrong input.");
		}
	}

	private String readLine() {
		try {
			return input.readLine();
		} catch (IOException e) {
			return null;
		}
	}

	private void saveGame() {
		try {
			field.save();
		} catch (IOException e) {
			System.err.println("Nepodarilo sa ulozit rozohranu hru");
			e.printStackTrace();
		}

	}
}