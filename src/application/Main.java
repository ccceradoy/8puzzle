package application;

import game.Puzzle;
import javafx.application.Application;
import javafx.stage.Stage;


public class Main extends Application {
	public static void main(String[] args) {
		launch(args);
	}

	public void start(Stage stage) {
		Puzzle p = new Puzzle();
		p.setStage(stage);
	}
}