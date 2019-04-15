package course.oop.model.board;

import course.oop.model.Game;
import course.oop.model.Marker;

public interface TileProperty {
    void applyEffect(Game g, Tile t);
}

class NoProperty implements TileProperty {
    @Override
    public void applyEffect (Game g, Tile t){
        ;;
    }
}

class ClearBoardProperty implements TileProperty {
   @Override
    public void applyEffect (Game g, Tile t) {
       g.clearBoard();
   }
}

class EraseProperty implements TileProperty {
    @Override
    public void applyEffect (Game g, Tile t) {
        t.clearMarker();
    }
}

// Makes the game spin indefinitely
class SpinProperty implements TileProperty {
    @Override
    public void applyEffect (Game g, Tile t){
        g.spin();
    }
}

class ReboundProperty implements TileProperty {
    @Override
    public void applyEffect(Game g, Tile t){
        g.rebound();
    }
}

class BlockProperty implements TileProperty {
    @Override
    public void applyEffect(Game g, Tile t){
        t.placeMarker(Marker.BLOCKED);
    }
}

class InvertTileProperty implements TileProperty {
    @Override
    public void applyEffect(Game g, Tile t){
        t.clearMarker();
        int otherTeam = (t.getOccupantId() + 1) % 2 ;
        if (otherTeam == 0) t.placeMarker(Marker.CLOWN_TEAM1);
        else if (otherTeam == 1) t.placeMarker(Marker.CLOWN_TEAM2);
    }
}
