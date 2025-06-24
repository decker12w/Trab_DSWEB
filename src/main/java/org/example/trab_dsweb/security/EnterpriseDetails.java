package org.example.trab_dsweb.security;

import org.example.trab_dsweb.models.Enterprise;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class EnterpriseDetails implements UserDetails {
    private final Enterprise enterprise;

    public EnterpriseDetails(Enterprise enterprise) {
        this.enterprise = enterprise;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_ENTERPRISE");
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

    public Enterprise getEnterprise() {
        return this.enterprise;
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
