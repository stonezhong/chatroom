package myWeb;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;
import java.util.HashSet;
import java.util.Set;

public class HelloConfig implements ServerApplicationConfig {

    @Override
    public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> endpointClasses) {
//        Set<ServerEndpointConfig> result = new HashSet<ServerEndpointConfig>();
//        
//        if (endpointClasses.contains(ChatRoomEndpoint.class)) {
//            result.add(ServerEndpointConfig.Builder.create(
//                ChatRoomEndpoint.class, "/websocket/hello").build());
//        }
//
//        return result;
        return null;
    }

    @Override
    public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scanned) {
//        Set<Class<?>> results = new HashSet<Class<?>>();
//        return results;
        return null;
    }
}
