package course.oop.view;


import course.oop.controller.TTTControllerImpl;
import course.oop.util.Utilities;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.util.Scanner;

public class View {

    String os = System.getProperty("os.name").toLowerCase();

    private static BufferedReader reader = new BufferedReader(new InputStreamReader(System.in));


    public static void interruptTurn(){
        System.out.println("\n");
        TTTControllerImpl.consume(new Command("\n"));
        printPrompt();

    }

    // controller can send commands to itself
    public static void execute(String command){
        System.out.println();
        TTTControllerImpl.consume(new Command(command));
        printPrompt();
    }

    private static void printPrompt(){
        System.out.print(TTTControllerImpl.getPrompt() + " " + Utilities.EMOJI + ": ");
    }

}

