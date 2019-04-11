package course.oop.controller.state;

import course.oop.view.CommandCall;
import javafx.scene.Scene;

import java.util.Map;

public interface GameState{


    GameState consumeCommand(CommandCall c);
    String getPrompt();
    void printInitialText();

    Scene asScene();
}


