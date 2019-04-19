package course.oop.model;

import course.oop.fileio.FileIO;

import java.io.Serializable;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

public class Player implements Serializable {
    private boolean valid;
    private final String username;
    private String password;
    private int marker;
    private int wins, losses; // todo implement for player records
    private int cash;
    public List<Integer> emojisUnlocked;

    public static final int DEFAULT_MARKER = 1;

    public Player(String username){
        this(username, DEFAULT_MARKER);
    }

    public Player(String username, int marker) {
        this.emojisUnlocked = new LinkedList<>();
        emojisUnlocked.add(1);
        emojisUnlocked.add(2);
        emojisUnlocked.add(3);
        this.username = username;
        this.marker = marker;
        this.wins = 0;
        this.losses = 0;
    }


    public void unlockEmoji(int emojiId){
        this.emojisUnlocked.add(emojiId);
        FileIO.writePlayer(this);
    }

    public int getEmoji(int index){
        while (index < 0) index += this.emojisUnlocked.size();
        return this.emojisUnlocked.get(index % emojisUnlocked.size());
    }
    public boolean hasUnlockedEmoji(int emojiID){
        return this.emojisUnlocked.contains(emojiID);
    }

    public boolean charge(int cash){
        if (this.cash >= cash){
            this.cash -= cash;
            FileIO.writePlayer(this);
            return true;
        }
        return false;
    }
    public void awardCash(){
        this.cash += 100;
        System.out.println("CASH! " + this.cash);
        //FileIO.writePlayer(this);
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
    public int getCash(){return this.cash;}
    public String getUsername(){return this.username;}
}

