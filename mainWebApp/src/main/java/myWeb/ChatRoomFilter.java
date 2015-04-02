package myWeb;

import net.franklychat.model.ClientEndpoint;
import org.apache.log4j.Logger;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

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

    // hack, using a Tomcat 7 internal API to check if this is a websocket upgrade request
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

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        
        if (!isWebSocketUpgradeRequest(request, response)) {
            // not a websocket request, pass it through
            chain.doFilter(request, response);
            return ;
        }

        // put the client ip and port into session's "clientEndpoint"
        ClientEndpoint clientEndpoint = new ClientEndpoint(request.getRemoteAddr(), request.getRemotePort());
        ((HttpServletRequest) request).getSession().setAttribute(Constants.CLIENT_ENDPOINT_KEY, clientEndpoint);

        chain.doFilter(request, response);
    }
    
}
