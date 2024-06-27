
package MK.HTTPServer;

import java.io.IOException; 

import MK.HTTPServer.Logger.PrintLevel;

public class HTTPServer 
{
    SocketManager multiplexer;
    static Logger logger = Logger.getLogger();
    public HTTPServer(int port, int buffer_length, String static_root, Iterable<Route> routes)
    {
        try
        {
            multiplexer = SocketManager.getSocketManager();

            BaseHTTPHandler pipeline = new RequestRouter(routes);
            pipeline.setTail(new StaticRequests());

            HTTPServerPostOperations callback = new HTTPServerPostOperations(static_root, pipeline);
            ServerSocketOperations operations = new ServerSocketOperations(buffer_length, callback, false);

            multiplexer.registerServerSocket("localhost", port, operations);
        }
        catch(IOException e)
        {
            logger.printf(PrintLevel.ERROR, "Unable to initialise HTTPServer\n");
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
            logger.printf(PrintLevel.ERROR, "Unexpected issue in the asynchronous socket loop\n");
        } 
        finally
        {
            multiplexer.close();
        }
    }
}

