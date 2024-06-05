
package MK.HTTPServer;

import java.io.IOException; 
import java.net.InetSocketAddress; 
import java.net.ServerSocket;
import java.nio.ByteBuffer; 
import java.nio.channels.SelectionKey; 
import java.nio.channels.Selector; 
import java.nio.channels.ServerSocketChannel; 
import java.nio.channels.SocketChannel;
import java.util.HashMap;
import java.util.Iterator; 
import java.util.Set; 

public class HTTPServer 
{
    private Selector selector = null; 
    private int port;
    private int buffer_length = 3;
    private HashMap<String, StringBuilder> request_builder = new HashMap<>();
    private String static_root;
    private BaseHTTPHandler pipeline;

    public HTTPServer(int port, int buffer_length, String static_root, BaseHTTPHandler pipeline)
    {
        this.port = port;
        this.buffer_length = buffer_length;
        this.static_root = static_root;
        this.pipeline = pipeline;
    }

    public void start()
    {
        try { 
            selector = Selector.open(); 
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open(); 
            ServerSocket serverSocket = serverSocketChannel.socket(); 
            serverSocket.bind(new InetSocketAddress("localhost", port)); 
            serverSocketChannel.configureBlocking(false); 
            int ops = serverSocketChannel.validOps(); 
            serverSocketChannel.register(selector, ops, null); 
    
            while (true) { 

                selector.select(); //Select keys that are ready //Blocking
                Set<SelectionKey> selectedKeys = selector.selectedKeys(); //Get the keys
                Iterator<SelectionKey> i = selectedKeys.iterator(); 

                while (i.hasNext()) { 
                    SelectionKey key = i.next(); 

                    if (key.isAcceptable()) 
                        handleAccept(key); 
                    else if (key.isReadable()) 
                        handleRead(key); 
                    i.remove(); 
                } 
            }
        } 
        catch (IOException e) { 
            e.printStackTrace(); 
        } 

    }

    private void
        handleAccept(SelectionKey key)
            throws IOException 
        { 
            ServerSocketChannel server = (ServerSocketChannel) key.channel();

            System.out.println("****ACCEPT****");
            System.out.println("Connection Accepted.."); 
            SocketChannel client = server.accept(); 
            client.configureBlocking(false); 
            client.register(selector, SelectionKey.OP_READ); 
        } 

    private void handleRead(SelectionKey key) 
            throws IOException 
        { 
            System.out.println("****READ****");
            SocketChannel client = (SocketChannel)key.channel(); 
            String socketAddress = ((InetSocketAddress)client.getRemoteAddress()).toString();

            ByteBuffer buffer = ByteBuffer.allocate(buffer_length); 
            int received_length = client.read(buffer); 

            String data = new String(buffer.array());
            data = data.substring(0, received_length);

            System.out.printf("Received length %d bytes from %s\n", received_length , socketAddress);
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
}

// Not sure how to determine end of HTTP request.
// - What prevents the RequestMap from being overloaded with request data
