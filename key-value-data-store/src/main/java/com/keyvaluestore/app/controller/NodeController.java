package com.keyvaluestore.app.controller;

import com.keyvaluestore.app.service.Cluster;

import javax.inject.Singleton;
import javax.naming.OperationNotSupportedException;
import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

@Path("/key-value-store/node")
@Singleton
public class NodeController {
    Cluster cluster;

    public NodeController(Cluster cluster){
        this.cluster = cluster;
    }

    @GET
    @Path("/{nodeIndex}/stop")
    @Produces(MediaType.APPLICATION_JSON)
    public Response stopNode(@PathParam("nodeIndex")Integer nodeIndex){
        try{
            cluster.shutDown(nodeIndex);
            return Response.ok().build();
        }catch (OperationNotSupportedException e){
            return Response.serverError()
                    .entity(com.keyvaluestore.models.Response.builder()
                            .errorCode(500)
                            .errorReason(e.getMessage()).build())
                    .build();
        }
    }

    @GET
    @Path("/{nodeIndex}/resume")
    public Response resumeNode(@PathParam("nodeIndex")Integer nodeIndex){
        cluster.restart(nodeIndex);
        return Response.ok().build();
    }

    @GET
    public Response increaseClusterSize(@QueryParam("nodeCount")Integer nodeCount){
        cluster.increaseClustorSize(nodeCount);
        return Response.ok().build();
    }

}