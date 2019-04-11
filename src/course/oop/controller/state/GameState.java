package course.oop.controller.state;

import course.oop.view.Command;
import javafx.scene.Scene;

import java.util.Map;

public interface GameState{


    GameState consumeCommand(Command c);
    String getCommands();
    String getPrompt();
    Map<String, Executable> getCommandMap();
    void printInitialText();

    Scene asScene();
}


