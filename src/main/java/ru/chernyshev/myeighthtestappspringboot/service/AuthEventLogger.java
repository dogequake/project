package ru.chernyshev.myeighthtestappspringboot.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.event.EventListener;
import org.springframework.security.authentication.event.AbstractAuthenticationFailureEvent;
import org.springframework.security.authentication.event.AuthenticationSuccessEvent;
import org.springframework.stereotype.Component;

/** Пишет входы и неудачные попытки входа в logs/user-actions.log */
@Component
public class AuthEventLogger {

    private static final Logger actions = LoggerFactory.getLogger("user.actions");

    @EventListener
    public void onSuccess(AuthenticationSuccessEvent e) {
        actions.info("user={} action=LOGIN_SUCCESS", e.getAuthentication().getName());
    }

    @EventListener
    public void onFailure(AbstractAuthenticationFailureEvent e) {
        actions.warn("user={} action=LOGIN_FAILED reason={}",
                e.getAuthentication().getName(), e.getException().getClass().getSimpleName());
    }
}
