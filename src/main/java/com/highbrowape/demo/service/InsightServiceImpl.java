package com.highbrowape.demo.service;

import com.highbrowape.demo.dto.output.UserCommentChartData;
import com.highbrowape.demo.entity.*;
import com.highbrowape.demo.repository.*;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


@Service
@Slf4j
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
    MissionMemberInsightRepository missionMemberInsightRepository;

    @Autowired
    UserInteractionRepository userInteractionRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    ConversationRepository conversationRepository;


    @Override
    public void objectiveNonCompletionUpdate(String missionId, String loggedInEmail, int n) {

        Mission mission = missionRepository.findByMissionId(missionId).get();

        Mission parent = mission.getMissionParent();

        while (parent != null) {
            parent.getMissionInsight().setCompletedObjectiveCount(parent.getMissionInsight().getCompletedObjectiveCount() - n);
            missionRepository.save(mission);
            parent = parent.getMissionParent();
        }


        List<MissionMember> missionsMembers = new ArrayList(mission.getMissionMembers());


        for (MissionMember missionsMember : missionsMembers) {

            User user = missionsMember.getMember().getUser();

            user.getUserInsights().setCompletedObjectiveCount(user.getUserInsights().getCompletedObjectiveCount() + n);
            userRepository.save(user);

            MissionMemberInsight missionMemberInsight = missionsMember.getMissionMemberInsight();
            missionMemberInsight.setCompletedObjectiveCount(missionsMember.getMissionMemberInsight().getCompletedObjectiveCount() - n);
            missionMemberInsightRepository.save(missionMemberInsight);


            MemberInsight MemberInsight = missionsMember.getMember().getMemberInsight();
            MemberInsight.setCompletedObjectiveCount(MemberInsight.getCompletedObjectiveCount() + n);
            memberInsightRepository.save(MemberInsight);

        }


        Project project = mission.getProject();

        project.getProjectInsight().setCompletedObjectiveCount(project.getProjectInsight().getCompletedObjectiveCount() + n);

        //Member member = project.get

        projectRepository.save(project);

    }

    @Override
    public void objectiveRemovedUpdate(String missionId, String loggedInEmail, int n) {
        Mission mission = missionRepository.findByMissionId(missionId).get();

        Mission parent = mission;

        while (parent != null) {
            parent.getMissionInsight().setObjectiveCount(parent.getMissionInsight().getObjectiveCount() + n);
            missionRepository.save(mission);
            parent = parent.getMissionParent();
        }


        List<MissionMember> missionsMembers = new ArrayList(mission.getMissionMembers());

        for (MissionMember missionsMember : missionsMembers) {

            User user = missionsMember.getMember().getUser();

            user.getUserInsights().setObjectiveCount(user.getUserInsights().getObjectiveCount() + n);
            userRepository.save(user);


            MissionMemberInsight missionMemberInsight = missionsMember.getMissionMemberInsight();
            missionMemberInsight.setObjectiveCount(missionsMember.getMissionMemberInsight().getObjectiveCount() + n);
            missionMemberInsightRepository.save(missionMemberInsight);

            MemberInsight MemberInsight = missionsMember.getMember().getMemberInsight();
            MemberInsight.setObjectiveCount(MemberInsight.getObjectiveCount() + n);
            memberInsightRepository.save(MemberInsight);


        }

        Project project = mission.getProject();

        project.getProjectInsight().setObjectiveCount(project.getProjectInsight().getObjectiveCount() + n);

        projectRepository.save(project);


    }


    @Override
    public void objectiveCompletionUpdate(String missionId, String loggedInEmail, int n) {

        Mission mission = missionRepository.findByMissionId(missionId).get();
        System.out.println("objective marked as done ======> ------------------");
        Mission parent = mission;

        while (parent != null) {
            parent.getMissionInsight().setCompletedObjectiveCount(parent.getMissionInsight().getCompletedObjectiveCount() + n);
            missionRepository.save(mission);
            parent = parent.getMissionParent();
        }


        List<MissionMember> missionsMembers = new ArrayList(mission.getMissionMembers());


        for (MissionMember missionsMember : missionsMembers) {

            User user = missionsMember.getMember().getUser();

            user.getUserInsights().setCompletedObjectiveCount(user.getUserInsights().getCompletedObjectiveCount() + n);
            userRepository.save(user);

            MissionMemberInsight missionMemberInsight = missionsMember.getMissionMemberInsight();
            missionMemberInsight.setCompletedObjectiveCount(missionsMember.getMissionMemberInsight().getCompletedObjectiveCount() + n);
            missionMemberInsightRepository.save(missionMemberInsight);


            MemberInsight MemberInsight = missionsMember.getMember().getMemberInsight();
            MemberInsight.setCompletedObjectiveCount(MemberInsight.getCompletedObjectiveCount() + n);
            memberInsightRepository.save(MemberInsight);

        }


        Project project = mission.getProject();

        project.getProjectInsight().setCompletedObjectiveCount(project.getProjectInsight().getCompletedObjectiveCount() + n);

        //Member member = project.get

        projectRepository.save(project);

    }

    @Override
    public void objectiveAddedUpdate(String missionId, String loggedInEmail, int n) {
        Mission mission = missionRepository.findByMissionId(missionId).get();
        System.out.println("================== objective Added ");
        Mission parent = mission;

        while (parent != null) {
            parent.getMissionInsight().setObjectiveCount(parent.getMissionInsight().getObjectiveCount() + n);
            missionRepository.save(mission);
            parent = parent.getMissionParent();
        }


        List<MissionMember> missionsMembers = new ArrayList(mission.getMissionMembers());

        for (MissionMember missionsMember : missionsMembers) {

            User user = missionsMember.getMember().getUser();

            user.getUserInsights().setObjectiveCount(user.getUserInsights().getObjectiveCount() + n);
            userRepository.save(user);


            MissionMemberInsight missionMemberInsight = missionsMember.getMissionMemberInsight();
            missionMemberInsight.setObjectiveCount(missionsMember.getMissionMemberInsight().getObjectiveCount() + n);
            missionMemberInsightRepository.save(missionMemberInsight);

            System.out.println(missionMemberInsight.getId());
            MemberInsight MemberInsight = missionsMember.getMember().getMemberInsight();
            System.out.println(MemberInsight.getId());
            MemberInsight.setObjectiveCount(MemberInsight.getObjectiveCount() + n);
            memberInsightRepository.save(MemberInsight);


        }

        Project project = mission.getProject();

        project.getProjectInsight().setObjectiveCount(project.getProjectInsight().getObjectiveCount() + n);

        projectRepository.save(project);

    }


    @Override
    public void linkAddedUpdate(String missionId, String loggedInEmail, int n) {
        Mission mission = missionRepository.findByMissionId(missionId).get();

        System.out.println("------------ adding links" + n);

        Mission parent = mission;

        while (parent != null) {
            parent.getMissionInsight().setLinkCount(parent.getMissionInsight().getLinkCount() + n);
            missionRepository.save(mission);
            parent = parent.getMissionParent();
        }


        List<MissionMember> missionsMembers = new ArrayList(mission.getMissionMembers());

        for (MissionMember missionsMember : missionsMembers) {

            User user = missionsMember.getMember().getUser();

            user.getUserInsights().setLinkCount(user.getUserInsights().getLinkCount() + n);
            userRepository.save(user);


            MissionMemberInsight missionMemberInsight = missionsMember.getMissionMemberInsight();
            missionMemberInsight.setLinkCount(missionsMember.getMissionMemberInsight().getLinkCount() + n);
            missionMemberInsightRepository.save(missionMemberInsight);

            MemberInsight MemberInsight = missionsMember.getMember().getMemberInsight();
            MemberInsight.setLinkCount(MemberInsight.getLinkCount() + n);
            memberInsightRepository.save(MemberInsight);


        }

        Project project = mission.getProject();

        project.getProjectInsight().setLinkCount(project.getProjectInsight().getLinkCount() + n);

        projectRepository.save(project);


    }


    @Override
    public void linkRemovedUpdate(String missionId, String loggedInEmail, int n) {
        Mission mission = missionRepository.findByMissionId(missionId).get();

        Mission parent = mission;

        while (parent != null) {
            parent.getMissionInsight().setLinkCount(parent.getMissionInsight().getLinkCount() - n);
            missionRepository.save(mission);
            parent = parent.getMissionParent();
        }


        List<MissionMember> missionsMembers = new ArrayList(mission.getMissionMembers());

        for (MissionMember missionsMember : missionsMembers) {

            User user = missionsMember.getMember().getUser();

            user.getUserInsights().setLinkCount(user.getUserInsights().getLinkCount() - n);
            userRepository.save(user);


            MissionMemberInsight missionMemberInsight = missionsMember.getMissionMemberInsight();
            missionMemberInsight.setLinkCount(missionsMember.getMissionMemberInsight().getLinkCount() - n);
            missionMemberInsightRepository.save(missionMemberInsight);

            MemberInsight MemberInsight = missionsMember.getMember().getMemberInsight();
            MemberInsight.setLinkCount(MemberInsight.getLinkCount() - n);
            memberInsightRepository.save(MemberInsight);
        }

        Project project = mission.getProject();

        project.getProjectInsight().setLinkCount(project.getProjectInsight().getLinkCount() - n);

        projectRepository.save(project);

    }


    @Override
    public void noteAddedUpdate(String missionId, String loggedInEmail, int n) {
        Mission mission = missionRepository.findByMissionId(missionId).get();
        System.out.println("------------ adding notes------------------" + n);

        Mission parent = mission;

        while (parent != null) {
            parent.getMissionInsight().setNoteCount(parent.getMissionInsight().getNoteCount() + n);
            missionRepository.save(mission);
            parent = parent.getMissionParent();
        }


        List<MissionMember> missionsMembers = new ArrayList(mission.getMissionMembers());

        for (MissionMember missionsMember : missionsMembers) {

            User user = missionsMember.getMember().getUser();

            user.getUserInsights().setNoteCount(user.getUserInsights().getNoteCount() + n);
            userRepository.save(user);


            MissionMemberInsight missionMemberInsight = missionsMember.getMissionMemberInsight();
            missionMemberInsight.setNoteCount(missionsMember.getMissionMemberInsight().getNoteCount() + n);
            missionMemberInsightRepository.save(missionMemberInsight);

            MemberInsight MemberInsight = missionsMember.getMember().getMemberInsight();
            MemberInsight.setNoteCount(MemberInsight.getNoteCount() + n);
            memberInsightRepository.save(MemberInsight);


        }

        Project project = mission.getProject();

        project.getProjectInsight().setNoteCount(project.getProjectInsight().getNoteCount() + n);

        projectRepository.save(project);


    }


    @Override
    public void noteRemovedUpdate(String missionId, String loggedInEmail, int n) {
        Mission mission = missionRepository.findByMissionId(missionId).get();
        Mission parent = mission;

        while (parent != null) {
            parent.getMissionInsight().setNoteCount(parent.getMissionInsight().getNoteCount() - n);
            missionRepository.save(mission);
            parent = parent.getMissionParent();
        }


        List<MissionMember> missionsMembers = new ArrayList(mission.getMissionMembers());

        for (MissionMember missionsMember : missionsMembers) {
            User user = missionsMember.getMember().getUser();
            user.getUserInsights().setNoteCount(user.getUserInsights().getNoteCount() - n);
            userRepository.save(user);
            MissionMemberInsight missionMemberInsight = missionsMember.getMissionMemberInsight();
            missionMemberInsight.setNoteCount(missionsMember.getMissionMemberInsight().getNoteCount() - n);
            missionMemberInsightRepository.save(missionMemberInsight);
            MemberInsight MemberInsight = missionsMember.getMember().getMemberInsight();
            MemberInsight.setNoteCount(MemberInsight.getNoteCount() - n);
            memberInsightRepository.save(MemberInsight);


        }

        Project project = mission.getProject();
        project.getProjectInsight().setNoteCount(project.getProjectInsight().getNoteCount() - n);
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

    @Override
    public void addUserInteraction(String email, int n) {

        Date d = new Date();
        LocalDate localDate = d.toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        int month = localDate.getMonthValue();
        int date = localDate.getDayOfMonth();
        int year = localDate.getYear();
        int day = localDate.getDayOfWeek().getValue();

        log.error(date+" "+month+" "+year+" "+email+" "+userInteractionRepository.existsByDateAndMonthAndYearAndUserEmail(date, month, year,email));

        if (userInteractionRepository.existsByDateAndMonthAndYearAndUserEmail(date, month, year,email)) {

            UserInteraction userInteraction = userInteractionRepository.findByDateAndMonthAndYearAndUser(date, month, year, userRepository.findByEmail(email).get()).get();
            userInteraction.setCount(userInteraction.getCount() + 1);
            userInteractionRepository.save(userInteraction);

        } else {
            User user = userRepository.findByEmail(email).get();
            UserInteraction userInteraction = UserInteraction.builder().date(date).user(user).month(month).year(year).count(1).day(day).build();
            userInteractionRepository.save(userInteraction);
        }

    }

    @Override
    public  HashMap<String,Object> getUSerCommentChartData(String email) {

        List<UserCommentChartData> userCommentDateChartDataList = commentRepository.countOrderByDate(email);
        List<UserCommentChartData> userCommentMonthChartDataList=commentRepository.countOrderByMonth(email);

        List<UserCommentChartData> userConversationDateChartDataList = conversationRepository.countOrderByDate(email);
        List<UserCommentChartData> userConversationMonthChartDataList=conversationRepository.countOrderByMonth(email);


        HashMap<String,Object> hashMap= new HashMap<String,Object>(){{

            put("comment",new HashMap<String,Object>(){{
                put("date",userCommentDateChartDataList);
                put("month",userCommentMonthChartDataList);
            }});

            put("conversation",new HashMap<String,Object>(){{
                put("date",userConversationDateChartDataList);
                put("month",userConversationMonthChartDataList);
            }});

        }};

        return hashMap;

    }
}
