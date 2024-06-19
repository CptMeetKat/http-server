
package MK.HTTPServer.AcceptanceTests;

import org.junit.Test;
import org.junit.experimental.categories.Category;
import static org.junit.Assert.*;

@Category(SlowTests.class)
public class Basic_Test{

    @Test
    @Category(value={SlowTests.class})
    public void testApp() {
        assertTrue(true);
   }

   @Test
   public void doTestForDev(){}

}

