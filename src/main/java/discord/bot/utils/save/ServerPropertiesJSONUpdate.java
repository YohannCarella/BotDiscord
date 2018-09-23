package discord.bot.utils.save;

import java.io.*;
import java.util.Properties;

public class ServerPropertiesJSONUpdate {

    private String autoRole;
    private String userEventEnabled;
    private String userEventChannel;
    private String volume;
    private String loop;

    public ServerPropertiesJSONUpdate(String serverID) {
        try {
            //Opening properties file
            File configFile = new File("jackson-guild-"+serverID+".properties");
            if(configFile.createNewFile()){
                System.out.println("Fichier créé pour le serveur : " + serverID);
            }
            FileInputStream fileInput = new FileInputStream(configFile);
            Properties properties = new Properties();
            properties.load(fileInput);
            fileInput.close();
            initProperties(serverID);
            FileOutputStream fileOutput = new FileOutputStream(configFile);
            properties.setProperty("autoRole", autoRole);
            properties.setProperty("userEventEnabled", userEventEnabled);
            properties.setProperty("userEventChannel", userEventChannel);
            properties.setProperty("volume", volume);
            properties.setProperty("loop",loop);

            properties.store(fileOutput, null);

        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private void initProperties(String serverId){
        autoRole = ServerPropertiesManager.getInstance().getPropertyOrBlankFromServer(serverId,"autoRole");
        userEventEnabled = ServerPropertiesManager.getInstance().getPropertyOrBlankFromServer(serverId,"userEventEnabled");
        userEventChannel = ServerPropertiesManager.getInstance().getPropertyOrBlankFromServer(serverId,"userEventChannel");
        volume = ServerPropertiesManager.getInstance().getPropertyOrBlankFromServer(serverId,"volume");
        loop = ServerPropertiesManager.getInstance().getPropertyOrBlankFromServer(serverId,"loop");
    }
}