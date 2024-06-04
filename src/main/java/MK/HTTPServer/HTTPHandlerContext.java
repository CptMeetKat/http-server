package MK.HTTPServer;

import java.nio.channels.SocketChannel;

public class HTTPHandlerContext
{
    SocketChannel sender;
    HTTPRequest request;
    String static_root;

    public HTTPHandlerContext (){}

    public HTTPHandlerContext addSender(SocketChannel sender)
    {
        this.sender = sender;
        return this;
    }



    public HTTPHandlerContext addStaticRoot(String static_root)
    {
        this.static_root = static_root;
        return this;
    }

    public String getStaticRoot()
    {
        return static_root;
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
