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

        File htmlFile = new File(path); // Replace "path/to/your/file.html" with the actual path to your HTML file
        byte[] htmlBytes = new byte[(int) htmlFile.length()];
        FileInputStream fis = new FileInputStream(htmlFile);
        fis.read(htmlBytes);
        fis.close();
        String body = new String(htmlBytes);




        String httpResponse = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\nContent-Length: " + body.length() + "\r\n\r\n" + body;
        byte[] byteArray = httpResponse.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
        client.write(byteBuffer);
    }
}
