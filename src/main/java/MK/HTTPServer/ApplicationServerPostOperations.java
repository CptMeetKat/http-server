
package MK.HTTPServer;

import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

public class ApplicationServerPostOperations implements SocketReadCallback
{
    Logger logger = Logger.getLogger();
    Sendable replyTo;

    public ApplicationServerPostOperations(Sendable replyTo)
    {
        this.replyTo = replyTo;
    }

	@Override
	public void onReadComplete(SocketChannel sender, String data) {
     
        replyTo.send(   ByteBuffer.wrap(data.getBytes())    );

        try {
            sender.close(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
	}
}
