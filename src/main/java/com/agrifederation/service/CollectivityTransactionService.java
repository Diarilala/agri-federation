package com.agrifederation.service;

import com.agrifederation.dto.CollectivityTransactionDTO;
import com.agrifederation.entity.CollectivityTransaction;
import com.agrifederation.exception.BadRequestException;
import com.agrifederation.repository.CollectivityRepository;
import com.agrifederation.repository.CollectivityTransactionRepository;
import com.agrifederation.repository.FinancialAccountRepository;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@Data
public class CollectivityTransactionService {
    private final CollectivityTransactionRepository collectivityTransactionRepository;
    private final CollectivityRepository collectivityRepository;
    private final FinancialAccountRepository financialAccountRepository;

    public List<CollectivityTransaction> getCollectivityTransactions(String collectivityId, LocalDate fromDate, LocalDate toDate) {
        if (fromDate == null) {
            throw new BadRequestException("From date is null or empty");
        }
        if (toDate == null) {
            throw new BadRequestException("To date is null or empty");
        }

        List<CollectivityTransaction> transactions = collectivityTransactionRepository.findCollectivityTransaction(collectivityId, fromDate, toDate);
        return transactions;
    }
}
