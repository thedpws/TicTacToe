package course.oop.controller.state;

import course.oop.model.Game;
import course.oop.model.GameConfig;
import course.oop.util.Utilities;
import course.oop.view.Command;
import javafx.scene.Scene;

import java.util.Map;

public class GameInitState implements GameState {

    private GameConfig gameConfig;

    public GameInitState(GameConfig gameConfig) {
        this.gameConfig = gameConfig;
        // user will always be player 1, computer is player 2
        for (int i = gameConfig.getNumPlayers(); i <= 2; i++){
            gameConfig.addComputer(i);
        }
    }

    public Game produceGame(){
        return new Game(gameConfig);
    }

    @Override
    public GameState consumeCommand(Command c) {
        return null;
    }

    public String getCommands() {
        return "";
    }

    @Override
    public String getPrompt(){
        return "Game initialization";
    }

    @Override
    public Map<String, Executable> getCommandMap(){
        return null;
    }

    @Override
    public void printInitialText(){
        System.out.println(Utilities.HELP_START + "Initializing game..." + Utilities.ANSI_RESET);

    }

    @Override
    public Scene asScene() {
        return null;
    }
}
