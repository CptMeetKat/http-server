
package MK.HTTPServer;
import org.junit.Test;
import static org.junit.Assert.*;

import java.nio.file.Paths;
import java.util.Arrays;
import java.util.Collection;

import org.junit.runner.RunWith;
import org.junit.runners.Parameterized;
import org.junit.runners.Parameterized.Parameters;

@RunWith(value = Parameterized.class)
public class PathUtilsTest{

    private String basePath;  
    private String userPath;  

    public PathUtilsTest(String basePath, String userPath)
    {
        this.basePath = basePath;
        this.userPath = userPath;
    }
    
    @Test
    public void does_path_result_in_path_traversal() {
        
        try
        {
            PathUtils.resolvePath(Paths.get(basePath),
                                            Paths.get(userPath));
            fail("Path Traversal vulnerability occured");
        }
        catch(IllegalArgumentException e) { }
    }
        
    @Parameters
    public static Collection<Object[]> data() {
        return Arrays.asList(new Object[][] {
            { "/base/folder1",    "../../file1"},
            { "/foo/bar/baz",     "../attack"},
            {"../",               "../"},
            {"/base",             "/base2"}
        });
    }
}

