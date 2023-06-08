package com.epam.esm.security.service;

import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserRequest;
import org.springframework.security.oauth2.client.oidc.userinfo.OidcUserService;
import org.springframework.security.oauth2.core.OAuth2AuthenticationException;
import org.springframework.security.oauth2.core.oidc.user.OidcUser;

public class OAuth2Service extends OidcUserService {

    /**
     * @param userRequest
     * @return
     * @throws OAuth2AuthenticationException
     */
    @Override
    public OidcUser loadUser(
            final OidcUserRequest userRequest)
            throws OAuth2AuthenticationException { //TODO
        return super.loadUser(userRequest);
    }
}
