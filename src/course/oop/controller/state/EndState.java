package course.oop.controller.state;

import course.oop.fileio.FileIO;
import course.oop.model.Game;
import course.oop.model.Player;
import course.oop.util.Utilities;
import course.oop.view.CommandCall;
import course.oop.view.ResultsView;
import course.oop.view.TTTView;
import javafx.scene.Scene;

import java.util.HashMap;
import java.util.Map;

public class EndState implements GameState {

    private Game game;
    private int endCode;
    private TTTView view;

    private Map<String, Command> commands;

    EndState(Game game, int endCode) {
        this.view = new ResultsView(game);
        this.endCode = endCode;
        this.game = game;

        String result;
        switch (endCode) {
            case Game.TIE: {
                result = "Tie. Everyone loses.";
                for (int i = 1; i <= 2; i++) {
                    Player p = game.getPlayer(i);
                    p.addLoss();
                    FileIO.writePlayer(p);
                    System.out.println("EndState.java: " + p.asEntry());
                }
                break;
            }

            default: {
                int winner = endCode;
                //int winnerIndex = winner - 1;
                result = String.format("Player %d wins! Congratulations %s!%n", winner, game.getPlayer(winner));
                for (int i = 1; i <= 2; i++) {
                    Player p = game.getPlayer(i);
                    if (winner == i) p.addWin();
                    else p.addLoss();
                    if (p.isHuman()) FileIO.writePlayer(p);
                    System.out.println("EndState.java: " + p.asEntry());
                }
                break;
            }
        }

        game.printGameBoard();
        System.out.println(result);

        this.commands = new HashMap<>();
        commands.put("rematch", REMATCH);
        commands.put("mainmenu", MAIN_MENU);
        commands.put("print", PRINT);
        commands.put("setup", SETUP);

        for (Player p : FileIO.loadHashMap().values()) System.out.println(p.asEntry());
    }

    //allowed commands: select row column
    @Override
    public GameState consumeCommand(CommandCall c) {
        String cmd = c.getArgv()[0];
        if (!commands.containsKey(cmd)) return null;
        return commands.get(cmd.toLowerCase()).execute(c);
    }

    @Override
    public String getCommands() {
        StringBuilder sb = new StringBuilder();
        for (String s : this.commands.keySet()) {
            sb.append(String.format(", %s", s));
        }
        return sb.toString();
    }

    @Override
    public String getPrompt() {
        return "End-Results";
    }

    @Override
    public Map<String, Command> getCommandMap() {
        return this.commands;
    }

    @Override
    public void printInitialText() {
        String circle = Utilities.CIRCLE;
        //"\u2b55";
        String cross = Utilities.CROSS;
        //"\u274c";
        System.out.printf("%s%s%s%s%s End Results %s%s%s%s%s%n", circle, circle, circle, circle, circle, cross, cross, cross, cross, cross);
        String result = "";
        switch (endCode) {
            case Game.TIE: {
                result = "Tie. Everyone loses.";
                break;
            }

            default: {
                int winner = endCode;
                int winnerIndex = winner - 1;
                result = String.format("Player %d wins! Congratulations %s!%n", winner, game.getPlayer(winner));
                break;
            }
        }

        game.printGameBoard();
        System.out.println(result);
    }

    @Override
    public Scene asScene() {
        return this.view.getScene();
    }

    private final Command REMATCH = new Command() {

        @Override
        public GameState execute(CommandCall c) {
            final int N_PARAMS = 0;
            if (c.getNumParams() != N_PARAMS) {
                printCorrectUsage();
                return EndState.this;
            }

            return new GameInitState(game.getConfig());
        }

        @Override
        public String getHelp() {
            return "COMMAND\n\trematch\nSYNOPSIS\n\tstart a new game using the same configuration.";
        }

        @Override
        public String getCorrectUsage() {
            return "Usage: rematch";
        }
    };
    private final Command PRINT = new Command() {

        @Override
        public GameState execute(CommandCall c) {
            final int N_PARAMS = 0;
            if (c.getNumParams() != N_PARAMS) {
                printCorrectUsage();
                return EndState.this;
            }

            game.printGameBoard();
            return EndState.this;
        }

        @Override
        public String getHelp() {
            return "COMMAND\n\tprint\nSYNOPSIS\n\tprint out the game board.";
        }

        @Override
        public String getCorrectUsage() {
            return "print";
        }
    };
    private final Command MAIN_MENU = new Command() {
        @Override
        GameState execute(CommandCall c) {
            final int N_PARAMS = 0;
            if (c.getNumParams() != N_PARAMS) {
                this.printCorrectUsage();
                return EndState.this;
            }

            return new InitialState();
        }

        @Override
        String getHelp() {
            return "COMMAND\n\tmainmenu\nSYNOPSIS\n\tReturn to main menu.";
        }

        @Override
        String getCorrectUsage() {
            return "mainmenu";
        }
    };
    private final Command SETUP = new Command() {
        @Override
        GameState execute(CommandCall c) {
            final int N_PARAMS = 0;
            if (c.getNumParams() != N_PARAMS) {
                this.printCorrectUsage();
                return EndState.this;
            }

            return new GameSetupState(game.getConfig());
        }

        @Override
        String getHelp() {
            return "COMMAND\n\tsetup\nSYNOPSIS\n\tReturn to game setup menu.";
        }

        @Override
        String getCorrectUsage() {
            return "setup";
        }
    };
}