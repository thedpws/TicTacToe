package course.oop.model;

import java.io.Serializable;

public class Player implements Serializable {
    private boolean valid;
    private String username, password;
    private int marker;
    private int wins, losses; // todo implement for player records

    public Player(String username, int marker) {
        this.username = username;
        this.marker = marker;
        this.wins = 0;
        this.losses = 0;
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

    boolean isValid() {
        return true;
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

    public void updateMarkerID(int id) {
        this.marker = id;
    }
}

