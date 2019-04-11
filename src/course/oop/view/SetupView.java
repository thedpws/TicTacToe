package course.oop.view;

import course.oop.fileio.FileIO;
import course.oop.model.Player;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.geometry.Pos;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.text.TextAlignment;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class SetupView implements TTTView{
    private GridPane root ;
    private Scene scene;
    private ImageView currEmoji1;
    private ImageView currEmoji2;

    private int emoji1 = 1;
    private int emoji2 = 1;

    private boolean computer;

    private final int maxEmoji = 39;

    public SetupView(){
        root = new GridPane();
        View.execute("set players 2");


        GridPane player1menu = new GridPane();

        HashMap<String, Player> map = FileIO.loadHashMap();
        List<String> entries = new LinkedList<>();
        for (Player p : map.values()) entries.add(p.asEntry());

        // Enter username:
        Label usernameLabel1 = new Label("Player 1 ");
        ComboBox usernameCombo1 = new ComboBox();
        usernameCombo1.setEditable(true);
        usernameCombo1.getItems().addAll(entries);

        // Action handler
        usernameCombo1.setOnAction(event -> {
            String username = usernameCombo1.getEditor().getText();
            username = username.replaceFirst("(\\w*).*", "$1");
            Player p = FileIO.loadPlayer(username);
            if (p != null) emoji1 = p.getMarkerID();
            currEmoji1.setImage(new Image(String.format("%d.png", emoji1 % maxEmoji)));
        });

        player1menu.add(usernameLabel1, 0, 0);
        player1menu.add(usernameCombo1, 1, 0);

        // choose an emoji
        currEmoji1 = new ImageView(new Image(String.format("%d.png", emoji1)));
        currEmoji1.setFitWidth(200);
        currEmoji1.setFitHeight(200);

        Button emojiPlus1 = new Button(">");
        emojiPlus1.setOnAction(e -> currEmoji1.setImage(new Image(String.format("%d.png", ++emoji1 % maxEmoji))));
        Button emojiMinus1 = new Button("<");
        emojiMinus1.setOnAction(e -> {
            if (emoji1 == 0) emoji1 += maxEmoji;
            currEmoji1.setImage(new Image(String.format("%d.png", (--emoji1) % maxEmoji)));
        });

        player1menu.add(emojiMinus1,0, 1);
        player1menu.add(currEmoji1, 1, 1);
        player1menu.add(emojiPlus1, 2, 1);

        // Enter username:
        Label usernameLabel2 = new Label("Player 2 ");
        ComboBox usernameCombo2 = new ComboBox();
        usernameCombo2.setEditable(true);
        usernameCombo2.getItems().addAll(entries);
        usernameCombo2.setOnAction(event -> {
            String username = usernameCombo2.getEditor().getText();
            username = username.replaceFirst("(\\w*).*", "$1");
            Player p = FileIO.loadPlayer(username);
            if (p != null) emoji2 = p.getMarkerID();
            currEmoji2.setImage(new Image(String.format("%d.png", emoji2 % maxEmoji)));
        });

        GridPane player2menu = new GridPane();
        player2menu.add(usernameLabel2, 0, 0);
        player2menu.add(usernameCombo2, 1, 0);


        currEmoji2 = new ImageView(new Image(String.format("%d.png", emoji2)));
        currEmoji2.setFitWidth(200);
        currEmoji2.setFitHeight(200);

        Button emojiPlus2 = new Button(">");
        emojiPlus2.setOnAction(e -> currEmoji2.setImage(new Image(String.format("%d.png", ++emoji2 % maxEmoji))));
        Button emojiMinus2 = new Button("<");
        emojiMinus2.setOnAction(e -> {
            if (emoji2 == 0) emoji2 += maxEmoji;
            currEmoji2.setImage(new Image(String.format("%d.png", (--emoji2) % maxEmoji)));
        });


        Button makeComputer2 = new Button("Make CPU");
        makeComputer2.setAlignment(Pos.CENTER);
        makeComputer2.setOnAction(e -> {
            computer = !computer;
            usernameCombo2.setDisable(computer);
            currEmoji2.setImage(new Image(computer ? "39.png" : String.format("%d.png", emoji2)));
            emojiPlus2.setDisable(computer);
            emojiMinus2.setDisable(computer);
        });

        player2menu.add(emojiMinus2,0, 1);
        player2menu.add(currEmoji2, 1, 1);
        player2menu.add(emojiPlus2, 2, 1);
        player2menu.add(makeComputer2, 1, 2);
        makeComputer2.setAlignment(Pos.CENTER);


        scene = new Scene(root, 850, 300);



        root.add(player1menu, 0, 0);
        root.add(player2menu, 1, 0);

        Button start = new Button("Start game");
        start.setPrefWidth(100);
        start.setAlignment(Pos.CENTER_LEFT);

        root.add(start, 2, 1);

        root.getColumnConstraints().add(new ColumnConstraints(350));
        root.getColumnConstraints().add(new ColumnConstraints(350));

        root.getColumnConstraints().add(new ColumnConstraints(200));






        Label timeoutLabel = new Label("Timeout (s) ");
        timeoutLabel.setTextAlignment(TextAlignment.RIGHT);
        timeoutLabel.setAlignment(Pos.BOTTOM_RIGHT);
        timeoutLabel.setPrefWidth(200);
        TextField timeout = new TextField("0");
        timeout.setAlignment(Pos.BOTTOM_RIGHT);
        timeout.setPrefWidth(200);
        HBox timeoutStuff = new HBox();
        timeoutStuff.getChildren().addAll(timeoutLabel, timeout);
        root.add(timeoutStuff, 0, 1);


        start.setOnAction(e -> {
            System.out.printf("Emoji1: %d. Emoji2 %d%n", emoji1, emoji2);
            View.execute(String.format("set players %d", computer ? 1 : 2));
            // write player 1
            String username1 = usernameCombo1.getEditor().getText();
            username1 = username1.replaceAll("(\\w*).*", "$1");
            System.out.println(username1);
            Player p1 = FileIO.loadPlayer(username1);
            if (p1 == null) p1 = new Player(username1, emoji1);
            p1.updateMarkerID(emoji1);
            View.execute(String.format("createplayer %s %d %d", username1, emoji1, 1));
            FileIO.writePlayer(p1);
            System.out.println("SetupView.java: " + p1.asEntry());

            // write player 2
            if (!computer) {
                String username2 = usernameCombo2.getEditor().getText();
                username2 = username2.replaceAll("(\\w*).*", "$1");
                System.out.println(username2);
                Player p2 = FileIO.loadPlayer(username2);
                if (p2 == null) p2 = new Player(username2, emoji2);
                p2.updateMarkerID(emoji1);
                View.execute(String.format("createplayer %s %d %d", username2, emoji2, 2));
                FileIO.writePlayer(p2);
                System.out.println("SetupView.java: " + p2.asEntry());
            }

            // set timeout
            View.execute(String.format("set timeout %d", Integer.parseInt(timeout.getText())));


            View.execute("start");

        });

        /*
        root = new VBox();
        Button start = new Button("set players 2");
        Button quit = new Button("Quit");
        Button shit = new Button("Help");
        Button quickstart = new Button("Quick start");

        quit.setOnAction(e -> View.execute("quit"));
        shit.setOnAction(e -> View.execute("help"));
        quickstart.setOnAction(e -> {
            //⭕ ❌
            String[] commands = {
                    "set players 2",
                    "createplayer player_one x 1",
                    "createplayer player_two o 2",
                    "set timeout 10",
                    "start",
            };
            for (String command : commands) View.execute(command);
        });

        start.setOnAction(e -> onStart());
        root.getChildren().add(start);
        root.getChildren().add(quit);
        root.getChildren().add(shit);
        root.getChildren().add(quickstart);
        scene = new Scene(root, 300, 250);
        */
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    private void onStart() {
        View.execute("set players 2");
    }
}
