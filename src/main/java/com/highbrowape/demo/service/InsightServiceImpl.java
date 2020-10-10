package com.highbrowape.demo.service;

import com.highbrowape.demo.entity.*;
import com.highbrowape.demo.repository.MissionRepository;
import com.highbrowape.demo.repository.ProjectRepository;
import com.highbrowape.demo.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class InsightServiceImpl implements IInsightService {


    @Autowired
    MissionRepository missionRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;


    @Override
    public void objectiveCompletionUpdate(String missionId, String loggedInEmail) {

        Mission mission=missionRepository.findByMissionId(missionId).get();

       Mission parent= mission.getMissionParent();

       while(parent!=null){
           parent.getMissionInsight().setCompletedObjectiveCount(parent.getMissionInsight().getCompletedObjectiveCount()+1);
           missionRepository.save(mission);
           parent=parent.getMissionParent();
       }


       List<MissionMember> missionsMembers=new ArrayList(mission.getMissionMembers());


       for (MissionMember missionsMember :missionsMembers){

           User user = missionsMember.getMember().getUser();

           user.getUserInsights().setCompletedObjectiveCount(user.getUserInsights().getCompletedObjectiveCount()+1);
           userRepository.save(user);

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


        List<MissionMember> missionsMembers=new ArrayList(mission.getMissionMembers());

        for (MissionMember missionsMember :missionsMembers){

            User user = missionsMember.getMember().getUser();

            user.getUserInsights().setObjectiveCount(user.getUserInsights().getObjectiveCount()+1);
            userRepository.save(user);

        }

        Project project=mission.getProject();

        project.getProjectInsight().setObjectiveCount(project.getProjectInsight().getObjectiveCount()+1);

        projectRepository.save(project);



    }

    @Override
    public void userActionUpdate(String loggedInEmail, String comment) {

        User user = userRepository.findByEmail(loggedInEmail).get();

        UserAction userAction = new UserAction();
        userAction.setComment(comment);
        user.addUserAction(userAction);

        userRepository.save(user);

    }
}
