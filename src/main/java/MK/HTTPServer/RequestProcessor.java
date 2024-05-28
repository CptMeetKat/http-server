package MK.HTTPServer;

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
        String httpResponse = "HTTP/1.1 200 OK\r\nContent-Type: text/html\r\nContent-Length: 37\r\n\r\n<html><button>BUTTON1</button></html>";
        byte[] byteArray = httpResponse.getBytes(StandardCharsets.UTF_8);
        ByteBuffer byteBuffer = ByteBuffer.wrap(byteArray);
        client.write(byteBuffer);
    }
}
