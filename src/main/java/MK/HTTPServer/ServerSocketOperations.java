
package MK.HTTPServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

import MK.HTTPServer.Logger.PrintLevel;

public class ServerSocketOperations implements SelectionKeyOperations
{
    private int buffer_length = 256; //This is also managed through config
    private HashMap<String, StringBuilder> request_builder = new HashMap<>();
    private static Logger logger = Logger.getLogger();
    private SocketPostOperations postOperations;
    boolean closeAfterRead;


    public ServerSocketOperations(int buffer_length, SocketPostOperations postOperations, boolean closeAfterRead)
    {
        this.buffer_length = buffer_length;
        this.postOperations = postOperations;
        this.closeAfterRead = closeAfterRead;
    }


    public ServerSocketOperations(int buffer_length, SocketPostOperations postOperations)
    {
        this.buffer_length = buffer_length;
        this.postOperations = postOperations;
        this.closeAfterRead = true;
    }

	@Override
	public void accept(SelectionKey key) {
        try
        {
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            SocketChannel client = server.accept(); 
            logger.printf(PrintLevel.INFO, "Server %s accepted connection from %s\n", server.getLocalAddress(), client.getRemoteAddress());
            client.configureBlocking(false); 
            client.register(key.selector(), SelectionKey.OP_READ, key.attachment()); 
        }
        catch(IOException e)
        {
            System.out.println("Error: HTTPServer could not accept connection");
            e.printStackTrace();
        }
	}

	@Override
	public void read(SelectionKey key) {
        try
        {
            SocketChannel client = (SocketChannel)key.channel(); 
            String socketAddress = ((InetSocketAddress)client.getRemoteAddress()).toString();

            ByteBuffer buffer = ByteBuffer.allocate(buffer_length); 
            int received_length = client.read(buffer); 

            String data = new String(buffer.array());
            
            if(received_length == -1) //Don't process anything if end of stream i.e. timeout or close
            {
                key.cancel();
                logger.print("End of stream: cancelling key");
                request_builder.remove(socketAddress);
                return;
            }

            data = data.substring(0, received_length);

            logger.printf(PrintLevel.INFO, "Received length %d bytes from %s\n", received_length , socketAddress);

            if(request_builder.containsKey(socketAddress))
                request_builder.get(socketAddress).append(data);
            else
                request_builder.put(socketAddress, new StringBuilder(data));

            if( request_builder.get(socketAddress).toString().endsWith("\r\n\r\n") ) //may change later when body is parsed
            {
                logger.printf(PrintLevel.TRACE, "Final Message: '%s'\n", request_builder.get(socketAddress));
                logger.printf(PrintLevel.INFO, "%s operating on completed data\n", postOperations.getClass().getSimpleName());

                String request = request_builder.get(socketAddress).toString();
                postOperations.onReadComplete(client, request);

                if(closeAfterRead)
                {
                    client.close();
                }
                request_builder.remove(socketAddress);
            }
        }
        catch(IOException e)
        {
            System.out.println("error: " + e);
            e.printStackTrace();
        }
	}

	@Override
	public void connect(SelectionKey key) {
		// Server cannot connect
		throw new UnsupportedOperationException("Unimplemented method 'connect'");
	}

	@Override
	public void write(SelectionKey key) {
		// Server cannot write
		throw new UnsupportedOperationException("Unimplemented method 'write'");
	}
}
