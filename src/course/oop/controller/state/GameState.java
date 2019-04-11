package course.oop.controller.state;

import course.oop.view.CommandCall;
import javafx.scene.Scene;

public interface GameState {


    GameState consumeCommand(CommandCall c);

    String getPrompt();

    void printInitialText();

    Scene asScene();
}t a


