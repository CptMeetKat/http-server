package MK.HTTPServer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

import MK.HTTPServer.Logger.PrintLevel;

public class RequestRouter extends BaseHTTPHandler
{
    public static Logger logger = Logger.getLogger();
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
            logger.printf(PrintLevel.ERROR, "Unable to obtain socket manager\n");
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
            logger.printf(PrintLevel.WARNING, "Unable to return HTTP 503 to sender\n");
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
            logger.printf(PrintLevel.INFO, "Matched route '%s' forwarding request to %s:%s\n", route.getPath(), route.getIP(), route.getPort());
            try
            {
                connection_manager.registerClientSocket(route.getIP(), route.getPort(), 
                        new ClientSocketOperations(context.getHTTPRequest(), new ApplicationServerPostOperations(context.getResponder())));
            }

            catch(IOException e)
            {
                logger.printf(PrintLevel.WARNING, "Unable to reach routed service at %s:%s\n", route.getIP(), route.getPort());
                returnServiceUnavailable(context);
            }
        }
        else
        {
            this.next.processRequest(context);
        }
    }
}
