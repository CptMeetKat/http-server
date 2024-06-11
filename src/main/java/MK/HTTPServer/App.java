package MK.HTTPServer;

import MK.HTTPServer.Logger.PrintLevel;

public class App 
{
    Logger logger;
    public static void main(String[] args) 
    {
        new App();
    }

    public App()
    {
        Logger.setLogger(PrintLevel.TRACE);
        Logger logger = Logger.getLogger();

        ConfigManager manager = new ConfigManager("./server.config");
        System.out.println("***Loaded Config***");
        System.out.print(manager);
        System.out.println("*******************");

        int port = Integer.parseInt(manager.getField("port"));
        int buffer_size = Integer.parseInt(manager.getField("buffer_size"));
        String static_root = manager.getField("static_root");

        HTTPServer server = new HTTPServer(port, buffer_size, static_root);
        server.start();
    }
}
