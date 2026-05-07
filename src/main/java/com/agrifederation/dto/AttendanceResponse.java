package com.agrifederation.dto;

import com.agrifederation.enums.AttendanceStatus;
import lombok.Data;

@Data
public class AttendanceResponse {
    private String id;
    private MemberDescriptionDTO memberDescription;
    private AttendanceStatus attendanceStatus;
}
