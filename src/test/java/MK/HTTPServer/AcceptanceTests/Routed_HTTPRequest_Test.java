
package MK.HTTPServer.AcceptanceTests;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import MK.HTTPServer.App;
import MK.HTTPServer.ClientSocketOperations;
import MK.HTTPServer.HTTPRequest;
import MK.HTTPServer.HTTPResponse;
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

import MK.HTTPServer.Logger;
import MK.HTTPServer.Logger.PrintLevel;

@Category(SlowTests.class)
public class Routed_HTTPRequest_Test{

    private static App service;
    private static ExecutorService executorService;
    private static Logger logger = Logger.getLogger();

    @BeforeClass
    public static void startService() {

        Route route1 = new Route("localhost", 8005, "dynamic"); //TODO: This only work while we still have our sample file, test is not isolated enough
        service = new App();
        service.loadDefault1();
        service.routes.add(route1);

        try {
            HTTPResponse response = new HTTPResponse("HTTP/1.1 200 OK\r\nContent-Type: text/plain\r\nContent-Length: 13\r\n\r\nHello, world!");
            SocketManager manager = SocketManager.getSocketManager();
            manager.registerServerSocket("localhost", 8005, new ServerSocketOperations(256, 
                                                             new MockRoutedServerOperations(response.toString()))); //TODO: This is a hardcoded response that should be changed
        } catch (IOException e) {
            logger.printf(PrintLevel.ERROR, "Unable to obtain socket manager\n");
        }

        executorService = Executors.newSingleThreadExecutor();
        executorService.submit(() -> service.start());
    }

    @AfterClass
    public static void stopService() throws InterruptedException {
        if (service != null) {
            logger.printf(PrintLevel.INFO, "Stopping app...\n");
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
            } catch (InterruptedException e) {}
        }
        catch(IOException e)
        {
            logger.printf(PrintLevel.ERROR, "Unable to obtain socket\n");
            fail("Unable to obtain socket");
            return;
        }

        String result = browser.getResult();
        assertTrue(result.contains("200"));
        assertNotNull(result);
    }
}

