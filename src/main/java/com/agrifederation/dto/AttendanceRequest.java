package com.agrifederation.dto;

import com.agrifederation.enums.AttendanceStatus;
import lombok.Data;

@Data
public class AttendanceRequest {
    private String memberIdentifier;
    private AttendanceStatus attendanceStatus;
}
