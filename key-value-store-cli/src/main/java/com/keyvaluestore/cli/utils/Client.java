package com.keyvaluestore.cli.utils;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpEntity;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.io.UnsupportedEncodingException;

public class Client {

    private final CloseableHttpClient httpClient = HttpClients.createDefault();

    public String get(String url){
        HttpGet request = new HttpGet(url);
        request.addHeader("Content-Type", "application/json");
        request.addHeader("Accept", "application/json");
        try (CloseableHttpResponse response = httpClient.execute(request)) {
            if (response.getStatusLine().getStatusCode() != 200){
                System.out.println("Unable to connect to server");
                return null;
            }
            HttpEntity entity = response.getEntity();
            if(entity != null){
                return EntityUtils.toString(entity);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public boolean post(String url, Object entry) {
        try {
            HttpPost post = new HttpPost(url);
            ObjectMapper mapper = new ObjectMapper();
            HttpEntity httpEntity = new StringEntity(mapper.writeValueAsString(entry));
            post.setEntity(httpEntity);
            post.setHeader("Accept", "application/json");
            post.setHeader("Content-type", "application/json");
            try(CloseableHttpResponse response = httpClient.execute(post)){
                if(response.getStatusLine().getStatusCode() == 200)
                    return true;
            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return false;
    }
}
