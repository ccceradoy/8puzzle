package game;

import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Scanner;

import algorithm.AStar;
import algorithm.BFS;
import algorithm.DFS;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ScrollPane;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.FontWeight;
import javafx.scene.text.Text;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;
import javafx.stage.Stage;

public class Puzzle {
	private VBox layout;
	private FileChooser fileChooser;
	private Button selectorButton;
	private File inputFile;
	private Text solvablePrompt;
	private GridPane board;
	private HBox solutionHBox;
	private ComboBox dropdown;
	private Button solutionButton;
	private Scene scene;
	private HBox solText;

	private Cell[] cells;
	private int[] cellsAsNum;
	private int[] input;
	private int inputCounter;

	private AStar aStar;
	private BFS bfs;
	private DFS dfs;

	public final static int[] WINNING_STATE = {1,2,3,4,5,6,7,8,0};
	public final static int LENGTH = 9;

	public Puzzle() {
		layout = new VBox();
		selectorButton = new Button("Select file...");
		fileChooser = new FileChooser();
		solvablePrompt = new Text();
		board = new GridPane();
		cells = new Cell[Puzzle.LENGTH];
		cellsAsNum = new int[Puzzle.LENGTH];
		input = new int[Puzzle.LENGTH];
		inputCounter = 0;
		solText = new HBox();
	}

	public void setStage(Stage stage) {
		// Set the alignments, spacing, etc. of both the vbox and the gridpane
    setUIStyle();

    // Populate the input array from the file
    readInput();
    // Display the input as a board
    populateBoard();

    boolean isSolvable = checkSolvability();
    solvablePrompt.setText(isSolvable ? "Solvable. You can do this!" :
        "Non-Solvable. No Matter How Hard You Try");

    addSelectorButtonListener(stage);

    layout.getChildren().addAll(selectorButton, solvablePrompt, board);

    if (isSolvable) {
    	setHBox();
    }

    scene = new Scene(layout, 300, 400);
    stage.setTitle("8-Puzzle Game");
    stage.setScene(scene);
    stage.show();
	}

	private void reDraw(Stage stage, File f) {
		layout.getChildren().clear();

    setUIStyle();

    readInput(f);

    populateBoard();

    boolean isSolvable = checkSolvability();
    solvablePrompt.setText(isSolvable ? "Solvable. You can do this!" :
        "Non-Solvable. No Matter How Hard You Try");

    addSelectorButtonListener(stage);

    layout.getChildren().addAll(selectorButton, solvablePrompt, board);

    if (isSolvable) {
    	setHBox();
    }
	}

