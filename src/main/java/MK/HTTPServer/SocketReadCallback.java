

package MK.HTTPServer;

import java.nio.channels.SocketChannel;

public interface SocketReadCallback
{
    public void onReadComplete(SocketChannel sender, String data);
}
