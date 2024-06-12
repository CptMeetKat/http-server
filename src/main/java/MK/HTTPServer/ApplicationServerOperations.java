
package MK.HTTPServer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

public class ApplicationServerOperations implements SelectionKeyOperations
{

    private HashMap<String, StringBuilder> request_builder = new HashMap<>();
    HTTPRequest httpRequest;
    SocketChannel sender;
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
        try
        {
            int buffer_length = 10;
            SocketChannel client = (SocketChannel)key.channel(); 
            String socketAddress = client.getRemoteAddress().toString();

            ByteBuffer buffer = ByteBuffer.allocate(buffer_length); 
            int received_length = client.read(buffer); 

            System.out.println("LEN: " + received_length);
            if(received_length == -1)
            {
                String fulldata = request_builder.get(socketAddress).toString();
                System.out.println("FULL DATA: " + fulldata);
                sender.write(   ByteBuffer.wrap(fulldata.getBytes())    );
                client.close();
                sender.close(); //this work here, not sure if correct here
            }
            else
            {
                String data = new String(buffer.array()).substring(0, received_length);
                if(request_builder.containsKey(socketAddress))
                    request_builder.get(socketAddress).append(data);
                else
                    request_builder.put(socketAddress, new StringBuilder(data));
            }
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
	}

	@Override
	public void connect(SelectionKey key) {
		throw new UnsupportedOperationException("Unimplemented method 'connect'");
	}

	@Override
	public void write(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();

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
