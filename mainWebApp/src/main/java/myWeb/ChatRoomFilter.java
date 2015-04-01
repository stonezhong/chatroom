package myWeb;

import net.franklychat.model.ChatRoom;
import net.franklychat.model.ClientEndpoint;
import net.franklychat.model.Engine;
import org.apache.log4j.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletContext;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import javax.websocket.DeploymentException;
import javax.websocket.HandshakeResponse;
import javax.websocket.server.HandshakeRequest;
import javax.websocket.server.ServerContainer;
import javax.websocket.server.ServerEndpointConfig;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Collections;

public class ChatRoomFilter implements Filter {

    private final static Logger LOGGER = Logger.getLogger(ChatRoomFilter.class);

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        LOGGER.info("init");
    }

    @Override
    public void destroy() {
        LOGGER.info("destroy");
    }

    private boolean isWebSocketUpgradeRequest(ServletRequest request, ServletResponse response) {
        try {
            Class<?> upgradeUtilClass = Class.forName("org.apache.tomcat.websocket.server.UpgradeUtil");
            Method method = upgradeUtilClass.getMethod("isWebSocketUpgradeRequest", ServletRequest.class, ServletResponse.class);
            Boolean ret = (Boolean)method.invoke(null, request, response);
            return ret;
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            return false;
        }
    }
    
    private static class ChatEndpointConfigurator extends ServerEndpointConfig.Configurator {

        public void modifyHandshake(ServerEndpointConfig sec, HandshakeRequest request, HandshakeResponse response) {
            super.modifyHandshake(sec, request, response);

            ClientEndpoint clientEndpoint = (ClientEndpoint)((HttpSession)request.getHttpSession()).getAttribute("clientEndpoint");
            sec.getUserProperties().put("clientEndpoint", clientEndpoint);

            ChatRoom chatRoom = (ChatRoom)((HttpSession)request.getHttpSession()).getAttribute("chatRoom");
            sec.getUserProperties().put("chatRoom", chatRoom);

        }
        
        private static final ChatEndpointConfigurator me = new ChatEndpointConfigurator();
        public static ServerEndpointConfig.Configurator get() {
            return me;
        }

    }
    
    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
        if (!isWebSocketUpgradeRequest(request, response)) {
            // not a websocket request, pass it through
            chain.doFilter(request, response);
            return ;
        }
        
        String servletPath = ((HttpServletRequest) request).getServletPath();
        String chatRoomName = servletPath.substring(1);

        ClientEndpoint clientEndpoint = new ClientEndpoint(request.getRemoteAddr(), request.getRemotePort());
        ((HttpServletRequest) request).getSession().setAttribute("clientEndpoint", clientEndpoint);

        Engine engine = Engine.getEngine();
        synchronized (engine) {

            if (!engine.hasChatroom(chatRoomName)) {
                try {
                    ServletContext servletContext = request.getServletContext();
                    ServerContainer serverContainer = (ServerContainer) servletContext.getAttribute("javax.websocket.server.ServerContainer");

                    ServerEndpointConfig serverEndpointConfig = ServerEndpointConfig.Builder.create(
                            ChatRoomEndpoint.class, servletPath).
                            subprotocols(Collections.EMPTY_LIST).       // this websocket does not support sub protocols
                            configurator(ChatEndpointConfigurator.get()).build();
                    serverContainer.addEndpoint(serverEndpointConfig);
                    engine.addChatRoom(chatRoomName);
                } catch (DeploymentException e) {
                }
            }
            ChatRoom chatRoom = engine.getChatRoom(chatRoomName);
            ((HttpServletRequest) request).getSession().setAttribute("chatRoom", chatRoom);
            
        }
        chain.doFilter(request, response);
    }
    
}
