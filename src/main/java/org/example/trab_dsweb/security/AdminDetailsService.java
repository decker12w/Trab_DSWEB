package org.example.trab_dsweb.security;

import lombok.AllArgsConstructor;
import org.example.trab_dsweb.dao.AdminDAO;
import org.example.trab_dsweb.model.Admin;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class AdminDetailsService implements UserDetailsService {
    private AdminDAO adminDAO;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Admin admin = adminDAO.findByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Admin not found"));
        return new AdminDetails(admin);
    }
}
