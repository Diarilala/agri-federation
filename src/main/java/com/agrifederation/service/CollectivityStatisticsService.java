package com.agrifederation.service;

import com.agrifederation.dto.CollectivityOverallStatisticsDTO;
import com.agrifederation.entity.CollectivityLocalStatistics;
import com.agrifederation.repository.CollectivityLocalStatisticsRepository;
import com.agrifederation.repository.CollectivityOverallStatRepository;
import com.agrifederation.validator.CollectivityStatisticsValidator;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Data
public class CollectivityStatisticsService {
    private final CollectivityOverallStatRepository collectivityOverallStatRepository;
    private final CollectivityStatisticsValidator collectivityStatisticsValidator;
    private final CollectivityLocalStatisticsRepository collectivityLocalStatisticsRepository;

    public List<CollectivityOverallStatisticsDTO> getOverallStats(String fromDate, String toDate) {
        collectivityStatisticsValidator.validateDateParameters(fromDate, toDate);
        return collectivityOverallStatRepository.findAllCollectivityOverallStats(fromDate, toDate);
    }

    public List<CollectivityLocalStatistics> getLocalStats(LocalDate from, LocalDate to, String id) {
        String fromDate = from.toString();
        String toDate = to.toString();
        collectivityStatisticsValidator.validateDateParameters(fromDate, toDate);
        return collectivityLocalStatisticsRepository.getStatistics(from, to, id);
    }
}
