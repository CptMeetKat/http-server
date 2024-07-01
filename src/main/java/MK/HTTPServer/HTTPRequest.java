package MK.HTTPServer;

import java.util.HashMap;


public class HTTPRequest 
{
    String original_request;
    String method = "";
    String uri = "";
    String version = "";
    String body = "";

    HashMap<String,String> request_map = new HashMap<>();
    public HTTPRequest(String request)
    {
        parseRequest(request);
        this.original_request = request;
    }

    public void setBody(String body)
    {
        this.body = body;
    }

    public void setMethod(String method) //TODO: Only allow the standard methods
    {
        this.method = method.toUpperCase();
    }

    public void setURI(String uri)
    {
        this.uri = uri;
    }

    public void setVersion(String version)
    {
        this.version = version;
    }

    private void parseRequest(String request)
    {
        String[] lines = request.split("\n");

        String[] request_line = lines[0].split(" ");
        setMethod(request_line[0]);
        setURI(request_line[1]);
        setVersion(request_line[2]);
        
        for (int i = 1; i < lines.length; i++)
        {
            String l = lines[i];
            int seperator = l.indexOf(":");
            if(seperator != -1)
            {
                request_map.put(l.substring(0, seperator).toLowerCase(), 
                                l.substring(seperator+1, l.length()-1));
            }
        }
    }

    public String getURI()
    {
        return uri;
    }

    public String getField(String field)
    {
        return request_map.get(field.trim().toLowerCase());
    }

    public void setField(String field, String value)
    {
        request_map.put(field.trim().toLowerCase(),
                    value.trim());
    }

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        result.append(method + " " + uri + " " + version + "\r\n");

        for(String field : request_map.keySet())
        {
            result.append( field + ":" + getField(field) + "\r\n");
        }
        result.append("\r\n");
        result.append(body);
        return result.toString();
    }

    public byte[] serialize()
    { 
        byte[] bytes = toString().getBytes();
        return bytes;
    }
}
