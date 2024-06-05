package MK.HTTPServer;


public interface HTTPRequestHandler
{
    public void processRequest(HTTPHandlerContext context);
    public void setTail(HTTPRequestHandler handler);
}
