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
    private static TTTControllerImpl controller;

    //sends user-inputted commands to the controller
    public static void act(TTTControllerImpl controller){
        View.controller = controller;
        /*

        while (true){
            printPrompt();
            String input = null;

            try {
                input = reader.readLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
            Command c = new Command(input);
            controller.consume(c);
        }
        */
    }

    public static void interruptTurn(){
        System.out.println("\n");
        controller.consume(new Command("\n"));
        printPrompt();

    }

    // controller can send commands to itself
    public static void execute(String command){
        System.out.println();
        controller.consume(new Command(command));
        printPrompt();
    }

    private static void printPrompt(){
        String emoji = "😂";
        System.out.print(controller.getPrompt() + " " + Utilities.EMOJI + ": ");
    }

}

