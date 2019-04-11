package course.oop.view;

import java.util.Arrays;

public class CommandCall {

    private int argc;
    private String[] argv;

    public CommandCall(String cmd){
        this.argv = cmd.split(" +");
        this.argc = this.argv.length;
    }

    public String getArgAt(int index){
        if (index < 0 || index >= this.argc) return null;
        return this.argv[index];
    }

    public int getArgc(){ return argc;}

    public String[] getArgv(){ return argv.clone(); }

    public int getNumParams(){
        return this.argc - 1;
    }

    public boolean validateNumParams(int n){
        int numParams = this.argc - 1;
        if (numParams < n) System.out.println("Not enough arguments provided.");
        if (numParams > n) System.out.println("Too many arguments provided.");
        return numParams == n;
    }
}
