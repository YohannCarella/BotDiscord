package discord.bot.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ServerPropertiesManager {

    private Map<String,Map<String,String>> globalProperties;
    private Map<String,String> serverProperties;
    private List<String> propertiesList;

    private static ServerPropertiesManager instance;

    private void createPropertiesList(){
        propertiesList = new ArrayList<>();
        propertiesList.add("autoRole");
        propertiesList.add("userEventChannel");
        propertiesList.add("userEventEnabled");
    }

    public static ServerPropertiesManager getInstance(){
        if(instance == null){
            instance = new ServerPropertiesManager();
        }
        return instance;
    }

    public void setPropertyForServer(String serverId,String property, String value){
        if(globalProperties.get(serverId) != null){
           serverProperties = globalProperties.get(serverId);
        }
        serverProperties.put(property,value);
        globalProperties.put(serverId,serverProperties);
    }

    private ServerPropertiesManager(){
        serverProperties = new HashMap<>();
        globalProperties = new HashMap<>();
        createPropertiesList();
    }

    public List<String> getPropertiesList(){ return propertiesList; }

    public void setPropertiesForServer(String serverId,Map<String,String> properties){
        globalProperties.put(serverId,properties);
    }

    public Map<String,String> getPropertiesFromServer(String serverId){
        return globalProperties.get(serverId);
    }

    public String getPropertyFromServer(String serverId,String property){
        return globalProperties.get(serverId).get(property);
    }
}