package com.agrifederation.entity;

import com.agrifederation.enums.Occupation;
import lombok.Data;

@Data
public class CollectivityLocalStatistics {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private Occupation occupation;
    private float earnedAmount;
    private float unpaidAmount;
}
