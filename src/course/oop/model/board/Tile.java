package course.oop.model.board;


import course.oop.model.Marker;
import course.oop.util.Utilities;

import static course.oop.model.Game.NO_WINNER;

public class Tile {

    public static final int numDirections = 8;
    public static final int U       = 0;
    public static final int UR      = 1;
    public static final int R       = 2;
    public static final int DR      = 3;
    public static final int D       = 4;
    public static final int DL      = 5;
    public static final int L       = 6;
    public static final int UL      = 7;
    public static int reverse(int direction) {return (direction + numDirections / 2)%numDirections;}

    private Marker marker;

    private final Tile[] neighborhood = new Tile[numDirections];

    public Tile(){

    }

    //@Override
    public int getOccupantId() {
        if (!isEmpty()) return marker.getPlayerNumber(); else return -1;
    }

    //@Override
    public boolean placeMarker(Marker m) {
        System.err.println("Placing Marker!!!!!!");
        if (!this.isEmpty()) {
            System.out.println(Utilities.ANSI_RED + "Tile already occupied!" + Utilities.ANSI_RESET);
            return false;
        }
        this.marker = m;
        return true;
    }


    //@Override
    public boolean hasAvailableTiles(){
        return isEmpty();
    }



    //@Override
    public boolean isEmpty() {
        return marker == null;
    }

    //@Override
    public void biconnect(int direction, Tile t){
        this.neighborhood[direction] = t;
        t.neighborhood[reverse(direction)] = this;
    }

    //@Override
    public boolean isConnected(TileContainer t){
        for (Tile neighbor : neighborhood)
            if (neighbor == t) return true;
        return false;
    }

    public Tile getNeighbor(int direction){
        if (direction < 0 || direction >= numDirections) return null;
        return this.neighborhood[direction];
    }

    public String getMark(){
        if (this.marker == null) return "";
        return this.marker.getMark();
    }
}