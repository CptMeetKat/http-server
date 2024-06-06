package MK.HTTPServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SocketManager
{
    private Selector selector = null; 

    public SocketManager()
        throws IOException
    {
        selector = Selector.open(); 
    }

    public void registerServerSocket(String ip, int port, SelectionKeyOperations callbacks)
    {
        ServerSocket server = null;
        try
        {
            ServerSocketChannel serverSocketChannel = ServerSocketChannel.open(); 
            server = serverSocketChannel.socket(); 
            server.bind(new InetSocketAddress(ip, port)); 
            serverSocketChannel.configureBlocking(false); 
            int ops = serverSocketChannel.validOps(); 
            serverSocketChannel.register(selector, ops, callbacks); 
        }
        catch(IOException e)
        {
            System.out.println(e);
        }
    }

    public void run()
        throws IOException
    {
        while (true) {
            selector.select(); //Blocking
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> i = selectedKeys.iterator(); 

            while (i.hasNext()) { 
                SelectionKey key = i.next(); 
                SelectionKeyOperations operations = (SelectionKeyOperations) key.attachment();

                if (key.isAcceptable()) 
                {
                    System.out.println("****ACCEPT****");
                    System.out.println("Connection Accepted.."); 
                    operations.accept(key);
                }

                else if (key.isReadable()) 
                {
                    System.out.println("****READ****");
                    operations.read(key);
                }
                i.remove(); 
            } 
        }
    }
}
