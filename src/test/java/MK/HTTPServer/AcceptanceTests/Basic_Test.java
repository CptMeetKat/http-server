
package MK.HTTPServer.AcceptanceTests;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import org.junit.internal.runners.JUnit4ClassRunner;
import org.junit.runner.RunWith;
import static org.junit.Assert.*;

@Category(SlowTests.class)
@RunWith(JUnit4ClassRunner.class)
public class Basic_Test{

    @Test
    @Category(value={SlowTests.class})
    public void testApp() {
        assertTrue(true);
   }

   @Test
   public void doTestForDev(){}

}

