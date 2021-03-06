package com.highbrowape.demo.service;

import com.highbrowape.demo.dto.input.*;
import com.highbrowape.demo.entity.*;
import com.highbrowape.demo.exception.*;
import com.highbrowape.demo.repository.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Optional;

@Service
public class MissionServiceImpl implements IMissionService {


    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    MissionRepository missionRepository;


    @Autowired
    MissionInsightRepoistory missionInsightRepoistory;


    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MissionMemberRepository missionMemberRepository;

    @Autowired
    NoteRepository noteRepository;

    @Autowired
    LinkRepository linkRepository;

    @Autowired
    IInsightService insightService;

    @Autowired
    ObjectiveRepository objectiveRepository;

    @Autowired
    ConversationRepository conversationRepository;

    @Autowired
    CommentRepository commentRepository;

    @Autowired
    MissionMemberInsightRepository missionMemberInsightRepository;

    @Autowired
    INotificationService notificationService;


    @Override
    public ResponseEntity<?> addMissionAtLevel1(MissionAddDto missionAddDto, String id, String loggedInEmail) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        HashMap<String, Object> userMap = isValidUser(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        HashMap<String, Object> projectMap = isValidProject(id);
        if (!(boolean) projectMap.get("isValid"))
            throw new ProjectNotFoundException(" No project found with  id :" + id);
        Project project = (Project) projectMap.get("project");

        HashMap<String, Object> memberMap = isCreatorOrChiefOfProject(project, loggedInEmail);
        if (!(boolean) memberMap.get("isValid"))
            throw new InvalidAuthorityException(loggedInEmail + " is not allowed to add missions to the project");


        //Mission mission=new Mission(1,totalMissions+1+"",project,loggedInEmail,Status.TO_DO);
        Mission mission = mapper.map(missionAddDto, Mission.class);

        long totalMissions = countMissions(project, 1, null);
        mission.setMissionId(project.getProjectId() + "-" + (totalMissions + 1) + "");
        mission.setLevel(1);
        mission.setProject(project);
        mission.setAddedBy(loggedInEmail);
        mission.setStatus(Status.TO_DO);


        // try{
//        }catch (Exception ex){
//            throw new RuntimeException(ex.getMessage());
//        }
        mission = missionRepository.save(mission);


        if (missionAddDto.getNotes() != null) {
            for (NoteDto note : missionAddDto.getNotes()) {
                Note note1 = mapper.map(note, Note.class);
                note1.setMission(mission);
                note1.setAddedBy(loggedInEmail);
                mission.addNote(note1);
                noteRepository.save(note1);

            }
        }

        if (missionAddDto.getLinks() != null) {
            for (LinkDto link : missionAddDto.getLinks()) {
                Link link1 = mapper.map(link, Link.class);
                link1.setMission(mission);
                link1.setAddedBy(loggedInEmail);
                mission.addLink(link1);
                linkRepository.save(link1);
            }
            System.out.println("-----------------------> "+missionAddDto.getLinks().size());

        }


        if (missionAddDto.getMember() != null) {
            for (ProjectMemberDto pmd : missionAddDto.getMember()) {
                HashMap<String, Object> userMap1 = isValidProjectMember(project, pmd.getEmail());

                if ((boolean) userMap1.get("isValid")) {
                    User user1 = (User) userMap1.get("user");
                    Optional<MissionMember> optionalMissionMember = missionMemberRepository.findByMissionMissionIdAndMemberUserEmail(mission.getMissionId(), pmd.getEmail());
                    if (optionalMissionMember.isPresent()) continue;
                    MissionMember missionMember = mapper.map(pmd, MissionMember.class);
                    Member member = memberRepository.findByProjectProjectIdAndUserEmail(mission.getProject().getProjectId(), pmd.getEmail()).get();

                    member.addMissionMember(missionMember);
                    missionMember.setAddedBy(loggedInEmail);
                    missionMember.setMission(mission);
                    mission.addMissionMember(missionMember);

                    MissionMemberInsight missionMemberInsight = new MissionMemberInsight();
                    missionMemberInsight.setMissionMember(missionMember);
                    missionMember.setMissionMemberInsight(missionMemberInsight);
                    //   mission.addMissionMemberInsight(missionMemberInsight);


//                    memberRepository.save(member);
                }
            }
        }

        MissionInsight missionInsight = new MissionInsight();
        missionInsight.setMission(mission);
        mission.setMissionInsight(missionInsight);

        missionInsightRepoistory.save(missionInsight);

       if(missionAddDto.getLinks()!=null)insightService.linkAddedUpdate(mission.getMissionId(), loggedInEmail, missionAddDto.getLinks().size());
        if(missionAddDto.getNotes()!=null) insightService.noteAddedUpdate(mission.getMissionId(), loggedInEmail, missionAddDto.getNotes().size());

        notificationService.addProjectNotification(project, loggedInEmail + " add new mission to the project " + missionAddDto.getName());
        return new ResponseEntity<>(missionRepository.save(mission), HttpStatus.ACCEPTED);
    }

    private long countMissions(Project project, long level, Mission missionParent) {
        if (missionParent == null)
            return missionRepository.countByProjectAndLevel(project, level);
        return missionRepository.countByMissionParent(missionParent);

    }

