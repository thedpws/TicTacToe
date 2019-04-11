package course.oop.view;


import course.oop.controller.TTTControllerImpl;
import course.oop.util.Utilities;

public class View {

    public static void interruptTurn() {
        System.out.println("\n");
        TTTControllerImpl.consume(new CommandCall("\n"));
        printPrompt();

    }

    // controller can send commands to itself
    public static void execute(String command) {
        System.out.println();
        TTTControllerImpl.consume(new CommandCall(command));
        printPrompt();
    }

    private static void printPrompt() {
        System.out.print(TTTControllerImpl.getPrompt() + " " + Utilities.EMOJI + ": ");
    }

}

