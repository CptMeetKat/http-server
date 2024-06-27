
package MK.HTTPServer.AcceptanceTests;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.SocketChannel;

import MK.HTTPServer.ClientPostOperations;
import MK.HTTPServer.HTTPResponse;

public class MockRoutedServerOperations implements ClientPostOperations
{
    ByteBuffer response;
    SocketChannel sender;
    boolean complete;
    String data;

    public MockRoutedServerOperations(String response)
    {
        this.response = ByteBuffer.wrap(response.getBytes());
        this.complete = false;
    }

	@Override
	public void onReadComplete(SocketChannel sender, String data) {
        this.data = data;
        try {
            //System.out.println("RESPONSE___:" + response);
            //sender.write(ByteBuffer.wrap(new HTTPResponse(response).serialize()));
            //sender.write(ByteBuffer.wrap(response.getBytes()));
            sender.write(response);
        } catch (IOException e) {
            System.err.println("Unable to send data " + e );
            e.printStackTrace();
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