package MK.HTTPServer;

import java.nio.file.Path;
import java.nio.file.Paths;

public class RequestRouter extends BaseHTTPHandler
{

//ideally this class is for static application, need a new application for offloading1,
//Then need another for sanitisations
    public void processRequest(HTTPHandlerContext context)
    {

        HTTPRequest request = context.getHTTPRequest();

        Path basePath = Paths.get(context.getStaticRoot());
        Path userPath = Paths.get("./" + request.getField("URI"));

        Path resolvedPath = PathUtils.resolvePath(basePath, userPath);
        
        Path application1Path = Paths.get(basePath.toString(), "dynamic"); 

        if(resolvedPath.startsWith(application1Path.toString()))
        {
            System.out.println("Handling dyhanmic request");
            DynamicApplication.sendRequest("hello");
        }
        else
        {
            this.processRequest(context);
        }

        String currentDirectory = System.getProperty("user.dir");
        
        // Print the current directory
        System.out.println("Current directory: " + currentDirectory);
    }
}
