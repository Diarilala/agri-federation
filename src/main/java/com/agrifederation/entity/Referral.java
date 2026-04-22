package com.agrifederation.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Referral {
    private String id;
    private Member referee;
    private Member referred;
}