	private void addSelectorButtonListener(Stage stage) {
		selectorButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
			@Override
			public void handle(MouseEvent e) {
				fileChooser.setTitle("Select input file");
				fileChooser.getExtensionFilters().addAll(new ExtensionFilter("All Files", "*.*"));
				inputFile = fileChooser.showOpenDialog(stage);
				if (inputFile != null) {
					clearBoard();
					reDraw(stage, inputFile);
				}
			}
		});
	}

	private void setUIStyle() {
		layout.setAlignment(Pos.CENTER);
    layout.setSpacing(20);
    board.setVgap(5);
    board.setHgap(5);
    board.setAlignment(Pos.CENTER);
	}

	private void readInput(File f) {
		try {
			inputCounter = 0;
			Scanner scanner = new Scanner(f);

      while (scanner.hasNextInt()) {
        input[inputCounter++] = scanner.nextInt();
      }
      scanner.close();
		} catch (Exception e) {
			System.out.println(e.getMessage());
		}
		populateBoard();
	}

  private void readInput() {
		try {
			Scanner scanner = new Scanner(getClass().getResourceAsStream("../application/puzzle.in"));
      while (scanner.hasNextInt()) {
        input[inputCounter++] = scanner.nextInt();
      }
      scanner.close();
    } catch (Exception e) {
      System.out.println(e.getMessage());
    }
	}

  //Reference: https://www.youtube.com/watch\?v\=bhmCmbj9VAg
  // Checking of inversion start from ith index
  // Add 1 to inversionCount if ith index > xth index where i < x < boardLength(9)
  // If the total inversion is even, the board is solvable
  private boolean checkSolvability() {
    int inversionCount = 0;
    for (int i = 0; i < inputCounter; i++) {
      for (int j = i + 1; j < inputCounter; j++) {
        if (input[j] != 0 && input[i] > input[j]) {
          inversionCount++;
        }
      }
    }
    return inversionCount % 2 == 0;
  }

  private void clearBoard() {
  	solText.getChildren().clear();
  	solText.getChildren().add(new Text(""));
  	for (int i = 0; i < Puzzle.LENGTH; i++) {
  		cells[i].getButton().setText("");
  		cells[i].setContent(0);
  	}
  }

  public void setSolText(ArrayList<String> solution) {
//  	HBox solText = new HBox();
  	String str = "";
		for (String s : solution) {
			str = str.concat(s);
		}

		Text t = new Text(str);
		t.setFont(Font.font("Verdana", FontWeight.BOLD, 20));
		t.setOpacity(0.3);

		if (str.length() >= 18) {
			ScrollPane sp = new ScrollPane();
			sp.setContent(t);
			solText.getChildren().add(sp);
		} else {
			solText.getChildren().add(t);
		}

		solText.setAlignment(Pos.CENTER);
		solText.setSpacing(5);
		if (!layout.getChildren().contains(solText))
			layout.getChildren().add(solText);
  }

  private void populateBoard() {
  	board.getChildren().clear();
  	int currRow = 0, currCol = 0;
	  for (int i = 0; i < Puzzle.LENGTH; i++) {
		  cells[i] = new Cell(i, input[i], this);
		  cellsAsNum[i] = input[i];
		  cells[i].getButton().setMinSize(50, 50);
		  board.add(cells[i].getButton(), currCol, currRow);

		  currCol++;
		  if (i == 2 || i == 5) {
		  	currRow++;
		  	currCol = 0;
		  }
	  }
  }

  private void setHBox() {
  	solutionHBox = new HBox();
  	dropdown = new ComboBox();
  	solutionButton = new Button("Solution");

  	dropdown.getItems().add("BFS");
    dropdown.getItems().add("DFS");
    dropdown.getItems().add("A*");
    dropdown.getSelectionModel().selectFirst();

    solutionHBox.setAlignment(Pos.CENTER);
    solutionHBox.setSpacing(15);
    solutionHBox.getChildren().addAll(dropdown, solutionButton);


    addSolutionHandler();
  }

  private void addSolutionHandler() {
  	solutionButton.setOnMouseClicked(new EventHandler<MouseEvent>() {
      @Override
      public void handle(MouseEvent mouseEvent) {
      	if (solutionButton.getText().equals("Solution") && !solvablePrompt.getText().equals("YOU WON!")) {
      		if (dropdown.getValue().equals("BFS")) {
  	        bfs = new BFS(getThis());
  	        ArrayList<String> sol = bfs.solve();
  	        setSolText(sol);
  	        writeSolution(sol);
        	} else if (dropdown.getValue().equals("DFS")) {
          	dfs = new DFS(getThis());
          	ArrayList<String> sol = dfs.solve();
          	setSolText(sol);
          	writeSolution(sol);
        	} else if (dropdown.getValue().equals("A*")) {
        		aStar = new AStar(getThis());
        		ArrayList<String> sol = aStar.solve();
        		setSolText(sol);
        		writeSolution(sol);
        	}

      		// disable the buttons, next is the only option
      		for (Cell c : cells) {
      			c.getButton().setDisable(true);
      		}

      		solutionButton.setText("Next");
      	} else if (solutionButton.getText().equals("Next") && !solvablePrompt.getText().equals("YOU WON!")) {
      		if (dropdown.getValue().equals("BFS") && bfs.getSolutionIndex() != bfs.getSolution().size()) {
      			String currentStep = bfs.getSolution().get(bfs.getSolutionIndex());
        		moveNext(currentStep);
        		bfs.setSolutionIndex();
      		} else if (dropdown.getValue().equals("DFS") && dfs.getSolutionIndex() != dfs.getSolution().size()) {
      			String currentStep = dfs.getSolution().get(dfs.getSolutionIndex());
        		moveNext(currentStep);
        		dfs.setSolutionIndex();
      		} else if (dropdown.getValue().equals("A*") && aStar.getSolutionIndex() != aStar.getSolution().size()) {
      			String currentStep = aStar.getSolution().get(aStar.getSolutionIndex());
        		moveNext(currentStep);
        		aStar.setSolutionIndex();
      		}

      		if (Arrays.equals(Puzzle.WINNING_STATE, getCellsAsNum())) {
            setSolvablePrompt();
            setPopupStage();
          }
      	}
      }
    });
  	layout.getChildren().add(solutionHBox);
  }

  private void setPopupStage() {
  	Stage newStage = new Stage();

  	String pathCost = "Path Cost: ";
    if (dropdown.getValue().equals("BFS")) {
    	pathCost = pathCost.concat(Integer.toString(bfs.getSolution().size()));
    } else if (dropdown.getValue().equals("DFS")) {
    	pathCost = pathCost.concat(Integer.toString(dfs.getSolution().size()));
    } else if (dropdown.getValue().equals("A*")) {
    	pathCost = pathCost.concat(Integer.toString(aStar.getSolution().size()));
    }

    Text t = new Text(pathCost);
    t.setFont(Font.font("Verdana", FontWeight.NORMAL, 12));

    HBox cost = new HBox();
    cost.setAlignment(Pos.CENTER);
    cost.getChildren().add(t);

    Scene s = new Scene(cost, 200, 100);
    newStage.setScene(s);
    newStage.show();
  }

  private void writeSolution(ArrayList<String> solution) {
  	try {
  		File file = new File("./src/application/puzzle.out");
  		FileWriter fileWriter = new FileWriter(file);

  		fileWriter.write(String.join(" ", solution));
  		fileWriter.close();
  	} catch (Exception e) {
  		e.getMessage();
  	}
  }

  private Puzzle getThis() {
  	return this;
  }

  Cell getCell(int index) {
	  return cells[index];
  }

  public int[] getCellsAsNum() {
  	return cellsAsNum;
  }

  void setSolvablePrompt() {
  	solvablePrompt.setText("YOU WON!");
  }

  public Cell[] getCells() {
  	return cells;
  }

  public VBox getLayout() {
  	return layout;
  }

  private void moveNext(String currentStep) {
  	Cell c = getCellFromBlank(currentStep);
  	Cell blank = getBlank();
  	c.swapWith(blank);
  }

  private Cell getCellFromBlank(String currentStep) {
  	Cell c = null;
  	Cell blankCell = getBlank();
  	switch (currentStep) {
	  	case "U":
	  		c = cells[blankCell.getIndex()-3];
	  		break;
	  	case "R":
	  		c = cells[blankCell.getIndex()+1];
	  		break;
	  	case "D":
	  		c = cells[blankCell.getIndex()+3];
	  		break;
	  	case "L":
	  		c = cells[blankCell.getIndex()-1];
	  		break;
  	}
  	return c;
  }

  private Cell getBlank() {
  	for (int i = 0; i < Puzzle.LENGTH; i++) {
  		if (cells[i].getContent() == 0) {
  			return cells[i];
  		}
  	}
  	return null;
  }
}