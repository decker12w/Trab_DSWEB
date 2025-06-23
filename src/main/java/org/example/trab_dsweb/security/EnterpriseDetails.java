package org.example.trab_dsweb.security;

import org.example.trab_dsweb.models.Enterprise;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;

import java.util.Collection;
import java.util.List;

public class EnterpriseDetails implements org.springframework.security.core.userdetails.UserDetails {
    private final Enterprise enterprise;

    public EnterpriseDetails(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ENTERPRISE");
        return List.of(authority);
    }

    @Override
    public String getPassword() {
        return enterprise.getPassword();
    }

    @Override
    public String getUsername() {
        return enterprise.getEmail();
    }

    @Override
    public boolean isAccountNonExpired() {
        return true;
    }

    @Override
    public boolean isAccountNonLocked() {
        return true;
    }

    @Override
    public boolean isCredentialsNonExpired() {
        return true;
    }

    @Override
    public boolean isEnabled() {
        return true;
    }
}
