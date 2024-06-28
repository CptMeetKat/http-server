package MK.HTTPServer;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Iterator;
import java.util.Set;

import MK.HTTPServer.Logger.PrintLevel;

public class SocketManager
{
    private static SocketManager manager = null;
    private Selector selector = null; 
    private Logger logger;
    private boolean isRunning = false;

    public static SocketManager getSocketManager()
        throws IOException
    {
        if(manager == null)
        {
            manager = new SocketManager();
        }
        return manager;
    }

    public void close()
    {
        try { //TODO: This is never called?
            logger.printf(PrintLevel.INFO, "Closing selector...\n");
            stop();
            if(selector != null && selector.isOpen())
            {
                selector.close();
                manager = null;
                selector = null;
            }

        } catch (IOException e) {
            logger.printf(PrintLevel.ERROR, "Unable to close selector\n");
        }
    }


    private SocketManager()
        throws IOException
    {
        this.logger = Logger.getLogger();
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
            logger.printf(PrintLevel.ERROR, "Unable to register sever socket\n");
        }
    }

    public void registerClientSocket(String ip, int port, SelectionKeyOperations callbacks)
        throws IOException
    {
            SocketChannel clientChannel = SocketChannel.open(new InetSocketAddress(ip,port));

            clientChannel.configureBlocking(false); 
            int ops = clientChannel.validOps(); 
            clientChannel.register(selector, ops, callbacks); 
    }

    public void stop()
    {
        isRunning = false;
    }

    public void run()
        throws IOException
    {
        isRunning = true;
        while (isRunning) {
            selector.selectNow(); 
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> i = selectedKeys.iterator(); 

            while (i.hasNext()) { 
                SelectionKey key = i.next(); 
                SelectionKeyOperations operations = (SelectionKeyOperations) key.attachment();

                if (key.isAcceptable()) 
                {
                    logger.print(PrintLevel.INFO, "***Accepted connection***"); 
                    operations.accept(key);
                }
                else if (key.isReadable()) 
                {
                    logger.print(PrintLevel.INFO, "***Reading connection***");
                    operations.read(key);
                }
                else if(key.isConnectable())
                {
                    logger.print(PrintLevel.INFO, "***Connecting application***");
                    operations.connect(key);
                }
                else if(key.isWritable())
                {
                    logger.print(PrintLevel.INFO, "***Writing application***");
                    operations.write(key);
                }
                i.remove(); 
            } 
        }
    }
}
