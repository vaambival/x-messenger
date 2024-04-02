package ru.otus.xmessenger.service.impl;

import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;
import ru.otus.xmessenger.service.UserSecurityService;
import ru.otus.xmessenger.service.exception.IsNotAuthenticated;

import java.util.Objects;
import java.util.UUID;

@Service
public class UserSecurityServiceImpl implements UserSecurityService {
    @Override
    public UUID getUuid() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (Objects.isNull(auth)) {
            throw new IsNotAuthenticated("Token is invalid");
        }
        return (UUID) auth.getPrincipal();
    }
}
