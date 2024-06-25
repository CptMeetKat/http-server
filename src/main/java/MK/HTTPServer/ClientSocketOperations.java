        
package MK.HTTPServer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

public class ClientSocketOperations implements SelectionKeyOperations //Client Operations
{
    StringBuilder request_builder = new StringBuilder();
    public HTTPRequest httpRequest;
    ClientPostOperations postOperations;

	public ClientSocketOperations(HTTPRequest httpRequest, ClientPostOperations postOperations) {
		this.httpRequest = httpRequest;
        this.postOperations = postOperations;
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
            int buffer_length = 256; //TODO: Make this bigger and dynamic
            SocketChannel client = (SocketChannel)key.channel(); 

            ByteBuffer buffer = ByteBuffer.allocate(buffer_length); 
            int received_length = client.read(buffer); 

            System.out.println("LEN: " + received_length);
            if(received_length == -1) //TODO: What if 0 (i.e. connection left open, and no bits to recv)
            {
                String fulldata = request_builder.toString();
                System.out.println("FULL DATA: " + fulldata);
                client.close();
                postOperations.onReadComplete(client, fulldata);
            }
            else
            {
                String data = new String(buffer.array()).substring(0, received_length);
                request_builder.append(data);
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
