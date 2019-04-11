package course.oop.model.board;

import course.oop.model.Marker;

import static course.oop.model.Game.NO_WINNER;

public class TileGrid implements TileContainer{

    private Tile[][] tiles;
    private TileGrid[] neighborhood;

    // nxn grid
    TileGrid(int n){
        this.tiles = new Tile[n][n];
    }

    @Override
    public int getOccupantId() {
        return 0;
    }

    @Override
    public boolean placeMarker(Marker m) {
        return false;
    }

    @Override
    public int findWinner() {
        for (int rowcol = 0; rowcol < tiles.length; rowcol++){
            Tile current = tiles[rowcol][rowcol];
            int winner = current.findWinner();
            if (winner != NO_WINNER) return winner;
        }
        return NO_WINNER;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public void biconnect(int direction, TileContainer tc) {
        if (tc instanceof TileGrid){
            neighborhood[direction] = (TileGrid) tc;
            ((TileGrid)tc).neighborhood[Tile.reverse(direction)] = this;
        }
    }

    @Override
    public boolean isConnected(TileContainer tc) {
        for (TileGrid grid : neighborhood)
            if (tc == grid) return true;
        return false;
    }

    @Override
    public boolean hasAvailableTiles() {
        for (Tile[] row : tiles)
            for (Tile tile : row)
                if (tile.isEmpty()) return true;
        return false;
    }

    Tile getTile(int row, int col){
        return tiles[row][col];
    }

    Tile[][] getTiles(){
        return this.tiles;
    }

    void setTile(int row, int col, Tile t){
        tiles[row][col] = t;
    }
}