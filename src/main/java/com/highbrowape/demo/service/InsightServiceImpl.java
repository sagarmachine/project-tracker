package com.highbrowape.demo.service;

import com.highbrowape.demo.entity.*;
import com.highbrowape.demo.repository.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;


@Service
public class InsightServiceImpl implements IInsightService {


    @Autowired
    MemberInsightRepository memberInsightRepository;

    @Autowired
    MissionRepository missionRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    MissionMemberInsightRepository  missionMemberInsightRepository;


    @Override
    public void objectiveNonCompletionUpdate(String missionId, String loggedInEmail, int n) {

        Mission mission=missionRepository.findByMissionId(missionId).get();

       Mission parent= mission.getMissionParent();

       while(parent!=null){
           parent.getMissionInsight().setCompletedObjectiveCount(parent.getMissionInsight().getCompletedObjectiveCount()-n);
           missionRepository.save(mission);
           parent=parent.getMissionParent();
       }


       List<MissionMember> missionsMembers=new ArrayList(mission.getMissionMembers());


       for (MissionMember missionsMember :missionsMembers){

           User user = missionsMember.getMember().getUser();

           user.getUserInsights().setCompletedObjectiveCount(user.getUserInsights().getCompletedObjectiveCount()+n);
           userRepository.save(user);

          MissionMemberInsight missionMemberInsight= missionsMember.getMissionMemberInsight();
          missionMemberInsight.setCompletedObjectiveCount(missionsMember.getMissionMemberInsight().getCompletedObjectiveCount()-n);
          missionMemberInsightRepository.save(missionMemberInsight);


           MemberInsight MemberInsight= missionsMember.getMember().getMemberInsight();
           MemberInsight.setCompletedObjectiveCount(MemberInsight.getCompletedObjectiveCount()+n);
           memberInsightRepository.save(MemberInsight);

       }




        Project project=mission.getProject();

       project.getProjectInsight().setCompletedObjectiveCount(project.getProjectInsight().getCompletedObjectiveCount()+n);

       //Member member = project.get

       projectRepository.save(project);

    }

    @Override
    public void objectiveRemovedUpdate(String missionId, String loggedInEmail, int n) {
        Mission mission=missionRepository.findByMissionId(missionId).get();

        Mission parent= mission.getMissionParent();

        while(parent!=null){
            parent.getMissionInsight().setObjectiveCount(parent.getMissionInsight().getObjectiveCount()+n);
            missionRepository.save(mission);
            parent=parent.getMissionParent();
        }


        List<MissionMember> missionsMembers=new ArrayList(mission.getMissionMembers());

        for (MissionMember missionsMember :missionsMembers){

            User user = missionsMember.getMember().getUser();

            user.getUserInsights().setObjectiveCount(user.getUserInsights().getObjectiveCount()+n);
            userRepository.save(user);


            MissionMemberInsight missionMemberInsight= missionsMember.getMissionMemberInsight();
            missionMemberInsight.setObjectiveCount(missionsMember.getMissionMemberInsight().getObjectiveCount()+n);
            missionMemberInsightRepository.save(missionMemberInsight);

            MemberInsight MemberInsight= missionsMember.getMember().getMemberInsight();
            MemberInsight.setObjectiveCount(MemberInsight.getObjectiveCount()+n);
             memberInsightRepository.save(MemberInsight);


        }

        Project project=mission.getProject();

        project.getProjectInsight().setObjectiveCount(project.getProjectInsight().getObjectiveCount()+n);

        projectRepository.save(project);



    }




