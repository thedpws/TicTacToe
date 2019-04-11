package course.oop.controller.state;

import course.oop.model.GameConfig;
import course.oop.util.Utilities;
import course.oop.view.CommandCall;
import course.oop.view.SetupView;
import course.oop.view.TTTView;
import javafx.scene.Scene;

import java.util.HashMap;
import java.util.Map;

public class GameSetupState implements GameState {

    private GameConfig gameConfig;
    private Map<String, Command> commands;
    private TTTView view;

    GameSetupState() {
        this(new GameConfig());
    }

    GameSetupState(GameConfig config) {
        this.view = new SetupView();
        this.gameConfig = config;
        this.commands = new HashMap<>();
        this.commands.put("set", SET);
        this.commands.put("start", START);
        this.commands.put("createplayer", CREATE_PLAYER);
    }

    @Override
    public GameState consumeCommand(CommandCall c) {
        String cmd = c.getArgv()[0];
        if (!commands.containsKey(cmd)) return null;
        return commands.get(cmd.toLowerCase()).execute(c);
    }

    @Override
    public String getPrompt() {
        return "Game Configuration";
    }

    @Override
    public void printInitialText() {
        String circle = Utilities.CIRCLE;
        String cross = Utilities.CROSS;
        System.out.printf("%s%s%s%s%s Game Setup %s%s%s%s%s%n", circle, circle, circle, circle, circle, cross, cross, cross, cross, cross);
        System.out.println();
        System.out.println();
        System.out.println("\tThis is the game setup menu.");
        System.out.println("\tTo create a basic 1v1 game with 10s timer:");
        System.out.println(Utilities.HELP_START + "\t\t1. set players 2\n\t\t2. createplayer {player 1 username} X 1\n\t\t3. createplayer {player 2 username} o 2\n\t\t4. set timeout 10\n\t\t5. start" + Utilities.ANSI_RESET);
        System.out.println();
        System.out.printf("\tTry %shelp set%s for more configurations you may set\n", Utilities.HELP_START, Utilities.ANSI_RESET);
        System.out.println();
        System.out.println();
        System.out.println();
    }


    @Override
    public Scene asScene() {
        return view.getScene();
    }


    private Command SET = new Command() {
        @Override
        public GameState execute(CommandCall c) {
            String attribute = c.getArgv()[1];
            String value = c.getArgv()[2];
            gameConfig.setAttribute(attribute, value);
            return GameSetupState.this;
        }

    };
    private final Command START = new Command() {
        @Override
        public GameState execute(CommandCall c) {
            if (!gameConfig.isValid()) {
                gameConfig.printStatus();
                return GameSetupState.this;
            }
            return new GameInitState(gameConfig);
        }
    };

    private final Command CREATE_PLAYER = new Command() {
        @Override
        public GameState execute(CommandCall c) {
            String username = c.getArgv()[1];
            String marker = c.getArgv()[2];
            String number = c.getArgv()[3];
            gameConfig.createPlayer(username, marker, number);
            return GameSetupState.this;
        }
    };

}
