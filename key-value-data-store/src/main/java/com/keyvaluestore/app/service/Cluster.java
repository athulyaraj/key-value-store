package com.keyvaluestore.app.service;

import com.keyvaluestore.models.Entry;
import com.keyvaluestore.utils.CommonUtils;
import lombok.extern.java.Log;

import javax.naming.OperationNotSupportedException;
import java.util.ArrayList;
import java.util.List;

@Log
public class Cluster {

    List<Node> nodes;
    public static final int MIN_ACTIVE_NODES_COUNT = 2;

    public Cluster(){
        nodes = new ArrayList<>();
        for(int index = 0; index < 3 ; index ++){
            Node node = new Node();
            nodes.add(node);
        }
    }

    public Entry get(String key){
        Node node = nodes.stream().filter(i -> i.isActive()).findFirst().get();
        return node.get(key);
    }


    public void set(Entry entry){
        validateEntry(entry);
        for(Node node : nodes){
            if(node.isActive()){
                node.set(entry);
            }
        }
    }

    public void expire(String key){
        for(Node node : nodes){
            if(node.isActive()){
                node.expire(key);
            }
        }
    }

    public void shutDown(int nodeIndex) throws OperationNotSupportedException {
        validateIndex(nodeIndex);
        isRequiredNodesPresent();
        Node node = nodes.get(nodeIndex-1);
        node.clear();
        node.setActive(false);
    }

    public void restart(int nodeIndex){
        validateIndex(nodeIndex);
        Node node = nodes.get(nodeIndex);
        Node activeNode = nodes.stream().filter(i -> i.isActive()).findFirst().get();
        activeNode.copyContentsTo(node);
        node.setActive(true);
    }

    public void increaseClustorSize(int nodeCount){
        if(nodeCount <= 0){
            log.info("Invalid node count");
            throw new IllegalArgumentException("Invalid node count");
        }
        Node activeNode = nodes.stream().filter(i -> i.isActive()).findFirst().get();
        for(int index = 0;index < nodeCount ; index++){
            Node node = new Node();
            activeNode.copyContentsTo(node);
            nodes.add(node);
        }
        log.info("cluster size increased. current size = " + nodes.size());
    }

    private void validateIndex(int nodeIndex){
        if(nodeIndex > nodes.size()){
            log.info("nodeIndex is out of permissible value");
            throw new IllegalArgumentException();
        }
    }

    private void validateActive(Node node) throws OperationNotSupportedException {
        if(!node.isActive()){
            log.info("node is not active");
            throw new OperationNotSupportedException();
        }
    }

    private void isRequiredNodesPresent() throws OperationNotSupportedException {
        long count = nodes.stream().filter(i -> i.isActive()).count();
        if(!(count > MIN_ACTIVE_NODES_COUNT)){
            log.info("Active node count is low");
            throw new OperationNotSupportedException("Active node count is low");
        }
    }

    private void validateEntry(Entry entry){
        if(CommonUtils.isEmpty(entry.getKey()) || CommonUtils.isEmpty(entry.getValue())){
            log.info("key/value is not present in entry");
            throw new IllegalArgumentException();
        }
    }
}
