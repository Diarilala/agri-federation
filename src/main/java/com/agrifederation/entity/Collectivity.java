package com.agrifederation.entity;

import java.util.List;

public class Collectivity {
    private String id;
    private String name;
    private String location;
    private Member president;
    private Member vicePresident;
    private Member treasurer;
    private Member secretary;
    private List<Member> members;
    private boolean federationApproved;
}
