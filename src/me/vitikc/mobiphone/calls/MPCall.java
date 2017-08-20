package me.vitikc.mobiphone.calls;

public class MPCall<K,V> {

    private final K key;
    private final V value;

    public MPCall(K key, V value) {
        this.key = key;
        this.value = value;
    }

    public V getValue() {
        return value;
    }

    public K getKey() {
        return key;
    }

    @Override
    public int hashCode(){
        return value.hashCode() * key.hashCode();
    }

    @Override
    public boolean equals(Object o){
        if (!(o instanceof MPCall)) return false;
        MPCall callo = (MPCall) o;
        return this.key.equals(callo.key) && this.value.equals(callo.value);
    }

    @Override
    public String toString(){
        return key + ":" + value;
    }

}
