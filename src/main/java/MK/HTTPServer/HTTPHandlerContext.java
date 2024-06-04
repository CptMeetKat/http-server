package MK.HTTPServer;

import java.nio.channels.SocketChannel;

public class HTTPHandlerContext
{
    SocketChannel sender;
    HTTPRequest request;

    public HTTPHandlerContext (){}

    public HTTPHandlerContext addSender(SocketChannel sender)
    {
        this.sender = sender;
        return this;
    }

    public HTTPHandlerContext addHTTPRequest(HTTPRequest request)
    {
        this.request = request;
        return this;
    }

    public SocketChannel getSender()
    {
        return sender;
    }

    public HTTPRequest getHTTPRequest()
    {
        return request;
    }
}
