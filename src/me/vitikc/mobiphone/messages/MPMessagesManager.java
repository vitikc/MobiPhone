package me.vitikc.mobiphone.messages;

import me.vitikc.mobiphone.MPMain;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;
import java.io.IOException;
import java.util.*;

public class MPMessagesManager{
    private File messagesFile;
    private YamlConfiguration messages;

    public MPMessagesManager(){
        messagesFile = new File(MPMain.getInstance().getDataFolder(), "messages.yml");
        messages = YamlConfiguration.loadConfiguration(messagesFile);
        if (!messagesFile.exists())
            saveMessages();
    }

    private void saveMessages(){
        try {
            messages.save(messagesFile);
        } catch (IOException e) {
            MPMain.getInstance().getLogger().info("Can't save messages.yml");
            e.printStackTrace();
        }
    }

    public void sendMessage(UUID holder, String to, String msg){
        int id = 0;
        if (messages.isSet(holder.toString() + ".sent")){
            ConfigurationSection section = messages.getConfigurationSection(holder.toString() + ".sent");
            ArrayList<String> list = new ArrayList<>();
            list.addAll(section.getKeys(false));
            id = list.size();
        }
        if (id > 10) {
            id = 0;
            messages.set(holder.toString() + ".sent", null);
        }
        messages.set(holder.toString() + ".sent." + id + ".to", to);
        messages.set(holder.toString() + ".sent." + id + ".msg", msg);
        saveMessages();
    }

    public void receiveMessage(UUID holder, String from, String msg){
        int id = 0;
        if (messages.isSet(holder.toString() + ".received")){
            ConfigurationSection section = messages.getConfigurationSection(holder.toString() + ".received");
            ArrayList<String> list = new ArrayList<>();
            list.addAll(section.getKeys(false));
            id = list.size();
        }
        if (id > 10) {
            id = 0;
            messages.set(holder.toString() + ".received", null);
        }
        messages.set(holder.toString() + ".received." + id + ".from", from);
        messages.set(holder.toString() + ".received." + id + ".msg", msg);
        saveMessages();
    }

    public String getMessages(UUID holder){
        StringBuilder sb = new StringBuilder();
        if (!messages.isSet(holder.toString())){
            return "No messages found";
        }
        ConfigurationSection section = messages.getConfigurationSection(holder.toString() + ".sent");
        for(String s: section.getKeys(false)){
            int id = Integer.parseInt(s);
            sb.append("->");
            sb.append(getSentMessageById(holder, id));
            sb.append("\n");
        }
        section = messages.getConfigurationSection(holder.toString() + ".received");
        for(String s: section.getKeys(false)){
            int id = Integer.parseInt(s);
            sb.append("<-");
            sb.append(getReceivedMessageById(holder, id));
            sb.append("\n");
        }
        if (sb.length()<=2){
            return "No messages found";
        }
        return sb.toString();
    }

    public String getSentMessageById(UUID holder, int id){
        String path = holder.toString() + ".sent." + id;
        if (!messages.isSet(path + ".to") || !messages.isSet(path + ".msg")){
            return "";
        }
        String to =  messages.getString(path + ".to");
        String msg = messages.getString(path + ".msg");
        return to + " : " + msg;
    }
    public String getReceivedMessageById(UUID holder, int id){
        String path = holder.toString() + ".received." + id;
        if (!messages.isSet(path + ".from") || !messages.isSet(path + ".msg")){
            return "";
        }
        String from =  messages.getString(path + ".from");
        String msg = messages.getString(path + ".msg");
        return from + " : " + msg;
    }
}
