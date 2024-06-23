
package MK.HTTPServer;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

import MK.HTTPServer.Logger.PrintLevel;

public class ApplicationServerOperations implements SelectionKeyOperations
{
    Logger logger = Logger.getLogger();
    private HashMap<String, StringBuilder> request_builder = new HashMap<>();
    HTTPRequest httpRequest;
    ByteBuffer buffer;
    Sendable sender;
	public ApplicationServerOperations(HTTPRequest httpRequest, Sendable sender) {
		this.httpRequest = httpRequest;
        this.sender = sender;
        buffer = ByteBuffer.wrap(httpRequest.serialize());
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
                String final_message = request_builder.get(socketAddress).toString();
                logger.printf(PrintLevel.INFO, "Server %s assembled %d bytes from %s\n", client.getLocalAddress().toString(), final_message.length(), client.getRemoteAddress().toString());
                logger.printf(PrintLevel.TRACE, "Message Received:\n%s\n", final_message);
                logger.printf(PrintLevel.INFO, "Closing connection to %s\n", client.getRemoteAddress().toString());
                client.close(); 
                sender.send(   ByteBuffer.wrap(final_message.getBytes())    );
            }
            else
            {
                logger.printf(PrintLevel.INFO, "Received length %d bytes from %s\n", received_length , socketAddress);
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
             int bytes_written = channel.write(buffer);
             logger.printf(PrintLevel.INFO, "_Wrote %d bytes to %s\n", bytes_written, channel.getRemoteAddress());
             if(buffer.remaining() > 0)
             {
                 logger.printf(PrintLevel.TRACE, "Message sent:(not accurate)\n%s\n", httpRequest.toString()); //TODO: IMPORTANT! what is sent and what is display may vary
             }
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
	}
}
