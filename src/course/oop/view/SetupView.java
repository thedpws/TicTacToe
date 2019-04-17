package course.oop.view;

import course.oop.controller.Controller;
import course.oop.fileio.FileIO;
import course.oop.model.Player;
import course.oop.util.Utilities;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.VBox;
import javafx.scene.text.TextAlignment;

import java.util.*;

public class SetupView implements TTTView {
    private final Scene scene;

    private boolean computer;

    private Label status = new Label();

    // for holding error messages
    private Queue<String> statusMessages = new LinkedList<>();

    final static int MAX_EMOJI = 39;
    static List<String> ENTRIES;
    PriorityQueue<Integer> removedPlayerNumbers = new PriorityQueue<>();
    LinkedList<PlayerSetup> team1 = new LinkedList<>();
    LinkedList<PlayerSetup> team2 = new LinkedList<>();

    public SetupView() {
        GridPane root = new GridPane();


        GridPane player1menu = new GridPane();

        // Load player entries
        HashMap<String, Player> map = FileIO.loadHashMap();
        ENTRIES = new LinkedList<>();
        for (Player p : map.values()) ENTRIES.add(p.asEntry());

        // Allow for adding players
        VBox team1Setups = new VBox();

        VBox team2Setups = new VBox();
        // team 1 initial player
        PlayerSetup initialSetup1 = new PlayerSetup();
        team1Setups.getChildren().add(initialSetup1.getMenu());
        team1.add(initialSetup1);
        // team 2 initial player
        PlayerSetup initialSetup2 = new PlayerSetup();
        team2Setups.getChildren().add(initialSetup2.getMenu());
        team2.add(initialSetup2);

        Button addPlayerTeam2 = new Button("Add player Team 2");
        addPlayerTeam2.setOnAction(e -> {
            PlayerSetup setup = new PlayerSetup();
            team2.add(setup);
            team2Setups.getChildren().add(setup.getMenu());
        });

        Button addPlayerTeam1 = new Button("Add player Team 1");
        addPlayerTeam1.setOnAction(e -> {
            PlayerSetup setup = new PlayerSetup();
            team1.add(setup);
            team1Setups.getChildren().add(setup.getMenu());
        });


        // Start Button
        Button start = new Button("Start game");
        start.setPrefWidth(100);
        start.setAlignment(Pos.CENTER_LEFT);

        Button mainmenu = new Button("REturn to main menu");
        mainmenu.setOnAction(e -> Controller.execute("mainmenu"));




        // Timeout
        Label timeoutLabel = new Label("Timeout (s) ");
        timeoutLabel.setTextAlignment(TextAlignment.RIGHT);
        timeoutLabel.setAlignment(Pos.BOTTOM_RIGHT);
        timeoutLabel.setPrefWidth(200);
        TextField timeout = new TextField("0");
        timeout.setAlignment(Pos.BOTTOM_RIGHT);
        timeout.setPrefWidth(200);
        HBox timeoutStuff = new HBox();
        timeoutStuff.getChildren().addAll(timeoutLabel, timeout);

        // Tile Properties
        CheckBox properties = new CheckBox("Tiles have special properties");
        properties.setOnAction(e -> {
            boolean selected = properties.isSelected();
            if (selected){
                Controller.execute("set properties on");
            } else Controller.execute("set properties off");
        });

        // Ultimate TTT
        CheckBox ultimate = new CheckBox("Ultimate Tic-Tac-Toe");
        ultimate.setOnAction(e -> {
            if (ultimate.isSelected()) Controller.execute("set ultimate on");
            else Controller.execute("set ultimate off");
        });
        //3d TTT
        CheckBox threedim = new CheckBox("3D Tic-Tac-Toe");
        threedim.setOnAction(e -> {
            if (threedim.isSelected()) Controller.execute("set three_dimensional on");
            else Controller.execute("set three_dimensional off");
        });
        // variable N
        HBox variableN = new HBox();
        Label variableNLabel = new Label("N =");
        TextField variableNInput = new TextField("3");
        //variableNInput.setTextFormatter(new TextFormatter)
        variableN.getChildren().addAll(variableNLabel, variableNInput);

        threedim.setOnAction(e -> {
            if (threedim.isSelected()) Controller.execute("set three_dimensional on");
            else Controller.execute("set three_dimensional off");
        });
        // Status
        this.status = new Label();


        this.scene = new Scene(root, 800, 600);

        start.setOnAction(e -> {
            //Controller.execute(String.format("set players %d", players));
            // Iterate through each player, adding them.
            for (PlayerSetup setup : team1)
                try {
                    setup.createPlayer(1);
                } catch (BadUsernameException ex) {
                    addError(ex.getMessage());
                }
            for (PlayerSetup setup : team2)
                try {
                    setup.createPlayer(2);
                } catch (BadUsernameException ex) {
                    addError(ex.getMessage());
                }
            // set timeout
            // TODO verify input
            Controller.execute(String.format("set timeout %d", Integer.parseInt(timeout.getText())));;
            Controller.execute(String.format("set n %d", Utilities.parseIntValue(variableNInput.getText())));
            if (statusMessages.isEmpty()) Controller.execute("start");
            else updateMessages();
        });

        root.getColumnConstraints().add(new ColumnConstraints(100));
        root.getColumnConstraints().add(new ColumnConstraints(350));
        root.getColumnConstraints().add(new ColumnConstraints(100));
        root.getColumnConstraints().add(new ColumnConstraints(350));
        root.add(addPlayerTeam1, 0, 0);
        root.add(team1Setups, 1, 0);
        root.add(addPlayerTeam2, 2, 0);
        root.add(team2Setups, 3, 0);
        root.add(timeoutStuff, 0, 1);
        root.add(properties, 0, 2);
        root.add(ultimate, 0, 3);
        root.add(threedim, 0, 4);
        root.add(variableN, 0, 5);
        root.add(status, 3, 1);
        root.add(mainmenu, 2, 2);
        root.add(start, 2, 1);

    }

