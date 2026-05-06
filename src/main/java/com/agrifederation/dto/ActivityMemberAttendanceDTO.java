package com.agrifederation.dto;


import com.agrifederation.enums.AttendanceStatus;
import lombok.Data;

@Data
public class ActivityMemberAttendanceDTO {
    private String id;
    private MemberDescriptionDTO memberDescription;
    private AttendanceStatus attendanceStatus;
}
