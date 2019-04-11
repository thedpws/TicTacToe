package course.oop.controller.state;

import course.oop.model.Computer;
import course.oop.model.Game;
import course.oop.model.Player;
import course.oop.view.Command;
import course.oop.view.TTTView;
import course.oop.view.TurnView;
import course.oop.view.View;
import javafx.application.Platform;
import javafx.scene.Scene;

import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import static course.oop.model.Game.NO_WINNER;

public class TurnState implements GameState {

    private Map<String, Executable> commands;

    private int player;
    private Game game;
    private LocalTime timeStarted;
    private boolean turnOver;
    private TurnView view;

    public TurnState(Game g){
        this(g, (int) Math.round( Math.random() ) + 1);
    }

    private TurnState(Game g, int player){
        this.player = player;
        this.game = g;
        this.view = new TurnView(game, player);
        timeStarted = LocalTime.now();

        printInitialText();

        // Enforces the timeout
        /*
        turnOver = false;
        if (game.getConfig().getTimeout() != 0) {
            Thread t = new Thread(() -> {
                LocalTime timeStarted = LocalTime.now();
                while (game.getConfig().getTimeout() != 0 && !turnOver) {
                    if (LocalTime.now().isAfter(timeStarted.plusSeconds(game.getConfig().getTimeout()))) {
                        System.out.printf("%n%s timed out.%n", game.getConfig().getPlayer(player - 1));
                        View.interruptTurn();
                        break;
                    }
                }
            });
            Platform.runLater(t);
            //t.start();
        }
        */

        this.commands = new HashMap<>();
        this.commands.put("select", new Executable() {
                    @Override
                    GameState execute(Command c) {
                        final int N_PARAMS = 2;
                        if (c.getNumParams() != N_PARAMS){
                            printCorrectUsage();
                            return TurnState.this;
                        }
                        String row = c.getArgv()[1];
                        String column = c.getArgv()[2];
                        if (!game.selectTile(row, column, player)) return TurnState.this;

                        int winner = game.determineWinner();

                        // Game is over!
                        if (winner != NO_WINNER) {
                            turnOver = true;
                            return new EndState(game, winner);
                        }

                        // Next turn!
                        turnOver = true;
                        return getNextTurnState();
                    }

                    @Override
                    public String getHelp() {
                        return "COMMAND\n\tselect [row_number] [column_number]\nSYNOPSIS\n\tselect tile by its row and column";
                    }

                    @Override
                    String getCorrectUsage() {
                        return "select [row_number] [column_number]";
                    }
                });
        this.commands.put("print", new Executable() {
              @Override
              GameState execute(Command c) {
                  final int N_PARAMS = 0;
                  if (c.getNumParams() != N_PARAMS){
                      printCorrectUsage();
                      return TurnState.this;
                  }
                  printGameBoard();
                  return TurnState.this;
              }

              @Override
              public String getHelp() {
                  return "COMMAND\n\tprint\nSYNOPSIS\n\tprints the game board.";
              }

              @Override
              String getCorrectUsage() {
                  return "print";
              }
          });

        /*
        // handles computer players
        if (game.getPlayer(player) instanceof Computer){
            Thread computer = new Thread(() -> {
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e){
                    e.printStackTrace();
                }

                View.execute("select " + game.selectRandomTile());
            });
            computer.start();
        }
        */
    }

    //allowed commands: select row column

    @Override
    public void printInitialText(){
        printGameBoard();
        System.out.printf("It's %s's turn!%n", game.getPlayer(player));
    }
    @Override
    public GameState consumeCommand(Command c) {
        if (turnOver) return getNextTurnState();
        if (game.getConfig().getTimeout() != 0 && !turnOver && LocalTime.now().isAfter(timeStarted.plusSeconds(game.getConfig().getTimeout()))){
            return getNextTurnState();
        }
        String cmd = c.getArgv()[0];
        if (!commands.containsKey(cmd)) return null;
        return commands.get(cmd.toLowerCase()).execute(c);
    }

    @Override
    public String getCommands() {
        StringBuilder sb = new StringBuilder();
        for (String s : this.commands.keySet()){
            sb.append(String.format(", %s", s));
        }
        return sb.toString();
    }

    private void printGameBoard(){
        game.printGameBoard();
    }

    private GameState getNextTurnState(){
        return new TurnState(game, (player)%2+1);
    }

    @Override
    public String getPrompt(){
        Player p = game.getConfig().getPlayer(player - 1);
        return String.format("%s %s", p, p.getMarker());
    }

    @Override
    public Map<String, Executable> getCommandMap(){
        return this.commands;
    }


    @Override
    public Scene asScene() {
        return view.getScene();
    }
}