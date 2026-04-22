package com.agrifederation.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class CollectivityMember {
    private String collectivityId;
    private String memberId;
    private LocalDateTime joinedAt;
    private Boolean isActive;
}