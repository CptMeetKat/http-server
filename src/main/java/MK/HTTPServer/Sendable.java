package MK.HTTPServer;

import java.nio.ByteBuffer;

public interface Sendable
{
    public void send(ByteBuffer response);
}
