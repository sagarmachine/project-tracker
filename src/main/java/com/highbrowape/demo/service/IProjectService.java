package com.highbrowape.demo.service;


import com.highbrowape.demo.dto.input.*;

import com.highbrowape.demo.entity.Authority;
import com.highbrowape.demo.entity.Project;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


 public interface IProjectService {

    ResponseEntity<?> addProject(ProjectAdd projectAdd, String loggedInEmail);

    ResponseEntity<?> updateProject(ProjectUpdate projectUpdate, String loggedInEmail);

    ResponseEntity<?> getProjectDashboard(Long projectId, String loggedInEmail);

    ResponseEntity<?> getProjectList(int pageNumber, String loggedInEmail);

    ResponseEntity<Project> getProjectDetail(Long projectId, String loggedInEmail);

    ResponseEntity<?> getMyCreatedProjectList(int pageNumber, String loggedInEmail);




    //project DB id
    ResponseEntity<?>  getProjectMembers(String id, String loggedInEmail);

     ResponseEntity<?>  addMemberToProject(Long id, ProjectMemberDto projectMemberDto, String loggedInEmail);
    //member DB id
     ResponseEntity<?>  updateMemberAuthorityToProject(Long id, @PathVariable("authority") Authority authority, String loggedInEmail);
     //member DB id
     ResponseEntity<?>  removeMemberFromProject(Long id, String loggedInEmail);





     ResponseEntity<?>  getProjectNotes(String id, String loggedInEmail);
     ResponseEntity<?>  addNoteToProject(Long id, NoteDto noteDto, String loggedInEmail);
     ResponseEntity<?>  updateNoteOfProject(Long id, NoteDto noteDto,String loggedInEmail);
     ResponseEntity<?>  removeNoteFromProject(Long id, String loggedInEmail);






     ResponseEntity<?>  getProjectLinks(String id,String loggedInEmail);
     ResponseEntity<?> addLinkToProject( Long id, LinkDto linkDto, String loggedInEmail);
     ResponseEntity<?> updateLinkOfProject( Long id, LinkDto linkDto, String loggedInEmail);
     ResponseEntity<?> removeLinkFromProject( Long id, String loggedInEmail);

}