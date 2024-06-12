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
        ConfigManager manager = new ConfigManager("./server.config");
        System.out.println("***Loaded Config***");
        System.out.print(manager);
        System.out.println("*******************");


        int log_level = Integer.parseInt(manager.getField("log_level"));
        Logger.setLogger(PrintLevel.fromInt(log_level));
        int port = Integer.parseInt(manager.getField("port"));
        int buffer_size = Integer.parseInt(manager.getField("buffer_size"));
        String static_root = manager.getField("static_root");


        HTTPServer server = new HTTPServer(port, buffer_size, static_root);
        server.start();
    }
}
