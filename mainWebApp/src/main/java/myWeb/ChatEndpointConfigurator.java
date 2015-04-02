package myWeb;

import net.franklychat.model.ClientEndpoint;

import javax.servlet.http.HttpSession;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerEndpointConfig;

class ChatEndpointConfigurator extends ServerEndpointConfig.Configurator {

    public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
        super.modifyHandshake(sec, request, response);

        ClientEndpoint clientEndpoint = (ClientEndpoint)((HttpSession)request.getHttpSession()).getAttribute(Constants.CLIENT_ENDPOINT_KEY);
        sec.getUserProperties().put(Constants.CLIENT_ENDPOINT_KEY, clientEndpoint);

    }

    private static final ChatEndpointConfigurator me = new ChatEndpointConfigurator();
    public static ServerEndpointConfig.Configurator get() {
        return me;
    }

}
