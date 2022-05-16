package it.polimi.ingsw.Constants;

public enum CLIColors {
    ANSI_RED("\u001B[31m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_BLUE("\u001B[34m"),
    ANSI_PURPLE("\u001B[35m"),
    ANSI_CYAN("\033[36m"),
    ANSI_WHITE("\033[37m"),
    ANSI_BACKGROUND_BLACK("\033[40m");

    public static final String RESET = "\u001B[0m";

    private String escape;

    CLIColors(String escape) {
        this.escape = escape;
    }

    public String getEscape() {
        return escape;
    }

    @Override
    public String toString() {
        return escape;
    }
}
