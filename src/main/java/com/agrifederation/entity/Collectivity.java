package com.agrifederation.entity;

import lombok.Data;

import java.util.List;

@Data
public class Collectivity {
    private String id;
    private String location;
    private CollectivityStructure structure;
    List<Member> members;
}
