package course.oop.model.board;

import course.oop.model.Marker;
import course.oop.util.Utilities;

public class GameBoard {
    private final TileGrid tiles;
    int rotation; //todo implement for rotating boardgame

    //todo extend for ultimate tic tac toe
    public GameBoard(){
        this.tiles = createClassicBoard();
    }

    public boolean select(int row, int col, Marker m){
        if (0 <= row && 0 <= col && row < tiles.getTiles().length && col < tiles.getTiles().length)
            return tiles.getTile(row, col).placeMarker(m);
        System.out.println(Utilities.ANSI_RED + "Bad tile: " + row + " " + col + Utilities.ANSI_RESET);
        return false;
    }

    // Public for future use (when other boards become implemented)
    private static TileGrid createClassicBoard(){
        int n = 3;
        TileGrid board = new TileGrid(n);

        // build tiles
        for (int row = 0; row < n; row++){
            for (int col = 0; col < n; col++){
                board.setTile(row, col, new Tile());
            }
        }

        // build connections
        for (int row = 0; row < n; row++){
            for (int col = 0; col < n; col++){
                Tile current = board.getTile(row, col);
                //connect all its neighbors

                for (int rowOffset = -1; rowOffset <= 1; rowOffset++){
                    for (int colOffset = -1; colOffset <=1; colOffset++){
                        int nRow = row + rowOffset;
                        int nCol = col + colOffset;
                        if (!(0 <= nRow && nRow < n && 0 <= nCol && nCol < n)) continue;
                        Tile neighbor  = board.getTile(nRow, nCol);
                        if (neighbor == current) continue;
                        //get direction
                        int direction = -1;

                        if (rowOffset == -1){
                            if (colOffset == -1) direction = Tile.UL;
                            if (colOffset ==  0) direction = Tile.U;
                            if (colOffset ==  1) direction = Tile.UR;
                        }

                        if (rowOffset == 0){
                            if (colOffset == -1) direction = Tile.L;
                            if (colOffset ==  1) direction = Tile.R;
                        }

                        if (rowOffset == 1){
                            if (colOffset == -1) direction = Tile.DL;
                            if (colOffset ==  0) direction = Tile.D;
                            if (colOffset ==  1) direction = Tile.DR;
                        }

                        current.biconnect(direction, neighbor);
                    }
                }

            }
        }

        return board;
    }

    public String getGameBoardDisplay(){
        //final int COLUMNS = Integer.parseInt(System.getenv("COLUMNS"));
        final String spacing = "    ";
        StringBuilder s = new StringBuilder().append(Utilities.BOARD);
        StringBuilder colNumbers = new StringBuilder().append('\n').append(spacing).append(' ').append(spacing);
        for (int i = 0; i < tiles.getTiles().length; i++) {
            colNumbers.append(spacing).append(i).append(spacing);
            if (tiles.getTile(0, i).getNeighborhood()[Tile.R] != null) colNumbers.append('|');
        }
        s.append(colNumbers);
        for (int rowNumber = 0; rowNumber < tiles.getTiles()[0].length; rowNumber++) {
            Tile[] row = tiles.getTiles()[rowNumber];
            StringBuilder rowString = new StringBuilder().append('\n').append(spacing).append(rowNumber).append(spacing);
            for (Tile t : row){

                //define the marker
                String mark;
                if (t.isEmpty()) mark = " ";
                else mark = t.getMark();

                //append to string
                rowString.append(spacing).append(mark).append(spacing);
                if (t.getNeighborhood()[Tile.R] != null) rowString.append("|");
            }
            StringBuilder emptyRow = new StringBuilder().append('\n');
            for (int i = 1; i < rowString.length(); i++) if (rowString.charAt(i) == '|') emptyRow.append('|'); else emptyRow.append(' ');
            s.append(emptyRow).append(rowString).append(emptyRow);

            if (row[0].getNeighborhood()[Tile.D] != null) {
                s.append('\n');
                for (int i = 0; i < rowString.length(); i++) s.append('-');
            }

        }
        s.append(Utilities.ANSI_RESET);
        return s.toString();
    }

    public String[][] getGameBoard(){
        int N = tiles.getTiles().length;
        String[][] result = new String[N][N];
        for (int row = 0; row < N; row++){
            for (int col = 0; col < N; col++){
                result[row][col] = tiles.getTiles()[row][col].getMark();
            }
        }
        return result;
    }

    public int hasWinner(){
        return tiles.findWinner();
    }

    public boolean hasAvailableTiles(){
        return tiles.hasAvailableTiles();
    }

    public String selectRandomTile(){
        int row = (int)Math.round(Math.random() * 2);
        int col = (int)Math.round(Math.random() * 2);
        Tile random = tiles.getTiles()[row][col];
        if (random.isEmpty()) return String.format("%d %d", row, col);
        else return selectRandomTile();
    }
}
