package MK.HTTPServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;
import java.nio.charset.StandardCharsets;

public class RequestProcessor
{
    public static void processRequest(SocketChannel client, HTTPRequest request)
        throws IOException
    {
        //writeOK(client);
        writeHTML(client);

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
    


    private static void writeHTML(SocketChannel client)
        throws IOException
    {
        //String httpResponse = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\nContent-Length: 37\r\n\r\n<html><button>BUTTON1</button></html>";
        //byte[] byteArray = httpResponse.getBytes(StandardCharsets.UTF_8);
        //ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
        //client.write(byteBuffer);

        File htmlFile = new File("./mysample.html"); // Replace "path/to/your/file.html" with the actual path to your HTML file
        byte[] htmlBytes = new byte[(int) htmlFile.length()];
        FileInputStream fis = new FileInputStream(htmlFile);
        fis.read(htmlBytes);
        fis.close();
        String body = new String(htmlBytes);




        String httpResponse = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\nContent-Length: " + body.length() + "\r\n\r\n" + body;
        byte[] byteArray = httpResponse.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
        client.write(byteBuffer);



        
//        ByteBuffer byteBuffer = ByteBuffer.wrap(htmlBytes);
//        client.write(byteBuffer);
    }
}
