package me.vitikc.mobiphone.listeners;

import me.vitikc.mobiphone.MPMain;
import me.vitikc.mobiphone.MPPhonesManager;
import me.vitikc.mobiphone.calls.MPCallsManager;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerQuitEvent;

public class MPPlayerLeftListener implements Listener{
    private MPPhonesManager pm = MPMain.getInstance().getPhonesManager();
    private MPCallsManager callsManager = MPMain.getInstance().getCallsManager();

    public void onPlayerLeft(PlayerQuitEvent event){
        Player p = event.getPlayer();
        String pNumber = pm.getNumber(p.getName());
        if (callsManager.contains(pNumber)){
            callsManager.remove(pNumber);
        }
    }
}
