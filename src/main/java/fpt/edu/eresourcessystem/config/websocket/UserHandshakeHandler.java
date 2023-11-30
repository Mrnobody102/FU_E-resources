//package fpt.edu.eresourcessystem.config.websocket;
//
//import com.sun.security.auth.UserPrincipal;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.http.server.ServerHttpRequest;
//import org.springframework.http.server.ServletServerHttpRequest;
//import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
//import org.springframework.web.socket.WebSocketHandler;
//import org.springframework.web.socket.server.support.DefaultHandshakeHandler;
//
//import java.security.Principal;
//import java.util.Map;
//import java.util.UUID;
//
//public class UserHandshakeHandler extends DefaultHandshakeHandler {
//    private final Logger LOG = LoggerFactory.getLogger((UserHandshakeHandler.class));
//
//    @Override
//    protected Principal determineUser(ServerHttpRequest request, WebSocketHandler wsHandler, Map<String, Object> attributes) {
////        final String randomId = UUID.randomUUID().toString();
////        LOG.info("User with ID '{}' opened the page", randomId);
////        return new UserPrincipal(randomId);
//        String name  = (String)attributes.get("name");
//        System.out.println(name);
//        //Return the User
//        return new UsernamePasswordAuthenticationToken(name, null);
//
//    }
//}

