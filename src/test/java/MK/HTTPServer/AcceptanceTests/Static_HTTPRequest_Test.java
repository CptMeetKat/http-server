
package MK.HTTPServer.AcceptanceTests;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import MK.HTTPServer.App;
import MK.HTTPServer.ClientSocketOperations;
import MK.HTTPServer.HTTPRequest;
import MK.HTTPServer.HTTPResponse;

import static org.junit.Assert.*;
import MK.HTTPServer.SocketManager;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Category(SlowTests.class)
public class Static_HTTPRequest_Test{

    private static App service;
    private static ExecutorService executorService;

    @BeforeClass
    public static void startService() {
        service = new App();
        service.loadDefault();
        

        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> service.start());

        try {
            Thread.sleep(2000); // Wait for the service to start
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }

    @AfterClass
    public static void stopService() throws InterruptedException {
        if (service != null) {
            service.stop();
        }
        if (executorService != null) {
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    @Test
    public void when_static_file_exists_return_HTTP_200() 
    {
        String message = "GET /mysample.html HTTP/1.1\r\n\r\n";
        HTTPRequest httpRequest = new HTTPRequest(message);
        MockWebBrowserOperations browser = new MockWebBrowserOperations();

        try
        {
            SocketManager manager = SocketManager.getSocketManager();

            manager.registerClientSocket("localhost", service.inbound_port,
                                        new ClientSocketOperations(httpRequest, browser));
            try
            {
                while(true)
                {
                    if(browser.isComplete())
                        throw new InterruptedException();
                    Thread.sleep(1000);
                }
            } catch (InterruptedException e) {}
        }
        catch(IOException e)
        {
            e.printStackTrace();
            fail("Unable to obtain socket");
            return;
        }
        
        String result = browser.getResult();
        HTTPResponse response = new HTTPResponse(result);
        assertTrue(response.getStatusCode().equals("200"));
    }



    @Test
    public void when_static_file_does_not_exist_requested_return_HTTP_404() 
    {
        String message = "GET /doesnotexist.html HTTP/1.1\r\n\r\n";
        HTTPRequest httpRequest = new HTTPRequest(message);
        MockWebBrowserOperations browser = new MockWebBrowserOperations();

        try
        {
            SocketManager manager = SocketManager.getSocketManager();
            manager.registerClientSocket("localhost", service.inbound_port, 
                                        new ClientSocketOperations(httpRequest, browser));
            try 
            {
                while(browser.isComplete())
                    throw new InterruptedException();
                Thread.sleep(100);
            } catch (InterruptedException e) {}
        }
        catch(IOException e)
        {
            fail("Unable to obtain socket");
            return;
        }
        
        String result = browser.getResult();
        HTTPResponse response = new HTTPResponse(result);
        assertTrue(response.getStatusCode().equals("404")); 
    }
}

