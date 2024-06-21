package MK.HTTPServer;

import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class HTTPResponse
{
    String version;
    String status_code;
    String reason_phrase;
    String body;

    HashMap<String, String> headers = new HashMap<String, String>();

    public HTTPResponse()
    {
        reason_phrase = "";
    }

    public HTTPResponse(String response)
    {
        parseResponse(response);
    }

    public void parseResponse(String response)
    {
        String[] lines = response.split("\n"); 
        String[] statusline = lines[0].split(" ");

        version = statusline[0];
        status_code = statusline[1];
        reason_phrase = statusline[2];

        for(int i = 1; i < lines.length; i++)
        {
            String[] pair = lines[i].split(":");
            headers.put(pair[0], pair[1]);
        }

//HTTP/1.1 200 OK
//Keep-Alive:max=100
//Connection:keep-alive
//Content-Length:91
//Content-Type:text/html


    }

    public static HTTPResponse createOKResponse()
    {
        HTTPResponse response = new HTTPResponse();
        response.setVersion("1.1");
        response.setReasonPhrase("OK");
        response.setStatusCode("200");
        return response;
    }

    public static HTTPResponse createNotFoundResponse()
    {
        HTTPResponse response = new HTTPResponse();
        response.setVersion("1.1");
        response.setStatusCode("404");
        response.setReasonPhrase("Not Found");
        response.setContentType("text/plain");
        response.setBody("404 Not Found"); //Future: Replace with default 404 not found page
        return response;
    }


    public static HTTPResponse createServiceUnavailable()
    {
        HTTPResponse response = new HTTPResponse();
        response.setVersion("1.1");
        response.setStatusCode("503");
        response.setReasonPhrase("Service Unavailable");
        response.setContentType("text/plain");
        response.setBody("503 Service Unavailable"); //Future: Replace with default 503 page
        return response;
    }

    public void setVersion(String version)
    {
        if(version.equals("1.1")) //Only currently supporting 1.1
            this.version = "HTTP/1.1";
    }
    
    public void setStatusCode(String status_code)
    {
        this.status_code = status_code;
    }

    public void setReasonPhrase(String reason_phrase)
    {
        this.reason_phrase = reason_phrase;
    }

    public void setContentType(String content_type)
    {
        headers.put("Content-Type", content_type);
    }

    public void setBody(String body)
    {
        this.body = body;
        headers.put("Content-Length", String.valueOf(body.length()));
    }

    private String getHeaders() //TODO: Name change
    {
        StringBuilder header_builder = new StringBuilder();
        for(String field : headers.keySet())
        {
            header_builder.append(field);
            header_builder.append(":");
            header_builder.append(headers.get(field));
            header_builder.append("\r\n");
        }

        return header_builder.toString().trim();
    }

    public void addKeepAlive() 
    {
        headers.put("Connection", "keep-alive");
        headers.put("Keep-Alive", "max=100");
    }

    public byte[] serialize()
    {
        addKeepAlive(); //TODO: This should be added when needed and honoured not all the time
        StringBuilder response = new StringBuilder();
        response.append(version + " " + status_code + " " + reason_phrase + "\r\n");
        response.append(getHeaders());
        response.append("\r\n\r\n");
        response.append(body);

        byte[] response_bytes = response.toString().getBytes(StandardCharsets.UTF_8);
        return response_bytes;
    }
}
