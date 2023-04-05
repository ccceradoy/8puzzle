package game;

import java.util.ArrayList;
import java.util.Arrays;

import javafx.event.EventHandler;
import javafx.scene.control.Button;
import javafx.scene.input.MouseEvent;

class Cell {
	private int index;
	private int content;
	private Puzzle puzzle;
	private ArrayList<Integer> moves;
	private Button button;

	Cell(int index, int content, Puzzle puzzle) {
		this.index = index;
		this.content = content;
		this.puzzle = puzzle;
		moves = new ArrayList<Integer>();
		button = content == 0 ? new Button("") : new Button(Integer.toString(content));
		setEventHandler();
	}

	private void setEventHandler() {
		button.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouseEvent) {
        if (content == 0) return;

        scanPossibleMoves();
        makeMove();
        moves.clear();
      }
    });
	}

	public void scanPossibleMoves() {
		// row
		if (index >= 0 && index <= 2) {
			// can move down
			moves.add(3);
		} else if (index >= 6 && index <= 8) {
			// can move up
			moves.add(-3);
		} else {
			moves.add(3);
			moves.add(-3);
		}

		if (index == 0 || index == 3 || index == 6) {
			// can move right
			moves.add(1);
		} else if (index == 2 || index == 5 || index == 8) {
			// can move left
			moves.add(-1);
		} else {
			moves.add(1);
			moves.add(-1);
		}
	}

	private void makeMove() {
		for (Integer i : moves) {
			Cell neighbor = puzzle.getCell(index + i);

			if (neighbor.content == 0) {
				swapWith(neighbor);

				// Check if the game is won
        if (Arrays.equals(Puzzle.WINNING_STATE, puzzle.getCellsAsNum())) {
          puzzle.setSolvablePrompt();
        }
        return;
			}
		}
	}

	void swapWith(Cell c) {
		c.setContent(content);
		c.puzzle.getCellsAsNum()[c.index] = content;
		c.button.setText(Integer.toString(content));

		content = 0;
		puzzle.getCellsAsNum()[index] = 0;
		button.setText("");
	}

	void setContent(int content) {
		this.content = content;
  }

	int getContent() {
		return content;
	}

	Button getButton() {
		return button;
	}

	int getIndex() {
		return index;
	}
}