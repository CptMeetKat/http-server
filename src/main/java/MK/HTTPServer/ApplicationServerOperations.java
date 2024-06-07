
package MK.HTTPServer;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.SocketAddress;
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
        //Dont think we care about read
        try
        {
            int buffer_length = 1024;
            SocketChannel client = (SocketChannel)key.channel(); 
            String socketAddress = client.getRemoteAddress().toString();

            ByteBuffer buffer = ByteBuffer.allocate(buffer_length); 
            int received_length = client.read(buffer); 
            
            String data = new String(buffer.array());
            System.out.println(data);
            client.close();

            String b = "HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: 13\r\n\r\nHello, world!";
            sender.write( ByteBuffer.wrap(b.getBytes()));
            sender.close();

            //System.out.println(httpRequest);


            //if(received_length == -1) 
            //{
            //    client.close();

            //    System.out.printf("Final Message: '%s'\n", request_builder.get(socketAddress));
            //    HTTPRequest request = new HTTPRequest(request_builder.get(socketAddress).toString());
            //    sender.write(ByteBuffer.wrap(request.serialize()));
            //    request_builder.remove(socketAddress);
            // }
            //else
            //{
            //    String data = new String(buffer.array());
            //    System.out.printf("RECEIEVED LENGTH: %d\n", received_length);
            //    data = data.substring(0, received_length);

            //    System.out.printf("___Received length %d bytes from %s\n", received_length , socketAddress);
            //    System.out.printf("Received message: '%s'\n", data);

            //    if(request_builder.containsKey(socketAddress))
            //        request_builder.get(socketAddress).append(data);
            //    else
            //        request_builder.put(socketAddress, new StringBuilder(data));
            //}





//            if( request_builder.get(socketAddress).toString().endsWith("\r\n\r\n") ) //may change later when body is parsed
//            {
//                System.out.printf("Final Message: '%s'\n", request_builder.get(socketAddress));
//                HTTPRequest request = new HTTPRequest(request_builder.get(socketAddress).toString());
//                
//                sender.write(ByteBuffer.wrap(request.serialize()));
//
//                client.close();
//                request_builder.remove(socketAddress);
//            }
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
        System.out.println("READING dyanmic operations");
	}

	@Override
	public void connect(SelectionKey key) {
		throw new UnsupportedOperationException("Unimplemented method 'read'");
	}

	@Override
	public void write(SelectionKey key) {
        SocketChannel channel = (SocketChannel) key.channel();
        Socket client = (Socket) channel.socket();

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
