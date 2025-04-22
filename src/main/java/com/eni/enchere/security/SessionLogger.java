package com.eni.enchere.security;

import jakarta.servlet.http.HttpSessionEvent;
import jakarta.servlet.http.HttpSessionListener;
import org.springframework.stereotype.Component;

@Component
public class SessionLogger implements HttpSessionListener {

    @Override
    public void sessionCreated(HttpSessionEvent se) {
        se.getSession().setMaxInactiveInterval(10);
        System.out.println("\uD83D\uDD35 : " + se.getSession().getId() + " -> " + se.getSession().getMaxInactiveInterval() + "s");
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        System.out.println("\uD83D\uDD34 : " + se.getSession().getId());
    }
}