    @Override
    public ResponseEntity<?> addMissionAtLevelN(MissionAddDto missionAddDto, String id, String loggedInEmail) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        HashMap<String, Object> userMap = isValidUser(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        HashMap<String, Object> missionMap = isValidMission(id);
        if (!(boolean) missionMap.get("isValid"))
            throw new MissionNotFoundException(" No Mission found with mission id :" + id);
        Mission mission = (Mission) missionMap.get("mission");

        System.out.println(mission.getMissionId());
        System.out.println(id);

        HashMap<String, Object> creatorOrChiefOfProjectMap = isCreatorOrChiefOfProject(mission.getProject(), loggedInEmail);
        HashMap<String, Object> captainOrChiefOrCreatorOfMissionMap = isCaptainOrChiefOrCreatorOfMission(mission, loggedInEmail);
        if (!(boolean) creatorOrChiefOfProjectMap.get("isValid")) {

            if (!(boolean) captainOrChiefOrCreatorOfMissionMap.get("isValid"))
                throw new InvalidAuthorityException(loggedInEmail + "  is not allowed to add member to the mission ");
        }

        Mission mission1 = mapper.map(missionAddDto, Mission.class);

        long totalMissions = countMissions(mission.getProject(), mission.getLevel() + 1, mission) + 1;
        mission1.setMissionId(mission.getMissionId() + "." + totalMissions);
        mission1.setLevel(mission.getLevel() + 1);
        mission1.setMissionParent(mission);
        mission1.setProject(mission.getProject());
        mission1.setStatus(Status.TO_DO);
        mission1.setAddedBy(loggedInEmail);

        try {
            mission = missionRepository.save(mission1);
        } catch (Exception ex) {
            throw new RuntimeException(ex.getMessage());
        }
        if (missionAddDto.getNotes() != null) {
            for (NoteDto note : missionAddDto.getNotes()) {
                Note note1 = mapper.map(note, Note.class);
                note1.setMission(mission1);
                note1.setAddedBy(loggedInEmail);
                mission1.addNote(note1);
                noteRepository.save(note1);

            }
//            insightService.noteAddedUpdate(mission.getMissionId(), loggedInEmail, missionAddDto.getNotes().size());

        }
        if (missionAddDto.getLinks() != null) {
            for (LinkDto link : missionAddDto.getLinks()) {
                Link link1 = mapper.map(link, Link.class);
                link1.setMission(mission1);
                link1.setAddedBy(loggedInEmail);
                mission1.addLink(link1);
                linkRepository.save(link1);
            }
//            insightService.linkAddedUpdate(mission.getMissionId(), loggedInEmail, missionAddDto.getLinks().size());

        }
        if (missionAddDto.getMember() != null) {
            for (ProjectMemberDto pmd : missionAddDto.getMember()) {
                HashMap<String, Object> userMap1 = isValidProjectMember(mission.getProject(), pmd.getEmail());

                if ((boolean) userMap1.get("isValid")) {
                    User user1 = (User) userMap.get("user");
                    Optional<MissionMember> optionalMissionMember = missionMemberRepository.findByMissionMissionIdAndMemberUserEmail(mission.getMissionId(), pmd.getEmail());
                    if (optionalMissionMember.isPresent())
                        continue;
                    MissionMember missionMember = mapper.map(pmd, MissionMember.class);
                    Member member = memberRepository.findByProjectProjectIdAndUserEmail(mission.getProject().getProjectId(), pmd.getEmail()).get();

                    member.addMissionMember(missionMember);
                    missionMember.setAddedBy(loggedInEmail);
                    missionMember.setMission(mission1);
                    mission1.addMissionMember(missionMember);

                    MissionMemberInsight missionMemberInsight = new MissionMemberInsight();
                    missionMemberInsight.setMissionMember(missionMember);
                    missionMember.setMissionMemberInsight(missionMemberInsight);
                    // mission1.addMissionMemberInsight(missionMemberInsight);

//                    memberRepository.save(member);
                }
            }
        }
        MissionInsight missionInsight = new MissionInsight();
        missionInsight.setMission(mission1);
        mission1.setMissionInsight(missionInsight);

        missionInsightRepoistory.save(missionInsight);
        if(missionAddDto.getLinks()!=null)insightService.linkAddedUpdate(mission.getMissionId(), loggedInEmail, missionAddDto.getLinks().size());
        if(missionAddDto.getNotes()!=null) insightService.noteAddedUpdate(mission.getMissionId(), loggedInEmail, missionAddDto.getNotes().size());

        notificationService.addMissionNotification(mission1.getMissionParent(),loggedInEmail +"added new mission to "+mission1.getMissionParent().getName());

        return new ResponseEntity<>(missionRepository.save(mission1), HttpStatus.ACCEPTED);

    }

    @Override
    public ResponseEntity<?> getMissionMembers(String id, String name) {
        return new ResponseEntity<>(missionMemberRepository.findByMissionMissionId(id), HttpStatus.OK);

    }

