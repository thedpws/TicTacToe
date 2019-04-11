package course.oop.model;

public class Marker {

    private String marker;
    private int playerNumber;

    //used only ingame. Purpose is to mark, not to store the marker.
    Marker(int playerNumber, String marker){
        this.marker = marker;
        this.playerNumber = playerNumber;
    }

    public int getPlayerNumber(){
        return playerNumber;
    }
    public String getMark(){ return this.marker; }

}