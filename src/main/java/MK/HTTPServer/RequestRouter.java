package MK.HTTPServer;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;


public class RequestRouter
{
    public static String static_root = "";

//ideally this class is for static application, need a new application for offloading1,
//Then need another for sanitisations
    public static void processRequest(HTTPHandlerContext context)
        throws IOException
    {

        HTTPRequest request = context.getHTTPRequest();

        Path basePath = Paths.get(static_root);
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
            RequestProcessor.processRequest(context);
        }

        String currentDirectory = System.getProperty("user.dir");
        
        // Print the current directory
        System.out.println("Current directory: " + currentDirectory);
    }
}
