
package MK.HTTPServer.AcceptanceTests;

import org.junit.Test;
import org.junit.experimental.categories.Category;

import MK.HTTPServer.App;

import static org.junit.Assert.*;

import org.junit.AfterClass;
import org.junit.BeforeClass;
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
            //Thread.sleep(200000); // Wait for 2 seconds to ensure service starts
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
    //@Category(value={SlowTests.class})
    public void testApp() 
    {


        
        assertTrue(true);
    }

   @Test
   public void doTestForDev(){}

}

