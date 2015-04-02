package myWeb;

import javax.websocket.Endpoint;
import javax.websocket.server.ServerApplicationConfig;
import javax.websocket.server.ServerEndpointConfig;
import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

public class ChatRoomWebSocketConfig implements ServerApplicationConfig {

    @Override
    public Set<ServerEndpointConfig> getEndpointConfigs(Set<Class<? extends Endpoint>> endpointClasses) {
        Set<ServerEndpointConfig> result = new HashSet<>();

        if (endpointClasses.contains(ChatRoomEndpoint.class)) {
            String uriTemplate = String.format("/{%s}", Constants.ROOM_NAME);
            result.add(ServerEndpointConfig.Builder.create(
                ChatRoomEndpoint.class, uriTemplate).
                subprotocols(Collections.EMPTY_LIST).       // this websocket does not support sub protocols
                configurator(ChatEndpointConfigurator.get()).build());
        }

        return result;
    }

    @Override
    public Set<Class<?>> getAnnotatedEndpointClasses(Set<Class<?>> scanned) {
//        Set<Class<?>> results = new HashSet<Class<?>>();
//        return results;
        return null;
    }
}
