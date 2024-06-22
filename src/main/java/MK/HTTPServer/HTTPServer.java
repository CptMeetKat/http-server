
package MK.HTTPServer;

import java.io.IOException; 

public class HTTPServer 
{
    SocketManager multiplexer;

    public HTTPServer(int port, int buffer_length, String static_root, Iterable<Route> routes)
    {
        try
        {
            multiplexer = SocketManager.getSocketManager();

            BaseHTTPHandler pipeline = new RequestRouter(routes);
            pipeline.setTail(new StaticRequests());
            HTTPServerOperations operations = new HTTPServerOperations(static_root, pipeline,
                                                                       buffer_length);
            multiplexer.registerServerSocket("localhost", port, operations);
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }

    public void stop()
    {
        multiplexer.stop();
    }

    public void start()
    {
        try { 
            multiplexer.run();
        } 
        catch (IOException e) { 
            System.out.println("An error occurred: " + e.getMessage());
        } 
        finally
        {
            multiplexer.close();
        }
    }
}

