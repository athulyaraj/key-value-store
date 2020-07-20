package com.keyvaluestore.app;

import com.keyvaluestore.app.controller.KeyValueStoreController;
import com.keyvaluestore.app.controller.NodeController;
import com.keyvaluestore.app.service.Cluster;
import org.glassfish.jersey.server.ResourceConfig;

import javax.ws.rs.ApplicationPath;
import java.util.HashSet;
import java.util.Set;

@ApplicationPath("/")
public class KeyValueStoreApplication  extends ResourceConfig {

    private Set<Object> singletons = new HashSet<Object>();

    public KeyValueStoreApplication() {
        Cluster cluster = new Cluster();
        packages("com.keyvaluestore.app.controller");
        register(new KeyValueStoreController(cluster));
        register(new NodeController(cluster));
        property("jersey.config.server.provider.classnames",
                "org.glassfish.jersey.jackson.JacksonFeature");
    }
}