    private void updateMessages() {
        status.setText("");
        StringBuilder sb = new StringBuilder();
        while (!statusMessages.isEmpty()) {
            String message = statusMessages.poll();
            sb.append(String.format("%s\n", message));
        }
        status.setText(sb.toString());
    }

    @Override
    public Scene getScene() {
        return scene;
    }

    private void addError(String s){
        this.statusMessages.add(s);
    }

    static class BadUsernameException extends Exception {
        int playerNumber;
        BadUsernameException(int playerNumber){
            this.playerNumber = playerNumber;
        }
        @Override
        public String getMessage(){
           return String.format("Bad username for player %d", playerNumber);
        }
    }
}

class PlayerSetup {
    private ImageView emojiImage;
    private int emoji = 1;
    private boolean computer;
    private final GridPane menu;
    private Player p;

    private ComboBox usernameCombo;


    PlayerSetup(){
        this.menu = createMenu();
    }

    void createPlayer(int teamNumber) throws SetupView.BadUsernameException {
        if (computer) {
            Controller.execute(String.format("createcomputer %d", teamNumber));
            return;
        } // else
        String username = getUsername();
        if (username.equals("")) throw new SetupView.BadUsernameException(teamNumber);
        Controller.execute(String.format("createplayer %s %d %d", username, emoji, teamNumber));
    }

    private String getUsername(){
        // parses the username from the player name using regex
        String username = usernameCombo.getEditor().getText();
        username = username.replaceAll("(\\w*).*", "$1");
        return username;
    }


    GridPane createMenu(){
        GridPane menu = new GridPane();

        // Username box
        Label usernameLabel = new Label(String.format("hey"));
        this.usernameCombo = new ComboBox();
        usernameCombo.setEditable(true);

        usernameCombo.getItems().addAll(SetupView.ENTRIES);

        // Action handling
        usernameCombo.setOnAction(event -> {
            String username = usernameCombo.getEditor().getText();
            username = username.replaceFirst("(\\w*).*", "$1");
            Player p = FileIO.loadPlayer(username);
            if (p.getMarkerID() != Player.DEFAULT_MARKER) emoji = p.getMarkerID();
            emojiImage.setImage(new Image(String.format("%d.png", emoji % SetupView.MAX_EMOJI)));
            this.p = p;
        });

        menu.add(usernameLabel, 0, 0);
        menu.add(usernameCombo, 1, 0);

        // choose an emoji
        emojiImage = new ImageView(new Image(String.format("%d.png", emoji)));
        emojiImage.setFitWidth(200);
        emojiImage.setFitHeight(200);

        Button emojiPlus = new Button(">");
        emojiPlus.setOnAction(e -> {
            if (p == null) emojiImage.setImage(new Image((++emoji % 3 + 1) + ".png"));
            //emojiImage.setImage(new Image(String.format("%d.png", ++emoji % SetupView.MAX_EMOJI)))
            else emojiImage.setImage(new Image(p.getEmoji(++emoji) + ".png"));
        });
        Button emojiMinus = new Button("<");
        emojiMinus.setOnAction(e -> {
            //if (emoji == 0) emoji += SetupView.MAX_EMOJI;
            if (p != null)
            emojiImage.setImage(new Image(p.getEmoji(--emoji) + ".png"));
            else
            emojiImage.setImage(new Image(String.format("%d.png", (--emoji) % 3 + 1)));
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
