package course.oop.controller.state;

import course.oop.model.Game;
import course.oop.model.Player;
import course.oop.view.CommandCall;
import course.oop.view.TurnView;
import javafx.scene.Scene;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static course.oop.model.Game.NO_WINNER;

public class TurnState implements GameState {

    private final Map<String, Command> commands;

    private final int player;
    private final Game game;
    private final LocalTime timeStarted;
    private boolean turnOver;
    private final TurnView view;

    public TurnState(Game g) {
        this(g, (int) Math.round(Math.random()) + 1);
    }

    private TurnState(Game g, int player) {
        this.player = player;
        this.game = g;
        this.view = new TurnView(game, player);
        timeStarted = LocalTime.now();

        printInitialText();

        // setup commands
        this.commands = new HashMap<>();
        Command SELECT = c -> {
            String row = c.getArgv()[1];
            String column = c.getArgv()[2];
            if (!game.selectTile(row, column, player)) return TurnState.this;

            int winner = game.determineWinner();

            if (winner == 0) return getNextTurnState();
            return new EndState(game, winner);
        };
        commands.put("select", SELECT);
    }

    @Override
    public void printInitialText() {
        //printGameBoard();
        System.out.printf("It's %s's turn!%n", game.getPlayer(player));
    }

    @Override
    public GameState consumeCommand(CommandCall c) {
        if (turnOver) return getNextTurnState();
        if (game.getConfig().getTimeout() != 0 && !turnOver && LocalTime.now().isAfter(timeStarted.plusSeconds(game.getConfig().getTimeout()))) {
            return getNextTurnState();
        }
        String cmd = c.getArgv()[0];
        if (!commands.containsKey(cmd)) return null;
        return commands.get(cmd.toLowerCase()).execute(c);
    }

    /*
    private void printGameBoard() {
        game.printGameBoard();
    }
    */

    private GameState getNextTurnState() {
        System.out.printf("Wow! Current player is %d. Next is %d\n", player, player % 2 + 1);
        return new TurnState(game, (player) % 2 + 1);
    }

    @Override
    public String getPrompt() {
        Player p = game.getConfig().getPlayer(player - 1);
        return String.format("%s %s", p, p.getMarker());
    }

    @Override
    public Scene asScene() {
        return view.getScene();
    }

}