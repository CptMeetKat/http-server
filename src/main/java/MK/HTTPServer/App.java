package MK.HTTPServer;

public class App 
{
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

        int port = Integer.parseInt(manager.getField("port"));
        RequestProcessor.static_root = manager.getField("static_root");
        RequestRouter.static_root = manager.getField("static_root");
        int buffer_size = Integer.parseInt(manager.getField("buffer_size"));



        HTTPServer server = new HTTPServer(port, buffer_size);
        server.start();
    }


}
