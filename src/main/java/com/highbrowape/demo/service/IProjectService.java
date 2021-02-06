package com.highbrowape.demo.service;


import com.highbrowape.demo.dto.input.*;

import com.highbrowape.demo.dto.output.ProjectDetailDto;
import com.highbrowape.demo.entity.Authority;
import com.highbrowape.demo.entity.Project;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;


public interface IProjectService {

    ResponseEntity<?> addProject(ProjectAdd projectAdd, String loggedInEmail);

    ResponseEntity<?> updateProject(ProjectUpdate projectUpdate, String loggedInEmail);

    ResponseEntity<?> getProjectDashboard(String projectId, String loggedInEmail);

    ResponseEntity<?> getProjectList(int pageNumber, String loggedInEmail);

    ResponseEntity<ProjectDetailDto> getProjectDetail(String projectId, String loggedInEmail);

    ResponseEntity<?> getMyCreatedProjectList(int pageNumber, String loggedInEmail);




    //project DB id
    ResponseEntity<?>  getProjectMembers(String id, String loggedInEmail);


    ResponseEntity<?>  addMemberToProject(String id, ProjectMemberDto projectMemberDto, String loggedInEmail);


    ResponseEntity<?>  addMembersToProject(String id, List<ProjectMemberDto> projectMemberDtoList, String loggedInEmail);

    //member DB id
     ResponseEntity<?>  updateMemberAuthorityToProject(Long id, @PathVariable("authority") Authority authority, String loggedInEmail);
     //member DB id
     ResponseEntity<?>  removeMemberFromProject(Long id, String loggedInEmail);





     ResponseEntity<?>  getProjectNotes(String id, String loggedInEmail);
     ResponseEntity<?>  addNoteToProject(String id, NoteDto noteDto, String loggedInEmail);
     ResponseEntity<?>  updateNoteOfProject(Long id, NoteDto noteDto,String loggedInEmail);
     ResponseEntity<?>  removeNoteFromProject(Long id, String loggedInEmail);






     ResponseEntity<?>  getProjectLinks(String id,String loggedInEmail);

     ResponseEntity<?> addLinkToProject(String id, LinkDto linkDto, String loggedInEmail);

     ResponseEntity<?> updateLinkOfProject( Long id, LinkDto linkDto, String loggedInEmail);
     ResponseEntity<?> removeLinkFromProject( Long id, String loggedInEmail);

    ResponseEntity<?> addProjectConversation(ConversationDto conversationDto, String id, String name);

    ResponseEntity<?> getConversation(Long id, String name);

    ResponseEntity<?> getProjectConversations(String id, String name);

    ResponseEntity<?> removeConversation(Long id, String name);

    ResponseEntity<?> addComment(CommentDto commentDto, Long id, String name);
}