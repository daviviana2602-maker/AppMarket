package org.example.auth;


import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticatedService {


    public Long getAuthenticatedUserId() {

        return (Long) SecurityContextHolder
                .getContext()
                .getAuthentication()
                .getPrincipal();

    }

}