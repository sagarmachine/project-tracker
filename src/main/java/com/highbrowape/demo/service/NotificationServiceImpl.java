package com.highbrowape.demo.service;

import com.highbrowape.demo.entity.Mission;
import com.highbrowape.demo.entity.MissionNotification;
import com.highbrowape.demo.entity.Project;
import com.highbrowape.demo.entity.ProjectNotification;
import com.highbrowape.demo.repository.MissionNotificationRepository;
import com.highbrowape.demo.repository.ProjectNotificationRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class NotificationServiceImpl implements INotificationService {


    @Autowired
    ProjectNotificationRepository projectNotificationRepository;

    @Autowired
    MissionNotificationRepository missionNotificationRepository;

    @Override
    public void addProjectNotification(Project project, String message) {

        ProjectNotification projectNotification = new ProjectNotification();
        projectNotification.setProject(project);
        projectNotification.setMessage(message);
        projectNotificationRepository.save(projectNotification);
    }

    @Override
    public ResponseEntity<?> getProjectNotifications(String  projectId) {
        return new ResponseEntity<>(projectNotificationRepository.findAllByProjectProjectId(projectId), HttpStatus.OK);

    }

    @Override
    public void addMissionNotification(Mission mission, String message) {
        MissionNotification missionNotification = new MissionNotification();
        missionNotification.setMission(mission);
        missionNotification.setMessage(message);
        missionNotificationRepository.save(missionNotification);
    }

    @Override
    public ResponseEntity<?> getMissionNotifications(String missionId) {
        return new ResponseEntity<>(missionNotificationRepository.findAllByMissionMissionId(missionId), HttpStatus.OK);
    }
}
