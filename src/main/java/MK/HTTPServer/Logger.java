package MK.HTTPServer;

public class Logger
{
    public static Logger logger;
    private PrintLevel minimum_level = PrintLevel.TRACE;
    // ANSI escape codes for colors
    public static final String RESET = "\033[0m";  // Text Reset
    public static final String CYAN = "\033[0;36m";   // CYAN
    public static final String BLUE = "\033[0;34m";   // BLUE
    public static final String YELLOW = "\033[0;33m"; // YELLOW
    public static final String RED = "\033[0;31m";    // RED
    public static final String MAGENTA = "\033[0;35m"; // MAGENTA
                                                       
                                                       
                                                       
    public static Logger getLogger()
    {
        if(logger == null)
            return new Logger();
        return logger;
    }

    public static void setLogger(PrintLevel level)
    {
        logger = new Logger(level);
    }


    public enum PrintLevel
    {
        TRACE(0),
        DEBUG(1),
        INFO(2),
        WARNING(3),
        ERROR(4),
        CRITICAL(5);

        private final int value;

        PrintLevel(int value) {
            this.value = value;
        }

        public int getValue()
        {
            return value;
        }

        public static PrintLevel fromInt(int i) {
            for (PrintLevel level : PrintLevel.values()) {
                if (level.getValue() == i) {
                    return level;
                }
            }
            throw new IllegalArgumentException("Invalid level value: " + i);
        }
    }

    public Logger(){}

    public Logger(PrintLevel minimum_level)
    {
        this.minimum_level = minimum_level;
    }

    public void printf(PrintLevel level, String format, Object... args) {
        
        if(level.ordinal() < minimum_level.ordinal())
            return;

        switch (level) {
            case TRACE:
                System.out.println(CYAN + "[TRACE]: " + String.format(format, args) + RESET);
                break;
            case DEBUG:
                System.out.println(BLUE + "[DEBUG]: " + String.format(format, args) + RESET);
                break;
            case INFO:
                System.out.println("[INFO]: " + String.format(format, args));
                break;
            case WARNING:
                System.out.println(YELLOW + "[WARNING]: " + String.format(format, args) + RESET);
                break;
            case ERROR:
                System.err.println(RED + "[ERROR]: " + String.format(format,args) +  RESET);  
                break;
            case CRITICAL:
                System.err.println(MAGENTA + "[CRITICAL]: " + String.format(format, args) + RESET);
                break;
            default:
                System.out.println("[UNKNOWN]: " + String.format(format,args));
        }
    }

    public void print(String message)
    {
        if(PrintLevel.INFO.ordinal() < minimum_level.ordinal())
            return;
        System.out.println("[INFO]: " + message);
    }

    public void print(PrintLevel level, String message)
    {
        if(level.ordinal() < minimum_level.ordinal())
            return;

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
                System.err.println(RED + "[ERROR]: " + message + RESET);  
                break;
            case CRITICAL:
                System.err.println(MAGENTA + "[CRITICAL]: " + message + RESET);
                break;
            default:
                System.out.println("[UNKNOWN]: " + message);
        }
    }
}
