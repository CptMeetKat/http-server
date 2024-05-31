package MK.HTTPServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class RequestProcessor
{
    public static String static_root = "";
    public static void processRequest(SocketChannel client, HTTPRequest request)
        throws IOException
    {
        Path basePath = new File(static_root).toPath();
        Path userPath = new File(request.getField("URI")).toPath();
        Path resolvedPath = PathUtils.resolvePath(basePath, userPath);
        
        System.out.printf("basepath %s\n", basePath);
        System.out.printf("userpath %s\n", userPath);

        if(Files.exists(resolvedPath) ) 
            writeHTMLFile(client, request);
        else
            writeNotFound(client);

        String currentDirectory = System.getProperty("user.dir");
        
        // Print the current directory
        System.out.println("Current directory: " + currentDirectory);
    }

    private static void writeNotFound(SocketChannel client)
        throws IOException
    {
        HTTPResponse response = HTTPResponse.createNotFoundResponse();
        client.write(ByteBuffer.wrap(response.serialize()));
    }       

    private static void writeHTMLFile(SocketChannel client, HTTPRequest request) //Remove request usage here
        throws IOException
    {
        String path = static_root + request.getField("URI");
        System.out.printf("GET file: %s\n", path);
        String body = new String(getFileAsBytes(path));
        
        HTTPResponse response = HTTPResponse.createOKResponse(); //ADD Class or Factory class or somthing tha tis like Response.CreateOKResponse()
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
