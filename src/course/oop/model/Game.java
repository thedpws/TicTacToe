package course.oop.model;

import course.oop.controller.state.GameState;
import course.oop.model.board.ClassicBoard;
import course.oop.model.board.GameBoard;
import course.oop.model.board.NullPlayer;
import course.oop.util.Utilities;
import javafx.scene.layout.StackPane;

import java.util.List;

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

    public boolean selectTile(String row, String col, int team, int[] players) {
        // Parse int values
        int rowInt = Utilities.parseIntValue(row);
        if (rowInt == Integer.MIN_VALUE) return false;
        int colInt = Utilities.parseIntValue(col);
        if (colInt == Integer.MIN_VALUE) return false;

        // get marker
        Player p = config.getPlayer(team, players[team]);
        Marker m = new Marker(team+1, p.getMarker());

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

    public Player getPlayer(int team, int player) {
        Player p = config.getPlayer(team, player);
        if (p == null) return new NullPlayer();
        return p;
    }

    public GameConfig getConfig() {
        return this.config;
    }

    public int randomTeam(){
       return config.randomTeam();
    }

    public int randomPlayer(int team){
        return config.randomPlayer(team);
    }
    public List<Player> getPlayers(int team){
        return config.getTeam(team);
    }
    public List<Player>[] getTeams(){
        return config.getTeams();
    }
    public List<Player> getTeam(int team){
        return config.getTeam(team);
    }

    public void rotate(String direction) {
       board.rotate(direction);
    }
}

