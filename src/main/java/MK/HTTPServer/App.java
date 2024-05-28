package MK.HTTPServer;

public class App 
{
    static int PORT = 2024;
    public static void main(String[] args) 
    {
        HTTPServer server = new HTTPServer(PORT, 3);
        server.start();
    }
}
