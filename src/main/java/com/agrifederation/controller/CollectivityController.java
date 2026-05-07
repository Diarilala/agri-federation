package com.agrifederation.controller;

import com.agrifederation.config.DatabaseConfig;
import com.agrifederation.dto.CollectivityInformationDTO;
import com.agrifederation.dto.CreateMembershipFeeDTO;
import com.agrifederation.dto.MembershipFeeDTO;
import com.agrifederation.entity.Collectivity;
import com.agrifederation.entity.CollectivityTransaction;
import com.agrifederation.exception.BadRequestException;
import com.agrifederation.exception.NotFoundException;
import com.agrifederation.repository.CollectivityRepository;
import com.agrifederation.service.CollectivityService;
import com.agrifederation.service.CollectivityTransactionService;
import com.agrifederation.service.MembershipFeeService;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.beans.ConstructorProperties;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/collectivities")
@Data

@AllArgsConstructor
public class CollectivityController {
    private final CollectivityService collectivityService;
    private final CollectivityRepository collectivityRepository;
    private final MembershipFeeService membershipFeeService;
    private final CollectivityTransactionService collectivityTransactionService;



    @PostMapping
    public ResponseEntity<List<Collectivity>> createCollectivity(@RequestBody List<Collectivity> collectivities) {
        List<Collectivity> created = collectivityService.createCollectivities(collectivities);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(created);
    }

    @GetMapping
    public ResponseEntity<List<Collectivity>> getAllCollectivities() {
        List<Collectivity> collectivities = collectivityService.getAllCollectivities();
        return ResponseEntity.ok(collectivities);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Collectivity> getCollectivityById(@RequestParam String id) {
        Collectivity collectivity = collectivityService.getCollectivity(Integer.valueOf(id));
        return ResponseEntity.ok(collectivity);
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

    @PostMapping("/{id}/membershipFees")
    public ResponseEntity<?> createMembershipFee(@PathVariable String id, @RequestBody List<CreateMembershipFeeDTO> createMembershipFeeDTO) {
        try {
            List<MembershipFeeDTO> createFees = membershipFeeService.createMembershipFees(id, createMembershipFeeDTO);
            return ResponseEntity.status(HttpStatus.CREATED)
                    .body(createFees);
        } catch (NotFoundException e) {
            return  ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(e.getMessage());
        }
    }

    @GetMapping("/{id}/membershipFees")
    public ResponseEntity<List<MembershipFeeDTO>> getMembershipFees(@PathVariable String id) {
        List<MembershipFeeDTO> membershipFeeDTOs = membershipFeeService.getMembershipFees(id);
        return ResponseEntity.ok(membershipFeeDTOs);
    }

    @GetMapping("/{id}/transactions")
    public ResponseEntity<?> getCollectivityTransactions(@PathVariable String id,
                                                                                     @RequestParam(required = true) LocalDate from,
                                                                                     @RequestParam(required = true) LocalDate to) {
        try {
            List<CollectivityTransaction> collectivityTransactions = collectivityTransactionService.getCollectivityTransactions(id, from, to);
            return ResponseEntity.ok(collectivityTransactions);
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        }

    }



}
