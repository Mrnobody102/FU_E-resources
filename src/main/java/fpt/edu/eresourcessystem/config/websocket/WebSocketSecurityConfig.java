package fpt.edu.eresourcessystem.config.websocket;

import fpt.edu.eresourcessystem.enums.AccountEnum;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.messaging.Message;
import org.springframework.messaging.simp.config.MessageBrokerRegistry;
import org.springframework.security.authorization.AuthorizationManager;
import org.springframework.security.config.annotation.web.socket.EnableWebSocketSecurity;
import org.springframework.security.messaging.access.intercept.MessageMatcherDelegatingAuthorizationManager;
import org.springframework.web.socket.config.annotation.*;

import static org.springframework.messaging.simp.SimpMessageType.MESSAGE;
import static org.springframework.messaging.simp.SimpMessageType.SUBSCRIBE;

@Configuration
@EnableWebSocketSecurity
public class WebSocketSecurityConfig{

    @Bean
    public AuthorizationManager<Message<?>> messageAuthorizationManager(MessageMatcherDelegatingAuthorizationManager.Builder messages) {
        messages
                .nullDestMatcher().authenticated()
                .simpSubscribeDestMatchers("/user/queue/errors").permitAll()
                .simpDestMatchers("/app/**").authenticated()
                .simpSubscribeDestMatchers("/admin/**").hasRole(AccountEnum.Role.ADMIN.name())
                .simpSubscribeDestMatchers("/librarian/**").hasRole(AccountEnum.Role.LIBRARIAN.name())
                .simpSubscribeDestMatchers("/lecturer/**").hasRole(AccountEnum.Role.LECTURER.name())
                .simpSubscribeDestMatchers("/student/**").hasRole(AccountEnum.Role.STUDENT.name())
                .simpTypeMatchers(MESSAGE, SUBSCRIBE).denyAll()
                .anyMessage().denyAll();
        return messages.build();
    }
}
