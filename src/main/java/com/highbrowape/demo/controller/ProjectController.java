package com.highbrowape.demo.controller;


import com.highbrowape.demo.dto.input.*;
import com.highbrowape.demo.dto.output.ProjectDetailDto;
import com.highbrowape.demo.entity.Authority;
import com.highbrowape.demo.entity.Project;
import com.highbrowape.demo.repository.ProjectRepository;
import com.highbrowape.demo.service.INotificationService;
import com.highbrowape.demo.service.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;


@RestController
@RequestMapping(value = "/api/v1/project")
public class ProjectController {

    @Autowired
    IProjectService projectService;

    @Autowired
    ProjectRepository projectRepository;

    @Autowired
    INotificationService notificationService;



    @PostMapping("")
    public ResponseEntity<?>  addProject(@RequestBody ProjectAdd projectAdd, Principal principal){

        return projectService.addProject(projectAdd,principal.getName());

    }

    @PutMapping("")
    public ResponseEntity<?>  updateProject(@RequestBody ProjectUpdate projectUpdate, Principal principal){

        return projectService.updateProject(projectUpdate,principal.getName());

    }

    @GetMapping("/created/list/{pageNumber}")
    public ResponseEntity<?>  getMyCreatedProjectList(@PathVariable("pageNumber")int pageNumber, Principal principal){

        return projectService.getMyCreatedProjectList(pageNumber,principal.getName());

    }
    @GetMapping("/list/{pageNumber}")
    public ResponseEntity<?>  getProjectList(@PathVariable("pageNumber")int pageNumber, Principal principal){

        return projectService.getProjectList(pageNumber,principal.getName());

    }


    @GetMapping("/{id}/dashboard")
    public  ResponseEntity<?> getProjectDashboard(@PathVariable("id")String id, Principal principal){
        System.out.println(12313);
        return projectService.getProjectDashboard(id,principal.getName());
    }

    @GetMapping("/{id}/detail")
    public  ResponseEntity<ProjectDetailDto> getProjectDetailView(@PathVariable("id")String id, Principal principal){
        return projectService.getProjectDetail(id,principal.getName());
    }



    @GetMapping("/{id}/member")
    public ResponseEntity<?>  getProjectMember(@PathVariable("id")String id,  Principal principal){
           return projectService.getProjectMembers(id,principal.getName());
    }

    @PostMapping("/{id}/member")

    public ResponseEntity<?>  addMemberToProject(@PathVariable("id")String id, @RequestBody ProjectMemberDto projectMemberDto, Principal principal){
        return projectService.addMemberToProject(id,projectMemberDto,principal.getName());
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<?>  addMembersToProject(@PathVariable("id")String id, @RequestBody List<ProjectMemberDto> projectMemberDtoList, Principal principal){
        return projectService.addMembersToProject(id,projectMemberDtoList,principal.getName());
    }
    @PutMapping("/member/{id}/authority/{authority}")
    public ResponseEntity<?>  updateMemberAuthorityToProject(@PathVariable("id")Long id, @PathVariable("authority") Authority authority, Principal principal){
        return projectService.updateMemberAuthorityToProject(id,authority,principal.getName());
    }
    @DeleteMapping("/member/{id}")
    public ResponseEntity<?>  removeMemberFromProject(@PathVariable("id")Long id, Principal principal){

        return projectService.removeMemberFromProject(id,principal.getName());
    }




    @GetMapping("/{id}/{note}")
    public ResponseEntity<?>  getProjectNote(@PathVariable("id")String id,  Principal principal){
        return projectService.getProjectNotes(id,principal.getName());
    }

    @PostMapping("/{id}/note")
    public ResponseEntity<?>  addNoteToProject(@PathVariable("id")String id, @RequestBody NoteDto noteDto,Principal principal){
        return projectService.addNoteToProject(id,noteDto,principal.getName());
    }
    @PutMapping("/{id}/note")
    public ResponseEntity<?>  updateNoteOfProject(@PathVariable("id")Long id, @RequestBody NoteDto noteDto,Principal principal){
        return projectService.updateNoteOfProject(id,noteDto,principal.getName());
    }
    @DeleteMapping("/note/{id}")
    public ResponseEntity<?>  removeNoteFromProject(@PathVariable("id")Long id, Principal principal){

        return projectService.removeNoteFromProject(id,principal.getName());
    }



    @GetMapping("/{id}/link")
    public ResponseEntity<?>  getProjectLink(@PathVariable("id")String id,  Principal principal){
        return projectService.getProjectLinks(id,principal.getName());
    }

    @PostMapping("/{id}/link")
    public ResponseEntity<?>  addLinkToProject(@PathVariable("id")String id, @RequestBody LinkDto linkDto, Principal principal){
        return projectService.addLinkToProject(id,linkDto,principal.getName());
    }
    @PutMapping("/link/{id}")
    public ResponseEntity<?>  updateLinkOfProject(@PathVariable("id")Long id, @RequestBody LinkDto linkDto,Principal principal){
        return projectService.updateLinkOfProject(id,linkDto,principal.getName());
    }
    @DeleteMapping("/link/{id}")
    public ResponseEntity<?>  removeLinkFromProject(@PathVariable("id")Long id, Principal principal){

        return projectService.removeLinkFromProject(id,principal.getName());
    }

    @GetMapping("/{projectId}/notification")
    ResponseEntity<?> getProjectNotification (@PathVariable("projectId")String  projectId){
        return notificationService.getProjectNotifications(projectId);
    }


    @PostMapping("/{id}/conversation")
    public  ResponseEntity<?> startConversationToProject(@PathVariable("id")String id, @Valid @RequestBody ConversationDto conversationDto, Principal principal){

        return projectService.addProjectConversation(conversationDto,id,principal.getName());

    }

    @GetMapping("/conversation/{id}")
    public ResponseEntity<?> getConversation(@PathVariable("id")Long id, Principal principal)//conversationId
    {
        return projectService.getConversation(id,principal.getName());
    }
    @GetMapping("/{id}/conversation")
    public ResponseEntity<?> getConversations(@PathVariable("id")String id, Principal principal)//missionId
    {
        return projectService.getProjectConversations(id,principal.getName());
    }

    @DeleteMapping("/conversation/{id}")
    public  ResponseEntity<?> deleteConversation(@PathVariable("id")Long id, Principal principal){
        return projectService.removeConversation(id,principal.getName());
    }




    @PostMapping("/conversation/{id}/comment")
    public  ResponseEntity<?> addComment(@PathVariable("id")Long id,@RequestBody CommentDto commentDto,Principal principal){
        return projectService.addComment(commentDto,id,principal.getName());
    }


}
