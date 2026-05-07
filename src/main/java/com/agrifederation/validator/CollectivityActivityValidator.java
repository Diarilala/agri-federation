package com.agrifederation.validator;

import com.agrifederation.dto.ActivityRequest;
import com.agrifederation.entity.CollectivityActivity;
import com.agrifederation.exception.BadRequestException;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CollectivityActivityValidator {
    public void validateCollectivityActivity(ActivityRequest collectivityActivity) {
        String message = "";
        if(collectivityActivity.getLabel() ==  null || collectivityActivity.getLabel().isBlank()){
            message += "Label is required, ";
        }
        if(collectivityActivity.getActivityType() == null || collectivityActivity.getActivityType().name().isBlank()) {
            message += "Activity Type is required, ";
        }
        if(collectivityActivity.getMemberOccupationConcerned() == null || collectivityActivity.getMemberOccupationConcerned().isEmpty()) {
            message += "Member Occupation Concerned is required, ";
        }
        if(collectivityActivity.getMonthlyRecurrenceRule() != null && collectivityActivity.getExecutiveDate() != null){
            message += "Monthly Recurrence and executive date cannot both be provided";
        }
        if(!message.isEmpty()) {
            throw new BadRequestException(message);
        }
    }

    public void validateActivityList(List<ActivityRequest> collectivityActivityList) {
        if(collectivityActivityList == null || collectivityActivityList.isEmpty()) {
            throw new BadRequestException("No Collectivity Activities are provided");
        }
        for(ActivityRequest collectivityActivity : collectivityActivityList) {
            validateCollectivityActivity(collectivityActivity);
        }
    }

    public void validateCollectivityId(String id) {
        String message = "";
        if(id == null || id.isBlank()) {
            message += "Collectivity ID is required, ";
        }
    }

    public void validateActivityAttendance(String collectivityId, String activityId) {
        String message = "";
        if(collectivityId == null || collectivityId.isBlank()) {
            message += "Collectivity ID is required, ";
        }

        if(activityId == null || activityId.isBlank()) {
            message += "Activity ID is required, ";
        }

        if(!message.isBlank()) {
            throw new BadRequestException(message);
        }
    }

}
