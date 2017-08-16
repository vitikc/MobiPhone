package me.vitikc.mobiphone;

import me.vitikc.mobiphone.commands.MPPhoneCommand;
import me.vitikc.mobiphone.listeners.MPChatListener;
import org.bukkit.plugin.java.JavaPlugin;

public class MPMain extends JavaPlugin {
    private static MPMain instance;


    public void onEnable(){
        instance = this;
        registerCommands();
        registerListeners();
    }

    public void onDisable(){
        instance = null;
    }

    public static MPMain getInstance(){
        return instance;
    }

    private void registerListeners(){
        getServer().getPluginManager().registerEvents(new MPChatListener(), this);
    }

    private void registerCommands(){
        getServer().getPluginCommand("phone").setExecutor(new MPPhoneCommand());
    }

}
