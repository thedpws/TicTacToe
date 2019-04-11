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
        commands.put("setup", SETUP);
    }

    @Override
    public GameState consumeCommand(CommandCall c) {
        String command = c.getArgAt(0).toLowerCase();
        if (!commands.containsKey(command)) return null;
        return commands.get(command).execute(c);
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

    private final Command SETUP = c -> new GameSetupState();
}
