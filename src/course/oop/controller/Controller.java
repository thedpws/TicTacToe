package course.oop.controller;

import course.oop.controller.state.*;
import course.oop.model.Game;
import course.oop.util.Utilities;
import course.oop.view.CommandCall;
import javafx.stage.Stage;


public class Controller {

    private GameState gameState;

    private Game game;

    private Stage primaryStage;

    private static Controller INSTANCE;

    public static void initiate(Stage primaryStage) {
        if (INSTANCE != null) return;
        INSTANCE = new Controller(primaryStage);
        INSTANCE.primaryStage = primaryStage;
        INSTANCE.gameState = new InitialState();
        INSTANCE.primaryStage.setScene(INSTANCE.gameState.asScene());
    }


    private Controller(Stage s) {
        this.primaryStage = s;
        this.gameState = new InitialState();
        this.primaryStage.setScene(this.gameState.asScene());
    }

    private static void printPrompt() {
        System.out.print(Controller.getPrompt() + " " + Utilities.EMOJI + ": ");
    }

    public static void interruptTurn() {
        System.out.println("\n");
        Controller.consume(new CommandCall("\n"));
        printPrompt();
    }

    public static void execute(String s){
        consume(new CommandCall(s));
    }


    public static void consume(CommandCall cmd) {
        System.err.println(cmd);

        // globally-scoped commands
        if (cmd.getArgAt(0) == null) return;
        switch (cmd.getArgAt(0).toLowerCase()) {

            case "quit": {
                quit();
                break;
            }

            case "": {
                break;
            }

            // pass other commands to the state
            default: {
                GameState rval = INSTANCE.gameState.consumeCommand(cmd);

                boolean badCommand = rval == null;
                if (badCommand) {
                    System.out.printf("%sCommand not found: %s%s%n", Utilities.ERR_START, cmd.getArgAt(0), Utilities.ANSI_RESET);
                    break;
                }

                // special case - initiate game
                if (rval instanceof GameInitState) {
                    GameInitState s = (GameInitState) rval;
                    INSTANCE.game = s.produceGame();
                    INSTANCE.gameState = new TurnState(INSTANCE.game);
                    INSTANCE.primaryStage.setScene(INSTANCE.gameState.asScene());
                    break;
                }

                if (INSTANCE.gameState.getClass() != rval.getClass()) {
                    rval.printInitialText();
                }

                INSTANCE.gameState = rval;
                INSTANCE.primaryStage.setScene(rval.asScene());
                break;
            }
        }
    }

    private static void quit() {
        System.exit(0);
    }

    public static String getPrompt() {
        return INSTANCE.gameState.getPrompt();
    }
}
