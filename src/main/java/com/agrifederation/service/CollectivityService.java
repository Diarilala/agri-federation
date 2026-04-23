package com.agrifederation.service;

import com.agrifederation.entity.Collectivity;
import com.agrifederation.entity.Member;
import com.agrifederation.exception.BadRequestException;
import com.agrifederation.exception.NotFoundException;
import com.agrifederation.repository.CollectivityRepository;
import com.agrifederation.repository.MemberRepository;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@Data
public class CollectivityService {
    private final CollectivityRepository collectivityRepository;
    private final MemberRepository memberRepository;

    public CollectivityService(CollectivityRepository collectivityRepository, MemberRepository memberRepository) {
        this.collectivityRepository = collectivityRepository;
        this.memberRepository = memberRepository;
    }

    public List<Collectivity> createCollectivities(List<Collectivity> collectivities) {
        List<Collectivity> newCollectivities = new ArrayList<>();
        for (Collectivity collectivity : collectivities) {
           if (collectivity.getFederationApproval() == null || !collectivity.getFederationApproval()) {
                throw new BadRequestException("Collectivity without federation approval");
            }

           if (collectivity.getLocation() == null || collectivity.getLocation().trim().isEmpty()) {
                throw new BadRequestException("Location is required");
            }
        }

        return collectivityRepository.createCollectivity(collectivities);
    }

    public Collectivity getCollectivity(Integer id) {
        Collectivity collectivity = collectivityRepository.findById(id);
        if (collectivity == null) {
            throw new NotFoundException("Collectivity with id " + id + " is nowhere to be found");
        }
        return collectivity;
    }

    public List<Collectivity> getAllCollectivities() {
        List<Collectivity> collectivities = collectivityRepository.findAll();
        return collectivities;
    }

    public Collectivity updateCollectivity(Integer id, String name, Integer number) {
        Collectivity existsCollectivity = collectivityRepository.findById(id);
        if (existsCollectivity == null) {
            throw new NotFoundException("Collectivity with id " + id + " is nowhere to be found");
        }

        if(existsCollectivity.getUniqueName() != null && name != null && !name.equals(existsCollectivity.getUniqueName())) {
            throw new BadRequestException("Modifying unique name " + existsCollectivity.getUniqueName() + " is not allowed");
        }

        if(existsCollectivity.getUniqueNumber() != null && number != null && !number.equals(existsCollectivity.getUniqueNumber())) {
            throw new BadRequestException("modifying number " + existsCollectivity.getUniqueNumber() + " is not allowed");
        }

        if(name !=null && collectivityRepository.existsByUniqueName(name)) {
            Collectivity collectivityName = collectivityRepository.findByUniqueName(name);
            if(collectivityName != null && !collectivityName.getId().equals(id)) {
                throw new BadRequestException("Collectivity with name " + name + " already exists");
            }
        }

        if (number != null && collectivityRepository.existsByUniqueNumber(String.valueOf(number))) {
            Collectivity collectivityNumber = collectivityRepository.findByUniqueNumber(String.valueOf(number));
            if(collectivityNumber != null && !collectivityNumber.getId().equals(id)) {
                throw new BadRequestException("Collectivity with number " + number + " already exists");
            }
        }

        throw new RuntimeException("Not implemented yet");
    }


}
