package MK.HTTPServer;

public class Logger
{

    // ANSI escape codes for colors
    public static final String RESET = "\033[0m";  // Text Reset
    public static final String CYAN = "\033[0;36m";   // CYAN
    public static final String BLUE = "\033[0;34m";   // BLUE
    public static final String YELLOW = "\033[0;33m"; // YELLOW
    public static final String RED = "\033[0;31m";    // RED
    public static final String MAGENTA = "\033[0;35m"; // MAGENTA

    public enum PrintLevel
    {
        TRACE,
        DEBUG,
        INFO,
        WARNING,
        ERROR,
        CRITICAL
    }

    public Logger(){}

    public void print(String message)
    {
        System.out.println("[INFO]: " + message);
    }

    public void print(PrintLevel level, String message)
    {
        switch (level) {
            case TRACE:
                System.out.println(CYAN + "[TRACE]: " + message + RESET);
                break;
            case DEBUG:
                System.out.println(BLUE + "[DEBUG]: " + message + RESET);
                break;
            case INFO:
                System.out.println("[INFO]: " + message);
                break;
            case WARNING:
                System.out.println(YELLOW + "[WARNING]: " + message + RESET);
                break;
            case ERROR:
                System.err.println(RED + "[ERROR]: " + message + RESET);  // Using System.err for errors
                break;
            case CRITICAL:
                System.out.println(MAGENTA + "[CRITICAL]: " + message + RESET);
                break;
            default:
                System.out.println("[UNKNOWN]: " + message);
        }
    }
}
