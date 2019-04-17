package course.oop.controller.state;

import course.oop.controller.Controller;
import course.oop.fileio.FileIO;
import course.oop.model.Player;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.effect.ColorAdjust;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.VBox;

import java.util.LinkedList;
import java.util.List;

public class StoreView implements course.oop.view.TTTView {
    private String username;
    @Override
    public Scene getScene() {
        BorderPane parent = new BorderPane();

        ComboBox playerSelection = new ComboBox();
        List<String>  entries = new LinkedList<>();
        FileIO.loadHashMap().values().forEach(player -> entries.add(player.asEntry()));
        playerSelection.getItems().addAll(entries);

        GridPane store = new GridPane();
        final int rows = 5;
        final int cols = 20;
        // fill with emojis
        for (int i = 0; i < 39; i++){
            VBox storeItem = new VBox();
            ImageView emoji = new ImageView(new Image(i + ".png"));
            emoji.setFitHeight(50);
            emoji.setFitWidth(50);
            Button buy = new Button("$100");
            storeItem.getChildren().addAll(emoji, buy);
            store.add(storeItem, i % cols, i / rows);
            //storeItem.getChildren().addAll();

        }
        // load player, update bought emojis
        playerSelection.setOnAction(e -> {
           String username = playerSelection.getEditor().getText();
           username = username.replaceFirst("(\\w*).*", "$1");
           this.username = username;
           //update bought emojis
           Player p = FileIO.loadPlayer(username);
           List<Integer> boughtEmojis = p.emojisUnlocked;
           boughtEmojis.forEach(emojiID -> {
               int row = emojiID / rows;
               int col = emojiID / cols;

               for (Node child : store.getChildren()){
                   int childCol = GridPane.getColumnIndex(child);
                   int childRow = GridPane.getRowIndex(child);
                   if (row == childRow && col==childCol){
                       child.setEffect(new ColorAdjust(0, -10, 0, 0));
                       child.setDisable(true);
                       break;
                   }
               }
           });

        });

        Button mainmenu = new Button("Return to main menu");
        mainmenu.setOnAction(e -> Controller.execute("mainmenu"));
        parent.setBottom(mainmenu);


        parent.setTop(playerSelection);
        parent.setCenter(store);
        return new Scene(parent, 800, 600);
    }
}
