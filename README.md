# **Distributed Key-Value Store**

### Requirements:

* Maven-3.6.3
* JDK-1.8.0_251
* Tomcat-8.5.57


### Build and Deploy instructions:

* Move to key-value-store directory
* Run `mvn clean install -DskipTests`
* Copy key-value-store.war to Tomcat/webapps
* Start tomcat `./catalina.sh start`
* To stop tomcat `./catalina.sh stop`
* To run commands, use key-value-store.jar `java -Dhost=localhost -Dport=8080 -jar key-value-store.jar`

### Supported Commands
* GET **_key_** - to get the value for a key
* SET **_key_** **_value_** - to set a value for a perticular key
* EXPIRE **_key_** - to expire a key
* SCALE **_node_count_** - to add additional nodes
* STOP **_node_index_** - to stop a node. default count is 3. so the index can go from 1-3
* RESUME **_node_index_** - to resume node.

### Demo

https://recordit.co/gtuJaAKbFb