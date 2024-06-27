

package MK.HTTPServer;

import java.nio.channels.SocketChannel;

public interface SocketPostOperations
{
    public void onReadComplete(SocketChannel sender, String data);
}
