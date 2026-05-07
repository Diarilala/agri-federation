package com.agrifederation.controller;


import com.agrifederation.dto.CollectivityOverallStatisticsDTO;
import com.agrifederation.exception.BadRequestException;
import com.agrifederation.service.CollectivityStatisticsService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/collectivities")
@Data
@AllArgsConstructor
public class ControllerStatisticsController {
    private final CollectivityStatisticsService collectivityStatisticsService;

    @GetMapping("/statistics")
    public ResponseEntity<?> getCollectivitiesOverallStatistics(@RequestParam(required = true) String fromDate, @RequestParam(required = true) String toDate) {
        try {
            List<CollectivityOverallStatisticsDTO> statistics = collectivityStatisticsService.getOverallStats(fromDate, toDate);
            return ResponseEntity.ok(statistics);
        } catch (BadRequestException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Error occurred while calculating stats" + e.getMessage());
        }
    }
}
