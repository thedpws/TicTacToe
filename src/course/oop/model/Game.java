package course.oop.model;

import course.oop.model.board.GameBoard;
import course.oop.util.Utilities;

import java.util.List;

public class Game {

    public static final int TIE = 3;
    public static final int PLAYER_ONE = 1;
    public static final int PLAYER_TWO = 2;
    public static final int NO_WINNER = 0;

    // Config separates the configuration from the actual game itself.
    // This allows for multiple games to use the same static config (rematches)
    // This also allows for serialization of configurations for faster configurations in the future.
    private GameConfig config;
    private GameBoard board;

    public Game(GameConfig config){
        this.config = config;
        this.board = new GameBoard();
    }

    public boolean selectTile(String row, String col, int playerNumber){
        int rowInt = Utilities.parseIntValue(row);
        if (rowInt == Integer.MIN_VALUE) return false;

        int colInt = Utilities.parseIntValue(col);
        if (colInt == Integer.MIN_VALUE) return false;

        int playerIndex = playerNumber - 1;
        Player p = config.getPlayer(playerIndex);

        Marker m = new Marker(playerNumber, p.getMarker());

        return board.select(rowInt, colInt, m);
    }

    public String selectRandomTile(){
        return board.selectRandomTile();
    }

    public void printGameBoard(){
        System.out.println();
        System.out.println(board.getGameBoardDisplay());
    }

    public String[][] getGameBoard(){
        return this.board.getGameBoard();
    }

    public int determineWinner(){
        int winner = board.hasWinner();
        if (winner != NO_WINNER) return winner;
        if (!board.hasAvailableTiles()) {
            return TIE;
        }
        return NO_WINNER;
    }

    public Player getPlayer(int playerNumber){
        return config.getPlayer(playerNumber - 1);
    }

    public GameConfig getConfig(){
        return this.config;
    }

    public String getBoardDisplay(){
        return this.board.getGameBoardDisplay();
    }

    public String[][] getBoard() {
        return this.board.getGameBoard();
    }

	public int getRotation(){
		return this.board.getRotation();
	}

    public void rotateRight(){
        this.board.rotateRight();
    }
    public void rotateLeft(){
        this.board.rotateLeft();
    }
    }

}

