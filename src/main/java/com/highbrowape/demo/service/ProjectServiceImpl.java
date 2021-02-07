package com.highbrowape.demo.service;


import com.highbrowape.demo.dto.input.*;
import com.highbrowape.demo.dto.output.MissionDto;
import com.highbrowape.demo.dto.output.ProjectDashboardDto;
import com.highbrowape.demo.dto.output.ProjectDetailDto;
import com.highbrowape.demo.dto.output.ProjectListDto;
import com.highbrowape.demo.entity.*;
import com.highbrowape.demo.exception.*;
import com.highbrowape.demo.repository.*;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.ZoneId;
import java.util.*;

@Service
public class ProjectServiceImpl implements IProjectService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectInsightRepository projectInsightRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProjectLinkRepository projectLinkRepository;

    @Autowired
    ProjectNoteRepository projectNoteRepository;

    @Autowired
    MissionRepository missionRepository;

    @Autowired
    IInsightService insightService;

    @Autowired
    INotificationService notificationService;

    @Autowired
    ProjectConversationRepository projectConversationRepository;

    @Autowired
    ProjectCommentRepository projectCommentRepository;

    public ResponseEntity<?> addProject(ProjectAdd projectAdd, String loggedInEmail) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Project project = mapper.map(projectAdd, Project.class);
        Optional<User> optionalUser = userRepository.findByEmail(loggedInEmail);
        User user = null;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            Optional<Project> projectOptional = projectRepository.findByUserAndProjectId(user, projectAdd.getProjectId().toUpperCase());
            if (projectOptional.isPresent())
                throw new ProjectAlreadyExistException("Project with id-- " + projectAdd.getProjectId() + " already exist ");

        }
        else throw new UserNotFoundException("User with email "+loggedInEmail+ " not found ");
        project.setUser(user);
        project.setProjectId(projectAdd.getProjectId().toUpperCase());

        try {
            project = projectRepository.save(project);
        } catch (DataIntegrityViolationException exception) {
            throw new ProjectAlreadyExistException("Project With projectId " + projectAdd.getProjectId() + " already exist ");
        }
        Member member=new Member(loggedInEmail,project,user,Authority.CREATOR,new java.util.Date());
        project.addMember(member);
        memberRepository.save(member);

        MemberInsight memberInsight= new MemberInsight();
        memberInsight.setMember(member);
        member.setMemberInsight(memberInsight);


        if(projectAdd.getMember()!=null) {
            for (ProjectMemberDto pmd : projectAdd.getMember()) {
                if (isValidUser(pmd.getEmail())) {
                    Optional<User> optional= userRepository.findByEmail(pmd.getEmail());
                    User user1=optional.get();
                    Optional<Member> optionalMember2 = memberRepository.findByProjectAndUserEmail(project, pmd.getEmail());
                    if (optionalMember2.isPresent()) continue;
                    Member member1 = mapper.map(pmd, Member.class);
                    member1.setAddedBy(loggedInEmail);
                    member1.setAddedOn(new java.util.Date());
                    member1.setProject(project);
                    member1.setUser(user1);
                    project.addMember(member1);

                     memberInsight= new MemberInsight();
                    memberInsight.setMember(member1);
                    member1.setMemberInsight(memberInsight);
                  //  project.addUserProjectObjectiveInsight(memberInsight);

                    userRepository.save(user1);
                    memberRepository.save(member1);
                }
            }
        }
        if(projectAdd.getLinks()!=null) {
            for (LinkDto link : projectAdd.getLinks()) {

                    ProjectLink link1  = mapper.map(link, ProjectLink.class);
                    link1.setProject(project);
                    link1.setAddedBy(loggedInEmail);
                    link1.setAddedOn(new java.util.Date());
                    project.addLink(link1);
                    projectLinkRepository.save(link1);

            }
        }
        if(projectAdd.getNotes()!=null) {
            for (NoteDto note : projectAdd.getNotes()) {

                ProjectNote note1  = mapper.map(note, ProjectNote.class);
                note1.setProject(project);
                note1.setAddedBy(loggedInEmail);
                note1.setAddedOn(new java.util.Date());
                project.addNote(note1);
                projectNoteRepository.save(note1);

            }
        }

        insightService.userActionUpdate(loggedInEmail,"New Project Added "+project.getProjectName());

        ProjectInsight projectInsight = new ProjectInsight();
        projectInsight.setProject(project);
        project.setProjectInsight(projectInsight);



           projectInsightRepository.save(projectInsight);
        return new ResponseEntity<>(projectRepository.save(project), HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> updateProject(ProjectUpdate projectUpdate, String loggedInEmail) {
        Optional<Project> projectOptional = projectRepository.findById(projectUpdate.getId());


        if (!projectOptional.isPresent())
            throw new ProjectNotFoundException("No Project Found with id " + projectUpdate.getId());
        Project project = projectOptional.get();
        User user=project.getUser();
        Date date=project.getAddedOn();


        Optional<Member> optionalMember= memberRepository.findByProjectAndUserEmail(project,loggedInEmail);
        if(!optionalMember.isPresent())
            throw new InvalidAuthorityException(loggedInEmail + " is not allowed to update the project ");


        if(optionalMember.get().getAuthority()==Authority.CREATOR) {
            ModelMapper mapper = new ModelMapper();
            mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
             project = mapper.map(projectUpdate, Project.class);
             project.setId(project.getId());
             project.setUser(user);
             project.setAddedOn(date);
            project.setProjectId(projectUpdate.getProjectId().toUpperCase());



            try {

                //        ProjectNotification projectNotification= new ProjectNotification();


                projectRepository.save(project);

            } catch (DataIntegrityViolationException exception) {
                throw new ProjectAlreadyExistException("Project With projectId " + projectUpdate.getProjectId() + " already exist ");
            }

            memberRepository.deleteAllByProjectId(projectUpdate.getId());
            Member member1 =new Member(loggedInEmail,project,user,Authority.CREATOR,new java.util.Date());
            //project.addMember(member1);
            memberRepository.save(member1);
            if(projectUpdate.getMember()!=null) {
                for (ProjectMemberDto pmd : projectUpdate.getMember()) {
                    if (isValidUser(pmd.getEmail())) {
                        Optional<User> optional = userRepository.findByEmail(pmd.getEmail());
                        User user1 = optional.get();

                        Optional<Member> optionalMember2 = memberRepository.findByProjectAndUserEmail(project, pmd.getEmail());
                        if (optionalMember2.isPresent()) continue;
                        Member member = mapper.map(pmd, Member.class);
                        member.setAddedBy(loggedInEmail);
                        member.setAddedOn(new java.util.Date());
                        member.setProject(project);
                        member.setUser(user1);
                        project.addMember(member);
                        userRepository.save(user1);
                        memberRepository.save(member);


                    }
                }
            }

            if(projectUpdate.getLinks()!=null) {
                for (LinkDto link : projectUpdate.getLinks()) {

                    ProjectLink link1  = mapper.map(link, ProjectLink.class);
                    link1.setProject(project);
                    link1.setAddedBy(loggedInEmail);
                    link1.setAddedOn(new java.util.Date());
                    project.addLink(link1);
                    projectLinkRepository.save(link1);

                }
            }
            if(projectUpdate.getNotes()!=null) {
                for (NoteDto note : projectUpdate.getNotes()) {

                    ProjectNote note1  = mapper.map(note, ProjectNote.class);
                    note1.setProject(project);
                    note1.setAddedBy(loggedInEmail);
                    note1.setAddedOn(new java.util.Date());
                    project.addNote(note1);
                    projectNoteRepository.save(note1);

                }
            }

        }
        else  throw new InvalidAuthorityException(loggedInEmail + " is not allowed to update the project ");

        insightService.userActionUpdate(loggedInEmail,project.getProjectName()+" Project Updated");

        Project updatedProject =projectRepository.save(project);
        notificationService.addProjectNotification(updatedProject,loggedInEmail+" updated project");

        return new ResponseEntity<>(updatedProject, HttpStatus.OK);
    }



    @Override
    public ResponseEntity<ProjectDetailDto> getProjectDetail(String projectId, String loggedInEmail) {

        if(!isValidUser(loggedInEmail)) throw  new UserNotFoundException(loggedInEmail+" is not a valid user ");
        Optional<Project> projectOptional=projectRepository.findByProjectId(projectId);

        if(!projectOptional.isPresent()) throw new ProjectNotFoundException("No project found with project id  "+projectId);

        if(!isValidMember(projectId,loggedInEmail)) throw new InvalidAuthorityException(loggedInEmail+ " is not member of the project with id "+projectId);

        Project project=projectOptional.get();

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        ProjectDetailDto projectDetailDto= mapper.map(project,ProjectDetailDto.class);

        projectDetailDto.setChiefCount(memberRepository.countByProjectAndAuthority(project,Authority.CHIEF));
        projectDetailDto.setSoldierCount(memberRepository.countByProjectAndAuthority(project,Authority.SOLDIER));
        projectDetailDto.setMemberCount(project.getMembers().size());
        projectDetailDto.setMissionCount(missionRepository.countByProject(project));
        projectDetailDto.setProjectInsight(project.getProjectInsight());


        return new ResponseEntity<ProjectDetailDto>(projectDetailDto,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getMyCreatedProjectList(int pageNumber, String loggedInEmail) {
        if(!isValidUser(loggedInEmail)) throw  new UserNotFoundException(loggedInEmail+" is not a valid user ");
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        double totalPages=Math.ceil(projectRepository.countByUserEmail(loggedInEmail)/2);
        Pageable pageable = PageRequest.of(pageNumber, 2);
        List<Project> projects= projectRepository.findByUserEmail(loggedInEmail,pageable);
        List<ProjectListDto> projectList=new ArrayList<>();

        for(Project p:projects){
            ProjectListDto project= mapper.map(p, ProjectListDto.class);
            long totalMembers=(long)memberRepository.countByProject(p);
            project.setTotalMembers(totalMembers);
            project.setTotalMissions(p.getMissions().size());
            project.setMissions(missionRepository.findByProjectProjectIdAndMissionMembersMember(p.getProjectId(),memberRepository.findByProjectAndUserEmail(p,loggedInEmail).get()));
            project.setProjectInsight(p.getProjectInsight());
            projectList.add(project);

        }
        HashMap<String,Object> result = new HashMap<>();
        result.put("totalPages",new Double(totalPages));
        result.put("data",projectList);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }



    @Override
    public ResponseEntity<?> getProjectDashboard(String projectId, String loggedInEmail) {
        if(!isValidUser(loggedInEmail)) throw  new UserNotFoundException(loggedInEmail+" is not a valid user ");
        Optional<Project> projectOptional=projectRepository.findByProjectId(projectId);

        if(!projectOptional.isPresent()) throw new ProjectNotFoundException("No project found with project id  "+projectId);

        if(!isValidMember(projectId,loggedInEmail)) throw new InvalidAuthorityException(loggedInEmail+ " is not member of the project with id "+projectId);

        Project project=projectOptional.get();

        List<MissionDto> list= new ArrayList<>();

        HashMap<Long, MissionDto> hashMap= new HashMap<>();

        Queue<Mission> queue = new LinkedList<>();

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);

        for(Mission m:missionRepository.findByProjectProjectIdAndLevel(projectId,1))
              queue.add(m);

        while(!queue.isEmpty()){


            Mission mission= queue.remove();

            System.out.println(mission.getMissionId()+"===");

            if(mission.getMissions()!=null)
            for (Mission missionTemp:mission.getMissions()) {
                queue.add(missionTemp);
                System.out.println(missionTemp.getMissionId()+" added");
            }


            MissionDto missionDto=mapper.map(mission,MissionDto.class);

            if(mission.getMissionParent()!=null)
            missionDto.setParentId(mission.getMissionParent().getMissionId());

            missionDto.setMemberCount(mission.getMissionMembers().size());
//            missionDto.setNoteCount(mission.getMissionNotes().size());
//            missionDto.setObjectiveCount(mission.getMissionInsight().getObjectiveCount());
//            missionDto.setCompletedObjectiveCount(mission.getMissionInsight().getCompletedObjectiveCount());
            missionDto.setMissionInsight(mission.getMissionInsight());
          //  missionDto.setLinkCount(mission.getMissionLinks().size());
            missionDto.setConversationCount(mission.getConversations().size());
            missionDto.setChildren(new ArrayList<>());


            if(mission.getMissionParent()!=null && mission.getMissionParent().getMissionId()!=null)
            {
//                System.out.println(hashMap);
               System.out.println("ADDING ..... "+mission.getMissionId()+"   "+mission.getMissionParent().getMissionId());

                MissionDto missionDtoTemp = hashMap.get(mission.getMissionParent().getId());
                if(missionDtoTemp.getChildren()==null)
                    missionDtoTemp.setChildren(new ArrayList<>());
                missionDtoTemp.getChildren().add(missionDto);
            }else{
                list.add(missionDto);
            }

            System.out.println(queue);

             hashMap.put(mission.getId(),missionDto);

        }

        ProjectDashboardDto projectDashboardDto=mapper.map(project,ProjectDashboardDto.class);
        projectDashboardDto.setProjectInsight(project.getProjectInsight());

        HashMap<String, Object> result= new HashMap<>();
        result.put("project",projectDashboardDto);
        result.put("children",list);

        return new ResponseEntity<>(result,HttpStatus.OK);


    }





    @Override
    public ResponseEntity<?> getProjectList(int pageNumber, String loggedInEmail) {

        if(!isValidUser(loggedInEmail)) throw  new UserNotFoundException(loggedInEmail+" is not a valid user ");
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        double totalPages=Math.ceil(memberRepository.countByUserEmail(loggedInEmail)/2);
        Pageable pageable = PageRequest.of(pageNumber, 10);
        User user= userRepository.findByEmail(loggedInEmail).get();
        List<Member> members= memberRepository.findByUserEmail(loggedInEmail,pageable);
        List<ProjectListDto> projectList=new ArrayList<>();

        for(Member m:members){
            Project p=m.getProject();
            ProjectListDto project= mapper.map(p, ProjectListDto.class);
            long totalMembers=(long)memberRepository.countByProject(p);
            project.setTotalMembers(totalMembers);
            project.setTotalMissions(p.getMissions().size());
            projectList.add(project);
        }
        HashMap<String,Object> result = new HashMap<>();
        result.put("totalPages",new Double(totalPages));
        result.put("data",projectList);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }



    @Override
    public ResponseEntity<?> getProjectMembers(String id, String loggedInEmail) {
        if (!isValidUser(loggedInEmail)) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");

        HashMap<String, Object> projectMemberMap = isValidProjectMember(id,loggedInEmail);
        if(!(boolean)projectMemberMap.get("isValid")) throw new MemberNotFoundException("No project found with projectID:"+id+" and email "+loggedInEmail);

        List<Member> members=memberRepository.findByProjectProjectId(id);


        return new ResponseEntity<>(members,HttpStatus.OK);

    }


        @Override
    public ResponseEntity<?> addMemberToProject(String id, ProjectMemberDto projectMemberDto, String loggedInEmail) {
        if (!isValidUser(loggedInEmail)) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");

        Optional<Project> projectOptional = projectRepository.findByProjectId(id);
        if (!projectOptional.isPresent()) throw new ProjectNotFoundException("No project found with project id  " + id);
        Project project = projectOptional.get();

        Optional<Member> optionalMember = memberRepository.findByProjectAndUserEmail(project, loggedInEmail);
        if (!optionalMember.isPresent())
            throw new InvalidAuthorityException(loggedInEmail + " is not allowed to update the project ");


        if (optionalMember.get().getAuthority() == Authority.CREATOR || optionalMember.get().getAuthority() == Authority.CHIEF)
        {

                addMember(project,projectMemberDto,loggedInEmail);
        }

        else  throw new InvalidAuthorityException(loggedInEmail + " is not allowed to update the project ");

        insightService.userActionUpdate(loggedInEmail,"Members Added to "+project.getProjectName());

        Project updatedProject = projectRepository.save(project);
        notificationService.addProjectNotification(updatedProject,loggedInEmail+" added new members");

        return new ResponseEntity<>(updatedProject,HttpStatus.ACCEPTED);

    }

    @Override
    public ResponseEntity<?> addMembersToProject(String id, List<ProjectMemberDto> projectMemberDtoList, String loggedInEmail) {
        if (!isValidUser(loggedInEmail)) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");

        Optional<Project> projectOptional = projectRepository.findByProjectId(id);
        if (!projectOptional.isPresent()) throw new ProjectNotFoundException("No project found with project id  " + id);
        Project project = projectOptional.get();

        Optional<Member> optionalMember = memberRepository.findByProjectAndUserEmail(project, loggedInEmail);
        if (!optionalMember.isPresent())
            throw new InvalidAuthorityException(loggedInEmail + " is not allowed to update the project ");


        if (optionalMember.get().getAuthority() == Authority.CREATOR || optionalMember.get().getAuthority() == Authority.CHIEF)
            {
                for(ProjectMemberDto projectMemberDto:projectMemberDtoList)
                {
                    addMember(project,projectMemberDto,loggedInEmail);
                }
            }

        else  throw new InvalidAuthorityException(loggedInEmail + " is not allowed to update the project ");

        insightService.userActionUpdate(loggedInEmail,"Members Added to "+project.getProjectName());
        Project updatedProject = projectRepository.save(project);
        notificationService.addProjectNotification(updatedProject,loggedInEmail+" added new members");

        return new ResponseEntity<>(updatedProject,HttpStatus.ACCEPTED);
    }


    private void addMember(Project project,ProjectMemberDto projectMemberDto,String loggedInEmail){
        if(!isValidUser(projectMemberDto.getEmail())) throw new UserNotFoundException(projectMemberDto.getEmail() + " is not a valid user ");
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Optional<Member> optionalMember2 = memberRepository.findByProjectAndUserEmail(project, projectMemberDto.getEmail());
        if (!optionalMember2.isPresent()) {
            Member member = mapper.map(projectMemberDto, Member.class);
            member.setAddedBy(loggedInEmail);
            member.setAddedOn(new java.util.Date());
            member.setProject(project);
            member.setUser(userRepository.findByEmail(projectMemberDto.getEmail()).get());

            MemberInsight memberInsight= new MemberInsight();
            memberInsight.setMember(member);
            member.setMemberInsight(memberInsight);
            // project.addUserProjectObjectiveInsight(memberInsight);

            project.addMember(member);
            memberRepository.save(member);

        }


    }
    @Override
    public ResponseEntity<?> updateMemberAuthorityToProject(Long id, Authority authority, String loggedInEmail) {
        if(!isValidUser(loggedInEmail)) throw  new UserNotFoundException(loggedInEmail+" is not a valid user ");
        Optional<Member> memberOptional= memberRepository.findById(id);

        if(!memberOptional.isPresent()) throw new MemberNotFoundException("No member found with member id  "+ id);

        Member member= memberOptional.get();
        Project project = member.getProject();
        Optional<Member> optionalMember = memberRepository.findByProjectAndUserEmail(project, loggedInEmail);
        if (!optionalMember.isPresent())
            throw new InvalidAuthorityException(loggedInEmail + " is not allowed to update the project ");


        if (optionalMember.get().getAuthority() == Authority.CREATOR || optionalMember.get().getAuthority() == Authority.CHIEF)
        {
            member.setAuthority(authority);
            memberRepository.save(member);

            }

        else  throw new InvalidAuthorityException(loggedInEmail + " is not allowed to update the project ");

        insightService.userActionUpdate(loggedInEmail,member.getEmail()+" authority changed to "+authority+" in project "+project.getProjectName());


        return new ResponseEntity<>("Authority Updated",HttpStatus.ACCEPTED);

    }

    @Override
    public ResponseEntity<?> removeMemberFromProject(Long id, String loggedInEmail) {
        if(!isValidUser(loggedInEmail)) throw  new UserNotFoundException(loggedInEmail+" is not a valid user ");
        Optional<Member> memberOptional= memberRepository.findById(id);

        if(!memberOptional.isPresent()) throw new MemberNotFoundException("No member found with member id  "+ id);

        Member member= memberOptional.get();
        Project project = member.getProject();
        Optional<Member> optionalMember = memberRepository.findByProjectAndUserEmail(project, loggedInEmail);
        if (!optionalMember.isPresent())
            throw new InvalidAuthorityException(loggedInEmail + " is not allowed to update the project ");


        if (optionalMember.get().getAuthority() == Authority.CREATOR || optionalMember.get().getAuthority() == Authority.CHIEF)
        {

            project.getMembers().remove(member);
            memberRepository.delete(member);

        }
        else  throw new InvalidAuthorityException(loggedInEmail + " is not allowed to update the project ");


        insightService.userActionUpdate(loggedInEmail,member.getEmail()+" removed from project "+project.getProjectName());

        notificationService.addProjectNotification(project,member.getUser().getFirstName()+" is removed from thr project.");

        return new ResponseEntity<>("MEMBER REMOVED SUCCESSFULLY",HttpStatus.ACCEPTED);

    }

    @Override
    public ResponseEntity<?> getProjectNotes(String id, String loggedInEmail) {
        if (!isValidUser(loggedInEmail)) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");

        HashMap<String, Object> projectMemberMap = isValidProjectMember(id,loggedInEmail);
        if(!(boolean)projectMemberMap.get("isValid")) throw new MemberNotFoundException("No project found with projectID:"+id+" and email "+loggedInEmail);

        List<ProjectNote> projectNotes=projectNoteRepository.findByProjectProjectId(id);



        return new ResponseEntity<>(projectNotes,HttpStatus.OK);
    }


    @Override
    public ResponseEntity<?> addNoteToProject(String id, NoteDto noteDto, String loggedInEmail) {
        if (!isValidUser(loggedInEmail)) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");

        Optional<Project> projectOptional = projectRepository.findByProjectId(id);
        if (!projectOptional.isPresent()) throw new ProjectNotFoundException("No project found with project id  " + id);

        Project project = projectOptional.get();
        Optional<Member> optionalMember = memberRepository.findByProjectAndUserEmail(project, loggedInEmail);
        if (!optionalMember.isPresent())
            throw new InvalidAuthorityException(loggedInEmail + " is not allowed to update the project ");

            ModelMapper mapper = new ModelMapper();
            mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ProjectNote note1  = mapper.map(noteDto, ProjectNote.class);
        note1.setProject(project);
        note1.setAddedBy(loggedInEmail);
        note1.setAddedOn(new java.util.Date());
        project.addNote(note1);
        projectNoteRepository.save(note1);


        insightService.userActionUpdate(loggedInEmail,"Note added to project "+project.getProjectName());
        Project updatedProject = projectRepository.save(project);
        notificationService.addProjectNotification(updatedProject,loggedInEmail+" added new notes to the project");


        return new ResponseEntity<>(updatedProject,HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> updateNoteOfProject(Long id, NoteDto noteDto, String loggedInEmail) {
        if(!isValidUser(loggedInEmail)) throw  new UserNotFoundException(loggedInEmail+" is not a valid user ");
        Optional<ProjectNote> projectNoteOptional= projectNoteRepository.findById(id);

        if(!projectNoteOptional.isPresent()) throw new ProjectNoteNotFoundException("No note found with  id  "+ id);


        Project project = projectNoteOptional.get().getProject();
        Optional<Member> optionalMember = memberRepository.findByProjectAndUserEmail(project, loggedInEmail);
        if (!optionalMember.isPresent())
            throw new InvalidAuthorityException(loggedInEmail + " is not allowed to update the project ");


        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ProjectNote note1  = mapper.map(noteDto, ProjectNote.class);
        note1.setId(id);
        note1.setProject(project);
        note1.setAddedBy(loggedInEmail);
        note1.setAddedOn(projectNoteOptional.get().getAddedOn());
        projectNoteRepository.save(note1);


        insightService.userActionUpdate(loggedInEmail,"Note updated in project "+project.getProjectName());


        return new ResponseEntity<>("Note Updated",HttpStatus.ACCEPTED);

    }

    @Override
    public ResponseEntity<?> removeNoteFromProject(Long id, String loggedInEmail) {
        if (!isValidUser(loggedInEmail)) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");

        Optional<ProjectNote> projectNoteOptional = projectNoteRepository.findById(id);
        if (!projectNoteOptional.isPresent()) throw new ProjectNoteNotFoundException("No project note found with id  " + id);

        ProjectNote projectNote = projectNoteOptional.get();
        Project project= projectNote.getProject();
        Optional<Member> optionalMember = memberRepository.findByProjectAndUserEmail(projectNote.getProject(), loggedInEmail);
        if (!optionalMember.isPresent())
            throw new InvalidAuthorityException(loggedInEmail + " is not allowed to update the project ");
        try {

            project.getProjectNotes().remove(projectNote);
            projectNoteRepository.delete(projectNote);
        }catch(Exception ex) {
            throw new ProjectNoteNotFoundException("No project note found with id  " + id);
        }


        insightService.userActionUpdate(loggedInEmail,"Note removed from project "+project.getProjectName());

//        Project updatedProject = projectRepository.save(project);
        notificationService.addProjectNotification(project,loggedInEmail+" removed some notes from the project");

        return new ResponseEntity<>("PROJECT NOTE DELETED SUCCESSFULLY",HttpStatus.ACCEPTED);

    }

    @Override
    public ResponseEntity<?> getProjectLinks(String id, String loggedInEmail) {

        if (!isValidUser(loggedInEmail)) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");

        HashMap<String, Object> projectMemberMap = isValidProjectMember(id,loggedInEmail);
        if(!(boolean)projectMemberMap.get("isValid")) throw new MemberNotFoundException("No project found with projectID:"+id+" and email "+loggedInEmail);

        List<ProjectLink> projectLinks=projectLinkRepository.findByProjectProjectId(id);



        return new ResponseEntity<>(projectLinks,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> addLinkToProject(String id, LinkDto linkDto, String loggedInEmail) {
        if (!isValidUser(loggedInEmail)) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");

        Optional<Project> projectOptional = projectRepository.findByProjectId(id);
        if (!projectOptional.isPresent()) throw new ProjectNotFoundException("No project found with id  " + id);

        Project project = projectOptional.get();
        Optional<Member> optionalMember = memberRepository.findByProjectAndUserEmail(project, loggedInEmail);
        if (!optionalMember.isPresent())
            throw new InvalidAuthorityException(loggedInEmail + " is not allowed to update the project ");

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ProjectLink link  = mapper.map(linkDto, ProjectLink.class);
        link.setProject(project);
        link.setAddedBy(loggedInEmail);
        link.setAddedOn(new java.util.Date());
        project.addLink(link);
        projectLinkRepository.save(link);

        insightService.userActionUpdate(loggedInEmail," new link added to project "+project.getProjectName());

        Project updatedProject = projectRepository.save(project);
        notificationService.addProjectNotification(updatedProject,loggedInEmail+" added new links to the project");



        return new ResponseEntity<>(updatedProject,HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> updateLinkOfProject(Long id, LinkDto linkDto, String loggedInEmail) {


        if(!isValidUser(loggedInEmail)) throw  new UserNotFoundException(loggedInEmail+" is not a valid user ");
        Optional<ProjectLink> projectLinkOptional= projectLinkRepository.findById(id);

        if(!projectLinkOptional.isPresent()) throw new ProjectLinkNotFoundException("No link found with member id  "+ id);


        Project project = projectLinkOptional.get().getProject();
        Optional<Member> optionalMember = memberRepository.findByProjectAndUserEmail(project, loggedInEmail);
        if (!optionalMember.isPresent())
            throw new InvalidAuthorityException(loggedInEmail + " is not allowed to update the project ");


        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ProjectLink link  = mapper.map(linkDto, ProjectLink.class);
        link.setId(id);
        link.setProject(project);
        link.setAddedBy(loggedInEmail);
        link.setAddedOn(projectLinkOptional.get().getAddedOn());
        projectLinkRepository.save(link);


        insightService.userActionUpdate(loggedInEmail,"  link updated in project "+project.getProjectName());


        return new ResponseEntity<>("LINK Updated",HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> removeLinkFromProject(Long id, String loggedInEmail) {
        if (!isValidUser(loggedInEmail)) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");

        Optional<ProjectLink> projectLinkOptional = projectLinkRepository.findById(id);
        if (!projectLinkOptional.isPresent()) throw new ProjectLinkNotFoundException("No project link found with id  " + id);

        ProjectLink projectLink = projectLinkOptional.get();
        Project project=projectLink.getProject();
        Optional<Member> optionalMember = memberRepository.findByProjectAndUserEmail(projectLink.getProject(), loggedInEmail);
        if (!optionalMember.isPresent())
            throw new InvalidAuthorityException(loggedInEmail + " is not allowed to update the project ");

        try {
            project.getProjectLinks().remove(projectLink);
            projectLinkRepository.delete(projectLink);


        }catch(Exception ex) {
            throw new ProjectLinkNotFoundException("No project link found with id  " + id);
        }


        insightService.userActionUpdate(loggedInEmail,"  link removed from project "+project.getProjectName());
        Project updatedProject = projectRepository.save(project);
        notificationService.addProjectNotification(updatedProject,loggedInEmail+" removed some links from the project");

        return new ResponseEntity<>("LINK DELETED SUCCESSFULLY ",HttpStatus.ACCEPTED);
    }

    @Override
    //projectId
    public ResponseEntity<?> getProjectConversations(String id, String loggedInEmail) {
        HashMap<String, Object> userMap = isValidUser2(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        Optional<Project> projectOptional = projectRepository.findByProjectId(id);
        if (!projectOptional.isPresent()) throw new ProjectNotFoundException("No project  found with id  " + id);

        Project project= projectOptional.get();

        HashMap<String, Object> projectMemberMap = isValidProjectMember(id,loggedInEmail);
        if (!(boolean) projectMemberMap.get("isValid")) {

                throw new ProjectMemberNotFoundException("No project member found with project id :" + id + " and email " + loggedInEmail);
        }

        List<ProjectConversation> conversationList = projectConversationRepository.findByProjectOrderByStartedOnDesc(project);
        return new ResponseEntity<>(conversationList, HttpStatus.ACCEPTED);

    }

    @Override
    //projectConversation DB id
    public ResponseEntity<?> getConversation(Long id, String loggedInEmail) {
        HashMap<String, Object> userMap = isValidUser2(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        HashMap<String, Object> conversationMap = isValidConversation(id);
        if (!(boolean) conversationMap.get("isValid"))
            throw new ConversationNotFoundException(" No Conversation found with  id :" + id);
        ProjectConversation conversation = (ProjectConversation) conversationMap.get("conversation");
        Project project = conversation.getProject();

        HashMap<String, Object> projectMemberMap = isValidProjectMember(project.getProjectId(), loggedInEmail);
        if (!(boolean) projectMemberMap.get("isValid")) {

                throw new ProjectMemberNotFoundException("No project member found with project id :" + project.getId() + " and email " + loggedInEmail);
        }

        return new ResponseEntity<>(conversation, HttpStatus.ACCEPTED);

    }

    @Override
    public ResponseEntity<?> addProjectConversation(ConversationDto conversationDto, String id, String loggedInEmail) {
        HashMap<String, Object> userMap = isValidUser2(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        Optional<Project> projectOptional = projectRepository.findByProjectId(id);
        if (!projectOptional.isPresent()) throw new ProjectNotFoundException("No project  found with id  " + id);

        Project project= projectOptional.get();

        HashMap<String, Object> projectMemberMap = isValidProjectMember(id,loggedInEmail);
        if (!(boolean) projectMemberMap.get("isValid")) {

            throw new ProjectMemberNotFoundException("No project member found with project id :" + id + " and email " + loggedInEmail);
        }




        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ProjectConversation conversation = mapper.map(conversationDto, ProjectConversation.class);
        conversation.setStartedBy(loggedInEmail);
        conversation.setProject(project);
        conversation.setStartedByImage(user.getImageUrl());

        LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        conversation.setMonth(localDate.getMonthValue());
        conversation.setDate(localDate.getDayOfMonth());

        project.addProjectConversations(conversation);
        projectRepository.save(project);


        return new ResponseEntity<>(projectConversationRepository.save(conversation), HttpStatus.ACCEPTED);

    }

    @Override
    public ResponseEntity<?> removeConversation(Long id, String loggedInEmail) {
        HashMap<String, Object> userMap = isValidUser2(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        HashMap<String, Object> conversationMap = isValidConversation(id);
        if (!(boolean) conversationMap.get("isValid"))
            throw new ConversationNotFoundException(" No Conversation found with  id :" + id);
        ProjectConversation conversation = (ProjectConversation) conversationMap.get("conversation");
        Project project = conversation.getProject();

        HashMap<String, Object> projectMemberMap = isValidProjectMember(project.getProjectId(), loggedInEmail);
        if (!(boolean) projectMemberMap.get("isValid")) {

            throw new ProjectMemberNotFoundException("No  member found with project id :" + project.getId() + " and email " + loggedInEmail);
        }
        try {

            project.getProjectConversations().remove(conversation);
            projectConversationRepository.delete(conversation);
        } catch (Exception ex) {
            throw new ConversationNotFoundException("No conversation found with id  " + id);
        }


        return new ResponseEntity<>("MISSION CONVERSATION DELETED SUCCESSFULLY", HttpStatus.ACCEPTED);

    }

    @Override
    public ResponseEntity<?> addComment(CommentDto commentDto, Long id, String loggedInEmail) {

        HashMap<String, Object> userMap = isValidUser2(loggedInEmail);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(loggedInEmail + " is not a valid user ");
        User user = (User) userMap.get("user");

        HashMap<String, Object> conversationMap = isValidConversation(id);
        if (!(boolean) conversationMap.get("isValid"))
            throw new ConversationNotFoundException(" No Conversation found with  id :" + id);
        ProjectConversation conversation = (ProjectConversation) conversationMap.get("conversation");
        Project project = conversation.getProject();

        HashMap<String, Object> projectMemberMap = isValidProjectMember(project.getProjectId(), loggedInEmail);
        if (!(boolean) projectMemberMap.get("isValid")) {

            throw new ProjectMemberNotFoundException("No  member found with project id :" + project.getId() + " and email " + loggedInEmail);
        }


        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ProjectComment comment = mapper.map(commentDto, ProjectComment.class);
        comment.setAddedBy(loggedInEmail);
        comment.setAddedOn(new java.util.Date());
        comment.setProjectConversation(conversation);
        comment.setAddedByImage(user.getImageUrl());

        LocalDate localDate = new Date().toInstant().atZone(ZoneId.systemDefault()).toLocalDate();
        comment.setMonth(localDate.getMonthValue());
        comment.setDate(localDate.getDayOfMonth());

        conversation.addComment(comment);
//        conversationRepository.save(conversation);


        return new ResponseEntity<>(projectCommentRepository.save(comment), HttpStatus.ACCEPTED);


    }

    public HashMap<String, Object> isValidUser2(String email) {

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
    public boolean isValidUser(String email) {

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) return true;
        return false;
    }
    private boolean isValidMember(String projectId, String loggedInEmail) {

        long count=memberRepository.countByProjectProjectIdAndUserEmail(projectId,loggedInEmail);

        if(count>0) return true;
        return false;
    }
    public HashMap<String ,Object> isValidProjectMember(String projectId,String email) {
        HashMap<String,Object> result= new HashMap<>();
        Optional<Member> optionalMember = memberRepository.findByProjectProjectIdAndUserEmail(projectId,email);
        if (optionalMember.isPresent()) {
            result.put("isValid",true);
            result.put("member",optionalMember.get());
            return result;
        }
        result.put("isValid",false);
        return result;
    }
    public HashMap<String, Object> isValidConversation(Long id) {
        HashMap<String, Object> result = new HashMap<>();
        Optional<ProjectConversation> optionalConversation = projectConversationRepository.findById(id);
        if (optionalConversation.isPresent()) {
            result.put("isValid", true);
            result.put("conversation", optionalConversation.get());
            return result;
        }
        result.put("isValid", false);
        return result;
    }



}
