package com.keyvaluestore.app.service;

import com.keyvaluestore.models.Entry;
import com.keyvaluestore.utils.CommonUtils;
import lombok.extern.java.Log;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

@Log
public class Node {

    private boolean isActive;
    private ConcurrentHashMap<String, Entry> store;

    Node(){
        isActive = true;
        store = new ConcurrentHashMap<String, Entry>();
    }

    public boolean isActive(){
        return isActive;
    }

    public void setActive(boolean active){
        log.info("Setting node as " + active);
        isActive = active;
    }

    public Entry get(String key){
        Entry entry = store.getOrDefault(key,null);
        if(entry == null){
            log.info("Entry for key = " + key + " not found");
            return null;
        }
        if(CommonUtils.hasExpired(entry.getCreatedTime())){
            log.info("Entry for key = " + key + " has expired");
            return null;
        }
        log.info("Found value = " + entry.getValue() + " for key = "  + key);
        return entry;
    }

    public void set(Entry entry){
        log.info("Creating entry for key = " + entry.getKey());
        entry.setCreatedTime(System.currentTimeMillis());
        store.put(entry.getKey(),entry);
    }

    public void expire(String key){
        Entry entry = store.get(key);
        if(entry == null){
            log.info("No entry found for key = " + key);
            return;
        }
        log.info("Expiring entry for key = " + key);
        store.remove(key);
    }

    public void clear(){
        log.info("store clear requested");
        store.clear();
    }

    public void copyContentsTo(Node node){
        for(Map.Entry<String,Entry> entry : store.entrySet()){
            node.set(entry.getValue());
        }
    }

}

