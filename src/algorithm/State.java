package algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;

import game.Puzzle;

class State {
	// same as the cellsAsNum[] in puzzle
	private int[] configuration;
	private int childNum;
	private ArrayList<Integer> moves;
	private State parent;
	private int ithChild;

	private int h; // computed from manhattan
	private int g; // path cost from init state;
	private int f;

	public final static HashMap<Integer, ArrayList<Integer>> manhattanConfig = new HashMap<Integer, ArrayList<Integer>>(){{
		put(1, new ArrayList<Integer>(Arrays.asList(0, 0)));
		put(2, new ArrayList<Integer>(Arrays.asList(0, 1)));
		put(3, new ArrayList<Integer>(Arrays.asList(0, 2)));
		put(4, new ArrayList<Integer>(Arrays.asList(1, 0)));
		put(5, new ArrayList<Integer>(Arrays.asList(1, 1)));
		put(6, new ArrayList<Integer>(Arrays.asList(1, 2)));
		put(7, new ArrayList<Integer>(Arrays.asList(2, 0)));
		put(8, new ArrayList<Integer>(Arrays.asList(2, 0)));
	}};

	private HashMap<Integer, ArrayList<Integer>> configDistance;

	State(int[] configuration) {
		this.configuration = configuration;
		childNum = countChild(getBlankIndex());
		moves = new ArrayList<Integer>();
		parent = null;

		setAvailableMoves();

		int h = 0;

		setConfigDistance();
		computeF();
	}

	State(State parent, int ithChild) {
		this.parent = parent;
		this.ithChild = ithChild;
		configuration = new int[Puzzle.LENGTH];
		moves = new ArrayList<Integer>();
		setConfiguration(parent);

		int h = 0;

		setConfigDistance();
		computeF();
	}

	private void setConfiguration(State parent) {
		configuration = parent.configuration.clone();
		// get the ith index in the arraylist of moves
		int move = parent.moves.get(ithChild);
		int blankIndex = parent.getBlankIndex();

		int indexToSwap = 0;
		switch (move) {
			case -3:
				indexToSwap = blankIndex-3;
				break;
			case 1:
				indexToSwap = blankIndex+1;
				break;
			case 3:
				indexToSwap = blankIndex+3;
				break;
			case -1:
				indexToSwap = blankIndex-1;
				break;
		}
		configuration[blankIndex] = parent.configuration[indexToSwap];
		configuration[indexToSwap] = 0;
		setAvailableMoves();

		// childCount;
		childNum = countChild(indexToSwap);
	}

	private void setConfigDistance() {
		configDistance = new HashMap<Integer, ArrayList<Integer>>();
		int row = 0, col = 0;
		for (int i = 0; i < Puzzle.LENGTH; i++) {

			if (configuration[i] == 0) {
				col++;
				if (i == 2 || i == 5) {
			  	row++;
			  	col = 0;
			  }
				continue;
			}

			configDistance.put(configuration[i], new ArrayList<Integer>(Arrays.asList(row, col)));

			col++;
		  if (i == 2 || i == 5) {
		  	row++;
		  	col = 0;
		  }
		}
	}

	private void computeF() {
		computeH();
		computeG();
		f = g + h;
	}

	private void computeG() {
		State s = this;
		while (s.parent != null) {
			g++;
			s = s.parent;
		}
	}

	private void computeH() {
		for (int i = 0; i < Puzzle.LENGTH; i++) {
			if (configuration[i] == 0) continue;
			h += computeManhattan(configuration[i]);
		}
	}

	private int computeManhattan(int i) {
		int xFromManhattan = State.manhattanConfig.get(i).get(0);
		int yFromManhattan = State.manhattanConfig.get(i).get(1);
		int xFromConfig = configDistance.get(i).get(0);
		int yFromConfig = configDistance.get(i).get(1);

		int x = Math.abs(xFromConfig - xFromManhattan);
		int y = Math.abs(yFromConfig - yFromManhattan);

		return x + y;
	}

	int getF() {
		return f;
	}


	private int getBlankIndex() {
		for (int i = 0; i < Puzzle.LENGTH; i++) {
			if (configuration[i] == 0) {
				return i;
			}
		}
		return -1;
	}

	// count how many moves from the index
	private int countChild(int index) {
		if (index == 4) return 4;
		if (index == 0 || index == 2 || index == 6 || index == 8) return 2;
		return 3;
	}

	private void setAvailableMoves() {
		int index = getBlankIndex();

		if (index >= 3 && index <= 8) moves.add(-3);

		if (index == 0 || index == 1 || index == 3 || index == 4
				|| index == 6 || index == 7) moves.add(1);

		if (index >= 0 && index <= 5) moves.add(3);

		if (index == 1 || index == 2 || index == 4 || index == 5
				|| index ==7 || index == 8) moves.add(-1);
	}

	boolean goalTest() {
		return Arrays.equals(configuration, Puzzle.WINNING_STATE);
	}

	State getParent() {
		return parent;
	}

	String getMove() {
		int move = parent.moves.get(ithChild);
		String s = null;
		switch(move) {
			case -3:
				s = "U";
				break;
			case 1:
				s = "R";
				break;
			case 3:
				s = "D";
				break;
			case -1:
				s = "L";
				break;
		}
		return s;
	}

	int getChildNum() {
		return childNum;
	}

	int getG() {
		return g;
	}

	int[] getConfiguration() {
		return configuration;
	}

	@Override
	public String toString() {
		return Arrays.toString(configuration);
	}
}