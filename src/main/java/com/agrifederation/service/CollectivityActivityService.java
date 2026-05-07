package com.agrifederation.service;

import com.agrifederation.dto.ActivityResponse;
import com.agrifederation.dto.ActivityMemberAttendanceDTO;
import com.agrifederation.dto.ActivityRequest;
import com.agrifederation.entity.CollectivityActivity;
import com.agrifederation.exception.BadRequestException;
import com.agrifederation.repository.CollectivityActivityRepository;
import com.agrifederation.validator.CollectivityActivityValidator;
import lombok.AllArgsConstructor;
import lombok.Data;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@Data
@AllArgsConstructor
public class CollectivityActivityService {
    private final CollectivityActivityRepository collectivityActivityRepository;
    private final CollectivityActivityValidator collectivityActivityValidator;

    public List<CollectivityActivity> createActivities(String id, List<ActivityRequest> activitiesGivenList) {
        collectivityActivityValidator.validateActivityList(activitiesGivenList);

        return collectivityActivityRepository.createActivities(id, activitiesGivenList);
    }

    public List<CollectivityActivity> getActivitiesByCollectivityId(String id) {
        collectivityActivityValidator.validateCollectivityId(id);

        List<CollectivityActivity> activities = collectivityActivityRepository.getActivitiesCollectivityById(id);
        if(activities.isEmpty()){
            throw new BadRequestException("No activities found for collectivity id " + id);
        }
        return activities;
    }

    public List<ActivityMemberAttendanceDTO> getAttendanceByActivityId(String collectivityId, String activityId) {
        collectivityActivityValidator.validateActivityAttendance(collectivityId, activityId);
        return collectivityActivityRepository.getAttendanceWithMemberDetails(activityId);
    }
}
