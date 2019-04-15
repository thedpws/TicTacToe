package course.oop.model.board;

import course.oop.controller.Controller;
import course.oop.model.Marker;
import course.oop.util.Utilities;
import javafx.animation.RotateTransition;
import javafx.geometry.Pos;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.StackPane;
import javafx.scene.transform.Rotate;
import javafx.util.Duration;

import java.util.*;

import static java.lang.System.exit;

public class ClassicBoard implements GameBoard {
    Tile[][] tiles;
    int n;
    int rotation; //todo implement for rotating boardgame

    public ClassicBoard(int n){
        this.n = n;
        this.tiles = createClassicBoard(n);
    }


    // Creates an n by n grid
    private static Tile[][] createClassicBoard(int n){
        Tile[][] tiles = new Tile[n][n];

        // build tiles
        for (int row = 0; row < n; row++) for (int col = 0; col < n; col++){
            tiles[row][col] = new Tile();
        }

        // build connections
        for (int row = 0; row < n; row++){
            for (int col = 0; col < n; col++){
                Tile current = tiles[row][col];
                //connect all its neighbors

                for (int rowOffset = -1; rowOffset <= 1; rowOffset++){
                    for (int colOffset = -1; colOffset <=1; colOffset++){
                        int nRow = row + rowOffset;
                        int nCol = col + colOffset;
                        if (!(0 <= nRow && nRow < n && 0 <= nCol && nCol < n)) continue;
                        Tile neighbor  = tiles[nRow][nCol];
                        if (neighbor == current) continue;
                        //get direction
                        int direction = -1;
                        if (rowOffset == -1){
                            if (colOffset == -1) direction = Tile.UL;
                            if (colOffset ==  0) direction = Tile.U;
                            if (colOffset ==  1) direction = Tile.UR;
                        }
                        if (rowOffset == 0){
                            if (colOffset == -1) direction = Tile.L;
                            if (colOffset ==  1) direction = Tile.R;
                        }
                        if (rowOffset == 1){
                            if (colOffset == -1) direction = Tile.DL;
                            if (colOffset ==  0) direction = Tile.D;
                            if (colOffset ==  1) direction = Tile.DR;
                        }
                        current.biconnect(direction, neighbor);
                    }
                }
            }
        }

        return tiles;
    }


    @Override
    public StackPane asJavaFXNode() {
        StackPane stack = new StackPane();

        HashMap<String, Image> images = new HashMap<>();

        final int s = 100;
        // Setup the board
        GridPane board = new GridPane();
        for (int row=0; row<n; row++) for (int col=0;col<n;col++){
           Tile t = tiles[row][col];
           ImageView emoji;

           String emojiID = t.getMark();
           if (emojiID.equals("")) emojiID = "blank";

           // Get image and update table
           Image image = images.getOrDefault(emojiID, new Image(String.format("%s.png", emojiID)));
           images.put(emojiID, image);

           emoji = new ImageView(image);
           emoji.setFitWidth(s-5);
           emoji.setFitHeight(s-5);

           // onClick
           String command = String.format("select %d %d", row, col);
           String clicked = String.format("Clicked %d %d", row, col);
           emoji.setOnMouseClicked(e -> {
               System.out.println(clicked);
               Controller.execute(command);
           });

           board.add(emoji, col, row);
        }
        // Setup board constraints
        ColumnConstraints cMax = new ColumnConstraints(s,s,s);
        RowConstraints rMax = new RowConstraints(s,s,s);
        board.getColumnConstraints().addAll(cMax, cMax, cMax);
        board.getRowConstraints().addAll(rMax, rMax, rMax);

        // Setup tic tac toe board image
        ImageView gridImage = new ImageView(new Image("ttt.jpg"));
        gridImage.setFitWidth(s*3);
        gridImage.setFitHeight(s*3);
        board.setAlignment(Pos.CENTER);

        stack.getChildren().addAll(gridImage, board);
        //stack.getChildren().addAll(gridImage);
        stack.getChildren().forEach(c -> StackPane.setAlignment(c, Pos.CENTER));

        double centerX = stack.getWidth()/2.0;
        double centerY = stack.getHeight()/2.0;
        System.out.println("stack x " + stack.getTranslateX());
        System.out.println("stack y " + stack.getTranslateY());


        //stack.getTransforms().add(new Rotate(-90*rotation, 312, 312));
        //stack.getTransforms().add(new Rotate(-90*rotation, 354.5, 300));
        RotateTransition rt = new RotateTransition();
        int signe = rotation == 0 ? 1 : rotation / Math.abs(rotation);
        if (rotation != 0){
            rt.setByAngle(signe*Math.abs(rotation)*-90);
        } else rt.setByAngle(0);
        rt.setDuration(Duration.millis(1));
        rt.setCycleCount(Math.abs(rotation) % 4);
        rt.setNode(stack);
        rt.play();
        System.out.println("angle " + rt.getByAngle());

        //stack.getTransforms().add(new Rotate(90, stack.getTranslateX()/2, stack.getTranslateY()/2.0));
        return stack;
    }

    @Override
    public boolean selectTile(int row, int col, Marker m) {
        if (0 <= row && 0 <= col && row < n && col < n)
            return tiles[row][col].placeMarker(m);
        System.out.println(Utilities.ANSI_RED + "Bad tile: " + row + " " + col + Utilities.ANSI_RESET);
        return false;
    }

    @Override
    public String selectRandomTile() {
        List<Integer[]> available = new ArrayList<>();
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++){
            if (tiles[i][j].isEmpty()) {
                available.add(new Integer[]{i,j});
            }
        }

        int randomIndex = (int) Math.round(Math.random() * (available.size()-1));

        int randomRow = available.get(randomIndex)[0];
        int randomCol = available.get(randomIndex)[1];
        return String.format("%d %d", randomRow, randomCol);
    }

    @Override
    public int determineWinner() {
        // Logic for finding winner
        // 1 -> Party 1 wins. 2 -> Party 2 wins. 3 -> Tie. 0 -> No winner.

        // Check diagonal for winning solutions
        for (int rowcol = 0; rowcol < n; rowcol++){
            Tile curr = tiles[rowcol][rowcol];
            if (curr.isEmpty()) continue;
            int targetId = curr.getOccupantId();

            // Check all directions for a winner
            for (int direction = Tile.U; direction < Tile.numDirections; direction++){
                int inARow = 1;
                Tile neighbor = curr.getNeighbor(direction);
                while (neighbor != null && neighbor.getOccupantId() == targetId){
                    System.out.println("Woo! " + neighbor);
                    inARow++;
                    neighbor = neighbor.getNeighbor(direction);
                }
                int reverseDirection = Tile.reverse(direction);
                neighbor = curr.getNeighbor(reverseDirection);
                while (neighbor != null && neighbor.getOccupantId() == targetId){
                    System.out.println("Woo! " + neighbor);
                    inARow++;
                    neighbor = neighbor.getNeighbor(reverseDirection);
                }
                if (inARow == n) return targetId;
            }
            // N in a row!
        }

        // Check to see if Tie
        if (!hasAvailableTiles()) return 3;

        // No winner and no tie.
        return 0;
    }

    @Override
    public void rotate(String direction) {
        switch (direction){
            case "cw":{
                this.rotation--;
                break;
            }
            case "ccw":{
                this.rotation++;
                break;
            }
        }
    }

    private boolean hasAvailableTiles() {
        for (int i = 0; i < n; i++) for (int j = 0; j < n; j++)
                if (tiles[i][j].isEmpty()) return true;
        return false;
    }
}