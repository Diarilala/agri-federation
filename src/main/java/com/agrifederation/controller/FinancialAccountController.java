package com.agrifederation.controller;

import com.agrifederation.entity.FinancialAccount;
import com.agrifederation.service.FinancialAccountService;
import lombok.Data;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/collectivities")
@Data
public class FinancialAccountController {
    private final FinancialAccountService financialAccountService;

    @GetMapping("/{id}/financialAccounts")
    public ResponseEntity<?> getFinancialAccounts(@PathVariable String id,
                                               @RequestBody LocalDate at) {
        return ResponseEntity.ok(financialAccountService.getFinancialAccounts(id, at));
    }

}
