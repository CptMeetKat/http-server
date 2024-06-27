package MK.HTTPServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import MK.HTTPServer.Logger.PrintLevel;


public class StaticRequests extends BaseHTTPHandler
{
    private static Logger logger = Logger.getLogger();
    public void processRequest(HTTPHandlerContext context)
    {
        HTTPRequest request = context.getHTTPRequest();

        Path basePath = Paths.get(context.getStaticRoot());
        Path userPath = Paths.get("./" + request.getField("URI"));

        Path resolvedPath = PathUtils.resolvePath(basePath, userPath);

        try
        {
            Sendable sender = context.getResponder();
            HTTPResponse response;
            if(Files.exists(resolvedPath) && ! Files.isDirectory(resolvedPath)) 
                response = getHTMLFile(resolvedPath);
            else
                response = HTTPResponse.createNotFoundResponse();
            sender.send(ByteBuffer.wrap(response.serialize()));
        }
        catch(IOException e)
        {
            logger.printf(PrintLevel.ERROR, "%s unable to process request\n", this.getClass().getSimpleName());
        }
    }

    private static HTTPResponse getHTMLFile( Path path) 
        throws IOException
    {
        logger.printf(PrintLevel.INFO,"Retrieving file: %s\n", path);
        String body = new String(getFileAsBytes(path.toString()));
        
        HTTPResponse response = HTTPResponse.createOKResponse();
        response.setContentType("text/html");
        response.setBody(body);
        return response;
    }

    private static byte[] getFileAsBytes(String path)
    throws IOException
    {
        File file = new File(path); // Replace "path/to/your/file.html" with the actual path to your HTML file
        byte[] fileBytes = new byte[(int) file.length()];
        FileInputStream fis = new FileInputStream(file);
        fis.read(fileBytes);
        fis.close();
        return fileBytes;
    }
}
