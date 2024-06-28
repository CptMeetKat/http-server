
package MK.HTTPServer;

import static org.junit.Assert.assertTrue;

import org.junit.Test;

public class HTTPResponseTest{
    @Test
    public void should_parse_HTTP_string_to_object() {

        String input = "HTTP/1.1 200 OK\r\n"
                    + "Server: SimpleHTTP/0.6 Python/3.10.12\r\n"
                    + "Content-type: text/html\r\n"
                    + "Content-Length: 83\r\n"
                    + "Last-Modified: Wed, 05 Jun 2024 05:29:34 GMT\r\n\r\n"
                    + "<html><body>This is my file, if u see this - then somthing is working</body></html>";

        HTTPResponse proposed = new HTTPResponse(input);

        HTTPResponse expected = new HTTPResponse();
        expected.setVersion("1.1");
        expected.setStatusCode("200");
        expected.setReasonPhrase("OK");
        expected.setField("Server", "SimpleHTTP/0.6 Python/3.10.12");
        expected.setField("Content-type", "text/html");
        expected.setField("Content-Length", "83");
        expected.setField("Last-Modified", "Wed, 05 Jun 2024 05:29:34 GMT");
        expected.setBody("<html><body>This is my file, if u see this - then somthing is working</body></html>");

        assertTrue(proposed.equals(expected)); 
        //When compared like this the assert does not reveal what field is actually problematic
    }

    @Test
    public void should_parse_HTTP_string_without_body_to_object() {

        String input = "HTTP/1.1 200 OK\r\n"
                    + "Server: SimpleHTTP/0.6 Python/3.10.12\r\n"
                    + "Content-type: text/html\r\n"
                    + "Content-Length: 83\r\n"
                    + "Last-Modified: Wed, 05 Jun 2024 05:29:34 GMT\r\n\r\n";

        HTTPResponse proposed = new HTTPResponse(input);

        HTTPResponse expected = new HTTPResponse();
        expected.setVersion("1.1");
        expected.setStatusCode("200");
        expected.setReasonPhrase("OK");
        expected.setField("Server", "SimpleHTTP/0.6 Python/3.10.12");
        expected.setField("Content-type", "text/html");
        expected.setField("Content-Length", "83");
        expected.setField("Last-Modified", "Wed, 05 Jun 2024 05:29:34 GMT");

        assertTrue(proposed.equals(expected)); 
        //When compared like this the assert does not reveal what field is actually problematic
    }
}

