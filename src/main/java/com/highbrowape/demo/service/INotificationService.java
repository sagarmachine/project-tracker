package com.highbrowape.demo.service;

import com.highbrowape.demo.entity.Mission;
import com.highbrowape.demo.entity.Project;
import com.highbrowape.demo.entity.ProjectNotification;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;


public interface INotificationService {

    void addProjectNotification(Project project, String message);

    ResponseEntity<?> getProjectNotifications(String id);


    void addMissionNotification(Mission mission, String message);

    ResponseEntity<?> getMissionNotifications(String id);


}
