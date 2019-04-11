package course.oop.application;

import course.oop.controller.TTTControllerImpl;
import course.oop.fileio.FileIO;
import course.oop.view.View;
import javafx.application.Application;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class Main extends Application {
    @Override
    public void start(Stage primaryStage){
        FileIO.init();
        TTTControllerImpl.initiate(primaryStage);
        primaryStage.setTitle("Tic Tac Toe: The Game");
        primaryStage.show();
    }
}
