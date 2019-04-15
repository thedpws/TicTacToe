package course.oop.model;

import course.oop.fileio.FileIO;
import course.oop.util.Utilities;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class GameConfig {

    enum Config {PLAYERS, TIMEOUT, PLAYERS_INITIALIZED}

    private final int MIN_PLAYERS = 1;
    private final int MAX_PLAYERS = 2;

    private int numPlayers;
    private long timeoutSeconds;
    private final List<Player>[] teams;
    // mapping from playerId to player
    private HashMap<Integer, Player> players = new HashMap<>();
    private final boolean[] configs;

    public GameConfig() {
        teams = new LinkedList[2];
        teams[0] = new LinkedList<>();
        teams[1] = new LinkedList<>();
        configs = new boolean[Config.values().length];
        configs[Config.TIMEOUT.ordinal()] = true;
    }

    /*
    public void printStatus() {
        if (!configs[Config.PLAYERS.ordinal()])
            System.out.println(Utilities.ANSI_RED + "Invalid number of players: " + numPlayers + Utilities.ANSI_RESET);
        if (!configs[Config.TIMEOUT.ordinal()])
            System.out.println(Utilities.ANSI_RED + "Invalid timeout value: " + timeoutSeconds + " seconds" + Utilities.ANSI_RESET);
        if (!configs[Config.PLAYERS_INITIALIZED.ordinal()]) {
            for (int i = 0; i < numPlayers; i++) {
                if (players.get(i) == null)
                    System.out.printf(Utilities.ANSI_RED + "Player #%d has not been created%n" + Utilities.ANSI_RESET, i + 1);
            }
        }
    }
     */

    public void setAttribute(String attribute, String value) {
        Config c;
        try {
            c = Config.valueOf(attribute.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println(Utilities.ANSI_RED + "Unknown attribute: " + attribute + Utilities.ANSI_RESET);
            return;
        }
        switch (c) {
            case TIMEOUT: {
                int timeout = Utilities.parseIntValue(value);
                this.setTimeout(timeout);
                return;
            }

            default: {
            }
        }
    }

    /*
    private void updatePlayersInitialized() {
        //update players_initialized
        int config = Config.PLAYERS_INITIALIZED.ordinal();
        configs[config] = true;
        while (players.size() < numPlayers) players.add(null);
        for (int i = 0; i < numPlayers; i++) {
            configs[config] &= players.get(i) != null;
        }
    }
     */


    /*
    private void setNumPlayers(int numPlayers) {
        int config = Config.PLAYERS.ordinal();
        this.numPlayers = numPlayers;
        if (this.numPlayers < 0 || this.numPlayers > 2)
            System.out.println(Utilities.ANSI_RED + "Invalid players: " + this.numPlayers + " must be 1 or 2" + Utilities.ANSI_RESET);

        configs[config] = (MIN_PLAYERS <= this.numPlayers) && (this.numPlayers <= MAX_PLAYERS);
    }


     */
    private void setTimeout(int timeout) {
        int config = Config.TIMEOUT.ordinal();
        this.timeoutSeconds = timeout;
        if (this.timeoutSeconds < 0)
            System.out.println(Utilities.ANSI_RED + "Invalid timeout: " + this.timeoutSeconds + "s" + Utilities.ANSI_RESET);
        configs[config] = (0 <= this.timeoutSeconds);
    }

    // TODO Don't call until all players are valid
    public void createPlayer(String username, String marker, String team) {

        int markerID = Integer.parseInt(marker);
        Player p = FileIO.loadPlayer(username);

        // Update Player marker
        p.updateMarkerID(markerID);
        FileIO.writePlayer(p);

        System.out.println("GameConfig.java: " + p.asEntry());

        int teamNumber = Utilities.parseIntValue(team);
        List<Player> teamList = teams[teamNumber - 1];
        teamList.add(p);
    }

    public int getNumPlayers() {
        return players.size();
    }

    public Player getPlayer(int team, int player) {
        System.out.println(players);
        /*
        if (index < 0 || index >= players.size()) {
            return null;
        }
         */
        return teams[team].get(player);
    }

    public long getTimeout() {
        return this.timeoutSeconds;
    }

    public void createComputer(String team) {
        int teamNumber = Utilities.parseIntValue(team);
        List<Player> teamList = teams[teamNumber-1];
        Computer c = new Computer();
        teamList.add(c);
    }

    public int randomTeam(){
        return (int) (Math.random() * (teams.length - 1));
    }

    public int randomPlayer(int teamNumber){
       return (int) (Math.random() * (teams[teamNumber-1].size() - 1)) ;
    }

    public int[] updateTeam(int team, int[] teamPlayer){
        teamPlayer[team] = (teamPlayer[team] + 1) % teams[team].size();
        return teamPlayer;
    }
    public List<Player> getTeam(int team){
        return teams[team];
    }
    public List<Player>[] getTeams(){
        return teams;
    }

}
