package com.agrifederation.service;

import com.agrifederation.dto.FinancialAccountDTO;
import com.agrifederation.entity.CashAccount;
import com.agrifederation.entity.FinancialAccount;
import com.agrifederation.exception.BadRequestException;
import com.agrifederation.exception.NotFoundException;
import com.agrifederation.repository.CollectivityRepository;
import com.agrifederation.repository.FinancialAccountRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Data
@AllArgsConstructor
public class FinancialAccountService {
    public final FinancialAccountRepository financialAccountRepository;
    private final CollectivityRepository collectivityRepository;

    public List<FinancialAccountDTO> getFinancialAccounts(String id, LocalDate atDate) {

        if(!collectivityRepository.CollectivityExists(id)){
            throw new NotFoundException("Collectivity " + id + " Not Found");
        }

        if(atDate == null){
            throw new BadRequestException("Date is Null");
        }


        return List.of();
    }
}
