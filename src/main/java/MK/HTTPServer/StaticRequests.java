package MK.HTTPServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.channels.WritableByteChannel;


public class StaticRequests extends BaseHTTPHandler
{
    public void processRequest(HTTPHandlerContext context)
    {
        HTTPRequest request = context.getHTTPRequest();
        WritableByteChannel channel = context.getSender();

        Path basePath = Paths.get(context.getStaticRoot());
        Path userPath = Paths.get("./" + request.getField("URI"));

        System.out.printf("______basepath %s\n", basePath);
        System.out.printf("______userpath %s\n", userPath);
        System.out.printf("______URIPath %s\n", request.getField("URI"));

        //Pipeline Sanitize, Routeing
        Path resolvedPath = PathUtils.resolvePath(basePath, userPath);

        try
        {
            if(Files.exists(resolvedPath) && ! Files.isDirectory(resolvedPath)) 
                writeHTMLFile(channel, resolvedPath);
            else
                writeNotFound(channel);
            context.getSender().close(); //not sure i like this here
        }
        catch(IOException e)
        {
            System.out.println("ERROR");
        }
    }

    private static void writeNotFound(WritableByteChannel client)
        throws IOException
    {
        HTTPResponse response = HTTPResponse.createNotFoundResponse();
        client.write(ByteBuffer.wrap(response.serialize()));
    }       

    private static void writeHTMLFile(WritableByteChannel client, Path path) //Remove request usage here
        throws IOException
    {
        System.out.printf("GET file: %s\n", path);
        String body = new String(getFileAsBytes(path.toString()));
        
        HTTPResponse response = HTTPResponse.createOKResponse();
        response.setContentType("text/html");
        response.setBody(body);
        client.write(ByteBuffer.wrap(response.serialize()));
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
