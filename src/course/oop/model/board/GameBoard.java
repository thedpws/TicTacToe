package course.oop.model.board;

import course.oop.model.Marker;
import javafx.scene.Node;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.StackPane;

public interface GameBoard {
    StackPane asJavaFXNode();

    boolean selectTile(int row, int col, Marker m);
    String selectRandomTile();
    int determineWinner();
}
