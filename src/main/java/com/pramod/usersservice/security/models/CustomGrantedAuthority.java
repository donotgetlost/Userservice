package com.pramod.usersservice.security.models;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.pramod.usersservice.models.Role;
import org.springframework.security.core.GrantedAuthority;

@JsonDeserialize
public class CustomGrantedAuthority implements GrantedAuthority {
    private String authority;

    public CustomGrantedAuthority() {

    }

    public CustomGrantedAuthority(Role role) {
        this.authority = role.getRoleName();
    }

    @Override
    public String getAuthority() {
        return authority;
    }
}
