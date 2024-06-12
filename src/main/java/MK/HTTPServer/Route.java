package MK.HTTPServer;

public class Route
{
    private String ip;
    private int port;
    private String path;

    public Route(String ip, int port, String path)
    {
        this.ip = ip;
        this.port = port;
        this.path = path;
    }

    public String getPath() 
    {
        return path;
    }

    public int getPort()
    {
        return port;
    }

    public String getIP()
    {
        return ip;
    }
}
