        
package MK.HTTPServer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;

import MK.HTTPServer.Logger.PrintLevel;

public class ClientSocketOperations implements SelectionKeyOperations //Client Operations
{
    StringBuilder request_builder = new StringBuilder();
    public ByteBuffer buffer;
    SocketPostOperations postOperations;
    Logger logger = Logger.getLogger();

	public ClientSocketOperations(HTTPRequest httpRequest, SocketPostOperations postOperations) {
		this.buffer = ByteBuffer.wrap(httpRequest.serialize());
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
            int bytes_written = channel.write(buffer);
            logger.printf(PrintLevel.INFO, "_Wrote %d bytes to %s\n", bytes_written, channel.getRemoteAddress());
            if(buffer.remaining() > 0)
            {
                //logger.printf(PrintLevel.TRACE, "Message sent:(not accurate)\n%s\n", httpRequest.toString()); //TODO: IMPORTANT! what is sent and what is display may vary
                logger.printf(PrintLevel.INFO, "still sending buffer\n"); //TODO: IMPORTANT! what is sent and what is display may vary
            }
            else
            {
                //channel.close();
                key.interestOps(SelectionKey.OP_READ);
            }
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
	}
}
