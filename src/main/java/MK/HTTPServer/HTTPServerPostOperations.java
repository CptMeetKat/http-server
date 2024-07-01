
package MK.HTTPServer;

import java.nio.channels.SocketChannel;

import MK.HTTPServer.Logger.PrintLevel;

public class HTTPServerPostOperations implements SocketReadCallback
{
    BaseHTTPHandler pipeline;
    Logger logger = Logger.getLogger();
    String static_root;

    public HTTPServerPostOperations(String static_root, BaseHTTPHandler pipeline)
    {
        this.static_root = static_root;
        this.pipeline = pipeline;
    }

	@Override
	public void onReadComplete(SocketChannel sender, String data) {
     
//        logger.printf(PrintLevel.INFO, "%s operating on completed data\n", this.getClass().getSimpleName());

        HTTPRequest request = new HTTPRequest(data);

        boolean honourKeepAlive = true; //TODO: Add as config

        boolean keep_alive = false;
        if(honourKeepAlive && request.getField("Keep-Alive") == "keep-alive") 
            keep_alive = true;

        HTTPHandlerContext context = new HTTPHandlerContext()
            .addSender(sender)
            .addHTTPRequest(request)
            .addStaticRoot(static_root)
            .addResponder(new Responder(sender, keep_alive));

        pipeline.processRequest(context);
	}
}
