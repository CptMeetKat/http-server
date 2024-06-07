package MK.HTTPServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.io.InputStreamReader;

public class DynamicApplication
{
    public static String sendRequest(HTTPRequest request)
    {
        String hostname = "localhost"; // Server address
        int port = 8000; // Server port number
        Socket socket = null;
        String result = "";

        try {
            socket = new Socket(hostname, port);

            OutputStream output = socket.getOutputStream();
            output.write(request.serialize());
            output.flush();

            BufferedReader reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            int response_char;
            while ((response_char = reader.read()) != -1) {
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