    @Override
    public void objectiveCompletionUpdate(String missionId, String loggedInEmail, int n) {

        Mission mission=missionRepository.findByMissionId(missionId).get();

        Mission parent= mission.getMissionParent();

        while(parent!=null){
            parent.getMissionInsight().setCompletedObjectiveCount(parent.getMissionInsight().getCompletedObjectiveCount()+n);
            missionRepository.save(mission);
            parent=parent.getMissionParent();
        }


        List<MissionMember> missionsMembers=new ArrayList(mission.getMissionMembers());


        for (MissionMember missionsMember :missionsMembers){

            User user = missionsMember.getMember().getUser();

            user.getUserInsights().setCompletedObjectiveCount(user.getUserInsights().getCompletedObjectiveCount()+n);
            userRepository.save(user);

            MissionMemberInsight missionMemberInsight= missionsMember.getMissionMemberInsight();
            missionMemberInsight.setCompletedObjectiveCount(missionsMember.getMissionMemberInsight().getCompletedObjectiveCount()+n);
            missionMemberInsightRepository.save(missionMemberInsight);


            MemberInsight MemberInsight= missionsMember.getMember().getMemberInsight();
            MemberInsight.setCompletedObjectiveCount(MemberInsight.getCompletedObjectiveCount()+n);
            memberInsightRepository.save(MemberInsight);

        }




        Project project=mission.getProject();

        project.getProjectInsight().setCompletedObjectiveCount(project.getProjectInsight().getCompletedObjectiveCount()+n);

        //Member member = project.get

        projectRepository.save(project);

    }

    @Override
    public void objectiveAddedUpdate(String missionId, String loggedInEmail, int n) {
        Mission mission=missionRepository.findByMissionId(missionId).get();

        Mission parent= mission.getMissionParent();

        while(parent!=null){
            parent.getMissionInsight().setObjectiveCount(parent.getMissionInsight().getObjectiveCount()+n);
            missionRepository.save(mission);
            parent=parent.getMissionParent();
        }


        List<MissionMember> missionsMembers=new ArrayList(mission.getMissionMembers());

        for (MissionMember missionsMember :missionsMembers){

            User user = missionsMember.getMember().getUser();

            user.getUserInsights().setObjectiveCount(user.getUserInsights().getObjectiveCount()+n);
            userRepository.save(user);


            MissionMemberInsight missionMemberInsight= missionsMember.getMissionMemberInsight();
            missionMemberInsight.setObjectiveCount(missionsMember.getMissionMemberInsight().getObjectiveCount()+n);
            missionMemberInsightRepository.save(missionMemberInsight);

            System.out.println(missionMemberInsight.getId());
            MemberInsight MemberInsight= missionsMember.getMember().getMemberInsight();
            System.out.println(MemberInsight.getId());
            MemberInsight.setObjectiveCount(MemberInsight.getObjectiveCount()+n);
            memberInsightRepository.save(MemberInsight);


        }

        Project project=mission.getProject();

        project.getProjectInsight().setObjectiveCount(project.getProjectInsight().getObjectiveCount()+n);

        projectRepository.save(project);

    }





    @Override
    public void linkAddedUpdate(String missionId, String loggedInEmail, int n) {
        Mission mission=missionRepository.findByMissionId(missionId).get();

        Mission parent= mission.getMissionParent();

        while(parent!=null){
            parent.getMissionInsight().setLinkCount(parent.getMissionInsight().getLinkCount()+n);
            missionRepository.save(mission);
            parent=parent.getMissionParent();
        }


        List<MissionMember> missionsMembers=new ArrayList(mission.getMissionMembers());

        for (MissionMember missionsMember :missionsMembers){

            User user = missionsMember.getMember().getUser();

            user.getUserInsights().setLinkCount(user.getUserInsights().getLinkCount()+n);
            userRepository.save(user);


            MissionMemberInsight missionMemberInsight= missionsMember.getMissionMemberInsight();
            missionMemberInsight.setLinkCount(missionsMember.getMissionMemberInsight().getLinkCount()+n);
            missionMemberInsightRepository.save(missionMemberInsight);

            MemberInsight MemberInsight= missionsMember.getMember().getMemberInsight();
            MemberInsight.setLinkCount(MemberInsight.getLinkCount()+n);
            memberInsightRepository.save(MemberInsight);


        }

        Project project=mission.getProject();

        project.getProjectInsight().setLinkCount(project.getProjectInsight().getLinkCount()+n);

        projectRepository.save(project);



    }


