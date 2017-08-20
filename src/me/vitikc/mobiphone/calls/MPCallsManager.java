package me.vitikc.mobiphone.calls;

import java.util.HashSet;
import java.util.Iterator;

public class MPCallsManager {
    private HashSet<MPCall<String, String>> activeCalls;

    public MPCallsManager(){
        activeCalls = new HashSet<>();
    }


    public HashSet<MPCall<String, String>> getActiveCalls() {
        return activeCalls;
    }

    public boolean contains(String s){
        Iterator<MPCall<String, String>> iterator = activeCalls.iterator();
        while (iterator.hasNext()){
            MPCall<String,String> call = iterator.next();
            if (call.getKey().equalsIgnoreCase(s)||call.getValue().equalsIgnoreCase(s))
                return true;
        }
        return false;
    }

    public boolean containsKey(String k){
        Iterator<MPCall<String, String>> iterator = activeCalls.iterator();
        while (iterator.hasNext()){
            MPCall<String,String> call = iterator.next();
            if (call.getKey().equalsIgnoreCase(k))
                return true;
        }
        return false;
    }

    public boolean containsValue(String v){
        Iterator<MPCall<String, String>> iterator = activeCalls.iterator();
        while (iterator.hasNext()){
            MPCall<String,String> call = iterator.next();
            if (call.getValue().equalsIgnoreCase(v))
                return true;
        }
        return false;
    }

    public void remove(String s){
        Iterator<MPCall<String, String>> iterator = activeCalls.iterator();
        while (iterator.hasNext()){
            MPCall<String,String> call = iterator.next();
            if (call.getKey().equalsIgnoreCase(s)||call.getValue().equalsIgnoreCase(s)) {
                iterator.remove();
                return;
            }
        }

    }

}
