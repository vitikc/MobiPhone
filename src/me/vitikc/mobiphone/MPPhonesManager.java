package me.vitikc.mobiphone;

import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

public class MPPhonesManager {
    private File phonesFile;
    private YamlConfiguration phones;

    private HashMap<String, String> phoneAliases;

    public MPPhonesManager(){
        phonesFile = new File(MPMain.getInstance().getDataFolder(), "phones.yml");
        phones = YamlConfiguration.loadConfiguration(phonesFile);
        phoneAliases = new HashMap<>();
        if (!phonesFile.exists()){
            savePhones();
        }
        loadPhones();
    }

    public String getPlayer(String number){
        return phoneAliases.get(number);
    }

    public String getNumber(String player){
        for (Map.Entry k : phoneAliases.entrySet()){
            if (player.equals(k.getValue())){
                return (String)k.getKey();
            }
        }
        return "Not found";
    }

    public boolean isNumberExist(String number){
        return phoneAliases.containsKey(number);
    }

    public void addPlayer(String number, String player){
        phoneAliases.put(number, player);
        savePhones();
    }

    public boolean isRegistered(String player){
        return phoneAliases.values().contains(player);
    }

    public void  loadPhones(){
        for (String k : phones.getKeys(false)){
            phoneAliases.put(k, phones.getString(k));
        }
    }

    public void storePhones(){
        for (String p : phoneAliases.keySet()){
            phones.set(p, phoneAliases.get(p));
        }
        savePhones();
    }

    private void savePhones(){
        try {
            phones.save(phonesFile);
        } catch (IOException e) {
            MPMain.getInstance().getLogger().info("Can't save phones.yml");
            e.printStackTrace();
        }
    }
}
