package MK.HTTPServer;

import java.util.ArrayList;

import MK.HTTPServer.Logger.PrintLevel;

public class App 
{
    Logger logger;
    HTTPServer server;
    public static void main(String[] args) 
    {
        App app = new App();
        app.start();
    }

    public App()
    {
        ConfigManager manager = new ConfigManager("./server.config");
        int log_level = manager.getInt("log_level");
        int inbound_port = manager.getInt("port");
        int buffer_size = manager.getInt("buffer_size");
        String static_root = manager.getField("static_root");
        
        logger = Logger.getLogger();
        Logger.setLogger(PrintLevel.fromInt(log_level));
        ArrayList<Route> routes = createRoutes(manager);

        printConfig(manager);
        server = new HTTPServer(inbound_port, buffer_size, static_root, routes);
    }

    public void start()
    {
        server.start();
    }

    public void stop()
    {
        server.stop();
    }

    private static ArrayList<Route> createRoutes(ConfigManager manager)
    {
        ArrayList<Route> routes = new ArrayList<Route>();

        ArrayList<String> forwardEndpoints = manager.getIncrementedField("forward_endpoint");
        for(String endpoint: forwardEndpoints)
        {
            String[] tokens = endpoint.split(" ");
            String ip = tokens[0]; int port = Integer.parseInt(tokens[1]); String path = tokens[2];
            routes.add(new Route(ip, port, path));
        }
        return routes;
    }

    private void printConfig(ConfigManager manager)
    {
        logger.printf(PrintLevel.INFO,"%s\n%s\n%s\n",
                    "***Loaded Config***",
                    manager.toString(),
                    "*******************");
    }
}
