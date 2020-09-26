package com.highbrowape.demo.service;


import com.highbrowape.demo.dto.input.ProjectAdd;
import com.highbrowape.demo.dto.input.ProjectMemberDto;
import com.highbrowape.demo.dto.input.ProjectUpdate;
import com.highbrowape.demo.dto.output.ProjectDashboardDto;
import com.highbrowape.demo.dto.output.ProjectListDto;
import com.highbrowape.demo.entity.Authority;
import com.highbrowape.demo.entity.Member;
import com.highbrowape.demo.entity.Project;
import com.highbrowape.demo.entity.User;
import com.highbrowape.demo.exception.InvalidAuthorityException;
import com.highbrowape.demo.exception.ProjectAlreadyExistException;
import com.highbrowape.demo.exception.ProjectNotFoundException;
import com.highbrowape.demo.repository.MemberRepository;
import com.highbrowape.demo.repository.ProjectRepository;
import com.highbrowape.demo.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.OptionalInt;

@Service
public class ProjectServiceImpl implements IProjectService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    MemberRepository memberRepository;

    public ResponseEntity<?> addProject(ProjectAdd projectAdd, String loggedInEmail) {
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        Project project = mapper.map(projectAdd, Project.class);
        Optional<User> optionalUser = userRepository.findByEmail(loggedInEmail);
        User user = null;
        if (optionalUser.isPresent()) {
            user = optionalUser.get();
            Optional<Project> projectOptional = projectRepository.findByUserAndProjectId(user, projectAdd.getProjectId());
            if (projectOptional.isPresent())
                throw new ProjectAlreadyExistException("Project With id " + projectAdd.getProjectId() + " already exist ");

        }
        project.setUser(user);

        try {
            project = projectRepository.save(project);
        } catch (DataIntegrityViolationException exception) {
            throw new ProjectAlreadyExistException("Project With id " + projectAdd.getProjectId() + " already exist ");
        }
        for (ProjectMemberDto pmd : projectAdd.getMembers()) {
            if (isValidUser(pmd.getEmail())) {
                Optional<Member> optionalMember2= memberRepository.findByProjectAndEmail(project,pmd.getEmail());
                if(optionalMember2.isPresent()) continue;
                Member member = mapper.map(pmd, Member.class);
                member.setAddedBy(user.getEmail());
                member.setAddedOn(new java.util.Date());
                member.setProject(project);
                memberRepository.save(member);
            }
        }

        return new ResponseEntity<>(project, HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> updateProject(ProjectUpdate projectUpdate, String loggedInEmail) {
        Optional<Project> projectOptional = projectRepository.findById(projectUpdate.getId());

        if (!projectOptional.isPresent())
            throw new ProjectNotFoundException("No Project Found with id " + projectUpdate.getId());
        Project project = projectOptional.get();

        Optional<Member> optionalMember= memberRepository.findByProjectAndEmail(project,loggedInEmail);
        if(!optionalMember.isPresent())
            throw new InvalidAuthorityException(loggedInEmail + " is not allowed to update the project ");

        if(optionalMember.get().getAuthority().equals(Authority.CREATOR)||optionalMember.get().getAuthority().equals(Authority.CHIEF)) {

            for (ProjectMemberDto pmd : projectUpdate.getMembers()) {
                if (isValidUser(pmd.getEmail())) {

                    Optional<Member> optionalMember2= memberRepository.findByProjectAndEmail(project,pmd.getEmail());
                    if(optionalMember2.isPresent()) continue;
                    ModelMapper mapper = new ModelMapper();
                    mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
                    Member member = mapper.map(pmd, Member.class);
                    member.setAddedBy(loggedInEmail);
                    member.setAddedOn(new java.util.Date());
                    member.setProject(project);
                    memberRepository.save(member);
                }
            }

        }
        else  throw new InvalidAuthorityException(loggedInEmail + " is not allowed to update the project ");

        return new ResponseEntity<>(project, HttpStatus.BAD_REQUEST);
    }

    public boolean isValidUser(String email) {

        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) return true;
        return false;
    }

    @Override
    public ResponseEntity<Project> getProjectDetail(Long projectId, String loggedInEmail) {
        return null;
    }

    @Override
    public ResponseEntity<ProjectListDto> getMyCreatedProjectList(int pageNumber, String loggedInEmail) {
        return null;
    }

    @Override
    public ResponseEntity<ProjectDashboardDto> getProjectDashboard(Long projectId, String loggedInEmail) {
        return null;
    }

    @Override
    public ResponseEntity<ProjectListDto> getProjectList(int pageNumber, String loggedInEmail) {
        return null;
    }

}
