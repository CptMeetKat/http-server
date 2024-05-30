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
        //writeOK(client);
        if( fileExists(request) ) //!directory navigation vuln -> Ennumerate all static files to serve and map
            writeHTMLFile(client, request);
        else
            writeNotFound(client);

        String currentDirectory = System.getProperty("user.dir");
        
        // Print the current directory
        System.out.println("Current directory: " + currentDirectory);
    }

    private static void writeOK(SocketChannel client)
        throws IOException
    {
        String httpResponse = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: 3\r\n\r\nabc";
        byte[] byteArray = httpResponse.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
        client.write(byteBuffer);
    }

    private static void writeNotFound(SocketChannel client)
        throws IOException
    {
        String httpResponse = "HTTP/1.1 404 Not Found\r\nContent-Type: text/plain\r\nContent-Length: 13\r\n\r\n404 Not Found";
        byte[] byteArray = httpResponse.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
        client.write(byteBuffer);
    }       

    private static boolean fileExists(HTTPRequest request)
    {

        Path filePath = Paths.get(  static_root + request.getField("URI")   );
        System.out.printf("Does Exist: %s\n", filePath);
        return Files.exists(filePath);
    }


    private static void writeHTMLFile(SocketChannel client, HTTPRequest request)
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
