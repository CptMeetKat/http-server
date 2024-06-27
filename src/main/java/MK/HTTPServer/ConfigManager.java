package MK.HTTPServer;

import java.util.ArrayList;
import java.util.HashMap;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

import MK.HTTPServer.Logger.PrintLevel;

public class ConfigManager
{
    private HashMap<String, String> config = new HashMap<>();
    private ArrayList<String> field_order = new ArrayList<String>();//Maintain order for printing
    private Logger logger = Logger.getLogger();

    public ConfigManager(String config_path)
    {
        importConfig(config_path);
    }

    public ConfigManager()
    {
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

    public void addConfig(String key, String value)
    {
        config.put(key,value);
        field_order.add(key);
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
                    logger.printf(PrintLevel.ERROR, "Bad config detected, more than 1 value pair in field\n"); //TODO: Could add line number
                    System.exit(1);
                }
                addConfig(config_tuple[0], config_tuple[1]);
            }
        }
        catch(IOException e)
        {
            logger.printf(PrintLevel.ERROR, "%s\n", e.getMessage());
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


    public ArrayList<String> getIncrementedField(String field)
    {
        int i = 0;
        ArrayList<String> result = new ArrayList<String>();
        
        String value = config.get(field+i);
        while(value != null)
        {
            result.add(value);
            i++;
            value = config.get(field+i);
        }

        return result;
    }
    
    public int getInt(String field)
    {
        return Integer.parseInt(config.get(field));
    }

    @Override
    public String toString()
    {
        StringBuilder builder = new StringBuilder();
        for(String key : field_order)
        {
            builder.append(key + "=" + config.get(key) + "\n");
        }

        return builder.toString().trim();
    }
}
