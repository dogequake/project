package ru.chernyshev.myeighthtestappspringboot.service;

import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.stereotype.Component;

/**
 * Правила доступа к данным. Бин называется "access", поэтому его можно
 * вызывать прямо из Thymeleaf: ${@access.canModify(#authentication, s.createdBy)}
 */
@Component("access")
public class AccessService {

    public boolean isAdmin(Authentication a) {
        return hasAuthority(a, "ROLE_ADMIN");
    }

    public boolean isUser(Authentication a) {
        return hasAuthority(a, "ROLE_USER");
    }

    /** Роль USER видит только свои записи; ADMIN и READ_ONLY видят все. */
    public boolean seesOnlyOwn(Authentication a) {
        return isUser(a) && !isAdmin(a);
    }

    public boolean canCreate(Authentication a) {
        return isAdmin(a) || isUser(a);
    }

    public boolean canModify(Authentication a, String createdBy) {
        if (a == null) {
            return false;
        }
        if (isAdmin(a)) {
            return true;
        }
        return isUser(a) && a.getName().equals(createdBy);
    }

    private boolean hasAuthority(Authentication a, String authority) {
        if (a == null) {
            return false;
        }
        for (GrantedAuthority ga : a.getAuthorities()) {
            if (authority.equals(ga.getAuthority())) {
                return true;
            }
        }
        return false;
    }
}
