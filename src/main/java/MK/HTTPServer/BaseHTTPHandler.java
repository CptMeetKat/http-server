package MK.HTTPServer;


public abstract class BaseHTTPHandler implements HTTPRequestHandler
{
    public HTTPRequestHandler next;   
    public void setTail(HTTPRequestHandler tail)
    {
        if(this.next == null)
            this.next = tail;
        else
            this.next.setTail(tail);
    }
    public abstract void processRequest(HTTPHandlerContext context);
    
}
