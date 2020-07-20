package com.keyvaluestore.app.controller;

import com.keyvaluestore.app.service.Cluster;
import com.keyvaluestore.models.Entry;

import javax.inject.Singleton;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/key-value-store")
@Singleton
public class KeyValueStoreController {
    Cluster cluster;

    public KeyValueStoreController(Cluster cluster){
        this.cluster = cluster;
    }

    @GET
    @Path("/get")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getKey(@QueryParam("key") String key) {
        com.keyvaluestore.models.Response response;
        try{
            Entry entry = cluster.get(key);
            response = com.keyvaluestore.models.Response.builder().data(entry).statusCode(200).build();
            return Response.status(200).entity(response).build();
        }catch(Exception e){
            response = com.keyvaluestore.models.Response.builder()
                    .errorCode(400).errorReason(e.getMessage()).build();
            return Response.status(500).entity(response).build();
        }

    }

    @POST
    @Path("/set")
    @Consumes(MediaType.APPLICATION_JSON)
    @Produces(MediaType.APPLICATION_JSON)
    public Response setKey(Entry entry){
        cluster.set(entry);
        return Response.ok().build();
    }

    @GET
    @Path("/expire")
    @Produces(MediaType.APPLICATION_JSON)
    public Response expireKey(@QueryParam("key") String key){
        cluster.expire(key);
        return Response.ok().build();
    }
}
