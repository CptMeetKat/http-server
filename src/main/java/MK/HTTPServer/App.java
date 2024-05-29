package MK.HTTPServer;

public class App 
{
    private int port = 2024;

    public static void main(String[] args) 
    {
        new App();
   }

    public App()
    {
        ConfigManager manager = new ConfigManager("./server.config");

        this.port = Integer.parseInt(manager.getField("port"));
        RequestProcessor.static_root = manager.getField("static_root");

        HTTPServer server = new HTTPServer(port, 3);
        server.start();
    }


}
