package net.franklychat.model;

public class Client {
    
    private final MessengerProxy messengerProxy;
    private final ClientEndpoint clientEndpoint;

    public Client(ClientEndpoint clientEndpoint, MessengerProxy messengerProxy) {
        this.clientEndpoint = clientEndpoint;
        this.messengerProxy = messengerProxy;
    }

    public ClientEndpoint getClientEndpoint() {
        return clientEndpoint;
    }

    public void sendText(String message) {
        messengerProxy.sendText(message);
    }
    
    private ChatRoom chatRoom = null;
    public void setChatRoom(ChatRoom chatRoom) {
        this.chatRoom = chatRoom;
    }
    public ChatRoom getChatRoom() {
        return chatRoom;
    }
    
    public void say(String message) {
        if (chatRoom != null) {
            chatRoom.broadcast(this, message);
        }
    }
    
}
