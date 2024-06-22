
package MK.HTTPServer.AcceptanceTests;

import java.nio.channels.SocketChannel;

import MK.HTTPServer.ClientPostOperations;

public class MockWebBrowserOperations implements ClientPostOperations
{
    boolean complete = false;
    String data;

	@Override
	public void onReadComplete(SocketChannel sender, String data) {
        this.data = data;
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
