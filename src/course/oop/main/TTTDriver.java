package course.oop.main;

import course.oop.controller.TTTControllerImpl;
import course.oop.fileio.FileIO;
import course.oop.model.Player;
import course.oop.util.Utilities;
import course.oop.view.View;

import java.util.Scanner;

class TTTDriver {

    public static void main(String[] args) {
        Utilities.convert();

        String circle = Utilities.CIRCLE;
        //"\u2b55";
        String cross = Utilities.CROSS;
        //"\u274c";
        System.out.printf("%s%s%s%s%s Welcome to TIC TAC TOE! %s%s%s%s%s%n", circle, circle, circle, circle, circle, cross, cross, cross, cross, cross);
        System.out.println();System.out.println();
        System.out.println("\tThis is tic-tac-toe in an interactive shell.");
        System.out.printf("\tUse the %shelp%s command whenever you need help.\n", Utilities.HELP_START, Utilities.ANSI_RESET);
        System.out.printf("\tIf you need information on how to use a command, type %shelp [command]%s\n", Utilities.HELP_START, Utilities.ANSI_RESET);
        System.out.println();
        System.out.println();
        System.out.println();

        //FileIO.init();

        //FileIO.init();
        //TTTControllerImpl c = new TTTControllerImpl();
        //View.act(c);

    }
}