    @Override
    public ResponseEntity<?> addMemberToMission(ProjectMemberDto projectMemberDto, String id, String loggedInEmail) {
        HashMap<String, Object> userMap = isValidUser(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        HashMap<String, Object> userMap1 = isValidUser(projectMemberDto.getEmail());
        if (!(boolean) userMap1.get("isValid"))
            throw new UserNotFoundException(projectMemberDto.getEmail() + " is not a valid user ");
        User user1 = (User) userMap1.get("user");

        HashMap<String, Object> missionMap = isValidMission(id);
        if (!(boolean) missionMap.get("isValid"))
            throw new MissionNotFoundException(" No Mission found with  mission id :" + id);
        Mission mission = (Mission) missionMap.get("mission");


        HashMap<String, Object> memberMap = isValidProjectMember(mission.getProject(), projectMemberDto.getEmail());
        if (!(boolean) memberMap.get("isValid"))
            throw new MemberNotFoundException(projectMemberDto.getEmail() + " is not a member of the project ");
        Member member = (Member) memberMap.get("member");


        HashMap<String, Object> creatorOrChiefOfProjectMap = isCreatorOrChiefOfProject(mission.getProject(), loggedInEmail);
        HashMap<String, Object> captainOrChiefOrCreatorOfMissionMap = isCaptainOrChiefOrCreatorOfMission(mission, loggedInEmail);
        if (!(boolean) creatorOrChiefOfProjectMap.get("isValid")) {

            if (!(boolean) captainOrChiefOrCreatorOfMissionMap.get("isValid"))
                throw new InvalidAuthorityException(loggedInEmail + "  is not allowed to add member to the mission ");
        }

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        MissionMember missionMember1 = mapper.map(projectMemberDto, MissionMember.class);

        if (!missionMemberRepository.findByMissionMissionIdAndMemberUserEmail(mission.getMissionId(), projectMemberDto.getEmail()).isPresent()) {

            member.addMissionMember(missionMember1);
            memberRepository.save(member);

            missionMember1.setAddedBy(loggedInEmail);
            missionMember1.setMission(mission);
            missionMember1.setMember(member);

            mission.addMissionMember(missionMember1);
            missionRepository.save(mission);

            MissionMemberInsight missionMemberInsight = new MissionMemberInsight();
            missionMemberInsight.setMissionMember(missionMember1);
            missionMember1.setMissionMemberInsight(missionMemberInsight);
            missionMemberInsightRepository.save(missionMemberInsight);

        } else
            return new ResponseEntity<>("MEMBER ALREADY PRESENT ", HttpStatus.BAD_REQUEST);

                 notificationService.addMissionNotification(mission,loggedInEmail+" added new member "+missionMember1.getMember().getUser().getFirstName());
        return new ResponseEntity<>(missionMemberRepository.save(missionMember1), HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> addMembersToMission(List<ProjectMemberDto> projectMemberDtoList, String id, String loggedInEmail) {
        HashMap<String, Object> userMap = isValidUser(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");


        HashMap<String, Object> missionMap = isValidMission(id);
        if (!(boolean) missionMap.get("isValid"))
            throw new MissionNotFoundException(" No Mission found with  mission id :" + id);
        Mission mission = (Mission) missionMap.get("mission");


        HashMap<String, Object> creatorOrChiefOfProjectMap = isCreatorOrChiefOfProject(mission.getProject(), loggedInEmail);
        HashMap<String, Object> captainOrChiefOrCreatorOfMissionMap = isCaptainOrChiefOrCreatorOfMission(mission, loggedInEmail);
        if (!(boolean) creatorOrChiefOfProjectMap.get("isValid")) {

            if (!(boolean) captainOrChiefOrCreatorOfMissionMap.get("isValid"))
                throw new InvalidAuthorityException(loggedInEmail + "  is not allowed to add member to the mission ");
        }

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        for (ProjectMemberDto projectMemberDto : projectMemberDtoList) {


            HashMap<String, Object> userMap1 = isValidUser(projectMemberDto.getEmail());
            if (!(boolean) userMap1.get("isValid"))
                throw new UserNotFoundException(projectMemberDto.getEmail() + " is not a valid user ");


            HashMap<String, Object> memberMap = isValidProjectMember(mission.getProject(), projectMemberDto.getEmail());
            if (!(boolean) memberMap.get("isValid"))
                throw new MemberNotFoundException(projectMemberDto.getEmail() + " is not a member of the project ");
            Member member = (Member) memberMap.get("member");

            MissionMember missionMember1 = mapper.map(projectMemberDto, MissionMember.class);

            if (!missionMemberRepository.findByMissionAndMemberUserEmail(mission, projectMemberDto.getEmail()).isPresent()) {

                member.addMissionMember(missionMember1);
                memberRepository.save(member);

                missionMember1.setAddedBy(loggedInEmail);
                missionMember1.setMission(mission);
                missionMember1.setMember(member);
                missionMemberRepository.save(missionMember1);

                mission.addMissionMember(missionMember1);
                missionRepository.save(mission);

                MissionMemberInsight missionMemberInsight = new MissionMemberInsight();
                missionMemberInsight.setMissionMember(missionMember1);
                missionMember1.setMissionMemberInsight(missionMemberInsight);
                missionMemberInsightRepository.save(missionMemberInsight);

            }

        }

        Mission updatedMission =missionRepository.save(mission);

        notificationService.addMissionNotification(updatedMission, loggedInEmail+" added some new members");

        return new ResponseEntity<>(updatedMission, HttpStatus.ACCEPTED);

    }

    @Override
    public ResponseEntity<?> updateMemberAuthorityOfMission(Authority authority, Long id, String loggedInEmail) {

        MissionMember missionMember = updateAndRemoveMemberFromMissionPreProcessing(id, loggedInEmail);
        missionMember.setAuthority(authority);
        missionMember.setUpdatedOn(new java.util.Date());

        return new ResponseEntity<>(missionMemberRepository.save(missionMember), HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> removeMemberFromMission(Long id, String loggedInEmail) {
        MissionMember missionMember = updateAndRemoveMemberFromMissionPreProcessing(id, loggedInEmail);
        missionMember.getMission().getMissionMembers().remove(missionMember);
        missionMemberRepository.delete(missionMember);
        return new ResponseEntity<>("mission Member Deleted Succesfully ", HttpStatus.OK);
    }


    private MissionMember updateAndRemoveMemberFromMissionPreProcessing(long id, String loggedInEmail) {
        HashMap<String, Object> userMap = isValidUser(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");


        HashMap<String, Object> missionMemberMap = isValidMissionMemberById(id);
        if (!(boolean) missionMemberMap.get("isValid"))
            throw new MissionMemberNotFoundException("NO mission member found with id :" + id);
        MissionMember missionMember = (MissionMember) missionMemberMap.get("missionMember");

        Mission mission = missionMember.getMission();

        HashMap<String, Object> creatorOrChiefOfProjectMap = isCreatorOrChiefOfProject(mission.getProject(), loggedInEmail);
        HashMap<String, Object> captainOrChiefOrCreatorOfMissionMap = isCaptainOrChiefOrCreatorOfMission(mission, loggedInEmail);
        if (!(boolean) creatorOrChiefOfProjectMap.get("isValid")) {

            if (!(boolean) captainOrChiefOrCreatorOfMissionMap.get("isValid"))
                throw new InvalidAuthorityException(loggedInEmail + "  is not allowed to add member to the mission ");
        }
        return missionMember;
    }


    @Override
    public ResponseEntity<?> getMissionNotes(String id, String loggedInEmail) {

        HashMap<String, Object> userMap = isValidUser(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        HashMap<String, Object> missionMap = isValidMission(id);
        if (!(boolean) missionMap.get("isValid"))
            throw new MissionNotFoundException(" No Mission found with  id :" + id);
        Mission mission = (Mission) missionMap.get("mission");

        HashMap<String, Object> missionMemberMap = isValidMissionMember(mission, loggedInEmail);
        HashMap<String, Object> creatorOrChiefOfProjectMap = isCreatorOrChiefOfProject(mission.getProject(), loggedInEmail);
        if (!(boolean) missionMemberMap.get("isValid")) {
            if (!(boolean) creatorOrChiefOfProjectMap.get("isValid"))

                throw new MissionMemberNotFoundException("No mission member found with mission id :" + id + " and email " + loggedInEmail);
        }

        List<Note> noteList = noteRepository.findByMission(mission);

        return new ResponseEntity<>(noteList, HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> addNoteToMission(NoteDto noteDto, String id, String loggedInEmail) {
        HashMap<String, Object> userMap = isValidUser(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        HashMap<String, Object> missionMap = isValidMission(id);
        if (!(boolean) missionMap.get("isValid"))
            throw new MissionNotFoundException(" No Mission found with  id :" + id);
        Mission mission = (Mission) missionMap.get("mission");

        HashMap<String, Object> missionMemberMap = isValidMissionMember(mission, loggedInEmail);
        HashMap<String, Object> creatorOrChiefOfProjectMap = isCreatorOrChiefOfProject(mission.getProject(), loggedInEmail);
        if (!(boolean) missionMemberMap.get("isValid")) {
            if (!(boolean) creatorOrChiefOfProjectMap.get("isValid"))
                throw new MissionMemberNotFoundException("No mission member found with mission id :" + id + " and email " + loggedInEmail);
        }


        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Note note1 = mapper.map(noteDto, Note.class);
        note1.setMission(mission);
        note1.setAddedBy(loggedInEmail);
        note1.setAddedOn(new java.util.Date());
        mission.addNote(note1);
        noteRepository.save(note1);

        insightService.noteAddedUpdate(mission.getMissionId(), loggedInEmail, 1);


        Mission updatedMission = missionRepository.save(mission);
        notificationService.addMissionNotification(updatedMission, loggedInEmail+" added new notes");

        return new ResponseEntity<>(updatedMission, HttpStatus.ACCEPTED);

    }

    @Override
    public ResponseEntity<?> updateNoteOfMission(NoteDto noteDto, Long id, String loggedInEmail) {
        HashMap<String, Object> userMap = isValidUser(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        Optional<Note> noteOptional = noteRepository.findById(id);
        if (!noteOptional.isPresent()) throw new NoteNotFoundException("No note found with  id  " + id);

        Note note = noteOptional.get();

        Mission mission = note.getMission();

        HashMap<String, Object> missionMemberMap = isValidMissionMember(mission, loggedInEmail);
        HashMap<String, Object> creatorOrChiefOfProjectMap = isCreatorOrChiefOfProject(mission.getProject(), loggedInEmail);
        if (!(boolean) missionMemberMap.get("isValid")) {
            if (!(boolean) creatorOrChiefOfProjectMap.get("isValid"))
                throw new MissionMemberNotFoundException("No mission member found with mission id :" + note.getMission().getMissionId() + " and email " + loggedInEmail);
        }

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Note note1 = mapper.map(noteDto, Note.class);
        note1.setId(note.getId());
        note1.setMission(mission);
        note1.setUpdatedOn(new java.util.Date());
        note1.setAddedBy(note.getAddedBy());

        notificationService.addMissionNotification(mission, loggedInEmail+" updated a note");

        return new ResponseEntity<>(noteRepository.save(note1), HttpStatus.ACCEPTED);


    }

    @Override
    public ResponseEntity<?> removeNoteFromMission(Long id, String loggedInEmail) {
        HashMap<String, Object> userMap = isValidUser(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        Optional<Note> noteOptional = noteRepository.findById(id);
        if (!noteOptional.isPresent()) throw new NoteNotFoundException("No note found with  id  " + id);

        Note note = noteOptional.get();

        Mission mission = note.getMission();

        HashMap<String, Object> missionMemberMap = isValidMissionMember(mission, loggedInEmail);
        HashMap<String, Object> creatorOrChiefOfProjectMap = isCreatorOrChiefOfProject(mission.getProject(), loggedInEmail);
        if (!(boolean) missionMemberMap.get("isValid")) {
            if (!(boolean) creatorOrChiefOfProjectMap.get("isValid"))
                throw new MissionMemberNotFoundException("No mission member found with mission id :" + note.getMission().getMissionId() + " and email " + loggedInEmail);
        }
        try {

            mission.getMissionNotes().remove(note);
            noteRepository.delete(note);
        } catch (Exception ex) {
            throw new ProjectNoteNotFoundException("No note found with id  " + id);
        }

        insightService.noteRemovedUpdate(mission.getMissionId(), loggedInEmail, 1);


        notificationService.addMissionNotification(mission, loggedInEmail+" removed a note");


        return new ResponseEntity<>("MISSION NOTE DELETED SUCCESSFULLY", HttpStatus.ACCEPTED);


    }

    @Override
    public ResponseEntity<?> getMissionLinks(String id, String loggedInEmail) {

        HashMap<String, Object> userMap = isValidUser(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        HashMap<String, Object> missionMap = isValidMission(id);
        if (!(boolean) missionMap.get("isValid"))
            throw new MissionNotFoundException(" No Mission found with  id :" + id);
        Mission mission = (Mission) missionMap.get("mission");

        HashMap<String, Object> missionMemberMap = isValidMissionMember(mission, loggedInEmail);
        HashMap<String, Object> creatorOrChiefOfProjectMap = isCreatorOrChiefOfProject(mission.getProject(), loggedInEmail);
        if (!(boolean) missionMemberMap.get("isValid")) {
            if (!(boolean) creatorOrChiefOfProjectMap.get("isValid"))

                throw new MissionMemberNotFoundException("No mission member found with mission id :" + id + " and email " + loggedInEmail);
        }

        List<Link> linkList = linkRepository.findByMission(mission);



        return new ResponseEntity<>(linkList, HttpStatus.ACCEPTED);

    }

    @Override
    public ResponseEntity<?> addLinkToMission(LinkDto linkDto, String id, String loggedInEmail) {
        HashMap<String, Object> userMap = isValidUser(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        HashMap<String, Object> missionMap = isValidMission(id);
        if (!(boolean) missionMap.get("isValid"))
            throw new MissionNotFoundException(" No Mission found with  id :" + id);
        Mission mission = (Mission) missionMap.get("mission");

        HashMap<String, Object> missionMemberMap = isValidMissionMember(mission, loggedInEmail);
        HashMap<String, Object> creatorOrChiefOfProjectMap = isCreatorOrChiefOfProject(mission.getProject(), loggedInEmail);
        if (!(boolean) missionMemberMap.get("isValid")) {
            if (!(boolean) creatorOrChiefOfProjectMap.get("isValid"))

                throw new MissionMemberNotFoundException("No mission member found with mission id :" + id + " and email " + loggedInEmail);
        }


        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Link link1 = mapper.map(linkDto, Link.class);
        link1.setMission(mission);
        link1.setAddedBy(loggedInEmail);
        link1.setAddedOn(new java.util.Date());
        mission.addLink(link1);
        linkRepository.save(link1);


        insightService.linkAddedUpdate(mission.getMissionId(), loggedInEmail, 1);


        notificationService.addMissionNotification(mission, loggedInEmail+" added new links");


        return new ResponseEntity<>(missionRepository.save(mission), HttpStatus.ACCEPTED);

    }

    @Override
    public ResponseEntity<?> updateLinkOfMission(LinkDto linkDto, Long id, String loggedInEmail) {

        HashMap<String, Object> userMap = isValidUser(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        Optional<Link> linkOptional = linkRepository.findById(id);
        if (!linkOptional.isPresent()) throw new LinkNotFoundException("No link found with  id  " + id);

        Link link = linkOptional.get();

        Mission mission = link.getMission();

        HashMap<String, Object> missionMemberMap = isValidMissionMember(mission, loggedInEmail);
        HashMap<String, Object> creatorOrChiefOfProjectMap = isCreatorOrChiefOfProject(mission.getProject(), loggedInEmail);
        if (!(boolean) missionMemberMap.get("isValid")) {
            if (!(boolean) creatorOrChiefOfProjectMap.get("isValid"))
                throw new MissionMemberNotFoundException("No mission member found with mission id :" + link.getMission().getMissionId() + " and email " + loggedInEmail);
        }
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Link link1 = mapper.map(linkDto, Link.class);
        link1.setId(link.getId());
        link1.setMission(mission);
        link1.setUpdatedOn(new java.util.Date());
link1.setAddedBy(link.getAddedBy());

        notificationService.addMissionNotification(mission, loggedInEmail+" updated a link");


        return new ResponseEntity<>(linkRepository.save(link1), HttpStatus.ACCEPTED);


    }

    @Override
    public ResponseEntity<?> removeLinkFromMission(Long id, String loggedInEmail) {
        HashMap<String, Object> userMap = isValidUser(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        Optional<Link> linkOptional = linkRepository.findById(id);
        if (!linkOptional.isPresent()) throw new LinkNotFoundException("No link found with  id  " + id);

        Link link = linkOptional.get();

        Mission mission = link.getMission();

        HashMap<String, Object> missionMemberMap = isValidMissionMember(mission, loggedInEmail);
        HashMap<String, Object> creatorOrChiefOfProjectMap = isCreatorOrChiefOfProject(mission.getProject(), loggedInEmail);
        if (!(boolean) missionMemberMap.get("isValid")) {
            if (!(boolean) creatorOrChiefOfProjectMap.get("isValid"))

                throw new MissionMemberNotFoundException("No mission member found with mission id :" + link.getMission().getMissionId() + " and email " + loggedInEmail);
        }
        try {

            mission.getMissionLinks().remove(link);
            linkRepository.delete(link);
        } catch (Exception ex) {
            throw new ProjectNoteNotFoundException("No link found with id  " + id);
        }

        insightService.linkRemovedUpdate(mission.getMissionId(), loggedInEmail, 1);

        notificationService.addMissionNotification(mission, loggedInEmail+" removed a link");

        return new ResponseEntity<>("MISSION LINK DELETED SUCCESSFULLY", HttpStatus.ACCEPTED);

    }

    @Override
    public ResponseEntity<?> getMissionObjectives(String id, String loggedInEmail) {

        HashMap<String, Object> userMap = isValidUser(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        HashMap<String, Object> missionMap = isValidMission(id);
        if (!(boolean) missionMap.get("isValid"))
            throw new MissionNotFoundException(" No Mission found with  id :" + id);
        Mission mission = (Mission) missionMap.get("mission");

        HashMap<String, Object> missionMemberMap = isValidMissionMember(mission, loggedInEmail);
        HashMap<String, Object> creatorOrChiefOfProjectMap = isCreatorOrChiefOfProject(mission.getProject(), loggedInEmail);
        if (!(boolean) missionMemberMap.get("isValid")) {
            if (!(boolean) creatorOrChiefOfProjectMap.get("isValid"))

                throw new MissionMemberNotFoundException("No mission member found with mission id :" + id + " and email " + loggedInEmail);
        }

        List<Objective> objectiveList = objectiveRepository.findByMission(mission);

        return new ResponseEntity<>(objectiveList, HttpStatus.ACCEPTED);

    }

    @Override
    public ResponseEntity<?> addObjectiveToMission(ObjectiveDto objectiveDto, String id, String loggedInEmail) {
        HashMap<String, Object> userMap = isValidUser(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        HashMap<String, Object> missionMap = isValidMission(id);
        if (!(boolean) missionMap.get("isValid"))
            throw new MissionNotFoundException(" No Mission found with  id :" + id);
        Mission mission = (Mission) missionMap.get("mission");


        HashMap<String, Object> missionMemberMap = isValidMissionMember(mission, loggedInEmail);
        HashMap<String, Object> creatorOrChiefOfProjectMap = isCreatorOrChiefOfProject(mission.getProject(), loggedInEmail);
        if (!(boolean) missionMemberMap.get("isValid")) {
            if (!(boolean) creatorOrChiefOfProjectMap.get("isValid"))

                throw new MissionMemberNotFoundException("No mission member found with mission id :" + id + " and email " + loggedInEmail);
        }


        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Objective objective = mapper.map(objectiveDto, Objective.class);
        objective.setMission(mission);
        objective.setAddedOn(new java.util.Date());
        mission.addObjective(objective);
        missionRepository.save(mission);


        insightService.objectiveAddedUpdate(mission.getMissionId(), loggedInEmail, 1);

        notificationService.addMissionNotification(mission, loggedInEmail+" added new objectives");

        return new ResponseEntity<>(objectiveRepository.save(objective), HttpStatus.ACCEPTED);

    }

    @Override
    public ResponseEntity<?> updateObjectiveOfMission(ObjectiveDto objectiveDto, Long id, String loggedInEmail) {
        HashMap<String, Object> userMap = isValidUser(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        Optional<Objective> objectiveOptional = objectiveRepository.findById(id);
        if (!objectiveOptional.isPresent()) throw new ObjectiveNotFoundException("No objective found with  id  " + id);

        Objective objective = objectiveOptional.get();

        Mission mission = objective.getMission();

        HashMap<String, Object> missionMemberMap = isValidMissionMember(mission, loggedInEmail);
        HashMap<String, Object> creatorOrChiefOfProjectMap = isCreatorOrChiefOfProject(mission.getProject(), loggedInEmail);
        if (!(boolean) missionMemberMap.get("isValid")) {
            if (!(boolean) creatorOrChiefOfProjectMap.get("isValid"))
                throw new MissionMemberNotFoundException("No mission member found with mission id :" + objective.getMission().getMissionId() + " and email " + loggedInEmail);
        }
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Objective objective1 = mapper.map(objectiveDto, Objective.class);
        objective1.setId(objective.getId());
        objective1.setUpdatedOn(new java.util.Date());

        notificationService.addMissionNotification(mission, loggedInEmail+" updated an objective");

        return new ResponseEntity<>(objectiveRepository.save(objective1), HttpStatus.ACCEPTED);


    }

    @Override
    public ResponseEntity<?> removeObjectiveFromMission(Long id, String loggedInEmail) {
        HashMap<String, Object> userMap = isValidUser(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        Optional<Objective> objectiveOptional = objectiveRepository.findById(id);
        if (!objectiveOptional.isPresent()) throw new ObjectiveNotFoundException("No objective found with  id  " + id);

        Objective objective = objectiveOptional.get();

        Mission mission = objective.getMission();

        HashMap<String, Object> missionMemberMap = isValidMissionMember(mission, loggedInEmail);
        HashMap<String, Object> creatorOrChiefOfProjectMap = isCreatorOrChiefOfProject(mission.getProject(), loggedInEmail);
        if (!(boolean) missionMemberMap.get("isValid")) {
            if (!(boolean) creatorOrChiefOfProjectMap.get("isValid"))

                throw new MissionMemberNotFoundException("No mission member found with mission id :" + objective.getMission().getMissionId() + " and email " + loggedInEmail);
        }
        try {

            mission.getObjectives().remove(objective);
            objectiveRepository.delete(objective);
        } catch (Exception ex) {
            throw new ObjectiveNotFoundException("No objective found with id  " + id);
        }

        insightService.objectiveRemovedUpdate(mission.getMissionId(), loggedInEmail, 1);


        notificationService.addMissionNotification(mission, loggedInEmail+" removed an objective");

        return new ResponseEntity<>("MISSION LINK DELETED SUCCESSFULLY", HttpStatus.ACCEPTED);

    }

    @Override
    public ResponseEntity<?> updateObjectiveStatus(Long id, Status status, String loggedInEmail) {
        HashMap<String, Object> userMap = isValidUser(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        Optional<Objective> objectiveOptional = objectiveRepository.findById(id);
        if (!objectiveOptional.isPresent()) throw new ObjectiveNotFoundException("No objective found with  id  " + id);

        Objective objective = objectiveOptional.get();

        Mission mission = objective.getMission();

        HashMap<String, Object> missionMemberMap = isValidMissionMember(mission, loggedInEmail);
        HashMap<String, Object> creatorOrChiefOfProjectMap = isCreatorOrChiefOfProject(mission.getProject(), loggedInEmail);
        if (!(boolean) missionMemberMap.get("isValid")) {
            if (!(boolean) creatorOrChiefOfProjectMap.get("isValid"))
                throw new MissionMemberNotFoundException("No mission member found with mission id :" + objective.getMission().getMissionId() + " and email " + loggedInEmail);
        }


        objective.setStatus(status);
        objective.setUpdatedOn(new java.util.Date());


        if(status.toString().equals(Status.DONE.toString()))
              insightService.objectiveCompletionUpdate(mission.getMissionId(),loggedInEmail,1);

        notificationService.addMissionNotification(mission, loggedInEmail+" updated an objective STATUS to "+status);

        return new ResponseEntity<>(objectiveRepository.save(objective), HttpStatus.ACCEPTED);


    }

    @Override
    //missionId
    public ResponseEntity<?> getMissionConversations(String id, String loggedInEmail) {
        HashMap<String, Object> userMap = isValidUser(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        HashMap<String, Object> missionMap = isValidMission(id);
        if (!(boolean) missionMap.get("isValid"))
            throw new MissionNotFoundException(" No Mission found with  id :" + id);
        Mission mission = (Mission) missionMap.get("mission");

        HashMap<String, Object> missionMemberMap = isValidMissionMember(mission, loggedInEmail);
        HashMap<String, Object> creatorOrChiefOfProjectMap = isCreatorOrChiefOfProject(mission.getProject(), loggedInEmail);
        if (!(boolean) missionMemberMap.get("isValid")) {
            if (!(boolean) creatorOrChiefOfProjectMap.get("isValid"))

                throw new MissionMemberNotFoundException("No mission member found with mission id :" + id + " and email " + loggedInEmail);
        }

        List<Conversation> conversationList = conversationRepository.findByMissionOrderByStartedOnDesc(mission);
        return new ResponseEntity<>(conversationList, HttpStatus.ACCEPTED);

    }

    @Override
    //conversation DB id
    public ResponseEntity<?> getConversation(Long id, String loggedInEmail) {
        HashMap<String, Object> userMap = isValidUser(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        HashMap<String, Object> conversationMap = isValidConversation(id);
        if (!(boolean) conversationMap.get("isValid"))
            throw new ConversationNotFoundException(" No Conversation found with  id :" + id);
        Conversation conversation = (Conversation) conversationMap.get("conversation");
        Mission mission = conversation.getMission();

        HashMap<String, Object> missionMemberMap = isValidMissionMember(mission, loggedInEmail);
        HashMap<String, Object> creatorOrChiefOfProjectMap = isCreatorOrChiefOfProject(mission.getProject(), loggedInEmail);
        if (!(boolean) missionMemberMap.get("isValid")) {
            if (!(boolean) creatorOrChiefOfProjectMap.get("isValid"))

                throw new MissionMemberNotFoundException("No mission member found with mission id :" + mission.getId() + " and email " + loggedInEmail);
        }

        return new ResponseEntity<>(conversation, HttpStatus.ACCEPTED);

    }

    @Override
    public ResponseEntity<?> addMissionConversation(ConversationDto conversationDto, String id, String loggedInEmail) {
        HashMap<String, Object> userMap = isValidUser(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        HashMap<String, Object> missionMap = isValidMission(id);
        if (!(boolean) missionMap.get("isValid"))
            throw new MissionNotFoundException(" No Mission found with  id :" + id);
        Mission mission = (Mission) missionMap.get("mission");


        HashMap<String, Object> missionMemberMap = isValidMissionMember(mission, loggedInEmail);
        HashMap<String, Object> creatorOrChiefOfProjectMap = isCreatorOrChiefOfProject(mission.getProject(), loggedInEmail);
        if (!(boolean) missionMemberMap.get("isValid")) {
            if (!(boolean) creatorOrChiefOfProjectMap.get("isValid"))

                throw new MissionMemberNotFoundException("No mission member found with mission id :" + id + " and email " + loggedInEmail);
        }


        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Conversation conversation = mapper.map(conversationDto, Conversation.class);
        conversation.setStartedBy(loggedInEmail);
        conversation.setMission(mission);
        conversation.setStartedByImage(user.getImageUrl());

        LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        conversation.setMonth(localDate.getMonthValue());
        conversation.setDate(localDate.getDayOfMonth());

        mission.addConversations(conversation);
        missionRepository.save(mission);


        return new ResponseEntity<>(conversationRepository.save(conversation), HttpStatus.ACCEPTED);

    }

    @Override
    public ResponseEntity<?> removeConversation(Long id, String loggedInEmail) {
        HashMap<String, Object> userMap = isValidUser(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        Optional<Conversation> conversationOptional = conversationRepository.findById(id);
        if (!conversationOptional.isPresent())
            throw new ConversationNotFoundException("No conversation found with  id  " + id);

        Conversation conversation = conversationOptional.get();

        Mission mission = conversation.getMission();

        HashMap<String, Object> missionMemberMap = isValidMissionMember(mission, loggedInEmail);
        HashMap<String, Object> creatorOrChiefOfProjectMap = isCreatorOrChiefOfProject(mission.getProject(), loggedInEmail);
        if (!(boolean) missionMemberMap.get("isValid")) {
            if (!(boolean) creatorOrChiefOfProjectMap.get("isValid"))

                throw new MissionMemberNotFoundException("No mission member found with mission id :" + conversation.getMission().getMissionId() + " and email " + loggedInEmail);
        }
        try {

            mission.getConversations().remove(conversation);
            conversationRepository.delete(conversation);
        } catch (Exception ex) {
            throw new ConversationNotFoundException("No conversation found with id  " + id);
        }


        return new ResponseEntity<>("MISSION CONVERSATION DELETED SUCCESSFULLY", HttpStatus.ACCEPTED);

    }

    @Override
    public ResponseEntity<?> addComment(CommentDto commentDto, Long id, String loggedInEmail) {

        HashMap<String, Object> userMap = isValidUser(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        HashMap<String, Object> conversationMap = isValidConversation(id);
        if (!(boolean) conversationMap.get("isValid"))
            throw new MissionNotFoundException(" No Conversation found with  id :" + id);
        Conversation conversation = (Conversation) conversationMap.get("conversation");

        Mission mission = conversation.getMission();


        HashMap<String, Object> missionMemberMap = isValidMissionMember(mission, loggedInEmail);
        HashMap<String, Object> creatorOrChiefOfProjectMap = isCreatorOrChiefOfProject(mission.getProject(), loggedInEmail);
        if (!(boolean) missionMemberMap.get("isValid")) {
            if (!(boolean) creatorOrChiefOfProjectMap.get("isValid"))

                throw new MissionMemberNotFoundException("No mission member found with mission id :" + id + " and email " + loggedInEmail);
        }


        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Comment comment = mapper.map(commentDto, Comment.class);
        comment.setAddedBy(loggedInEmail);
        comment.setAddedOn(new java.util.Date());
        comment.setConversation(conversation);
        comment.setAddedByImage(user.getImageUrl());

        LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        comment.setMonth(localDate.getMonthValue());
        comment.setDate(localDate.getDayOfMonth());

        conversation.addComment(comment);
//        conversationRepository.save(conversation);


        return new ResponseEntity<>(commentRepository.save(comment), HttpStatus.ACCEPTED);


    }

    @Override
    public ResponseEntity<?> removeComment(Long id, String loggedInEmail) {

        HashMap<String, Object> userMap = isValidUser(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        Optional<Comment> commentOptional = commentRepository.findById(id);
        if (!commentOptional.isPresent()) throw new CommentNotFoundException("No comment found with  id  " + id);

        Comment comment = commentOptional.get();

        Conversation conversation = comment.getConversation();
        Mission mission = conversation.getMission();

        HashMap<String, Object> missionMemberMap = isValidMissionMember(mission, loggedInEmail);
        HashMap<String, Object> creatorOrChiefOfProjectMap = isCreatorOrChiefOfProject(mission.getProject(), loggedInEmail);
        if (!(boolean) missionMemberMap.get("isValid")) {
            if (!(boolean) creatorOrChiefOfProjectMap.get("isValid"))

                throw new MissionMemberNotFoundException("No mission member found with mission id :" + conversation.getMission().getMissionId() + " and email " + loggedInEmail);
        }
        try {

            conversation.getComments().remove(comment);
            conversationRepository.save(conversation);
            commentRepository.delete(comment);
        } catch (Exception ex) {
            throw new CommentNotFoundException("No comment found with id  " + id);
        }


        return new ResponseEntity<>(" CONVERSATION COMMENT DELETED SUCCESSFULLY", HttpStatus.ACCEPTED);


    }

    public HashMap<String, Object> isValidUser(String email) {

        HashMap<String, Object> result = new HashMap<>();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            result.put("isValid", true);
            result.put("user", optionalUser.get());
            return result;
        }
        result.put("isValid", false);
        return result;
    }

    public HashMap<String, Object> isValidProject(String id) {
        HashMap<String, Object> result = new HashMap<>();
        Optional<Project> optionalProject = projectRepository.findByProjectId(id);
        if (optionalProject.isPresent()) {
            result.put("isValid", true);
            result.put("project", optionalProject.get());
            return result;
        }
        result.put("isValid", false);
        return result;
    }

    public HashMap<String, Object> isValidProjectMember(Project project, String email) {
        HashMap<String, Object> result = new HashMap<>();
        Optional<Member> optionalMember = memberRepository.findByProjectAndUserEmail(project, email);
        if (optionalMember.isPresent()) {
            result.put("isValid", true);
            result.put("member", optionalMember.get());
            return result;
        }
        result.put("isValid", false);
        return result;
    }

    public HashMap<String, Object> isValidMission(String id) {
        HashMap<String, Object> result = new HashMap<>();
        Optional<Mission> optionalMission = missionRepository.findByMissionId(id);
        if (optionalMission.isPresent()) {
            result.put("isValid", true);
            result.put("mission", optionalMission.get());
            return result;
        }
        result.put("isValid", false);
        return result;
    }

    public HashMap<String, Object> isValidConversation(Long id) {
        HashMap<String, Object> result = new HashMap<>();
        Optional<Conversation> optionalConversation = conversationRepository.findById(id);
        if (optionalConversation.isPresent()) {
            result.put("isValid", true);
            result.put("conversation", optionalConversation.get());
            return result;
        }
        result.put("isValid", false);
        return result;
    }

    public HashMap<String, Object> isCreatorOrChiefOfProject(Project project, String loggedInEmail) {
        HashMap<String, Object> result = new HashMap<>();
        Optional<Member> optionalMember = memberRepository.findByProjectAndUserEmail(project, loggedInEmail);
        if (optionalMember.isPresent()) {
            Member member = optionalMember.get();
            if (member.getAuthority() == Authority.CREATOR || member.getAuthority() == Authority.CHIEF) {
                result.put("isValid", true);
                result.put("member", optionalMember.get());
                return result;
            }
        }
        result.put("isValid", false);
        return result;
    }

    public HashMap<String, Object> isCaptainOrChiefOrCreatorOfMission(Mission mission, String loggedInEmail) {
        HashMap<String, Object> result = new HashMap<>();
        Optional<MissionMember> optionalMissionMember = missionMemberRepository.findByMissionAndMemberUserEmail(mission, loggedInEmail);
        if (optionalMissionMember.isPresent()) {
            MissionMember missionMember = optionalMissionMember.get();
            if (missionMember.getAuthority() == Authority.CREATOR || missionMember.getAuthority() == Authority.CHIEF || missionMember.getAuthority() == Authority.CAPTAIN) {
                result.put("isValid", true);
                result.put("missionMember", optionalMissionMember.get());
                return result;
            }
        }
        result.put("isValid", false);
        return result;
    }

    public HashMap<String, Object> isValidMissionMember(Mission mission, String email) {
        HashMap<String, Object> result = new HashMap<>();
        Optional<MissionMember> optionalMissionMember = missionMemberRepository.findByMissionAndMemberUserEmail(mission, email);
        if (optionalMissionMember.isPresent()) {
            MissionMember missionMember = optionalMissionMember.get();
            result.put("isValid", true);
            result.put("missionMember", optionalMissionMember.get());
            return result;

        }
        result.put("isValid", false);
        return result;
    }

    public HashMap<String, Object> isValidMissionMemberById(long id) {
        HashMap<String, Object> result = new HashMap<>();
        Optional<MissionMember> optionalMissionMember = missionMemberRepository.findById(id);
        if (optionalMissionMember.isPresent()) {
            MissionMember missionMember = optionalMissionMember.get();
            result.put("isValid", true);
            result.put("missionMember", optionalMissionMember.get());
            return result;

        }
        result.put("isValid", false);
        return result;
    }
}
