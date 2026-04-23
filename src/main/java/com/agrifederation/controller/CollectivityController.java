package com.agrifederation.controller;

import com.agrifederation.config.DatabaseConfig;
import com.agrifederation.dto.CollectivityInformationDTO;
import com.agrifederation.entity.Collectivity;
import com.agrifederation.exception.BadRequestException;
import com.agrifederation.exception.NotFoundException;
import com.agrifederation.repository.CollectivityRepository;
import com.agrifederation.service.CollectivityService;
import lombok.Data;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/collectivities")
@Data
public class CollectivityController {
    private final CollectivityService collectivityService;
    private final CollectivityRepository collectivityRepository;

    public CollectivityController(CollectivityService collectivityService, CollectivityRepository collectivityRepository) {
        this.collectivityService = collectivityService;
        this.collectivityRepository = collectivityRepository;
    }

    @PostMapping
    public ResponseEntity<List<Collectivity>> createCollectivity(@RequestBody List<Collectivity> collectivities) {
        List<Collectivity> created = collectivityService.createCollectivities(collectivities);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Collectivity> getCollectivityById(@RequestParam String id) {
        Collectivity collectivity = collectivityService.getCollectivity(Integer.valueOf(id));
        return ResponseEntity.ok(collectivity);
    }

    @GetMapping
    public ResponseEntity<List<Collectivity>> getAllCollectivities() {
        List<Collectivity> collectivities = collectivityService.getAllCollectivities();
        return ResponseEntity.ok(collectivities);
    }

    @PutMapping("/{id}/informations")
    public ResponseEntity<?> updateCollectivity(@PathVariable String id, @RequestBody CollectivityInformationDTO  collectivityInformationDTO) {
        try {
            Collectivity updated = collectivityService.updateCollectivity(Integer.valueOf(id), collectivityInformationDTO.getName(), collectivityInformationDTO.getNumber());
            return ResponseEntity.ok(updated);
        } catch (NotFoundException e) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
        catch (BadRequestException e) {
            return  ResponseEntity.status(HttpStatus.BAD_REQUEST)
                    .body(e.getMessage());
        }
        catch (Exception e) {
            return  ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(e.getMessage());
        }
    }



}
