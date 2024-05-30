package MK.HTTPServer;

import java.nio.charset.StandardCharsets;

public class HTTPResponse
{
    String version;
    String status_code;
    String reason_phrase;
    String body;
    String content_type;
    String content_length;

    public HTTPResponse()
    {
        reason_phrase = "";
    }

    public static HTTPResponse createOKResponse()
    {
        HTTPResponse response = new HTTPResponse();
        response.setVersion("1.1");
        response.setReasonPhrase("OK");
        response.setStatusCode("200");
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
        this.content_type = content_type;
    }

    public void setBody(String body)
    {
        this.body = body;
        this.content_length = String.valueOf(body.length());
    }

    public byte[] serialize()
    {
        StringBuilder response = new StringBuilder();
        response.append(version + " " + status_code + " " + reason_phrase + "\r\n");
        response.append("Content-Type: " + content_type + "\r\n");
        response.append("Content-Length: " + content_length + "\r\n");
        response.append("\r\n\r\n");
        response.append(body);

        byte[] response_bytes = response.toString().getBytes(StandardCharsets.UTF_8);
        return response_bytes;
    }
}
