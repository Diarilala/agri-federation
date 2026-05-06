package com.agrifederation.validator;

import com.agrifederation.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class CollectivityStatisticsValidator {

    public void validateDateParameters(String fromDate, String toDate) {
        if(fromDate == null || toDate == null){
            throw new BadRequestException("From Date and To Date cannot be null");
        }

        if(!fromDate.matches("\\d{4}-\\d{2}-\\d{2}")){
            throw new BadRequestException("From Date is not valid, it must be in format YYYY-MM-DD");

        }

        if(!toDate.matches("\\d{4}-\\d{2}-\\d{2}")) {
            throw new BadRequestException("To Date is not valid, it must be in format YYYY-MM-DD");
        }

        LocalDate from = LocalDate.parse(fromDate);
        LocalDate to = LocalDate.parse(toDate);
        if(from.isAfter(to)){
            throw new BadRequestException("Error, From Date is after To Date");
        }

    }
}
