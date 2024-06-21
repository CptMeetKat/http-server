
package MK.HTTPServer.AcceptanceTests;

import MK.HTTPServer.ClientPostOperations;
import MK.HTTPServer.ClientSocketOperations;

public class MockWebBrowserOperations implements ClientPostOperations
{
    boolean complete = false;
    String data;

	@Override
	public void onReadComplete(ClientSocketOperations operations, String data) {
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
