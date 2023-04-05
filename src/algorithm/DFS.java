package algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Stack;

import game.Puzzle;

public class DFS extends Traversal {
	private Stack<State> frontier;
	private ArrayList<State> exploredState;

	public DFS(Puzzle puzzle) {
		super(puzzle);
		frontier = new Stack<State>();
		exploredState = new ArrayList<State>();
	}

	public ArrayList<String> solve() {
		frontier.add(initialState);
		while (!frontier.isEmpty()) {
			State currentState = frontier.pop();
			if (currentState.goalTest()) {
				whenGoalTest(currentState);
				return solution;
			} else {
				if (!listContains(exploredState, currentState)) {
					exploredState.add(currentState);
					exploredNum++;
					int count = currentState.getChildNum();
					for (int i = 0; i < count; i++) {
						State child = new State(currentState, i);
						if (!listContains((List<State>) frontier, child)) {
							frontier.add(child);
						}
					}
				}
			}
		}
		return null;
	}
}