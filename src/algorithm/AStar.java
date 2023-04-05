package algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import game.Puzzle;

public class AStar extends Traversal {
	private ArrayList<State> openList; // like frontier
	private ArrayList<State> closedList; // like exploredstate

	public AStar(Puzzle puzzle) {
		super(puzzle);
		openList = new ArrayList<State>();
		closedList = new ArrayList<State>();
	}

	public ArrayList<String> solve() {
		openList.add(initialState);
		while (!openList.isEmpty()) {
			State bestState = openList.remove(getMinFIndex());
			closedList.add(bestState);
			exploredNum++;
			if (bestState.goalTest()) {
				whenGoalTest(bestState);
				return solution;
			}

			int count = bestState.getChildNum();
			for (int i = 0; i < count; i++) {
				State child = new State(bestState, i);
				if ((listContains(openList, child) || listContains(closedList, child))
						|| (listContains(openList, child) && child.getG() >= getDuplicate(child).getG())) {
					continue;
				}
				openList.add(child);
			}
		}
		return null;
	}

	private State getDuplicate(State state) {
		for (State s : openList) {
			if (Arrays.equals(state.getConfiguration(), s.getConfiguration())) {
				return s;
			}
		}
		return null;
	}


	// compute for manhattan distance
	private int getMinFIndex() {
		int leastF = openList.get(0).getF();
		for (State s : openList) {
			if (s.getF() < leastF) {
				leastF = s.getF();
			}
		}

		for (State s : openList) {
			if (s.getF() == leastF) {
				return openList.indexOf(s);
			}
		}
		return -1;
	}
}
