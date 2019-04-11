package course.oop.controller.state;

import course.oop.view.CommandCall;
import course.oop.view.MainView;
import course.oop.view.TTTView;
import javafx.scene.Scene;

import java.util.HashMap;
import java.util.Map;

// initial
public class InitialState implements GameState {

    private Map<String, Command> commands;

    TTTView view;


    public InitialState(){
        this.view = new MainView();
        commands = new HashMap<>();
        commands.put("setup", new Command() {
                    @Override
                    GameState execute(CommandCall c) {
                        final int N_PARAMS = 0;
                        if (c.getNumParams() != N_PARAMS){
                            printCorrectUsage();
                            return InitialState.this;
                        }

                        return new GameSetupState();
                    }

                    @Override
                    public String getHelp() {
                        return "COMMAND\n\tsetup\nSYNOPSIS\n\tenters game setup mode.";
                    }

                    @Override
                    String getCorrectUsage() {
                        return "setup";
                    }
                });
    }

    @Override
    public GameState consumeCommand(CommandCall c) {
        String command = c.getArgAt(0).toLowerCase();
        if (!commands.containsKey(command)) return null;
        return commands.get(command).execute(c);
    }

    @Override
    public String getCommands() {
        StringBuilder sb = new StringBuilder();
        for (String s : this.commands.keySet()){
            sb.append(String.format(", %s", s));
        }
        return sb.toString();
    }

    @Override
    public String getPrompt() {
        return "Main Menu";
    }

    @Override
    public void printInitialText(){

    }

    @Override
    public Scene asScene() {
        return view.getScene();
    }

    @Override
    public Map<String, Command> getCommandMap(){
        return this.commands;
    }

}
