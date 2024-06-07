package MK.HTTPServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

public class SocketManager
{
    private static SocketManager manager = null;
    private Selector selector = null; 

    public static SocketManager getSocketManager()
        throws IOException
    {
        if(manager == null)
        {
            manager = new SocketManager();
        }
        return manager;
    }


    private SocketManager()
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
            server = serverSocketChannel.socket(); //Note this is never closed
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

    public void registerClientSocket(String ip, int port, SelectionKeyOperations callbacks)
    {
        try
        {
            SocketChannel clientChannel = SocketChannel.open(new InetSocketAddress(ip,port));

           // Socket client = clientChannel.socket();
           // client.connect(new InetSocketAddress(ip,port));
            clientChannel.configureBlocking(false); 
            int ops = clientChannel.validOps(); 
            clientChannel.register(selector, ops, callbacks); 
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
                    System.out.println("***Accepted connection***"); 
                    operations.accept(key);
                }

                else if (key.isReadable()) 
                {
                    System.out.println("***Reading connection***");
                    operations.read(key);
                }
                else if(key.isConnectable())
                {
                    System.out.println("***Connecting application***");
                    operations.connect(key);
                }
                else if(key.isWritable())
                {
                    System.out.println("***Writing application***");
                    operations.write(key);
                }
                i.remove(); 
            } 
        }
    }
}
