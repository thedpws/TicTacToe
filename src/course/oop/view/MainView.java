package course.oop.view;

import course.oop.controller.Controller;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;

public class MainView implements TTTView {
    private final Scene scene;

    public MainView() {

        // Root node
        VBox root = new VBox();
        root.setAlignment(Pos.CENTER);
        root.setSpacing(20);

        // Title page
        Label title = new Label("Tic-tac-toe: The Game!");
        title.setFont(Font.font(50));

        // Main Menu
        Button start = new Button("Start");
        Button quit = new Button("Quit");
        Button store = new Button("Shop");

        // Actions
        start.setOnAction(e -> onStart());
        quit.setOnAction(e -> Controller.execute("quit"));
        store.setOnAction(e -> Controller.execute("store"));

        root.getChildren().addAll(title, start, store, quit);
        this.scene = new Scene(root, 800, 600);
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    private void onStart() {
        Controller.execute("setup");
    }
}
