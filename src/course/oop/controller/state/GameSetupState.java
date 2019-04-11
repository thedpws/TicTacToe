package course.oop.controller.state;

import course.oop.model.GameConfig;
import course.oop.util.Utilities;
import course.oop.view.Command;
import course.oop.view.SetupView;
import course.oop.view.TTTView;
import javafx.scene.Scene;

import java.util.HashMap;
import java.util.Map;

public class GameSetupState implements GameState {

    private GameConfig gameConfig;
    private Map<String, Executable> commands;
    private TTTView view;
    //boolean readyToStart;

    public GameSetupState(){
        this(new GameConfig());
    }
    public GameSetupState(GameConfig config){
        this.view = new SetupView();
        this.gameConfig = config;
        this.commands = new HashMap<>();
        this.commands.put("set", new Executable() {
                    @Override
                    public GameState execute(Command c) {
                        final int N_PARAMS = 2;
                        if (c.getNumParams() != N_PARAMS){
                            printCorrectUsage();
                            return GameSetupState.this;
                        }

                        String attribute = c.getArgv()[1];
                        String value = c.getArgv()[2];
                        gameConfig.setAttribute(attribute, value);
                        return GameSetupState.this;
                    }

                    @Override
                    public String getHelp() {
                        return "COMMAND\n\tset [attribute] [value]\nSYNOPSIS\n\tsets a game attribute to a certain value.\nGAME ATTRIBUTES\n\tplayers - Number of players\n\ttimeout - Time limit per turn";
                    }

                    @Override
                    public String getCorrectUsage() {
                        return "set [attribute] [value]";
                    }
                });
        this.commands.put("start", new Executable(){

                    @Override
                    GameState execute(Command c) {
                        final int N_PARAMS = 0;
                        if (c.getNumParams() != N_PARAMS){
                            printCorrectUsage();
                            return GameSetupState.this;
                        }

                        if (!gameConfig.isValid()) {
                            gameConfig.printStatus();
                            return GameSetupState.this;
                        }
                        return new GameInitState(gameConfig);
                    }

                    @Override
                    public String getHelp() {
                        return "COMMAND\n\tstart\nSYNOPSIS\n\tstarts the game.";
                    }

                    @Override
                    String getCorrectUsage() {
                        return "start";
                    }
                });
        this.commands.put("createplayer", new Executable() {
                    @Override
                    GameState execute(Command c) {
                        final int N_PARAMS = 3;
                        if (c.getNumParams() != N_PARAMS){
                            printCorrectUsage();
                            return GameSetupState.this;
                        }
                        String username = c.getArgv()[1];
                        String marker = c.getArgv()[2];
                        String number = c.getArgv()[3];
                        gameConfig.createPlayer(username, marker, number);
                        return GameSetupState.this;

                    }

                    @Override
                    public String getHelp() {
                        return "COMMAND\n\tcreateplayer [username] [marker] [number]\nSYNOPSIS\n\tCreates a player.\nNOTE\n\tMarkers must be 1 character only";
                    }

                    @Override
                    String getCorrectUsage() {
                        return "createplayer [username] [marker] [number]";
                    }
                });
    }

    @Override
    public GameState consumeCommand(Command c) {
        String cmd = c.getArgv()[0];
        if (!commands.containsKey(cmd)) return null;
        return commands.get(cmd.toLowerCase()).execute(c);
        //return commands.get(c.getArgv()[0].toLowerCase()).execute(c);
    }

    public String getCommands() {
        StringBuilder sb = new StringBuilder();
        for (String s : this.commands.keySet()){
            sb.append(String.format(", %s", s));
        }
        return sb.toString();
    }

    @Override
    public String getPrompt(){
        return "Game Configuration";
    }

    @Override
    public Map<String, Executable> getCommandMap(){
        return this.commands;
    }

    @Override
    public void printInitialText(){
        String circle = Utilities.CIRCLE;
                //"\u2b55";
        String cross = Utilities.CROSS;
                //"\u274c";
        System.out.printf("%s%s%s%s%s Game Setup %s%s%s%s%s%n", circle, circle, circle, circle, circle, cross, cross, cross, cross, cross);
        System.out.println();System.out.println();
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
}
