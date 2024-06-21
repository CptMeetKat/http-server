
package MK.HTTPServer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

public class ClientSocketOperations implements SelectionKeyOperations //Client Operations
{
    private HashMap<String, StringBuilder> request_builder = new HashMap<>();
    public HTTPRequest httpRequest;
    //public Sendable sender;
    private ClientPostOperations postOperations;

	public ClientSocketOperations(HTTPRequest httpRequest/*, Sendable sender*/, ClientPostOperations postOperations) {
		this.httpRequest = httpRequest;
        //this.sender = sender; This interface is essentially to be replaced by onReadComplete
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
            String socketAddress = client.getRemoteAddress().toString();

            ByteBuffer buffer = ByteBuffer.allocate(buffer_length); 
            int received_length = client.read(buffer); 

            System.out.println("LEN: " + received_length);
            if(received_length == -1) //TODO: What if 0 (i.e. connection left open, and no bits to recv)
            {
                String fulldata = request_builder.get(socketAddress).toString();
                System.out.println("FULL DATA: " + fulldata);
                client.close();
                postOperations.onReadComplete(this, fulldata);
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

    //abstract void onReadComplete(String data);

    //protected void onReadComplete(String data)
    //{
    //    sender.send(   ByteBuffer.wrap(data.getBytes())    );
    //}

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
