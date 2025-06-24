package org.example.trab_dsweb.security;

import org.example.trab_dsweb.models.Worker;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.Collection;
import java.util.List;

public class WorkerDetails implements UserDetails {
    private final Worker worker;

    public WorkerDetails(Worker worker) {
        this.worker = worker;
    }

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        SimpleGrantedAuthority authority = new SimpleGrantedAuthority("ROLE_WORKER");
        return List.of(authority);
    }

    @Override
    public String getPassword() {
        return worker.getPassword();
    }

    @Override
    public String getUsername() {
        return worker.getEmail();
    }

    public Worker getWorker() {
        return this.worker;
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
