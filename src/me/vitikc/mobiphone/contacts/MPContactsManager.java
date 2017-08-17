package me.vitikc.mobiphone.contacts;


import me.vitikc.mobiphone.MPMain;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.UUID;

public class MPContactsManager {
    //Better direct get from yml than storing so big HashMap
    private File contactsFile;
    private YamlConfiguration contacts;

    public MPContactsManager(){
        contactsFile = new File(MPMain.getInstance().getDataFolder(), "contacts.yml");
        contacts = YamlConfiguration.loadConfiguration(contactsFile);
        if (!contactsFile.exists())
            saveContacts();
    }

    public void addContact(UUID holder, String name, String number){
        contacts.set(holder.toString() + "." + name, number);
        saveContacts();
    }

    public String getNumber(UUID holder, String name){
        return (String) contacts.get(holder.toString() + "." + name);
    }

    public String getContacts(UUID holder){
        StringBuilder sb = new StringBuilder();
        sb.append("[");
        if (!contacts.isSet(holder.toString())){
            return "No contacts found";
        }
        ConfigurationSection section = contacts.getConfigurationSection(holder.toString());
        for(String s : section.getKeys(false)){
            sb.append(s).append(", ");
        }
        sb.replace(sb.length()-2, sb.length(), "]");
        if (sb.length()<=2){
            return "No contacts found";
        }
        return sb.toString();
    }

    public void removeContact(UUID holder, String name){
        contacts.set(holder.toString() + "." + name, null);
        saveContacts();
    }

    public boolean isContainsContact(UUID holder, String name){
        return contacts.isSet(holder.toString() + "." + name);
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
