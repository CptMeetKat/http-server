
package MK.HTTPServer;

import java.nio.channels.SocketChannel;

import MK.HTTPServer.Logger.PrintLevel;

import java.io.IOException;
import java.nio.ByteBuffer;

public class Responder implements Sendable
{
    private SocketChannel respondTo; //Could be a ByteChannel?
    private boolean keepAlive = false;
    Logger logger = Logger.getLogger();

    public Responder(SocketChannel sender, boolean keepAlive)
    {
        this.respondTo = sender;
        this.keepAlive = keepAlive;
    }

    public Responder(SocketChannel sender)
    {
        this.respondTo = sender;
    }

    public void setKeepAlive(boolean keepAlive)
    {
        this.keepAlive = keepAlive;
    }

	@Override
	public void send(ByteBuffer response) {

        try
        {
            int bytes_written = respondTo.write(response); //This should perhaps be thrown
            logger.printf(PrintLevel.INFO, "Wrote %d bytes to %s\n", bytes_written, respondTo.getRemoteAddress());
            if(response.remaining() == 0)
            {
                respondTo.close();
            }
            //if(!keepAlive)
            //{
            //   //logger.printf(PrintLevel.WARNING, "You may be closing final socket before data is being sent\n");
            //    //logger.printf(PrintLevel.INFO, "Closing connection to %s\n", respondTo.getRemoteAddress());
            //    //respondTo.close();  //this can be caught
            //}
        }
        catch(IOException e)
        {
            System.err.println(e);
        }
	}
}

