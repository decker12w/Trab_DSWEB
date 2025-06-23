package org.example.trab_dsweb.security;

import jakarta.annotation.PostConstruct;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
@AllArgsConstructor
public class ComposedDetailsService implements UserDetailsService {
    private final AdminDetailsService adminDetailsService;
    private final EnterpriseDetailsService enterpriseDetailsService;
    private final WorkerDetailsService workerDetailsService;

    private List<UserDetailsService> serviceList;

    @PostConstruct
    public void setServices() {
        List<UserDetailsService> newServices = new ArrayList<>();
        newServices.add(adminDetailsService);
        newServices.add(enterpriseDetailsService);
        newServices.add(workerDetailsService);
        this.serviceList = newServices;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        for (UserDetailsService service : serviceList) {
            try {
                return service.loadUserByUsername(username);
            } catch (UsernameNotFoundException ignored) {
            }
        }
        throw new UsernameNotFoundException("User not found");
    }
}
