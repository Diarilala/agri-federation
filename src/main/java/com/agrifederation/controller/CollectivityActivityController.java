package com.agrifederation.controller;

import com.agrifederation.dto.ActivityMemberAttendanceDTO;
import com.agrifederation.entity.CollectivityActivity;
import com.agrifederation.exception.BadRequestException;
import com.agrifederation.exception.NotFoundException;
import com.agrifederation.service.CollectivityActivityService;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@AllArgsConstructor
@RestController
@RequestMapping("/collectivities")
public class CollectivityActivityController {
    private final CollectivityActivityService collectivityActivityService;

    @PostMapping("/{id}/activities")
    public ResponseEntity<?> createActivities(@PathVariable String id, @RequestBody List<CollectivityActivity> activitiesGivenList) {
        List<CollectivityActivity> collectivityActivities;
        try {
            collectivityActivities = collectivityActivityService.createActivities(id, activitiesGivenList);
            return ResponseEntity
                    .status(HttpStatus.CREATED)
                    .body(collectivityActivities);
        } catch (BadRequestException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/activities")
    public ResponseEntity<?> getActivities(@PathVariable String id) {
        List<CollectivityActivity> collectivityActivities;
        try{
            collectivityActivities = collectivityActivityService.getActivitiesByCollectivityId(id);
            return ResponseEntity.status(HttpStatus.OK).body(collectivityActivities);
    } catch (BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }

    @GetMapping("/{id}/activities/{activityId}/attendance")
    public ResponseEntity<?> getAttendance(@PathVariable String id, @PathVariable String activityId) {
        List<ActivityMemberAttendanceDTO> collectivityActivities;
        try {
            collectivityActivities = collectivityActivityService.getAttendanceByActivityId(id, activityId);
            return ResponseEntity.status(HttpStatus.OK).body(collectivityActivities);
    } catch (BadRequestException e) {
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(e.getMessage());
        } catch (NotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (Exception e){
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body(e.getMessage());
        }
    }
}
