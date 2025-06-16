package org.example.trab_dsweb.services;

import org.example.trab_dsweb.repositories.WorkerRepository;
import org.springframework.stereotype.Service;

@Service
public class WorkerService {

    private final WorkerRepository workerRepository;

    public WorkerService(WorkerRepository workerRepository) {
        this.workerRepository = workerRepository;
    }

//    public createWorker(Worker worker) {
//        return workerRepository.save(worker);
//    }
//
//    public updateWorker(Worker worker) {
//        return workerRepository.save(worker);
//    }
//
//    public void deleteWorker(UUID id) {
//        workerRepository.deleteById(id);
//    }
//
//    public Worker finsWorkerById(UUID id) {
//        return workerRepository.findById(id).orElse(null);
//    }
//
//    public List<Worker> getAllWorkers() {
//        return workerRepository.findAll();
//    }
}
