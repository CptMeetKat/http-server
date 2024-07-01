        
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
    SocketReadCallback postOperations;
    Logger logger = Logger.getLogger();
    HTTPRequest httpRequest;

	public ClientSocketOperations(HTTPRequest httpRequest, SocketReadCallback postOperations) {
        this.httpRequest = httpRequest;
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
            String socketAddress = client.getRemoteAddress().toString();

            ByteBuffer buffer = ByteBuffer.allocate(buffer_length); 
            int received_length = client.read(buffer); 

            if(received_length == -1) //TODO: What if 0 (i.e. connection left open, and no bits to recv)
            {
                logger.printf(PrintLevel.INFO, "End of stream received from %s\n", socketAddress);
                String final_message = request_builder.toString();
                logger.printf(PrintLevel.INFO, "Server %s assembled %d bytes from %s\n", client.getLocalAddress().toString(), final_message.length(), client.getRemoteAddress().toString());
                logger.printf(PrintLevel.TRACE, "Message Received:\n%s\n", final_message);
                logger.printf(PrintLevel.INFO, "Closing connection to %s\n", client.getRemoteAddress().toString());
                client.close();

                logger.printf(PrintLevel.INFO, "%s operating on receieved data\n", postOperations.getClass().getSimpleName());
                postOperations.onReadComplete(client, final_message);
            }
            else
            {
                logger.printf(PrintLevel.INFO, "Received length %d bytes from %s\n", received_length , socketAddress);
                String data = new String(buffer.array()).substring(0, received_length);
                request_builder.append(data);
            }
        }
        catch(IOException e)
        {
            logger.printf(PrintLevel.ERROR, "Failed to read from remote server\n");
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
            logger.printf(PrintLevel.INFO, "Wrote %d bytes to %s\n", bytes_written, channel.getRemoteAddress());
            // TODO: This can be improved by showing what was sent
            // logger.printf(PrintLevel.TRACE, "Message sent:(not accurate)\n%s\n", httpRequest.toString()); //TODO: IMPORTANT! what is sent and what is display may vary
            if(buffer.remaining() > 0)
            {
                logger.printf(PrintLevel.INFO, "still sending buffer\n"); 
            }
            else
            {
                key.interestOps(SelectionKey.OP_READ);
            }
        }
        catch(IOException e)
        {
            logger.printf(PrintLevel.ERROR, "Failed to write to remote server\n");
        }
	}
}
