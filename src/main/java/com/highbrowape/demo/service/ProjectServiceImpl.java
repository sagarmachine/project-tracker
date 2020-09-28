package com.highbrowape.demo.service;


import com.highbrowape.demo.dto.input.*;
import com.highbrowape.demo.dto.output.ProjectDashboardDto;
import com.highbrowape.demo.dto.output.ProjectListDto;
import com.highbrowape.demo.entity.*;
import com.highbrowape.demo.exception.InvalidAuthorityException;
import com.highbrowape.demo.exception.ProjectAlreadyExistException;
import com.highbrowape.demo.exception.ProjectNotFoundException;
import com.highbrowape.demo.exception.UserNotFoundException;
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

import java.util.*;

@Service
public class ProjectServiceImpl implements IProjectService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    ProjectLinkRepository projectLinkRepository;

    @Autowired
    ProjectNoteRepository projectNoteRepository;

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

        return new ResponseEntity<>(projectRepository.save(project), HttpStatus.OK);
    }



    @Override
    public ResponseEntity<Project> getProjectDetail(Long projectId, String loggedInEmail) {

        if(!isValidUser(loggedInEmail)) throw  new UserNotFoundException(loggedInEmail+" is not a valid user ");
        Optional<Project> projectOptional=projectRepository.findById(projectId);

        if(!projectOptional.isPresent()) throw new ProjectNotFoundException("No project found with id  "+projectId);

        if(!isValidMember(projectId,loggedInEmail)) throw new InvalidAuthorityException(loggedInEmail+ " is not member of the project with id "+projectId);

        Project project=projectOptional.get();
        return new ResponseEntity<>(project,HttpStatus.OK);
    }

    @Override
    public ResponseEntity<?> getMyCreatedProjectList(int pageNumber, String loggedInEmail) {
        if(!isValidUser(loggedInEmail)) throw  new UserNotFoundException(loggedInEmail+" is not a valid user ");
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        int totalPages=(int)Math.ceil(projectRepository.countByUserEmail(loggedInEmail)/2);
        Pageable pageable = PageRequest.of(pageNumber, 2);
        List<Project> projects= projectRepository.findByUserEmail(loggedInEmail,pageable);
        List<ProjectListDto> projectList=new ArrayList<>();

        for(Project p:projects){
            ProjectListDto project= mapper.map(p, ProjectListDto.class);
            long totalMembers=(long)memberRepository.countByProject(p);
            project.setTotalMembers(totalMembers);
            project.setTotalMissions(p.getMissions().size());
            projectList.add(project);
        }
        HashMap<String,Object> result = new HashMap<>();
        result.put("totalPages",new Integer(totalPages));
        result.put("data",projectList);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }


    @Override
    public ResponseEntity<ProjectDashboardDto> getProjectDashboard(Long projectId, String loggedInEmail) {
        if(!isValidUser(loggedInEmail)) throw  new UserNotFoundException(loggedInEmail+" is not a valid user ");
        Optional<Project> projectOptional=projectRepository.findById(projectId);

        if(!projectOptional.isPresent()) throw new ProjectNotFoundException("No project found with id  "+projectId);

        if(!isValidMember(projectId,loggedInEmail)) throw new InvalidAuthorityException(loggedInEmail+ " is not member of the project with id "+projectId);

        Project project=projectOptional.get();
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        ProjectDashboardDto pdd=mapper.map(project,ProjectDashboardDto.class);

        return new ResponseEntity<>(pdd,HttpStatus.OK);
    }

    private boolean isValidMember(Long projectId, String loggedInEmail) {
        int count=memberRepository.countByProjectIdAndUserEmail(projectId,loggedInEmail);
        if(count>0) return true;
        return false;
    }

    @Override
    public ResponseEntity<?> getProjectList(int pageNumber, String loggedInEmail) {

        if(!isValidUser(loggedInEmail)) throw  new UserNotFoundException(loggedInEmail+" is not a valid user ");
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        int totalPages=(int)Math.ceil(memberRepository.countByUserEmail(loggedInEmail)/2);
        Pageable pageable = PageRequest.of(pageNumber, 2);
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
        result.put("totalPages",new Integer(totalPages));
        result.put("data",projectList);
        return new ResponseEntity<>(result,HttpStatus.OK);
    }

    public boolean isValidUser(String email) {

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) return true;
        return false;
    }



    @Override
    public ResponseEntity<?> addMemberToProject(Long id, ProjectMemberDto projectMemberDto, String loggedInEmail) {
        return null;
    }

    @Override
    public ResponseEntity<?> updateMemberAuthorityToProject(Long id, Authority authority, String loggedInEmail) {
        return null;
    }

    @Override
    public ResponseEntity<?> removeMemberFromProject(Long id, String loggedInEmail) {
        return null;
    }

    @Override
    public ResponseEntity<?> addNoteToProject(Long id, NoteDto noteDto, String loggedInEmail) {
        return null;
    }

    @Override
    public ResponseEntity<?> updateNoteOfProject(Long id, NoteDto noteDto, String loggedInEmail) {
        return null;
    }

    @Override
    public ResponseEntity<?> removeNoteFromProject(Long id, String loggedInEmail) {
        return null;
    }

    @Override
    public ResponseEntity<?> addLinkToProject(Long id, LinkDto linkDto, String loggedInEmail) {
        return null;
    }

    @Override
    public ResponseEntity<?> updateLinkOfProject(Long id, LinkDto linkDto, String loggedInEmail) {
        return null;
    }

    @Override
    public ResponseEntity<?> removeLinkFromProject(Long id, String loggedInEmail) {
        return null;
    }






}
