package MK.HTTPServer;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class App 
{
    private int port = 2024;
    private HashMap<String,String> config = new HashMap<>();

    public static void main(String[] args) 
    {
        new App();
    }

    public App()
    {
        importConfig("./server.config");
        this.port = Integer.parseInt(config.get("port"));
        RequestProcessor.static_root = config.get("static_root");  
        
        HTTPServer server = new HTTPServer(port, 3);
        server.start();
    }

    public void importConfig(String config_path)
    {
        try
        {
            List<String> lines = Files.readAllLines(Paths.get(config_path));
            for(String l : lines)
            {
                String[] config_tuple = l.split("=");
                if(config_tuple.length > 2)
                {
                    System.err.println("Config error");
                    System.exit(1);
                }
                
                config.put(config_tuple[0], config_tuple[1]);
            }
        }
        catch(IOException e)
        {
            System.err.println("An error occurred: " + e.getMessage());
        }
    }

}