    @Override
    public void linkRemovedUpdate(String missionId, String loggedInEmail, int n) {
        Mission mission=missionRepository.findByMissionId(missionId).get();

        Mission parent= mission.getMissionParent();

        while(parent!=null){
            parent.getMissionInsight().setLinkCount(parent.getMissionInsight().getLinkCount()-n);
            missionRepository.save(mission);
            parent=parent.getMissionParent();
        }


        List<MissionMember> missionsMembers=new ArrayList(mission.getMissionMembers());

        for (MissionMember missionsMember :missionsMembers){

            User user = missionsMember.getMember().getUser();

            user.getUserInsights().setLinkCount(user.getUserInsights().getLinkCount()-n);
            userRepository.save(user);


            MissionMemberInsight missionMemberInsight= missionsMember.getMissionMemberInsight();
            missionMemberInsight.setLinkCount(missionsMember.getMissionMemberInsight().getLinkCount()-n);
            missionMemberInsightRepository.save(missionMemberInsight);

            MemberInsight MemberInsight= missionsMember.getMember().getMemberInsight();
            MemberInsight.setLinkCount(MemberInsight.getLinkCount()-n);
            memberInsightRepository.save(MemberInsight);
       }

        Project project=mission.getProject();

        project.getProjectInsight().setLinkCount(project.getProjectInsight().getLinkCount()-n);

        projectRepository.save(project);



    }


    @Override
    public void noteAddedUpdate(String missionId, String loggedInEmail, int n) {
        Mission mission=missionRepository.findByMissionId(missionId).get();

        Mission parent= mission.getMissionParent();

        while(parent!=null){
            parent.getMissionInsight().setNoteCount(parent.getMissionInsight().getNoteCount()+n);
            missionRepository.save(mission);
            parent=parent.getMissionParent();
        }


        List<MissionMember> missionsMembers=new ArrayList(mission.getMissionMembers());

        for (MissionMember missionsMember :missionsMembers){

            User user = missionsMember.getMember().getUser();

            user.getUserInsights().setNoteCount(user.getUserInsights().getNoteCount()+n);
            userRepository.save(user);


            MissionMemberInsight missionMemberInsight= missionsMember.getMissionMemberInsight();
            missionMemberInsight.setNoteCount(missionsMember.getMissionMemberInsight().getNoteCount()+n);
            missionMemberInsightRepository.save(missionMemberInsight);

            MemberInsight MemberInsight= missionsMember.getMember().getMemberInsight();
            MemberInsight.setNoteCount(MemberInsight.getNoteCount()+n);
            memberInsightRepository.save(MemberInsight);


        }

        Project project=mission.getProject();

        project.getProjectInsight().setNoteCount(project.getProjectInsight().getNoteCount()+n);

        projectRepository.save(project);



    }


    @Override
    public void noteRemovedUpdate(String missionId, String loggedInEmail, int n) {
        Mission mission=missionRepository.findByMissionId(missionId).get();

        Mission parent= mission.getMissionParent();

        while(parent!=null){
            parent.getMissionInsight().setNoteCount(parent.getMissionInsight().getNoteCount()-n);
            missionRepository.save(mission);
            parent=parent.getMissionParent();
        }


        List<MissionMember> missionsMembers=new ArrayList(mission.getMissionMembers());

        for (MissionMember missionsMember :missionsMembers){

            User user = missionsMember.getMember().getUser();

            user.getUserInsights().setNoteCount(user.getUserInsights().getNoteCount()-n);
            userRepository.save(user);


            MissionMemberInsight missionMemberInsight= missionsMember.getMissionMemberInsight();
            missionMemberInsight.setNoteCount(missionsMember.getMissionMemberInsight().getNoteCount()-n);
            missionMemberInsightRepository.save(missionMemberInsight);

            MemberInsight MemberInsight= missionsMember.getMember().getMemberInsight();
            MemberInsight.setNoteCount(MemberInsight.getNoteCount()-n);
            memberInsightRepository.save(MemberInsight);


        }

        Project project=mission.getProject();

        project.getProjectInsight().setNoteCount(project.getProjectInsight().getNoteCount()-n);

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
