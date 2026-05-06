package com.agrifederation.service;

import com.agrifederation.entity.CollectivityActivity;
import com.agrifederation.repository.CollectivityActivityRepository;
import com.agrifederation.validator.CollectivityActivityValidator;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@AllArgsConstructor
public class CollectivityActivityService {
    private final CollectivityActivityRepository collectivityActivityRepository;
    private final CollectivityActivityValidator collectivityActivityValidator;

    public List<CollectivityActivity> createActivities(String id, List<CollectivityActivity> activitiesGivenList) {
        collectivityActivityValidator.validateActivityList(activitiesGivenList);
        return collectivityActivityRepository.createActivities(id, activitiesGivenList);
    }
}
