package me.vitikc.mobiphone.contacts;

import me.vitikc.mobiphone.MPMain;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class MPContactsManager {
    private File contactsFile;
    private YamlConfiguration contacts;

    private HashMap<UUID, HashMap<String,String>> serverContacts;

    public MPContactsManager(){
        contactsFile = new File(MPMain.getInstance().getDataFolder(), "contacts.yml");
        contacts = YamlConfiguration.loadConfiguration(contactsFile);
        serverContacts = new HashMap<>();
        if (!contactsFile.exists())
            saveContacts();
    }

    public void addContact(UUID holder, String name, String number){
        if (!serverContacts.containsKey(holder)){
            serverContacts.put(holder, new HashMap<String, String>());
        }
        serverContacts.get(holder).put(name,number);
        contacts.set(holder.toString() + "." + name, number);
        saveContacts();
    }

    public String getNumber(UUID holder, String name){
        return serverContacts.get(holder).get(name);
    }

    public String getContact(UUID holder, String number){
        for (Map.Entry k : serverContacts.get(holder).entrySet()){
            if (number.equals(k.getValue())){
                return (String)k.getKey();
            }
        }
        return null;
    }

    public String getContacts(UUID holder){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (!serverContacts.containsKey(holder)){
            return "No contacts found";
        }
        for(String s : serverContacts.get(holder).keySet()){
            sb.append(s).append(", ");
        }
        sb.replace(sb.length()-2, sb.length(), "]");
        if (sb.length()<=2){
            return "No contacts found";
        }
        return sb.toString();
    }

    public void removeContact(UUID holder, String name){
        serverContacts.get(holder).remove(name);
        contacts.set(holder.toString() + "." + name, null);
        saveContacts();
    }

    public boolean isContainsContactName(UUID holder, String name){
        if (!serverContacts.containsKey(holder)) return false;
        return serverContacts.get(holder).containsKey(name);
    }

    public boolean isContainsContactNumber(UUID holder, String number){
        if (!serverContacts.containsKey(holder)) return false;
        return serverContacts.get(holder).containsValue(number);
    }

    public boolean isValidNumber(String number){
        return number.matches("\\d{4,8}");
    }

    private void saveContacts(){
        try {
            contacts.save(contactsFile);
        } catch (IOException e) {
            MPMain.getInstance().getLogger().info("Can't save contacts.yml");
            e.printStackTrace();
        }
    }
}
