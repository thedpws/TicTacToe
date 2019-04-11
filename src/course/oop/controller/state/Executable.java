package course.oop.controller.state;

import course.oop.util.Utilities;
import course.oop.view.Command;

// has an execute() method. for executing commands internal to game state
public abstract class Executable {
    abstract GameState execute(Command c);
    abstract String getHelp();
    abstract String getCorrectUsage();

    public void printCorrectUsage(){
        System.out.println(Utilities.ANSI_RED + "Usage: " + this.getCorrectUsage() + Utilities.ANSI_RESET);
    }
    public void printHelp(){ System.out.println(Utilities.HELP_START + this.getHelp() + Utilities.ANSI_RESET);}
}
