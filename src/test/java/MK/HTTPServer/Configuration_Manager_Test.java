package MK.HTTPServer;

import org.junit.Test;
import static org.junit.Assert.*;

public class Configuration_Manager_Test{

    @Test
    public void when_importing_config_imported_fields_exist() {
        
        String currentDirectory = System.getProperty("user.dir");
        String configPath = currentDirectory + "/src/test/java/MK/HTTPServer/test_server.config";

        ConfigManager manager = new ConfigManager(configPath);

        String port = "2024";
        String static_root = "/home/static/";
        String buffer_size = "3";
            
        assertEquals(manager.getField("port"), port);
        assertEquals(manager.getField("static_root"), static_root);
        assertEquals(manager.getField("buffer_size"), buffer_size);
    }
}

