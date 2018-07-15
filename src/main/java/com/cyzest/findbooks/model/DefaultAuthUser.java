package com.cyzest.findbooks.model;

import com.cyzest.findbooks.dao.User;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collections;

public class DefaultAuthUser extends org.springframework.security.core.userdetails.User {

    private static final String DEFAULT_ROLE = "ROLE_USER";

    private static final boolean enabled = true;
    private static final boolean accountNonExpired = true;
    private static final boolean credentialsNonExpired = true;
    private static final boolean accountNonLocked = true;

    public DefaultAuthUser(User user) {
        super(user.getId(), user.getPassword(),
                enabled, accountNonLocked, credentialsNonExpired, accountNonExpired,
                Collections.singletonList(new SimpleGrantedAuthority(DEFAULT_ROLE))
        );
    }

}
