package course.oop.view;

import course.oop.controller.Controller;
import course.oop.fileio.FileIO;
import course.oop.model.players.Player;
import javafx.geometry.Pos;
import javafx.scene.Node;
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

import java.util.*;

public class SetupView implements TTTView {
    private final Scene scene;

    private boolean computer;

    final static int MAX_EMOJI = 39;
    static List<String> ENTRIES;
    int players = 0;
    PriorityQueue<Integer> removedPlayerNumbers = new PriorityQueue<>();
    LinkedList<PlayerSetup> playerSetups = new LinkedList<>();

    public SetupView() {
        GridPane root = new GridPane();
        Controller.execute("set players 2");


        GridPane player1menu = new GridPane();

        // Load player entries
        HashMap<String, Player> map = FileIO.loadHashMap();
        ENTRIES = new LinkedList<>();
        for (Player p :  map.values()) ENTRIES.add(p.asEntry());

        // Allow for adding players
        Button addPlayer = new Button("Add player");
        addPlayer.setOnAction(e -> {
            int playerNumber;

            if (!removedPlayerNumbers.isEmpty()) {
                // get new number from PQ
                playerNumber = removedPlayerNumbers.poll();
            } else {
                playerNumber = players;
            }

            players++;
            PlayerSetup setup = new PlayerSetup(playerNumber);
            playerSetups.add(setup);
            root.add(setup.getMenu(), playerNumber - 1, 0);
        });
        root.add(addPlayer,0, 5);

        players = 2;
        PlayerSetup s1 = new PlayerSetup(1);
        PlayerSetup s2 = new PlayerSetup(2);
        playerSetups.add(s1);
        playerSetups.add(s2);

        root.add(s1.getMenu(), 0, 0);
        root.add(s2.getMenu(), 1, 0);


        scene = new Scene(root, 800, 600);



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

/*
        start.setOnAction(e -> {
            System.out.printf("Emoji1: %d. Emoji2 %d%n", emoji1, emoji2);
            Controller.execute(String.format("set players %d", computer ? 1 : 2));
            // write player 1
            String username1 = usernameCombo1.getEditor().getText();
            username1 = username1.replaceAll("(\\w*).*", "$1");
            System.out.println(username1);
            // TODO: Make a null player class
            Player p1 = FileIO.loadPlayer(username1);
            if (p1 == null) p1 = new Player(username1, emoji1);
            p1.updateMarkerID(emoji1);
            Controller.execute(String.format("createplayer %s %d %d", username1, emoji1, 1));
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
                Controller.execute(String.format("createplayer %s %d %d", username2, emoji2, 2));
                FileIO.writePlayer(p2);
                System.out.println("SetupView.java: " + p2.asEntry());
                */

            // set timeout
            Controller.execute(String.format("set timeout %d", Integer.parseInt(timeout.getText())));


            Controller.execute("start");

        }

    @Override
    public Scene getScene() {
        return scene;
    }
}

class PlayerSetup {
    private ImageView emojiImage;
    private int emoji = 0;
    private boolean computer;
    private final GridPane menu;
    private final int n;

    private ComboBox usernameCombo;


    PlayerSetup(int n){
        this.menu = createMenu();
        this.n = n;
    }

    Player getPlayer(){
        String username = this.usernameCombo.getEditor().getText();
        Player p = FileIO.loadPlayer(username);
        p.updateMarkerID(emoji);
        FileIO.writePlayer(p);
        return p;
    }


    GridPane createMenu(){
        GridPane menu = new GridPane();

        // Username box
        Label usernameLabel = new Label(String.format("Player %d", this.n));
        this.usernameCombo = new ComboBox();
        usernameCombo.setEditable(true);

        usernameCombo.getItems().addAll(SetupView.ENTRIES);

        // Action handling
        usernameCombo.setOnAction(event -> {
            String username = usernameCombo.getEditor().getText();
            username = username.replaceFirst("(\\w*).*", "$1");
            Player p = FileIO.loadPlayer(username);
            if (p != null) emoji = p.getMarkerID();
            emojiImage.setImage(new Image(String.format("%d.png", emoji % SetupView.MAX_EMOJI)));
        });

        menu.add(usernameLabel, 0, 0);
        menu.add(usernameCombo, 1, 0);

        // choose an emoji
        emojiImage = new ImageView(new Image(String.format("%d.png", emoji)));
        emojiImage.setFitWidth(200);
        emojiImage.setFitHeight(200);

        Button emojiPlus = new Button(">");
        emojiPlus.setOnAction(e -> emojiImage.setImage(new Image(String.format("%d.png", ++emoji % SetupView.MAX_EMOJI))));
        Button emojiMinus = new Button("<");
        emojiMinus.setOnAction(e -> {
            if (emoji == 0) emoji += SetupView.MAX_EMOJI;
            emojiImage.setImage(new Image(String.format("%d.png", (--emoji) % SetupView.MAX_EMOJI)));
        });


        Button makeComputer = new Button("Make CPU");
        makeComputer.setAlignment(Pos.CENTER);
        makeComputer.setOnAction(e -> {
            computer = !computer;
            usernameCombo.setDisable(computer);
            emojiImage.setImage(new Image(computer ? "39.png" : String.format("%d.png", emoji)));
            emojiPlus.setDisable(computer);
            emojiMinus.setDisable(computer);
        });

        menu.add(emojiMinus, 0, 1);
        menu.add(emojiImage, 1, 1);
        menu.add(emojiPlus, 2, 1);
        menu.add(makeComputer, 1, 2);
        makeComputer.setAlignment(Pos.CENTER);

        return menu;
    }

    Node getMenu(){
        return this.menu;
    }


}
