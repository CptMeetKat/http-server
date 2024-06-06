
package MK.HTTPServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.SelectionKey;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.HashMap;

public class HTTPServerOperations implements SelectionKeyOperations
{
    private String static_root;
    private BaseHTTPHandler pipeline;
    private int buffer_length = 256; //This is also managed through config
    private HashMap<String, StringBuilder> request_builder = new HashMap<>();

    public HTTPServerOperations(String static_root, BaseHTTPHandler pipeline, int buffer_length)
    {
        this.static_root = static_root;
        this.pipeline = pipeline;
        this.buffer_length = buffer_length;
    }

	@Override
	public void accept(SelectionKey key) {
        try
        {
            ServerSocketChannel server = (ServerSocketChannel) key.channel();
            SocketChannel client = server.accept(); 
            client.configureBlocking(false); 
            client.register(key.selector(), SelectionKey.OP_READ, key.attachment()); 
        }
        catch(IOException e)
        {
            System.out.println("Error: HTTPServer could not accept connection");
            System.out.println(e);
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
            data = data.substring(0, received_length);

            System.out.printf("___Received length %d bytes from %s\n", received_length , socketAddress);
            System.out.printf("Received message: '%s'\n", data);

            if(request_builder.containsKey(socketAddress))
                request_builder.get(socketAddress).append(data);
            else
                request_builder.put(socketAddress, new StringBuilder(data));

            if( request_builder.get(socketAddress).toString().endsWith("\r\n\r\n") ) //may change later when body is parsed
            {
                System.out.printf("Final Message: '%s'\n", request_builder.get(socketAddress));
                HTTPRequest request = new HTTPRequest(request_builder.get(socketAddress).toString());
                

                HTTPHandlerContext context = new HTTPHandlerContext()
                    .addSender(client)
                    .addHTTPRequest(request)
                    .addStaticRoot(static_root);


                pipeline.processRequest(context);

                client.close();
                request_builder.remove(socketAddress);
            }
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
	}
}
