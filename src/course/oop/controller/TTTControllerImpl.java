package course.oop.controller;

import course.oop.controller.state.*;
import course.oop.model.Game;
import course.oop.model.GameConfig;
import course.oop.util.Utilities;
import course.oop.view.Command;
import course.oop.view.View;
import javafx.stage.Stage;


public class TTTControllerImpl implements TTTControllerInterface {

    private GameState gameState;

    private Game game;

    private Stage primaryStage;

    public TTTControllerImpl(Stage primaryStage){

        this.primaryStage = primaryStage;
        this.gameState = new InitialState();
        this.primaryStage.setScene(this.gameState.asScene());
    }


    public void consume(Command cmd){

        // globally-scoped commands
        if (cmd.getArgAt(0) == null) return;
        switch (cmd.getArgAt(0).toLowerCase()){

            case "help": {
                help(cmd);
                break;
            }

            case "quit": {
                quit(cmd);
                break;
            }

            case "":{
                break;
            }

            // pass other commands to the state
            default: {
                GameState rval = gameState.consumeCommand(cmd);

                boolean badCommand = rval == null;
                if (badCommand) {
                    System.out.printf("%sCommand not found: %s%s%n", Utilities.ERR_START, cmd.getArgAt(0),Utilities.ANSI_RESET);
                    break;
                }

                // special case - initiate game
                if (rval instanceof GameInitState){
                    GameInitState s = (GameInitState) rval;
                    this.game = s.produceGame();
                    this.gameState = new TurnState(game);
                    this.primaryStage.setScene(gameState.asScene());
                    break;
                }

                if (this.gameState.getClass() != rval.getClass()){
                    rval.printInitialText();
                }

                this.gameState = rval;
                this.primaryStage.setScene(rval.asScene());
                break;
            }
        }
    }

    private void help(Command cmd){
        if (cmd.getNumParams() == 1) {
            if (cmd.getArgv()[1].equalsIgnoreCase("help")) {
                String help = Utilities.HELP_START + "COMMAND\n\thelp [command]\nSYNOPSIS\n\tshows help information." + Utilities.ANSI_RESET;
                System.out.println(help);
                return;
            } else if (cmd.getArgv()[1].equalsIgnoreCase("quit")){
                String help = Utilities.HELP_START + "COMMAND\n\tquit\nSYNOPSIS\n\tquits the game." + Utilities.ANSI_RESET;
                System.out.println(help);
                return;
            } else {
                Executable e = gameState.getCommandMap().get(cmd.getArgAt(1));
                if (e != null) {
                    System.out.println(Utilities.HELP_START);
                    e.printHelp();
                    System.out.println(Utilities.ANSI_RESET);
                    return;
                }
            }
        }

        final String help = Utilities.HELP_START + "Supported commands: quit, help" + gameState.getCommands() + Utilities.ANSI_RESET;
        System.out.println(help);
    }

    private static void quit(Command cmd){
        System.exit(0);
    }


    @Override
    public void startNewGame(int numPlayers, int timeoutInSecs) {
        GameConfig config = this.game.getConfig();
        config.setAttribute("players", Integer.toString(numPlayers));
        config.setAttribute("timeout", Integer.toString(timeoutInSecs));
        if (numPlayers == 1) config.addComputer(2);
        this.gameState = new TurnState(this.game);
    }

    @Override
    public void createPlayer(String username, String marker, int playerNum) {
        if (this.game == null) this.game = new Game(new GameConfig());
        this.game.getConfig().createPlayer(username, marker, Integer.toString(playerNum));
    }

    @Override
    public boolean setSelection(int row, int col, int currentPlayer) {
        return game.selectTile(Integer.toString(row), Integer.toString(col), currentPlayer);
    }

    @Override
    public int determineWinner() {
        return game.determineWinner();
    }

    @Override
    public String getGameDisplay() {
        return game.getBoardDisplay();
    }

    public String getPrompt(){
        return this.gameState.getPrompt();
    }
}