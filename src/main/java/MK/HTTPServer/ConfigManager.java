package MK.HTTPServer;

import java.util.HashMap;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

public class ConfigManager
{
    private HashMap<String, String> config = new HashMap<>();

    public ConfigManager(String config_path)
    {
        importConfig(config_path);
    }

    private boolean inExclusionList(String target)
    {
        final String exclusions[] = new String[]{"***forward_endpoints***"};

        for(String exclude : exclusions)
        {
            if(target.equals(exclude))
                return true;
        }

        return false;
    }
        
    private void importConfig(String config_path)
    {
        try
        {
            List<String> lines = Files.readAllLines(Paths.get(config_path));
            for(String l : lines)
            {
                if(inExclusionList(l))
                    continue;

                String[] config_tuple = l.split("=");
                if(config_tuple.length > 2)
                {
                    System.err.println("Config error: more than 1 value pair detected");
                    System.exit(1);
                }
                config.put(config_tuple[0], config_tuple[1]);
            }
        }
        catch(IOException e)
        {
            System.err.println("An error occurred: " + e.getMessage());
            System.exit(1);
        }
    }

    public HashMap<String, String> getConfigurations()
    {
        return config;
    }

    public String getField(String field)
    {
        return config.get(field);
    }

    
    public int getInt(String field)
    {
        return Integer.parseInt(config.get(field));
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        for(String key : config.keySet())
        {
            builder.append(key + "=" + config.get(key) + "\n");
        }

        return builder.toString();
    }
}
