package com.agrifederation.service;

import com.agrifederation.dto.CollectivityOverallStatisticsDTO;
import com.agrifederation.entity.Collectivity;
import com.agrifederation.repository.CollectivityOverallStatRepository;
import com.agrifederation.repository.CollectivityRepository;
import com.agrifederation.validator.CollectivityStatisticsValidator;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
public class CollectivityStatisticsService {
    private final CollectivityOverallStatRepository collectivityOverallStatRepository;
    private final CollectivityStatisticsValidator collectivityStatisticsValidator;

    public List<CollectivityOverallStatisticsDTO> getOverallStats(String fromDate, String toDate) {
        collectivityStatisticsValidator.validateDateParameters(fromDate, toDate);
        return collectivityOverallStatRepository.findAllCollectivityOverallStats(fromDate, toDate);
    }
}
