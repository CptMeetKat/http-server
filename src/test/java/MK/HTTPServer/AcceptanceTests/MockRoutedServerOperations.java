
package MK.HTTPServer.AcceptanceTests;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import MK.HTTPServer.SocketPostOperations;

import MK.HTTPServer.Logger;
import MK.HTTPServer.Logger.PrintLevel;

public class MockRoutedServerOperations implements SocketPostOperations
{
    ByteBuffer response;
    SocketChannel sender;
    boolean complete;
    String data;
    Logger logger = Logger.getLogger();

    public MockRoutedServerOperations(String response)
    {
        this.response = ByteBuffer.wrap(response.getBytes());
        this.complete = false;
    }

	@Override
	public void onReadComplete(SocketChannel sender, String data) {
        this.data = data;
        try {
            sender.write(response);
        } catch (IOException e) {
            logger.printf(PrintLevel.ERROR, "%s unable to send data\n", this.getClass().getSimpleName());
        }
        complete = true;
	}

	public boolean isComplete() {
        return complete;
	}

    public String getResult()
    {
        return data;
    }
}
