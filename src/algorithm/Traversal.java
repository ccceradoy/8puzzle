package algorithm;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import game.Puzzle;


public abstract class Traversal {
	protected State initialState;

	protected ArrayList<String> solution;
	protected int solutionIndex;

	protected int exploredNum;

	public Traversal(Puzzle puzzle) {
		initialState = new State(puzzle.getCellsAsNum().clone());
		solution = new ArrayList<String>();
	}

	protected abstract ArrayList<String> solve();

	protected void whenGoalTest(State s) {
		while (s.getParent() != null) {
			solution.add(s.getMove());
			s = s.getParent();
		}
		Collections.reverse(solution);
	}

	protected boolean listContains(List<State> list, State state) {
		if (list.size() == 0) return false;

		for (State s : list) {
			if (Arrays.equals(s.getConfiguration(), state.getConfiguration()))
				return true;
		}
		return false;
	}


	public int getSolutionIndex() {
		return solutionIndex;
	}

	public void setSolutionIndex() {
		solutionIndex++;
	}

	public ArrayList<String> getSolution() {
		return solution;
	}
}

