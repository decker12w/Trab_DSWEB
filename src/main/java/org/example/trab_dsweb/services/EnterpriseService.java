package org.example.trab_dsweb.services;
import org.example.trab_dsweb.dto.CreateEnterpriseRequestDTO;
import org.example.trab_dsweb.dto.CreateEnterpriseResponseDTO;
import org.example.trab_dsweb.exceptions.exceptions.ConflictException;
import org.example.trab_dsweb.models.Entreprise;
import org.example.trab_dsweb.repositories.EntrepriseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class EnterpriseService {

    @Autowired
    private EntrepriseRepository entrepriseRepository;

    /**
     * Cria uma nova empresa.
     * @param data O DTO com os dados da empresa a ser criada.
     * @return um DTO com os dados da empresa criada.
     * @throws ConflictException se já existir uma empresa com o mesmo CNPJ.
     */
    public CreateEnterpriseResponseDTO create(CreateEnterpriseRequestDTO data) {
        // Verifica se já existe uma empresa com o CNPJ informado
        Optional<Entreprise> existingEntreprise = Optional.ofNullable(entrepriseRepository.findByCnpj(data.cnpj()));
        if (existingEntreprise.isPresent()) {
            throw new ConflictException("Uma empresa com este CNPJ já existe.");
        }

        // Cria uma nova instância da entidade Entreprise
        Entreprise newEntreprise = new Entreprise();
        newEntreprise.setName(data.name());
        newEntreprise.setEmail(data.email());
        // Lembre-se de adicionar uma lógica para criptografar a senha em um projeto real
        newEntreprise.setPassword(data.password());
        newEntreprise.setCnpj(data.cnpj());
        newEntreprise.setDescription(data.description());

        // Salva a nova empresa no banco de dados
        Entreprise savedEntreprise = entrepriseRepository.save(newEntreprise);

        // Retorna o DTO de resposta
        return new CreateEnterpriseResponseDTO(
                savedEntreprise.getId(),
                savedEntreprise.getName(),
                savedEntreprise.getEmail(),
                savedEntreprise.getCnpj(),
                savedEntreprise.getDescription()
        );
    }
}
