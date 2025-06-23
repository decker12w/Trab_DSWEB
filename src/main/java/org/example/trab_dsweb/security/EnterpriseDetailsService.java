package org.example.trab_dsweb.security;

import lombok.AllArgsConstructor;
import org.example.trab_dsweb.models.Enterprise;
import org.example.trab_dsweb.repositories.EnterpriseRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class EnterpriseDetailsService implements UserDetailsService {
    private EnterpriseRepository enterpriseRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Enterprise enterprise = enterpriseRepository.findEnterpriseByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Enterprise not found"));
        return new EnterpriseDetails(enterprise);
    }
}
