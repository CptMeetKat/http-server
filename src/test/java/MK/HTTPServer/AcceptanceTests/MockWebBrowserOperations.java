
package MK.HTTPServer.AcceptanceTests;

import java.nio.channels.SocketChannel;

import MK.HTTPServer.SocketPostOperations;
import MK.HTTPServer.Logger;

public class MockWebBrowserOperations implements SocketPostOperations
{
    Logger logger = Logger.getLogger();
    boolean complete = false;
    String data;

	@Override
	public void onReadComplete(SocketChannel sender, String data) {
     
        this.data = data;
        complete = true;
        try {
            sender.close(); 
        } catch (Exception e) {
            e.printStackTrace();
        }
	}

	public boolean isComplete() {
        return complete;
	}

    public String getResult()
    {
        return data;
    }
}
