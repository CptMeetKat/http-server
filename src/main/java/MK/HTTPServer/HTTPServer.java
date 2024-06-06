
package MK.HTTPServer;

import java.io.IOException; 

public class HTTPServer 
{
    SocketManager multiplexer;

    public HTTPServer(int port, int buffer_length, String static_root)
    {
        BaseHTTPHandler pipeline = new RequestRouter();
        pipeline.setTail(new RequestProcessor());

        try
        {
            multiplexer = new SocketManager();
            HTTPServerOperations operations = new HTTPServerOperations(static_root, pipeline,
                                                                       buffer_length);
            multiplexer.registerServerSocket("localhost", port, operations);
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }

    public void start()
    {
        try { 
            multiplexer.run();
        } 
        catch (IOException e) { 
            System.out.println("An error occurred: " + e.getMessage());
        } 
    }
}

