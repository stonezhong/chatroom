package net.franklychat.model;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class ChatRoom {
    
    private final String name;
    
    public ChatRoom(String name) {
        // TODO: check: name should not be null
        this.name = name;
    }
    
    
    private final Map<ClientEndpoint, Client> clients = new HashMap<>();

    /**
     * Add a client to this chatroom
     * @param client
     */
    public void addClient(Client client) {
        // TODO: check: client should not be null

        List<Client> audiences = null;
        synchronized (this) {
            audiences = getClients();

            clients.put(client.getClientEndpoint(), client);
            client.setChatRoom(this);
        }
        
        String message = String.format("%s:%d joined the party!", client.getClientEndpoint().getAddress(), client.getClientEndpoint().getPort());
        broadcast(audiences, message);
    }

    /**
     * Remove a client from this chatroom
     * @param client
     */
    public void removeClient(Client client) {
        // TODO: check: client should not be null and should already be in the chatroom
        Engine engine = Engine.getEngine();
        
        List<Client> audiences = null;
        
        synchronized (engine) {
            synchronized (this) {
                client.setChatRoom(null);
                clients.remove(client.getClientEndpoint());

                if (clients.size() == 0) {
                    // we should remove the chatroom if it becomes empty
                    engine.removeChatRoom(this.name);
                    return;
                }

                audiences = getClients();
            }
        }
        
        String message = String.format("%s:%d passed out...", client.getClientEndpoint().getAddress(), client.getClientEndpoint().getPort());
        broadcast(audiences, message);
    }
    
    // broadcast the message to all clients (except the client in the paramter)
    public void broadcast(Client client, String message) {
        // TODO: check: client should not be null and should already be in the chatroom
        
        List<Client> audiences = new LinkedList<>();
        synchronized (this) {
            // send message to client could be slow, we only want to lock the room and get all the clients, then release the lock and send message
            for (Map.Entry<ClientEndpoint, Client> entry : clients.entrySet()) {
                Client currentClient = entry.getValue();
                if (currentClient.getClientEndpoint().equals(client.getClientEndpoint())) {
                    continue;
                }
                audiences.add(entry.getValue());
            }
        }
        
        for (Client audience : audiences) {
            audience.sendText(message);
        }
    }

    public void broadcast(List<Client> audiences, String message) {
        for (Client audience : audiences) {
            audience.sendText(message);
        }
    }
    
    private List<Client> getClients() {
        List<Client> ret = new LinkedList<>();

        for (Map.Entry<ClientEndpoint, Client> entry : clients.entrySet()) {
            ret.add(entry.getValue());
        }
        
        return ret;
    }
    
    
}
