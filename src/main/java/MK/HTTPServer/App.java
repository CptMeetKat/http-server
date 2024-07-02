package MK.HTTPServer;

import java.util.ArrayList;

import MK.HTTPServer.Logger.PrintLevel;

public class App 
{
    private static Logger logger = Logger.getLogger();
    HTTPServer server;
   
    public int log_level;
    public int inbound_port; 
    public int buffer_size;
    public ArrayList<Route> routes;
    String static_root;


    public static void main(String[] args) 
    {
        App app = new App();
        app.loadConfig();
        app.start();
    }

    public App() {
        inbound_port = 8080;
        buffer_size = 256;
        setStaticRoot("/static/");
        routes = new ArrayList<Route>();
    }

    public void setStaticRoot(String path)
    {
        this.static_root = System.getProperty("user.dir") + path;
    }

    public void setPort(int port)
    {
        this.inbound_port = port;
    }

    public void addRoute(Route r)
    {
        routes.add(r);
    }

    public void loadConfig()
    {
        ConfigManager manager = new ConfigManager("./server.config");
        log_level = manager.getInt("log_level");
        inbound_port = manager.getInt("port");
        buffer_size = manager.getInt("buffer_size");
        static_root = manager.getField("static_root");
       
        Logger.setLogger(PrintLevel.fromInt(log_level));
        routes = createRoutes(manager);
        printConfig(manager);
    }

    public void start()
    {
        server = new HTTPServer(inbound_port, buffer_size, static_root, routes);
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
