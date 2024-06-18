package MK.HTTPServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;


public class StaticRequests extends BaseHTTPHandler
{
    public void processRequest(HTTPHandlerContext context)
    {
        HTTPRequest request = context.getHTTPRequest();

        Path basePath = Paths.get(context.getStaticRoot());
        Path userPath = Paths.get("./" + request.getField("URI"));

        System.out.printf("______basepath %s\n", basePath);
        System.out.printf("______userpath %s\n", userPath);
        System.out.printf("______URIPath %s\n", request.getField("URI"));

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
            System.out.println("ERROR");
        }
    }

    private static HTTPResponse getHTMLFile( Path path) 
        throws IOException
    {
        System.out.printf("GET file: %s\n", path);
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
