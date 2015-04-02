package net.franklychat.model;

import java.util.HashMap;
import java.util.Map;

public class Engine {
    
    private final Map<String, ChatRoom> chatRoomByName = new HashMap<>();
    
    synchronized public ChatRoom getChatRoom(String name) {
        return chatRoomByName.get(name);
    }

    private static Engine me = new Engine();
    
    public static Engine getEngine() {
        return me;
    }
    
    synchronized public boolean hasChatroom(String name) {
        return chatRoomByName.containsKey(name);
    }
    
    synchronized public void addChatRoom(String name) {
        ChatRoom chatRoom = new ChatRoom(name);
        chatRoomByName.put(name, chatRoom);
    }
    
    synchronized public void removeChatRoom(String name) {
        chatRoomByName.remove(name);
    }
    
    synchronized public ChatRoom getOrCreateChatRoom(String name) {
        ChatRoom chatRoom = chatRoomByName.get(name);
        if (chatRoom == null) {
            chatRoom = new ChatRoom(name);
            chatRoomByName.put(name, chatRoom);
        }
        return chatRoom;
    }
    
    
}
