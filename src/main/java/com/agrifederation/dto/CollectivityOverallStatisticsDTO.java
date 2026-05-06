package com.agrifederation.dto;

import lombok.Data;

@Data
public class CollectivityOverallStatisticsDTO {
    private CollectivityInformationDTO collectivityInformation;
    private Integer MembersNumber;
    private Double overallMemberPercentage;
}
