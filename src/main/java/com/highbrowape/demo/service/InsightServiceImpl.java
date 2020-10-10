package com.highbrowape.demo.service;

import com.highbrowape.demo.entity.Mission;
import com.highbrowape.demo.entity.Project;
import com.highbrowape.demo.repository.MissionRepository;
import com.highbrowape.demo.repository.ProjectRepository;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;
import java.util.Set;

public class InsightServiceImpl implements IInsightService {


    @Autowired
    MissionRepository missionRepository;

    @Autowired
    ProjectRepository projectRepository;


    @Override
    public void objectiveCompletionUpdate(String missionId, String loggedInEmail) {

        Mission mission=missionRepository.findByMissionId(missionId).get();

       Mission parent= mission.getMissionParent();

       while(parent!=null){
           parent.getMissionInsight().setCompletedObjectiveCount(parent.getMissionInsight().getCompletedObjectiveCount()+1);
           missionRepository.save(mission);
           parent=parent.getMissionParent();
       }

        Project project=mission.getProject();

       project.getProjectInsight().setCompletedObjectiveCount(project.getProjectInsight().getCompletedObjectiveCount()+1);

       projectRepository.save(project);

    }

    @Override
    public void objectiveAddedUpdate(String missionId, String loggedInEmail) {
        Mission mission=missionRepository.findByMissionId(missionId).get();

        Mission parent= mission.getMissionParent();

        while(parent!=null){
            parent.getMissionInsight().setObjectiveCount(parent.getMissionInsight().getObjectiveCount()+1);
            missionRepository.save(mission);
            parent=parent.getMissionParent();
        }

        Project project=mission.getProject();

        project.getProjectInsight().setObjectiveCount(project.getProjectInsight().getObjectiveCount()+1);

        projectRepository.save(project);



    }
}
