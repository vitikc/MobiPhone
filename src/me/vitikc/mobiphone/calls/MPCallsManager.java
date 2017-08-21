package me.vitikc.mobiphone.calls;

import me.vitikc.mobiphone.MPMain;
import org.bukkit.Bukkit;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;

public class MPCallsManager {
    //TODO: Remove incoming call if don't get response for 10-20 seconds
    private HashSet<MPCall<String, String>> activeCalls;
    private ArrayList<MPCall<String, String>> incomingCalls;

    public MPCallsManager(){
        activeCalls = new HashSet<>();
        incomingCalls = new ArrayList<>();
    }


    public HashSet<MPCall<String, String>> getActiveCalls() {
        return activeCalls;
    }

    public ArrayList<MPCall<String, String>> getIncomingCalls() {
        return incomingCalls;
    }


    public boolean hasIncoming(String receiver){
        for (int i = 0; i < incomingCalls.size(); i++){
            if (incomingCalls.get(i).getValue().equalsIgnoreCase(receiver)){
                return true;
            }
        }
        return false;
    }

    public boolean contains(String s){
        return containsKey(s)||containsValue(s);
    }

    public boolean containsKey(String k){
        for (MPCall<String, String> call : activeCalls) {
            if (call.getKey().equalsIgnoreCase(k))
                return true;
        }
        return false;
    }

    public boolean containsValue(String v){
        for (MPCall<String, String> call : activeCalls) {
            if (call.getValue().equalsIgnoreCase(v))
                return true;
        }
        return false;
    }

    public String getCaller(String receiver){
        String s = "";
        for (MPCall<String, String> call : activeCalls) {
            if (call.getValue().equalsIgnoreCase(receiver))
                s = call.getKey();
        }
        return s;
    }

    public String getReceiver(String caller){
        String s = "";
        for (MPCall<String, String> call : activeCalls) {
            if (call.getKey().equalsIgnoreCase(caller))
                s = call.getValue();
        }
        return s;
    }

    public String getIncomingFrom(String receiver){
        String from = "";
        for (int i = 0; i < incomingCalls.size(); i++){
            if (incomingCalls.get(i).getValue().equalsIgnoreCase(receiver)){
                from = incomingCalls.get(i).getKey();
            }
        }
        return from;
    }

    public void removeIncoming(String s){
        int index = -1;
        for (int i = 0; i < incomingCalls.size(); i++){
            if (incomingCalls.get(i).getValue().equalsIgnoreCase(s)||
                    incomingCalls.get(i).getKey().equalsIgnoreCase(s)){
                index = i;
            }
        }
        if (index>-1)
            incomingCalls.remove(index);
    }

    public void remove(String s){
        MPCall<String, String> toRemove = null;
        for (MPCall<String, String> call : activeCalls){
            if (call.getKey().equalsIgnoreCase(s)||
                    call.getValue().equalsIgnoreCase(s)){
                toRemove = call;
            }
        }
        if (toRemove != null)
            activeCalls.remove(toRemove);
    }

    public void waitToRemove(final String toRemove){
        int id = new BukkitRunnable() {
            @Override
            public void run() {
                if (hasIncoming(toRemove)){
                    String caller = MPMain.getInstance().getPhonesManager().getPlayer(getIncomingFrom(toRemove));
                    Bukkit.getPlayer(caller).sendMessage(toRemove + " not responded too long");
                    removeIncoming(toRemove);
                }
            }
        }.runTaskLater(MPMain.getInstance(),30*20l).getTaskId();
    }
}
