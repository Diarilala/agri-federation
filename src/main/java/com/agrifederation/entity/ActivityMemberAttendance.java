package com.agrifederation.entity;

import com.agrifederation.enums.AttendanceStatus;
import lombok.Data;

@Data
public class ActivityMemberAttendance {
    private String id;
    private String memberId;
    private AttendanceStatus attendanceStatus;
}
