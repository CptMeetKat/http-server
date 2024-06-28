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

    @Override
    public boolean equals(Object obj)
    {
        if (obj == this)  //Memory location check
            return true;

        if (!(obj instanceof HTTPResponse)) 
            return false;

        HTTPResponse other = (HTTPResponse) obj;
        if(this.headers.size() != other.headers.size())
            return false;

        if(!version.equals(other.version) ||
                !status_code.equals(other.status_code) ||
                !reason_phrase.equals(other.reason_phrase) ||
                !body.equals(other.body))
            return false;

        for(String field : headers.keySet())
        {
            if(! headers.get(field).equals(other.headers.get(field)))
            {
                return false;
            }
        }

        return true;
    }

    public HTTPResponse(String response)
    {
        parseResponse(response);
    }


    public void parseResponse(String response)
    {
        String[] lines = response.split("\r\n"); 
        String[] statusline = lines[0].split(" ");

        version = statusline[0];
        status_code = statusline[1];
        reason_phrase = statusline[2];

        int i = 1;
        while(i < lines.length)
        {
            if(lines[i].trim() == "")
                break;

            int splitAt = lines[i].indexOf(":");
            String[] pair = StringUtils.splitStringAtPos(lines[i], splitAt);
            setField(pair[0], pair[1]);
            i++;
        }
        //TODO: Test for empty body resposne
       body = lines[i+1]; //TODO: parse body
    }

    public String getStatusCode()
    {
        return status_code;
    }

    public void setField(String field, String value)
    {
        headers.put(field.trim().toLowerCase(),
                    value.trim());
    }

    public String getField(String field)
    {
        return headers.get(field.trim().toLowerCase());
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
        setField("Content-Type", content_type);
    }

    public void setBody(String body)
    {
        this.body = body;
        setField("Content-Length", String.valueOf(body.length()));
    }

    public String getBody()
    {
        return body;
    }

    private String getRequestLine()
    {
        return version + " " + status_code + " " + reason_phrase + "\r\n";
    }

    private String getHeaders()
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
        setField("Connection", "keep-alive");
        setField("Keep-Alive", "max=100");
    }

    public byte[] serialize()
    {
        addKeepAlive(); //TODO: This should be added when needed and honoured not all the time
        String response = this.toString();
        byte[] response_bytes = response.toString().getBytes(StandardCharsets.UTF_8);
        return response_bytes;
    }
    
    @Override
    public String toString()
    {
        StringBuilder response = new StringBuilder();
        response.append(getRequestLine());
        response.append(getHeaders());
        response.append("\r\n\r\n");
        response.append(body);
		return response.toString();
    }
}
