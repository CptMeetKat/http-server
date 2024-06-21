
package MK.HTTPServer.AcceptanceTests;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import MK.HTTPServer.App;
import MK.HTTPServer.ClientSocketOperations;
import MK.HTTPServer.HTTPRequest;

import static org.junit.Assert.*;
import MK.HTTPServer.SocketManager;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;



@Category(SlowTests.class)
public class Basic_Test{

    private static App service;
    private static ExecutorService executorService;

    @BeforeClass
    public static void startService() {
        service = new App();
        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> service.start());

        // Wait for the service to start
        try {
            Thread.sleep(2000); // Wait for 2 seconds to ensure service starts
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
            manager.registerClientSocket("localhost", 2024,
                                        new ClientSocketOperations(httpRequest, browser));
            try {
                if(browser.isComplete())
                    throw new InterruptedException();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("_______Thread was interrupted!");
            }
        }
        catch(IOException e)
        {
            fail("Unable to obtain socket");
        }
        
        //HTTPResponse response = new HTTPResponse(browser.getResult());
        ////TODO: Make HTTP response parse a HTTP response
        String result = browser.getResult();
        assertTrue(result.contains("OK")); //TODO: Imperfect test
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
            manager.registerClientSocket("localhost", 2024,
                                        new ClientSocketOperations(httpRequest, browser));
            try {
                if(browser.isComplete())
                    throw new InterruptedException();
                Thread.sleep(100);
            } catch (InterruptedException e) {
                System.out.println("_______Thread was interrupted!");
            }
        }
        catch(IOException e)
        {
            fail("Unable to obtain socket");
        }
        
        //HTTPResponse response = new HTTPResponse(browser.getResult());
        ////TODO: Make HTTP response parse a HTTP response
        String result = browser.getResult();
        assertTrue(result.contains("404")); //TODO: Not perfect test
    }

   //@Test
   //public void doTestForDev(){}

   //@Test
   //public void doTestForDev(){}
   
   //@Test
   //public void doTestForDev(){}
}

