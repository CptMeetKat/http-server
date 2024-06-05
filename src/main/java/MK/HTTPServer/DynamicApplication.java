package MK.HTTPServer;

import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

public class DynamicApplication
{
    public static void sendRequest(String message)
    {
        String hostname = "localhost"; // Server address
        int port = 8000; // Server port number
        Socket socket = null;

        try {
            socket = new Socket(hostname, port);
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            writer.println("GET / HTTP/1.1\r\nTEST\r\n\r\n");

            System.out.println("Message sent to the server: " + message);

        } catch (UnknownHostException ex) {
            System.out.println("Server not found: " + ex.getMessage());
        } catch (IOException ex) {
            System.out.println("I/O error: " + ex.getMessage());
        } finally {
            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException ex) {
                    System.out.println("Error closing the socket: " + ex.getMessage());
                }
            }
        }
    }
}

