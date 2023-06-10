package com.epam.esm.entity;

import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import static com.epam.esm.entity.Permission.ADMIN_CREATE;
import static com.epam.esm.entity.Permission.ADMIN_DELETE;
import static com.epam.esm.entity.Permission.ADMIN_READ;
import static com.epam.esm.entity.Permission.ADMIN_UPDATE;
import static com.epam.esm.entity.Permission.GUEST_READ;
import static com.epam.esm.entity.Permission.USER_CREATE;
import static com.epam.esm.entity.Permission.USER_READ;
import static java.util.Arrays.asList;
import static java.util.Collections.emptySet;
import static java.util.Collections.singletonList;
import static java.util.stream.Collectors.toList;

/**
 * Enum representing the roles of users in the system.
 */
@RequiredArgsConstructor
public enum RoleType {
    ANONYMOUS(emptySet()),
    GUEST(new HashSet<>(singletonList(GUEST_READ))),
    USER(new HashSet<>(asList(
            USER_CREATE,
            USER_READ))),
    ADMIN(new HashSet<>(asList(
            ADMIN_CREATE,
            ADMIN_READ,
            ADMIN_UPDATE,
            ADMIN_DELETE)));
    @Getter
    private final Set<Permission> authorities;
    public List<SimpleGrantedAuthority> getGrantedAuthorities() {
        List<SimpleGrantedAuthority> grantedAuthorities =
                getAuthorities().stream()
                        .map(permission -> new SimpleGrantedAuthority(
                                permission.getAuthority()))
                        .collect(toList());
        grantedAuthorities.add(new SimpleGrantedAuthority("ROLE_" + this.name()));
        return grantedAuthorities;
    }
}
