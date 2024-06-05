package MK.HTTPServer;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class HTTPRequest
{
    String original_request;

    HashMap<String,String> request_map = new HashMap<>();

    public HTTPRequest(String request)
    {
        setRequest(request);
        this.original_request = request;
        System.out.println(this);
    }

    private void setRequest(String request)
    {
        String[] lines = request.split("\n");

        String[] request_line = lines[0].split(" ");
        request_map.put("method", request_line[0]);
        request_map.put("URI", request_line[1]);
        request_map.put("version", request_line[2]);
        

        for (int i = 1; i < lines.length; i++)
        {
            String l = lines[i];
            int seperator = l.indexOf(":");
            if(seperator != -1)
            {
                request_map.put(l.substring(0, seperator),
                                l.substring(seperator+1, l.length()-1));
            }
        }
    }

    public String getField(String fieldname)
    {
        return request_map.get(fieldname);
    }

    @Override
    public String toString()
    {
        StringBuilder result = new StringBuilder();
        for(String field : request_map.keySet())
        {
            result.append( field + ":" + request_map.get(field) + "\n");
        }
        return result.toString();
    }

    public byte[] serialize()
    {
        byte[] bytes = original_request.toString().getBytes(StandardCharsets.UTF_8);
        return bytes;
    }
}
