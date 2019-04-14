package course.oop.model;

import course.oop.model.board.ClassicBoard;
import course.oop.model.board.GameBoard;
import course.oop.model.board.NullPlayer;
import course.oop.util.Utilities;
import javafx.scene.layout.StackPane;

public class Game {

    public static final int TIE = 3;
    public static final int NO_WINNER = 0;

    // Config separates the configuration from the actual game itself.
    // This allows for multiple games to use the same static config (rematches)
    // This also allows for serialization of configurations for faster configurations in the future.
    private final GameConfig config;
    private final GameBoard board;

    public Game(GameConfig config) {
        this.config = config;
        this.board = new ClassicBoard(3);
    }

    public boolean selectTile(String row, String col, int playerNumber) {
        // Parse int values
        int rowInt = Utilities.parseIntValue(row);
        if (rowInt == Integer.MIN_VALUE) return false;
        int colInt = Utilities.parseIntValue(col);
        if (colInt == Integer.MIN_VALUE) return false;

        // get marker
        int playerIndex = playerNumber - 1;
        Player p = config.getPlayer(playerIndex);
        Marker m = new Marker(playerNumber, p.getMarker());

        return board.selectTile(rowInt, colInt, m);
    }

    public String selectRandomTile() {
        return board.selectRandomTile();
    }

    /*
    public void printGameBoard() {
        System.out.println();
        System.out.println(board.getGameBoardDisplay());
    }
     */

    /*
    public String[][] getGameBoard() {
        return this.board.getGameBoard();
    }
    */

    public int determineWinner() {
        return board.determineWinner();
    }

    public StackPane getDisplay(){
        return board.asJavaFXNode();
    }

    public Player getPlayer(int playerNumber) {
        Player p = config.getPlayer(playerNumber - 1);
        if (p == null) return new NullPlayer();
        return p;
    }

    public GameConfig getConfig() {
        return this.config;
    }
}

