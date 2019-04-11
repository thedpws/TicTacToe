package course.oop.view;

import course.oop.controller.Controller;
import course.oop.model.Game;
import javafx.animation.RotateTransition;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;
import javafx.util.Duration;

public class ResultsView extends TurnView {
    public ResultsView(Game g) {
        super(g, 0);

        int winnerId = g.determineWinner();
        String winner;
        switch (winnerId) {
            case 1:
                winner = g.getPlayer(winnerId).toString();
                break;
            case 2:
                winner = g.getPlayer(winnerId).toString();
                break;
            case 3:
                winner = "Nobody";
                break;
            default:
                winner = "problem";
                break;
        }
        this.status.setText(String.format("%s is the winner! Congratulations!", winner));
        this.status.setAlignment(Pos.TOP_CENTER);
        this.status.setTextAlignment(TextAlignment.CENTER);
        this.root.setTop(status);
        this.root.setBottom(null);

        VBox menu = new VBox();
        menu.setSpacing(20.0);
        menu.setAlignment(Pos.CENTER_LEFT);
        menu.setPadding(new Insets(5));
        Button rematch = new Button("rematch");
        Button mainmenu = new Button("main menu");
        Button setup = new Button("return to game setup");

        rematch.setOnAction(e -> Controller.execute("rematch"));
        mainmenu.setOnAction(e -> Controller.execute("mainmenu"));
        setup.setOnAction(e -> Controller.execute("setup"));

        menu.getChildren().addAll(rematch, setup, mainmenu);
        this.root.setRight(menu);

        RotateTransition rt = new RotateTransition();
        rt.setDuration(Duration.millis(10));
        rt.setNode(root.getCenter());
        rt.setByAngle(360);
        rt.setCycleCount(50);
        rt.play();
    }

    @Override
    public Scene getScene() {
        return scene;
    }

}
