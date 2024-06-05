package MK.HTTPServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.InputStreamReader;

public class DynamicApplication
{
    public static String sendRequest(String message)
    {
        String hostname = "localhost"; // Server address
        int port = 8000; // Server port number
        Socket socket = null;
        String result = "";

        try {
            socket = new Socket(hostname, port);
            OutputStream output = socket.getOutputStream();
            PrintWriter writer = new PrintWriter(output, true);

            writer.println("GET / HTTP/1.1\r\nTEST\r\n\r\n");

            System.out.println("Message sent to the server: " + message);
            // Read response from the server
            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            
            int response_char;
            while ((response_char = reader.read()) != -1) {
                System.out.println("Server response: " + (char)response_char);
                result += (char)response_char;
            }


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
        return result;
    }
}

