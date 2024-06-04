package MK.HTTPServer;


public abstract class BaseHTTPHandler implements HTTPRequestHandler
{
    public HTTPRequestHandler next;   
    public void setNext(HTTPRequestHandler tail)
    {
        this.next = tail;
    }
    public abstract void processRequest(HTTPHandlerContext context);
    
}
