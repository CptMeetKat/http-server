package MK.HTTPServer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;

public class RequestRouter extends BaseHTTPHandler
{
    SocketManager connection_manager;
    ArrayList<Route> routes = new ArrayList<Route>();
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


        routes.add(new Route("localhost", 8000, "dynamic"));
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
            connection_manager.registerClientSocket(route.getIP(), route.getPort(), new ApplicationServerOperations(context.getHTTPRequest(), context.getSender()));
        }
        else
        {
            this.next.processRequest(context);
        }
    }
}
