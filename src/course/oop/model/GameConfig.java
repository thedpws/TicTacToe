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
    private final List<Player> players;
    private final boolean[] configs;

    public GameConfig() {
        players = new LinkedList<>();
        configs = new boolean[Config.values().length];
        configs[Config.TIMEOUT.ordinal()] = true;
    }

    public boolean isValid() {
        boolean ready = true;
        updatePlayersInitialized();
        for (boolean b : configs) ready &= b;
        return ready;
    }

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

    public void setAttribute(String attribute, String value) {
        Config c;
        try {
            c = Config.valueOf(attribute.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println(Utilities.ANSI_RED + "Unknown attribute: " + attribute + Utilities.ANSI_RESET);
            return;
        }
        switch (c) {
            case PLAYERS: {
                int numPlayers = Utilities.parseIntValue(value);
                this.setNumPlayers(numPlayers);
                return;
            }

            case TIMEOUT: {
                int timeout = Utilities.parseIntValue(value);
                this.setTimeout(timeout);
                return;
            }

            default: {
            }
        }
    }

    private void updatePlayersInitialized() {
        //update players_initialized
        int config = Config.PLAYERS_INITIALIZED.ordinal();
        configs[config] = true;
        while (players.size() < numPlayers) players.add(null);
        for (int i = 0; i < numPlayers; i++) {
            configs[config] &= players.get(i) != null;
        }
    }


    private void setNumPlayers(int numPlayers) {
        int config = Config.PLAYERS.ordinal();
        this.numPlayers = numPlayers;
        if (this.numPlayers < 0 || this.numPlayers > 2)
            System.out.println(Utilities.ANSI_RED + "Invalid players: " + this.numPlayers + " must be 1 or 2" + Utilities.ANSI_RESET);

        configs[config] = (MIN_PLAYERS <= this.numPlayers) && (this.numPlayers <= MAX_PLAYERS);
    }

    private void setTimeout(int timeout) {
        int config = Config.TIMEOUT.ordinal();
        this.timeoutSeconds = timeout;
        if (this.timeoutSeconds < 0)
            System.out.println(Utilities.ANSI_RED + "Invalid timeout: " + this.timeoutSeconds + "s" + Utilities.ANSI_RESET);
        configs[config] = (0 <= this.timeoutSeconds);
    }

    public void createPlayer(String username, String marker, String number) {

        int markerID = Integer.parseInt(marker);
        Player p = FileIO.loadPlayer(username);

        // Update Player marker
        p.updateMarkerID(markerID);
        FileIO.writePlayer(p);

        System.out.println("GameConfig.java: " + p.asEntry());

        int playerNumber = Utilities.parseIntValue(number);
        /*
        if (playerNumber == Integer.MIN_VALUE) return;
        if (playerNumber < MIN_PLAYERS || playerNumber > MAX_PLAYERS) {
            System.out.println(Utilities.ANSI_RED + "Invalid player number: " + number + " must be 1 or 2" + Utilities.ANSI_RESET);
            return;
        }
        */
        int playerIndex = playerNumber - 1;

        //append to players to avoid index out of bounds
        while (players.size() < playerNumber) players.add(null);
        players.set(playerIndex, p);
    }

    public int getNumPlayers() {
        return numPlayers;
    }

    public Player getPlayer(int index) {
        System.out.println(players);
        if (index < 0 || index >= players.size()) {
            return null;
        }
        return players.get(index);
    }

    public long getTimeout() {
        return this.timeoutSeconds;
    }

    public void createComputer(String playerNumberString) {
        int playerNumber = Utilities.parseIntValue(playerNumberString);
        int playerIndex = playerNumber - 1;

        while (players.size() < playerNumber) players.add(null);
        players.set(playerIndex, new Computer());
    }

}
