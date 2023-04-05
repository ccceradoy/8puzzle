package algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import game.Puzzle;

public class BFS extends Traversal {
	private Queue<State> frontier;
	private ArrayList<State> exploredState;

	public BFS(Puzzle puzzle) {
		super(puzzle);
		frontier = new LinkedList<State>();
		exploredState = new ArrayList<State>();
	}

	@Override
	public ArrayList<String> solve() {
		frontier.add(initialState);
		while (!frontier.isEmpty()) {
			State currentState = frontier.remove();
			if (currentState.goalTest()) {
				whenGoalTest(currentState);
				return solution;
			}

			int count = currentState.getChildNum();
			for (int i = 0; i < count; i++) {
				State child = new State(currentState, i);
				if (listContains(exploredState, child) || listContains((List<State>) frontier, child)) {
					continue;
				}
				frontier.add(child);
			}
			exploredState.add(currentState);
			exploredNum++;
		}
		return null;
	}
}