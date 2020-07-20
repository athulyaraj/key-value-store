package com.keyvaluestore.cli;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.keyvaluestore.cli.constants.Constants;
import com.keyvaluestore.cli.utils.Client;
import com.keyvaluestore.models.Entry;
import com.keyvaluestore.models.Response;
import com.keyvaluestore.utils.CommonUtils;
import lombok.extern.java.Log;

import java.io.*;
import java.net.URL;
import java.util.Arrays;
import java.util.List;

@Log
public class KeyValueStoreClient {

    public static void main(String... args){
        String serviceUrl = System.getProperty(Constants.KEY_VALUE_STORE_SERVICE_URL);
        String port = System.getProperty(Constants.KEY_VALUE_STORE_SERVICE_PORT);
        getFileFromResources("/banner.txt");
        if(CommonUtils.isEmpty(serviceUrl) || CommonUtils.isEmpty(port)){
            log.info("No service url/port specified.Please re-run with -Dhost={hostname} -Dport={portNo}");
            return;
        }
        StringBuffer url = new StringBuffer("http://").append(serviceUrl).append(":").append(port);
        String command = "";
        BufferedReader reader =
                new BufferedReader(new InputStreamReader(System.in));
        do{
            log.info(">");
            try{
                command = reader.readLine();
                perform(command,url.toString());
            }catch (Exception ex){
                System.out.println(ex);
            }
        }while(!command.equalsIgnoreCase("quit"));

    }

    private static void perform(String command,String url){
        List<String> commandParams = Arrays.asList(command.split("\\s+"));
        switch(commandParams.get(0)){
            case "GET"                  : getKey(commandParams,url);
                break;
            case "SET"                  : setKey(commandParams,url);
                break;
            case "EXPIRE"               : expire(commandParams,url);
                break;
            case "STOP"                 : stop(commandParams,url);
                break;
            case "RESUME"               : resume(commandParams,url);
                break;
            case "SCALE"                : scale(commandParams,url);
                break;
            default                     : break;
        }
    }

    public static void getKey(List<String> params,String url){
        StringBuffer urlBuilder = new StringBuffer(url);
        urlBuilder.append("/key-value-store").append("/get?key=").append(params.get(1));
        processRequest(params,urlBuilder.toString());
    }

    public static void setKey(List<String> params,String url){
        StringBuffer urlBuilder = new StringBuffer(url);
        urlBuilder.append("/key-value-store").append("/set");
        Entry entry = Entry.builder()
                .key(params.get(1))
                .value(params.get(2))
                .build();
        Client client = new Client();
        boolean isPersisted = client.post(urlBuilder.toString(),entry);
        if(!isPersisted){
            log.info("ERROR :: Unable to process the request");
            return;
        }
        log.info("OK");
    }

    public static void expire(List<String> params,String url){
        StringBuffer urlBuilder = new StringBuffer(url);
        urlBuilder.append("/key-value-store").append("/expire?key=").append(params.get(1));
        processRequest(params,urlBuilder.toString());
    }

    public static void stop(List<String> params,String url){
        StringBuffer urlBuilder = new StringBuffer(url);
        urlBuilder.append("/key-value-store/node/").append(params.get(1)).append("/stop");
        processRequest(params,urlBuilder.toString());
    }

    public static void resume(List<String> params,String url){
        StringBuffer urlBuilder = new StringBuffer(url);
        urlBuilder.append("/key-value-store/node/").append(params.get(1)).append("/resume");
        processRequest(params,urlBuilder.toString());
    }

    public static void scale(List<String> params,String url){
        StringBuffer urlBuilder = new StringBuffer(url);
        urlBuilder.append("/key-value-store/node?nodeCount=").append(params.get(1));
        processRequest(params,urlBuilder.toString());
    }

    public static void processRequest(List<String> params,String url){
        Client client = new Client();
        String response = client.get(url);
        if(!CommonUtils.isEmpty(response)){
            try {
                Response r = new ObjectMapper().readValue(response,Response.class);
                if(r.getData() != null && r.getData() instanceof Entry){
                    log.info(((Entry)r.getData()).getValue());
                    return;
                }else{
                    log.info("ERROR :: " + r.getErrorReason());
                    return;
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        log.info("OK");
    }

    private static void  getFileFromResources(String fileName){
        try {
            InputStream is = KeyValueStoreClient.class.getResourceAsStream(fileName);
            BufferedReader reader = new BufferedReader(new InputStreamReader(is));
            String line;
            StringBuffer banner = new StringBuffer("\n");
            while ((line = reader.readLine()) != null) {
                banner.append(line).append("\n");
            }
            log.info(banner.toString());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private static void printFile(File file) throws IOException {

        if (file == null) return;

        try (FileReader reader = new FileReader(file);
             BufferedReader br = new BufferedReader(reader)) {

            String line;
            while ((line = br.readLine()) != null) {
                log.info(line);
            }
        }
    }

}
