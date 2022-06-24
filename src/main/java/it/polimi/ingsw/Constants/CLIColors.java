package it.polimi.ingsw.Constants;

/**
 * Class CLIColors contains all the Unicode color codes for everything regarding CLI printing.
 */
public enum CLIColors {

    ANSI_RED("\u001B[31m"),
    ANSI_GREEN("\u001B[32m"),
    ANSI_YELLOW("\u001B[33m"),
    ANSI_BLUE("\u001B[34m"),
    ANSI_PURPLE("\u001B[35m"),
    ANSI_CYAN("\033[36m"),
    ANSI_WHITE("\033[37m"),
    ANSI_PINK("\u001B[35m"),
    ANSI_BACKGROUND_BLACK("\033[40m");
    public static final String RESET = "\u001B[0m";

    private String escape;

    /**
     * Constructor CLIColors creates a new CLIColors instance.
     */
    CLIColors(String escape) {
        this.escape = escape;
    }

    /**
     * Method getEscape returns the CLIColors escape code.
     */
    public String getEscape() {
        return escape;
    }

    /**
     * Method toString returns the CLIColors escape code.
     */
    @Override
    public String toString() {
        return escape;
    }
}