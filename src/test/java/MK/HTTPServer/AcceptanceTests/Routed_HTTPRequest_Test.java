
package MK.HTTPServer.AcceptanceTests;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import MK.HTTPServer.App;
import MK.HTTPServer.ClientSocketOperations;
import MK.HTTPServer.HTTPRequest;
import MK.HTTPServer.Route;
import MK.HTTPServer.ServerSocketOperations;

import static org.junit.Assert.*;
import MK.HTTPServer.SocketManager;

import org.junit.AfterClass;
import org.junit.BeforeClass;

import java.io.IOException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;


@Category(SlowTests.class)
public class Routed_HTTPRequest_Test{

    private static App service;
    private static ExecutorService executorService;

    @BeforeClass
    public static void startService() {

        Route route1 = new Route("localhost", 8001, "dynamic");
        service = new App();
        service.loadDefault();
        service.routes.add(route1);

        try {
            SocketManager manager = SocketManager.getSocketManager();
            manager.registerServerSocket("localhost", 8001, new ServerSocketOperations(256, 
                                                             new MockRoutedServerOperations("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: 13\r\n\r\nHello, world!")));
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Fail1");
        }

        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> service.start());

        try {
            Thread.sleep(2000); // Wait for the service to start
        } catch (InterruptedException e) {
            System.out.println("interupt");
            Thread.currentThread().interrupt();
        }
    }

    @AfterClass
    public static void stopService() throws InterruptedException {
        if (service != null) {
            System.out.println("stopping app");
            service.stop();
        }
        if (executorService != null) {
            executorService.shutdown();
            executorService.awaitTermination(5, TimeUnit.SECONDS);
        }
    }

    @Test
    public void when_dynamic_file_exists_return_HTTP_200() 
    {
        String message = "GET /dynamic/mysample.html HTTP/1.1\r\n\r\n";
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
            } catch (InterruptedException e) 
            {
                System.out.println("Interupted Early");
            }
        }
        catch(IOException e)
        {
            System.out.println("Unable to obtain socket");
            fail("Unable to obtain socket");
            return;
        }

        String result = browser.getResult();
        assertTrue(result.contains("200"));
        assertNotNull(result);
    }

    //public void when_dynamic_file_does_not_exist_return_HTTP_404() //May not be needed
    //public void when_route_does_not_respond_return_403() //May not be needed
    //public void when_route_does_not_exist_return_xxxxxxxx() //May not be needed
    
}

