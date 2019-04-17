package course.oop.model;

import java.io.Serializable;

public class Player implements Serializable {
    private boolean valid;
    private final String username;
    private String password;
    private int marker;
    private int wins, losses; // todo implement for player records
    private int cash;

    public static final int DEFAULT_MARKER = 1;

    public Player(String username){
        this(username, DEFAULT_MARKER);
    }

    public Player(String username, int marker) {
        this.username = username;
        this.marker = marker;
        this.wins = 0;
        this.losses = 0;
    }

    public boolean charge(int cash){
        if (this.cash >= cash){
            this.cash -= cash;
            return true;
        }
        return false;
    }
    public void awardCash(){
        this.cash += 100;
        System.out.println("CASH! " + this.cash);
    }


    @Override
    public String toString() {
        return this.username;
    }

    public String getMarker() {
        return Integer.toString(marker);
    }

    public int getMarkerID() {
        return marker;
    }

    public void addWin() {
        this.wins++;
    }

    public void addLoss() {
        this.losses++;
    }

    public String asEntry() {
        return String.format("%s (%d W, %d L)", this.username, this.wins, this.losses);
    }

    public boolean isHuman() {
        return true;
    }
    public boolean isComputer(){ return false; }

    public void updateMarkerID(int id) {
        this.marker = id;
    }
}

