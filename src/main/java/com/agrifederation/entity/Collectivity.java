package com.agrifederation.entity;

import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
public class Collectivity {
    private String id;
    private String location;
    private String speciality;
    private Boolean federationApproval;
    private LocalDateTime approvalDate;
    private String uniqueNumber;
    private String uniqueName;
    private LocalDateTime createdAt;
    private CollectivityStructure structure;
    private List<Member> members;
}
