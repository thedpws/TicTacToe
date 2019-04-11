package course.oop.view;

import course.oop.model.Game;
import course.oop.model.Player;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Font;
import javafx.util.Duration;

public class TurnView implements TTTView {
    final BorderPane root;
    final Label status;
    private Label clock;
    final Scene scene;
    private long timeleft;

    private final int n = 100;

    // TODO Break into functions
    public TurnView(Game game, int player) {

        root = new BorderPane();
        GridPane board = new GridPane();
        String[][] stringboard = game.getGameBoard();

        // CENTER is the game board
        final int N = stringboard.length;
        for (int row = 0; row < N; row++) {
            for (int col = 0; col < N; col++) {
                String mark = stringboard[row][col];
                if (mark.equals("")) mark = "-1";
                int emojiID = Integer.parseInt(mark);
                ImageView emoji;

                if (emojiID == -1) {
                    emoji = new ImageView(new Image("blank.png"));
                    emoji.setFitWidth(n - 5);
                    emoji.setFitHeight(n - 5);
                } else {
                    emoji = new ImageView(new Image(String.format("%d.png", emojiID)));
                    emoji.setFitWidth(n);
                    emoji.setFitHeight(n);
                }
                int finalRow = row;
                int finalCol = col;
                emoji.setOnMouseClicked(event -> View.execute(String.format("select %d %d", finalRow, finalCol)));
                board.add(emoji, col, row);
            }
        }
        ColumnConstraints cMax = new ColumnConstraints(n, n, n);
        RowConstraints rMax = new RowConstraints(n, n, n);
        board.getColumnConstraints().addAll(cMax, cMax, cMax);
        board.getRowConstraints().addAll(rMax, rMax, rMax);
        // BOTTOM is the person's turn.
        StackPane center = new StackPane();

        ImageView ttt = new ImageView(new Image("ttt.jpg"));
        ttt.setFitWidth(n * 3);
        ttt.setFitHeight(n * 3);
        board.setAlignment(Pos.CENTER);


        center.getChildren().add(ttt);
        center.getChildren().add(board);

        StackPane.setAlignment(ttt, Pos.CENTER);

        StackPane.setAlignment(board, Pos.CENTER);

        root.setCenter(center);


        Player p = game.getPlayer(player);
        System.out.println("#### Player was " + player);
        if (player != 0 && player != 3 && !p.isHuman()) {
            Timeline cpu = new Timeline(new KeyFrame(Duration.seconds(1.0), event -> {
            }));
            cpu.setOnFinished(event -> View.execute("select " + game.selectRandomTile()));
            cpu.setCycleCount(1);
            cpu.play();
        }


        GridPane bottom = new GridPane();
        root.setBottom(bottom);
        timeleft = game.getConfig().getTimeout();

        if (game.getConfig().getTimeout() != 0) {
            clock = new Label();
            bottom.add(clock, 0, 0);
            clock.setText(String.format("%d", timeleft));
            Timeline timer = new Timeline(new KeyFrame(Duration.seconds(1.0), event -> clock.setText(String.format("%d", --timeleft))));
            timer.setCycleCount((int) game.getConfig().getTimeout());
            timer.setOnFinished(event -> {
                View.interruptTurn();
                View.interruptTurn();
            });
            timer.play();
        }

        status = new Label(String.format("It's %s's turn!", game.getPlayer(player)));
        bottom.add(status, 1, 0);
        bottom.setAlignment(Pos.TOP_CENTER);
        status.setFont(Font.font(20));
        this.scene = new Scene(root, 600, 500);
    }

    @Override
    public Scene getScene() {
        return this.scene;
    }
}
