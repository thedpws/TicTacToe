package course.oop.view;

import course.oop.controller.Controller;
import course.oop.model.Game;
import course.oop.model.Player;
import javafx.animation.KeyFrame;
import javafx.animation.RotateTransition;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

public class TurnView implements TTTView {
    Parent root;
    final Label status;
    private Label clock;
    Scene scene;
    private long timeleft;

    private final int n = 100;

    // TODO Break into functions
    public TurnView(Game game, int team, int[] player) {
        BorderPane root = new BorderPane();
        Parent center = game.getDisplay();

        root.setCenter(center);

        // If Computer, automate tile selection
        Player p = game.getPlayer(team, player[team]);
        if (p.isComputer()) {
            Timeline cpu = new Timeline(new KeyFrame(Duration.seconds(1.0), event -> {
            }));
            //cpu.setOnFinished(event -> Controller.execute("select " + game.selectRandomTile()));
            cpu.setOnFinished(e -> Controller.execute("select " + game.selectRandomTile()));
            cpu.setCycleCount(1);
            cpu.play();
        }

        GridPane bottom = new GridPane();
        root.setBottom(bottom);
        timeleft = game.getConfig().getTimeout();

        if (game.getConfig().getTimeout() > 0) {
            clock = new Label();
            bottom.add(clock, 0, 0);
            clock.setText(String.format("%d", timeleft));
            Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1.0), event -> clock.setText(String.format("%d", --timeleft))));
            timer.setCycleCount((int) game.getConfig().getTimeout());
            timer.setOnFinished(event -> {
                Controller.interruptTurn();
                Controller.interruptTurn();
            });
            timer.play();
        }

        System.out.println(center.getRotationAxis());
        Button rotateCCW = new Button("Rotate");
        rotateCCW.setOnMouseClicked(e -> Controller.execute("rotate ccw"));
        Button rotateCW = new Button("Rotate");
        rotateCW.setOnMouseClicked(e -> Controller.execute("rotate cw"));
        root.setLeft(rotateCW);
        root.setRight(rotateCCW);

        status = new Label(String.format("It's %s's turn!", game.getPlayer(team, player[team])));
        bottom.add(status, 1, 0);
        bottom.setAlignment(Pos.TOP_CENTER);
        status.setFont(Font.font(20));
        this.scene = new Scene(root, 800, 600);

        this.root = root;
    }


    @Override
    public Scene getScene() {
        return this.scene;
    }
}
