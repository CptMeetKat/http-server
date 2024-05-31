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
        Path userPath = new File("./" + request.getField("URI")).toPath();
        
        System.out.printf("______basepath %s\n", basePath);
        System.out.printf("______userpath %s\n", userPath);
        System.out.printf("______URIPath %s\n", request.getField("URI"));



        Path resolvedPath = PathUtils.resolvePath(basePath, userPath);
        if(Files.exists(resolvedPath) ) 
            writeHTMLFile(client, resolvedPath);
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

    private static void writeHTMLFile(SocketChannel client, Path path) //Remove request usage here
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
