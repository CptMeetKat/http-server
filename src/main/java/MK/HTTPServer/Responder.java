
package MK.HTTPServer;

import java.nio.channels.SocketChannel;
import java.io.IOException;
import java.nio.ByteBuffer;

public class Responder implements Sendable
{
    private SocketChannel respondTo; //Could be a ByteChannel?
    private boolean keepAlive = false;

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
	public void send(HTTPResponse response) {

        try
        {
            respondTo.write(ByteBuffer.wrap(response.serialize())); //This exception should be thrown?

            if(!keepAlive)
                respondTo.close();  //this can be caught
        }
        catch(IOException e)
        {
            System.err.println(e);
        }


	}
}

