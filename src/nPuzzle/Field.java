package nPuzzle;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Field implements Serializable {

	private static final long serialVersionUID = 1L;

	/**
	 * Field row count. Rows are indexed from 0 to (rowCount - 1).
	 */
	private final int rowCount;

	/**
	 * Column count. Columns are indexed from 0 to (columnCount - 1).
	 */
	private final int columnCount;

	/**
	 * Playing field tiles.
	 */
	private final int[][] tiles;

	private Position emptyPosition;

	private long startMillis = System.currentTimeMillis();

	private static final String SAVE_FILE = "field.bin";

	/**
	 * Constructor.
	 *
	 * @param rowCount
	 *            row count
	 * @param columnCount
	 *            column count
	 */
	public Field(int rowCount, int columnCount) {
		this.rowCount = rowCount;
		this.columnCount = columnCount;

		tiles = new int[rowCount][columnCount];

		generate();
	}

	public int getRowCount() {
		return rowCount;
	}

	public int getColumnCount() {
		return columnCount;
	}

	/**
	 * Return tile at specified indeces.
	 *
	 * @param row
	 *            row number
	 * @param column
	 *            column number
	 */
	public int getTile(int row, int column) {
		return tiles[row][column];
	}

	public void generate() {
		List<Integer> numbers = new ArrayList<>(rowCount * columnCount);
		for (int i = 0; i < rowCount * columnCount; i++) {
			numbers.add(i);
		}

		Collections.shuffle(numbers);
		int index = 0;
		for (int row = 0; row < rowCount; row++) {
			for (int column = 0; column < columnCount; column++) {
				int value = numbers.get(index++);
				if (value == 0) {
					emptyPosition = new Position(row, column);
				}
				tiles[row][column] = value;
			}
		}
	}

	public boolean isValidPosition(Position position) {
		return position.getRow() >= 0 & position.getRow() < rowCount & position.getColumn() >= 0
				& position.getColumn() < columnCount;
	}

	public int getTile(Position position) {
		return tiles[position.getRow()][position.getColumn()];
	}

	private void setTile(Position position, int value) {
		tiles[position.getRow()][position.getColumn()] = value;
	}

	public void move(int rowOffset, int columnOffset) {
		Position newEmptyPosition = new Position(emptyPosition.getRow() + rowOffset,
				emptyPosition.getColumn() + columnOffset);
		if (isValidPosition(newEmptyPosition)) {
			setTile(emptyPosition, getTile(newEmptyPosition));
			setTile(newEmptyPosition, 0);
			emptyPosition = newEmptyPosition;
		}
	}

	public void left() {
		move(0, 1);
	}

	public void right() {
		move(0, -1);
	}

	public void up() {
		move(1, 0);
	}

	public void down() {
		move(-1, 0);
	}

	public boolean isSolved() {
		int index = 1;
		for (int r = 0; r < rowCount; r++) {
			for (int c = 0; c < columnCount; c++) {
				if (index == rowCount * columnCount) {
					return true;
				}
				if (tiles[r][c] != index) {
					return false;
				}
				index++;
			}
		}
		return false;
	}

	public int getPlayingSeconds() {
		long endTime = System.currentTimeMillis();
		return (int) ((endTime - startMillis) / 1000);
	}

	public void save() throws IOException {
		try (ObjectOutputStream os = new ObjectOutputStream(new FileOutputStream(SAVE_FILE))) {
			os.writeObject(getPlayingSeconds());
			os.writeObject(this);
		}
	}
	
	public static Field load() throws ClassNotFoundException, IOException {
		try(ObjectInputStream is = new ObjectInputStream(new FileInputStream(SAVE_FILE))){
			
			return (Field)is.readObject();
		}
	}
}
