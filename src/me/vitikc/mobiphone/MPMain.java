package me.vitikc.mobiphone;

import me.vitikc.mobiphone.calls.MPCallsManager;
import me.vitikc.mobiphone.commands.MPPhoneCommand;
import me.vitikc.mobiphone.contacts.MPContactsManager;
import me.vitikc.mobiphone.listeners.MPChatListener;
import me.vitikc.mobiphone.messages.MPMessagesManager;
import org.bukkit.plugin.java.JavaPlugin;

public class MPMain extends JavaPlugin {
    //TODO: phone command alias as mobi, mobile
    //TODO: admin permissions
    //TODO: use Vault economy API
    //TODO:

    private static MPMain instance;

    private MPCallsManager callsManager;
    private MPContactsManager contactsManager;
    private MPMessagesManager messagesManager;
    private MPPhonesManager phonesManager;


    public void onEnable(){
        instance = this;
        callsManager = new MPCallsManager();
        contactsManager = new MPContactsManager();
        messagesManager = new MPMessagesManager();
        phonesManager = new MPPhonesManager();
        registerCommands();
        registerListeners();
    }

    public void onDisable(){
        instance = null;
        phonesManager.storePhones();
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

    public MPCallsManager getCallsManager() {
        return callsManager;
    }

    public MPContactsManager getContactsManager() {
        return contactsManager;
    }

    public MPMessagesManager getMessagesManager() {
        return messagesManager;
    }

    public MPPhonesManager getPhonesManager() {
        return phonesManager;
    }
}
