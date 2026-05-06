package com.agrifederation.controller;


import com.agrifederation.dto.CollectivityOverallStatisticsDTO;
import com.agrifederation.entity.CollectivityLocalStatistics;
import com.agrifederation.exception.BadRequestException;
import com.agrifederation.service.CollectivityStatisticsService;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/collectivites")
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

    @GetMapping("/{id}/statistics")
    public ResponseEntity<?> getCollectivitiesLocalStatistics(@PathVariable String id, @RequestParam(required = true) LocalDate from, @RequestParam(required = true) LocalDate to) {
        try {
            List<CollectivityLocalStatistics> LocalStatistics = collectivityStatisticsService.getLocalStats(from, to, id);
            return ResponseEntity
                    .status(HttpStatus.OK)
                    .body(LocalStatistics);
        } catch (BadRequestException e) {
            return ResponseEntity
                    .status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        } catch (RuntimeException e) {
            return  ResponseEntity
                    .status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }
}
