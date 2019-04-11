package course.oop.view;

public class CommandCall {

    private int argc;
    private String[] argv;

    public CommandCall(String cmd) {
        this.argv = cmd.split(" +");
        this.argc = this.argv.length;
    }

    public String getArgAt(int index) {
        if (index < 0 || index >= this.argc) return null;
        return this.argv[index];
    }

    public String[] getArgv() {
        return argv.clone();
    }
}
