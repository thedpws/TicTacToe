package course.oop.view;


import course.oop.controller.Controller;
import course.oop.util.Utilities;

class View {

    public static void interruptTurn() {
        System.out.println("\n");
        Controller.consume(new CommandCall("\n"));
        printPrompt();

    }

    // controller can send commands to itself
    public static void execute(String command) {
        System.out.println();
        Controller.consume(new CommandCall(command));
        printPrompt();
    }

    private static void printPrompt() {
        System.out.print(Controller.getPrompt() + " " + Utilities.EMOJI + ": ");
    }

}

