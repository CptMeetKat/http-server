
package MK.HTTPServer;

import java.io.IOException; 
import MK.HTTPServer.ServerSocketOperations;

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
//            ServerSocketOperations x = new ServerSocketOperations(buffer_length, postOperations);

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
        multiplexer = null;
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

