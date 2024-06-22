

package MK.HTTPServer;

import java.nio.channels.SocketChannel;

public interface ClientPostOperations
{
    public void onReadComplete(SocketChannel sender, String data);
}
