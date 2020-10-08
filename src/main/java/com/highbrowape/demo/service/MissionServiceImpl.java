package com.highbrowape.demo.service;

import com.highbrowape.demo.dto.input.*;
import com.highbrowape.demo.entity.*;
import com.highbrowape.demo.exception.*;
import com.highbrowape.demo.repository.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.HashMap;
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
    MemberRepository memberRepository;

    @Autowired
    MissionMemberRepository missionMemberRepository;

    @Autowired
    NoteRepository noteRepository;

    @Autowired
    LinkRepository linkRepository;


    @Override
    public ResponseEntity<?> addMissionAtLevel1(MissionAddDto missionAddDto, Long id, String loggedInEmail) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        HashMap<String,Object> userMap= isValidUser(loggedInEmail);
        if(!(boolean)userMap.get("isValid")) throw  new UserNotFoundException(loggedInEmail+" is not a valid user ");
        User user= (User)userMap.get("user");

        HashMap<String,Object> projectMap=isValidProject(id);
        if(!(boolean)projectMap.get("isValid")) throw  new ProjectNotFoundException(" No project found with  id :"+id);
        Project project=(Project)projectMap.get("project");

        if(!(boolean)isCreatorOrChiefOfProject(project,loggedInEmail).get("isValid"))
            throw new InvalidAuthorityException(loggedInEmail+" is not allowed to add missions to the project");


        Mission mission= mapper.map(missionAddDto,Mission.class);
        long totalMissions=countMissions(project,1,null);
        mission.setMissionId(totalMissions+1+"");
        mission.setLevel(1);
        mission.setProject(project);
        mission.setAddedBy(loggedInEmail);

        try{
            mission=missionRepository.save(mission);
        }catch (Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
        if(missionAddDto.getNotes()!=null) {
            for (NoteDto note : missionAddDto.getNotes()) {
                Note note1  = mapper.map(note, Note.class);
                note1.setMission(mission);
                note1.setAddedBy(loggedInEmail);
                mission.addNote(note1);
                noteRepository.save(note1);

            }
        }
        if(missionAddDto.getLinks()!=null) {
            for (LinkDto link : missionAddDto.getLinks()) {
                Link link1  = mapper.map(link, Link.class);
                link1.setMission(mission);
                link1.setAddedBy(loggedInEmail);
                mission.addLink(link1);
                linkRepository.save(link1);
            }
        }

        if(missionAddDto.getMember()!=null) {
            for (ProjectMemberDto pmd : missionAddDto.getMember()) {
                HashMap<String,Object> userMap1= isValidProjectMember(project,pmd.getEmail());

                if((boolean)userMap1.get("isValid"))  {
                    User user1= (User)userMap.get("user");
                    Optional<MissionMember> optionalMissionMember = missionMemberRepository.findByMissionAndUserEmail(mission, pmd.getEmail());
                    if (optionalMissionMember.isPresent()) continue;
                    MissionMember member = mapper.map(pmd, MissionMember.class);
                    member.setAddedBy(loggedInEmail);
                    member.setMission(mission);
                    member.setUser(user1);
                    mission.addMissionMember(member);
                    userRepository.save(user1);
                    missionMemberRepository.save(member);
                }
            }
        }
        return new ResponseEntity<>(missionRepository.save(mission), HttpStatus.ACCEPTED);
    }

    private long countMissions(Project project, long level, Mission missionParent) {
        if(missionParent==null)
       return missionRepository.countByProjectAndLevel(project,level);
        return missionRepository.countByMissionParent(missionParent);

    }

    @Override
    public ResponseEntity<?> addMissionAtLevelN(MissionAddDto missionAddDto, Long id, String loggedInEmail) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        HashMap<String,Object> userMap= isValidUser(loggedInEmail);
        if(!(boolean)userMap.get("isValid")) throw  new UserNotFoundException(loggedInEmail+" is not a valid user ");
        User user= (User)userMap.get("user");

        HashMap<String, Object> missionMap = isValidMission(id);
        if (!(boolean) missionMap.get("isValid"))
            throw new MissionNotFoundException(" No Mission found with mission id :" + id);
        Mission mission = (Mission) missionMap.get("mission");

        HashMap<String, Object> creatorOrChiefOfProjectMap = isCreatorOrChiefOfProject(mission.getProject(), loggedInEmail);
        HashMap<String, Object> captainOrChiefOrCreatorOfMissionMap=isCaptainOrChiefOrCreatorOfMission(mission, loggedInEmail);
        if (!(boolean) creatorOrChiefOfProjectMap.get("isValid")) {

            if (!(boolean) captainOrChiefOrCreatorOfMissionMap.get("isValid"))
                throw new InvalidAuthorityException(loggedInEmail + "  is not allowed to add member to the mission ");
        }

        Mission mission1= mapper.map(missionAddDto,Mission.class);

        long totalMissions=countMissions(mission.getProject(),mission.getLevel()+1,mission)+1;
        mission1.setMissionId(mission.getMissionId()+"."+totalMissions);
        mission1.setLevel(mission.getLevel()+1);
        mission1.setMissionParent(mission);
        mission1.setProject(mission.getProject());
        mission1.setAddedBy(loggedInEmail);

        try{
            mission=missionRepository.save(mission1);
        }catch (Exception ex){
            throw new RuntimeException(ex.getMessage());
        }
        if(missionAddDto.getNotes()!=null) {
            for (NoteDto note : missionAddDto.getNotes()) {
                Note note1  = mapper.map(note, Note.class);
                note1.setMission(mission1);
                note1.setAddedBy(loggedInEmail);
                mission1.addNote(note1);
                noteRepository.save(note1);

            }
        }
        if(missionAddDto.getLinks()!=null) {
            for (LinkDto link : missionAddDto.getLinks()) {
                Link link1  = mapper.map(link, Link.class);
                link1.setMission(mission1);
                link1.setAddedBy(loggedInEmail);
                mission1.addLink(link1);
                linkRepository.save(link1);
            }
        }

        if(missionAddDto.getMember()!=null) {
            for (ProjectMemberDto pmd : missionAddDto.getMember()) {
                HashMap<String,Object> userMap1= isValidProjectMember(mission.getProject(),pmd.getEmail());

                if((boolean)userMap1.get("isValid"))  {
                    User user1= (User)userMap.get("user");
                    Optional<MissionMember> optionalMissionMember = missionMemberRepository.findByMissionAndUserEmail(mission, pmd.getEmail());
                    if (optionalMissionMember.isPresent()) continue;
                    MissionMember member = mapper.map(pmd, MissionMember.class);
                    member.setAddedBy(loggedInEmail);
                    member.setMission(mission1);
                    member.setUser(user1);
                    mission1.addMissionMember(member);
                    userRepository.save(user1);
                    missionMemberRepository.save(member);
                }
            }
        }
        return new ResponseEntity<>(missionRepository.save(mission1), HttpStatus.ACCEPTED);

    }

    @Override
    public ResponseEntity<?> getMissionMembers(Long id, String name) {
        return null;
    }

    @Override
    public ResponseEntity<?> addMemberToMission(ProjectMemberDto projectMemberDto, Long id, String loggedInEmail) {
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


        HashMap<String, Object> creatorOrChiefOfProjectMap = isCreatorOrChiefOfProject(mission.getProject(), loggedInEmail);
        HashMap<String, Object> captainOrChiefOrCreatorOfMissionMap=isCaptainOrChiefOrCreatorOfMission(mission, loggedInEmail);
        if (!(boolean) creatorOrChiefOfProjectMap.get("isValid")) {

            if (!(boolean) captainOrChiefOrCreatorOfMissionMap.get("isValid"))
                throw new InvalidAuthorityException(loggedInEmail + "  is not allowed to add member to the mission ");
        }
        MissionMember missionMember = (MissionMember) captainOrChiefOrCreatorOfMissionMap.get("missionMember");

        HashMap<String, Object> missionMemberMap = isValidMissionMember(mission,projectMemberDto.getEmail());
        if (!(boolean) missionMap.get("isValid")) {

            ModelMapper mapper = new ModelMapper();
            mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

            MissionMember missionMember1 = mapper.map(projectMemberDto, MissionMember.class);
            missionMember1.setAddedBy(loggedInEmail);
            missionMember1.setMission(mission);
            missionMember1.setUser(user1);

            mission.addMissionMember(missionMember1);
            missionMemberRepository.save(missionMember1);

        }
        return new ResponseEntity<>(missionRepository.save(mission), HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> updateMemberAuthorityOfMission(Authority authority, Long id, String loggedInEmail) {

    MissionMember missionMember=updateAndRemoveMemberFromMissionPreProcessing(id,loggedInEmail);
        missionMember.setAuthority(authority);

        return new ResponseEntity<>(missionMemberRepository.save(missionMember),HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> removeMemberFromMission(Long id, String loggedInEmail) {
        MissionMember missionMember=updateAndRemoveMemberFromMissionPreProcessing(id,loggedInEmail);
        missionMember.getMission().getMissionMembers().remove(missionMember);
        missionMemberRepository.delete(missionMember);
        return new ResponseEntity<>("mission Member Deleted Succesfully ",HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getMissionNotes(Long id, String name) {
        return null;
    }

    private MissionMember updateAndRemoveMemberFromMissionPreProcessing(long id,String loggedInEmail){
        HashMap<String, Object> userMap = isValidUser(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");



        HashMap<String, Object> missionMemberMap = isValidMissionMemberById(id);
        if(!(boolean)missionMemberMap.get("isValid")) throw new MissionMemberNotFoundException("NO mission member found with id :"+id);
        MissionMember missionMember=(MissionMember)missionMemberMap.get("missionMember");

        Mission mission=missionMember.getMission();

        HashMap<String, Object> creatorOrChiefOfProjectMap = isCreatorOrChiefOfProject(mission.getProject(), loggedInEmail);
        HashMap<String, Object> captainOrChiefOrCreatorOfMissionMap=isCaptainOrChiefOrCreatorOfMission(mission, loggedInEmail);
        if (!(boolean) creatorOrChiefOfProjectMap.get("isValid")) {

            if (!(boolean) captainOrChiefOrCreatorOfMissionMap.get("isValid"))
                throw new InvalidAuthorityException(loggedInEmail + "  is not allowed to add member to the mission ");
        }
        return missionMember;
    }

    @Override
    public ResponseEntity<?> addNoteToMission(NoteDto noteDto, Long id, String loggedInEmail) {
        HashMap<String, Object> userMap = isValidUser(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        HashMap<String, Object> missionMap = isValidMission(id);
        if (!(boolean) missionMap.get("isValid"))
            throw new MissionNotFoundException(" No Mission found with  id :" + id);
        Mission mission = (Mission) missionMap.get("mission");

        HashMap<String, Object> missionMemberMap = isValidMissionMember(mission,loggedInEmail);
        if(!(boolean)missionMemberMap.get("isValid")) throw new MissionMemberNotFoundException("No mission member found with mission id :"+id+" and email "+loggedInEmail);
        MissionMember missionMember=(MissionMember)missionMemberMap.get("missionMember");




        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Note note1  = mapper.map(noteDto, Note.class);
        note1.setMission(mission);
        note1.setAddedBy(loggedInEmail);
        note1.setAddedOn(new java.util.Date());
        mission.addNote(note1);
        noteRepository.save(note1);


        return new ResponseEntity<>(missionRepository.save(mission),HttpStatus.ACCEPTED);

    }

    @Override
    public ResponseEntity<?> updateNoteOfMission(NoteDto noteDto, Long id, String loggedInEmail) {
        HashMap<String, Object> userMap = isValidUser(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        Optional<Note> noteOptional=noteRepository.findById(id);
        if(!noteOptional.isPresent()) throw new NoteNotFoundException("No note found with  id  "+ id);

        Note note=noteOptional.get();

        Mission mission=note.getMission();

        HashMap<String, Object> missionMemberMap = isValidMissionMember(mission,loggedInEmail);
        if(!(boolean)missionMemberMap.get("isValid")) throw new MissionMemberNotFoundException("No mission member found with mission id :"+id+" and email "+loggedInEmail);
        MissionMember missionMember=(MissionMember)missionMemberMap.get("missionMember");


        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Note note1  = mapper.map(noteDto, Note.class);
        note1.setId(note.getId());
        note1.setMission(mission);
        note1.setAddedBy(loggedInEmail);
        note1.setAddedOn(new java.util.Date());
        noteRepository.save(note1);


        return new ResponseEntity<>("Note Updated",HttpStatus.ACCEPTED);


    }

    @Override
    public ResponseEntity<?> removeNoteFromMission(Long id, String loggedInEmail) {
            HashMap<String, Object> userMap = isValidUser(loggedInEmail);
            if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
            User user = (User) userMap.get("user");

            Optional<Note> noteOptional=noteRepository.findById(id);
            if(!noteOptional.isPresent()) throw new NoteNotFoundException("No note found with  id  "+ id);

            Note note=noteOptional.get();

            Mission mission=note.getMission();

            HashMap<String, Object> missionMemberMap = isValidMissionMember(mission,loggedInEmail);
            if(!(boolean)missionMemberMap.get("isValid")) throw new MissionMemberNotFoundException("No mission member found with mission id :"+id+" and email "+loggedInEmail);
            MissionMember missionMember=(MissionMember)missionMemberMap.get("missionMember");

        try {

            mission.getNotes().remove(note);
            noteRepository.delete(note);
        }catch(Exception ex) {
            throw new ProjectNoteNotFoundException("No note found with id  " + id);
        }

        return new ResponseEntity<>("MISSION NOTE DELETED SUCCESSFULLY",HttpStatus.ACCEPTED);


    }

    @Override
    public ResponseEntity<?> getMissionLinks(Long id, String name) {
        return null;
    }

    @Override
    public ResponseEntity<?> addLinkToMission(LinkDto linkDto, Long id, String loggedInEmail) {
        HashMap<String, Object> userMap = isValidUser(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        HashMap<String, Object> missionMap = isValidMission(id);
        if (!(boolean) missionMap.get("isValid"))
            throw new MissionNotFoundException(" No Mission found with  id :" + id);
        Mission mission = (Mission) missionMap.get("mission");

        HashMap<String, Object> missionMemberMap = isValidMissionMember(mission,loggedInEmail);
        if(!(boolean)missionMemberMap.get("isValid")) throw new MissionMemberNotFoundException("No mission member found with mission id :"+id+" and email "+loggedInEmail);
        MissionMember missionMember=(MissionMember)missionMemberMap.get("missionMember");




        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Link link1 = mapper.map(linkDto, Link.class);
        link1.setMission(mission);
        link1.setAddedBy(loggedInEmail);
        link1.setAddedOn(new java.util.Date());
        mission.addLink(link1);
        linkRepository.save(link1);


        return new ResponseEntity<>(missionRepository.save(mission),HttpStatus.ACCEPTED);

    }

    @Override
    public ResponseEntity<?> updateLinkOfMission(LinkDto linkDto, Long id, String loggedInEmail) {

        HashMap<String, Object> userMap = isValidUser(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        Optional<Link> linkOptional=linkRepository.findById(id);
        if(!linkOptional.isPresent()) throw new LinkNotFoundException("No link found with  id  "+ id);

        Link link=linkOptional.get();

        Mission mission=link.getMission();

        HashMap<String, Object> missionMemberMap = isValidMissionMember(mission,loggedInEmail);
        if(!(boolean)missionMemberMap.get("isValid")) throw new MissionMemberNotFoundException("No mission member found with mission id :"+id+" and email "+loggedInEmail);
        MissionMember missionMember=(MissionMember)missionMemberMap.get("missionMember");


        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Link link1 = mapper.map(linkDto, Link.class);
        link1.setId(link.getId());
        link1.setMission(mission);
        link1.setAddedBy(loggedInEmail);
        link1.setAddedOn(new java.util.Date());
        linkRepository.save(link1);

        return new ResponseEntity<>("Link Updated",HttpStatus.ACCEPTED);


    }

    @Override
    public ResponseEntity<?> removeLinkFromMission(Long id, String loggedInEmail) {
        HashMap<String, Object> userMap = isValidUser(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        Optional<Link> linkOptional=linkRepository.findById(id);
        if(!linkOptional.isPresent()) throw new LinkNotFoundException("No link found with  id  "+ id);

        Link link=linkOptional.get();

        Mission mission=link.getMission();

        HashMap<String, Object> missionMemberMap = isValidMissionMember(mission,loggedInEmail);
        if(!(boolean)missionMemberMap.get("isValid")) throw new MissionMemberNotFoundException("No mission member found with mission id :"+id+" and email "+loggedInEmail);
        MissionMember missionMember=(MissionMember)missionMemberMap.get("missionMember");

        try {

            mission.getLinks().remove(link);
            linkRepository.delete(link);
        }catch(Exception ex) {
            throw new ProjectNoteNotFoundException("No link found with id  " + id);
        }

        return new ResponseEntity<>("MISSION LINK DELETED SUCCESSFULLY",HttpStatus.ACCEPTED);

    }

    @Override
    public ResponseEntity<?> getMissionObjectives(Long id, String name) {
        return null;
    }

    @Override
    public ResponseEntity<?> addObjectiveToMission(ObjectiveDto objectiveDto, Long id, String name) {
        return null;
    }

    @Override
    public ResponseEntity<?> updateObjectiveOfMission(ObjectiveDto objectiveDto, Long id, String name) {
        return null;
    }

    @Override
    public ResponseEntity<?> removeObjectiveFromMission(Long id, String name) {
        return null;
    }

    public HashMap<String ,Object> isValidUser(String email) {

        HashMap<String,Object> result= new HashMap<>();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            result.put("isValid",true);
            result.put("user",optionalUser.get());
          return result;
        }
        result.put("isValid",false);
        return result;
    }

    public HashMap<String ,Object> isValidProject(long id) {
        HashMap<String,Object> result= new HashMap<>();
        Optional<Project> optionalProject = projectRepository.findByProjectId(id);
        if (optionalProject.isPresent()) {
            result.put("isValid",true);
            result.put("project",optionalProject.get());
            return result;
        }
        result.put("isValid",false);
        return result;
    }

    public HashMap<String ,Object> isValidProjectMember(Project project,String email) {
        HashMap<String,Object> result= new HashMap<>();
        Optional<Member> optionalMember = memberRepository.findByProjectAndUserEmail(project,email);
        if (optionalMember.isPresent()) {
            result.put("isValid",true);
            result.put("member",optionalMember.get());
            return result;
        }
        result.put("isValid",false);
        return result;
    }
    public HashMap<String ,Object> isValidMission(long id) {
        HashMap<String,Object> result= new HashMap<>();
        Optional<Mission> optionalMission = missionRepository.findByMissionId(id);
        if (optionalMission.isPresent()) {
            result.put("isValid",true);
            result.put("mission",optionalMission.get());
            return result;
        }
        result.put("isValid",false);
        return result;
    }

    public HashMap<String ,Object> isCreatorOrChiefOfProject(Project project,String loggedInEmail) {
        HashMap<String,Object> result= new HashMap<>();
        Optional<Member> optionalMember = memberRepository.findByProjectAndUserEmail(project,loggedInEmail);
        if (optionalMember.isPresent()) {
            Member member=optionalMember.get();
            if (member.getAuthority() == Authority.CREATOR || member.getAuthority() == Authority.CHIEF) {
                result.put("isValid", true);
                result.put("member", optionalMember.get());
                return result;
            }
        }
        result.put("isValid",false);
        return result;
    }

    public HashMap<String ,Object> isCaptainOrChiefOrCreatorOfMission(Mission mission,String loggedInEmail) {
        HashMap<String,Object> result= new HashMap<>();
        Optional<MissionMember> optionalMissionMember = missionMemberRepository.findByMissionAndUserEmail(mission,loggedInEmail);
        if (optionalMissionMember.isPresent()) {
            MissionMember missionMember=optionalMissionMember.get();
            if (missionMember.getAuthority() == Authority.CREATOR || missionMember.getAuthority() == Authority.CHIEF||missionMember.getAuthority()==Authority.CAPTAIN) {
                result.put("isValid", true);
                result.put("missionMember", optionalMissionMember.get());
                return result;
            }
        }
        result.put("isValid",false);
        return result;
    }

    public HashMap<String ,Object> isValidMissionMember(Mission mission,String email) {
        HashMap<String,Object> result= new HashMap<>();
        Optional<MissionMember> optionalMissionMember = missionMemberRepository.findByMissionAndUserEmail(mission,email);
        if (optionalMissionMember.isPresent()) {
            MissionMember missionMember=optionalMissionMember.get();
                result.put("isValid", true);
                result.put("missionMember", optionalMissionMember.get());
                return result;

        }
        result.put("isValid",false);
        return result;
    }

    public HashMap<String ,Object> isValidMissionMemberById(long id) {
        HashMap<String,Object> result= new HashMap<>();
        Optional<MissionMember> optionalMissionMember = missionMemberRepository.findById(id);
        if (optionalMissionMember.isPresent()) {
            MissionMember missionMember=optionalMissionMember.get();
            result.put("isValid", true);
            result.put("missionMember", optionalMissionMember.get());
            return result;

        }
        result.put("isValid",false);
        return result;
    }
}
