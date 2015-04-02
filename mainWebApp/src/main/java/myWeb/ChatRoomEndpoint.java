package myWeb;

import net.franklychat.model.ChatRoom;
import net.franklychat.model.Client;
import net.franklychat.model.ClientEndpoint;
import net.franklychat.model.Engine;
import net.franklychat.model.MessengerProxy;

import javax.websocket.CloseReason;
import javax.websocket.Endpoint;
import javax.websocket.EndpointConfig;
import javax.websocket.MessageHandler;
import javax.websocket.RemoteEndpoint;
import javax.websocket.Session;
import java.io.IOException;

public class ChatRoomEndpoint extends Endpoint  {
    
    private Client client;

    @Override
    public void onOpen(Session session, EndpointConfig endpointConfig) {
        RemoteEndpoint.Basic remoteEndpointBasic = session.getBasicRemote();

        ClientEndpoint clientEndpoint = (ClientEndpoint)session.getUserProperties().get(Constants.CLIENT_ENDPOINT_KEY);

        String roomName = session.getRequestParameterMap().get(Constants.ROOM_NAME).get(0);
        Engine engine = Engine.getEngine();
        
        ChatRoom chatRoom = engine.getOrCreateChatRoom(roomName);
        ChatRoomMessageHandler messageHandler = new ChatRoomMessageHandler(remoteEndpointBasic);
        Client client = new Client(clientEndpoint, messageHandler);
        messageHandler.setClient(client);

        chatRoom.addClient(client);
        session.addMessageHandler(messageHandler);
        this.client = client;

    }

    public void onClose(Session session, CloseReason closeReason) {
        ChatRoom chatRoom = client.getChatRoom();
        chatRoom.removeClient(client);
    }


    private static class ChatRoomMessageHandler implements MessageHandler.Whole<String>, MessengerProxy {
        
        private Client client;
        private final RemoteEndpoint.Basic remoteEndpointBasic;
        
        private ChatRoomMessageHandler(RemoteEndpoint.Basic remoteEndpointBasic) {
            this.remoteEndpointBasic = remoteEndpointBasic;
        }
        
        public void setClient(Client client) {
            this.client = client;
        }
        
        @Override
        public void onMessage(String message) {
            if (remoteEndpointBasic != null) {
                client.say(message);
            }
        }


        @Override
        public void sendText(String message) {
            try {
                remoteEndpointBasic.sendText(message);
            } catch (IOException e) {
            }
        }
    }


}
