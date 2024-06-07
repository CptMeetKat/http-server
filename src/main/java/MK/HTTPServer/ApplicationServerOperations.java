
package MK.HTTPServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

public class ApplicationServerOperations implements SelectionKeyOperations
{

    private HashMap<String, StringBuilder> request_builder = new HashMap<>();
    HTTPRequest httpRequest;
    SocketChannel sender;
    String data = "";
	public ApplicationServerOperations(HTTPRequest httpRequest, SocketChannel sender) {
		this.httpRequest = httpRequest;
        this.sender = sender;
	}

	@Override
	public void accept(SelectionKey key) {
		// Client cannot accept
		throw new UnsupportedOperationException("Unimplemented method 'accept'");
	}

	@Override
	public void read(SelectionKey key) {
        //Dont think we care about read
        try
        {
            System.out.println("------test");
            int buffer_length = 10;
            SocketChannel client = (SocketChannel)key.channel(); 
            String socketAddress = client.getRemoteAddress().toString();

            ByteBuffer buffer = ByteBuffer.allocate(buffer_length); 
            int received_length = client.read(buffer); 

            System.out.println("LEN: " + received_length);
            if(received_length == -1)
            {
                System.out.println("FULL DATA: " + data);
                
                sender.write(ByteBuffer.wrap(data.getBytes()));
                client.close();
                sender.close(); //this work here, not sure if correct here
            }
            else
            {
                data += new String(buffer.array()).substring(0, received_length);
            }

            System.out.println("End read");

        }
        catch(IOException e)
        {
            System.out.println(e);
        }
	}

	@Override
	public void connect(SelectionKey key) {
		throw new UnsupportedOperationException("Unimplemented method 'read'");
	}

	@Override
	public void write(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();

        System.out.println("Fake writing...");
        try
        {
            channel.write(ByteBuffer.wrap(httpRequest.serialize()));
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
	}
}
