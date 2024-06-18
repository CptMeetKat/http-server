package MK.HTTPServer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class RequestRouter extends BaseHTTPHandler
{
    SocketManager connection_manager;
    ArrayList<Route> routes = new ArrayList<Route>();
    public RequestRouter(Iterable<Route> routes)
    {
        for(Route r : routes)
        {
            this.routes.add(r);
        }


        try
        {
            this.connection_manager = SocketManager.getSocketManager();
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }

    public void addRoute(Route r)
    {
        routes.add(r);
    }

    private Route findMatchingRoute(Path user_path, String static_root)
    {
        for(Route r : routes)
        {
            String applicationPath = Paths.get(static_root, r.getPath()).toString();

            if(user_path.startsWith(applicationPath))
                return r;
        }

        return null;
    }

    public void returnServiceUnavailable(HTTPHandlerContext context)
    {
        SocketChannel channel = context.getSender();
        HTTPResponse response = HTTPResponse.createServiceUnavailable();
        try
        {
            channel.write(ByteBuffer.wrap(response.serialize()));
        }
        catch(IOException e2)
        {
            System.out.println("Unable to contact sender");
        }
    }

    public void processRequest(HTTPHandlerContext context)
    {
        HTTPRequest request = context.getHTTPRequest();

        Path basePath = Paths.get(context.getStaticRoot());
        Path userPath = Paths.get("./" + request.getField("URI"));

        Path resolvedPath = PathUtils.resolvePath(basePath, userPath);
        
        Route route = findMatchingRoute(resolvedPath, context.getStaticRoot());
        if(route != null)
        {
            System.out.println("Handling dynamic request");
            try
            {
                connection_manager.registerClientSocket(route.getIP(), route.getPort(), new ApplicationServerOperations(context.getHTTPRequest(), context.getResponder()));//Maybe this should just recv context
            }
            catch(IOException e)
            {
                System.out.println("WARNING! : Unable to connect the 3rd party service");
                returnServiceUnavailable(context);
            }
        }
        else
        {
            this.next.processRequest(context);
        }
    }
}
