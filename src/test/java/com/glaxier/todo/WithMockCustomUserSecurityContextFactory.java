package com.glaxier.todo;

import com.glaxier.todo.services.UserDetailsImpl;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.test.context.support.WithSecurityContextFactory;

public class WithMockCustomUserSecurityContextFactory
        implements WithSecurityContextFactory<WithMockCustomUser> {
    @Override
    public SecurityContext createSecurityContext(WithMockCustomUser customUser) {
        SecurityContext context = SecurityContextHolder.createEmptyContext();

        UserDetailsImpl principal =
                new UserDetailsImpl(1, customUser.name(), customUser.email(), customUser.password());
        Authentication auth =
                new UsernamePasswordAuthenticationToken(principal, customUser.password(), principal.getAuthorities());
        context.setAuthentication(auth);
        return context;
    }
}
