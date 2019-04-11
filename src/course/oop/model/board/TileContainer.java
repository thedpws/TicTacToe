package course.oop.model.board;

import course.oop.model.Marker;

public interface TileContainer{
    int getOccupantId();
    boolean placeMarker(Marker m);
    int findWinner();
    boolean isEmpty();
    void biconnect(int direction, TileContainer tc);
    boolean isConnected(TileContainer tc);
    boolean hasAvailableTiles();
}