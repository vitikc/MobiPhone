package me.vitikc.mobiphone.listeners;

import me.vitikc.mobiphone.MPMain;
import me.vitikc.mobiphone.MPPhonesManager;
import me.vitikc.mobiphone.calls.MPCallsManager;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.AsyncPlayerChatEvent;

public class MPChatListener implements Listener {

    MPCallsManager callsManager = MPMain.getInstance().getCallsManager();
    MPPhonesManager pm = MPMain.getInstance().getPhonesManager();
    @EventHandler
    public void onAsyncPlayerChatEvent(AsyncPlayerChatEvent event){
        Player player = event.getPlayer();
        String playerNumber = pm.getNumber(player.getName());
        if (!callsManager.contains(playerNumber)){
            player.sendMessage("Allowed only phone calls!");
            event.setCancelled(true);
            return;
        }
        String receiverNumber = "";
        if (callsManager.containsKey(playerNumber)){
            receiverNumber = callsManager.getReceiver(playerNumber);
        } else if (callsManager.containsValue(playerNumber)){
            receiverNumber = callsManager.getCaller(playerNumber);
        }
        Player receiver = Bukkit.getPlayer(pm.getPlayer(receiverNumber));
        player.sendMessage("->" + receiverNumber + ": " + event.getMessage());
        receiver.sendMessage("<-" + playerNumber + ": " + event.getMessage());
        event.setCancelled(true);
    }

}
