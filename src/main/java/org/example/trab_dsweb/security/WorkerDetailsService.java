package org.example.trab_dsweb.security;

import lombok.AllArgsConstructor;
import org.example.trab_dsweb.models.Worker;
import org.example.trab_dsweb.repositories.WorkerRepository;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class WorkerDetailsService implements UserDetailsService {
    private WorkerRepository workerRepository;

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        Worker worker = workerRepository.findWorkerByEmail(email).orElseThrow(() -> new UsernameNotFoundException("Worker not found"));
        return new WorkerDetails(worker);
    }
}
