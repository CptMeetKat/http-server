package MK.HTTPServer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RequestRouter extends BaseHTTPHandler
{
    SocketManager connection_manager;
    public RequestRouter()
    {
        try
        {
            this.connection_manager = SocketManager.getSocketManager();
        }
        catch(IOException e)
        {
            System.out.println();
        }
    }

    public void processRequest(HTTPHandlerContext context)
    {

        HTTPRequest request = context.getHTTPRequest();

        Path basePath = Paths.get(context.getStaticRoot());
        Path userPath = Paths.get("./" + request.getField("URI"));

        Path resolvedPath = PathUtils.resolvePath(basePath, userPath);
        
        Path application1Path = Paths.get(basePath.toString(), "dynamic"); 

        //This needs to go.
        if(resolvedPath.startsWith(application1Path.toString()))
        {
            System.out.println("Handling dynamic request");
            connection_manager.registerClientSocket("localhost", 8000, new ApplicationServerOperations(context.getHTTPRequest(), context.getSender()));
        }
        else
        {
            this.next.processRequest(context);
        }

        String currentDirectory = System.getProperty("user.dir");
        
        // Print the current directory
        System.out.println("Current directory: " + currentDirectory);
    }
}
