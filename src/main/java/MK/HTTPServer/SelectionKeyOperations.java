
package MK.HTTPServer;
import java.nio.channels.SelectionKey; 

public interface SelectionKeyOperations
{
    public void accept(SelectionKey key);

    public void read(SelectionKey key);
}
