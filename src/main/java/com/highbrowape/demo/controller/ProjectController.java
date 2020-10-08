package com.highbrowape.demo.controller;


import com.highbrowape.demo.dto.input.*;
import com.highbrowape.demo.entity.Authority;
import com.highbrowape.demo.entity.Project;
import com.highbrowape.demo.repository.ProjectRepository;
import com.highbrowape.demo.service.IProjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import java.security.Principal;

@RestController
@RequestMapping(value = "/api/v1/project")
public class ProjectController {

    @Autowired
    IProjectService projectService;

    @Autowired
    ProjectRepository projectRepository;


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


    @GetMapping("/{id}dashboard/")
    public  ResponseEntity<?> getProjectDashboard(@PathVariable("id")String id, Principal principal){
        return projectService.getProjectDashboard(id,principal.getName());
    }

    @GetMapping("/{id}/detail")
    public  ResponseEntity<Project> getProjectDetailView(@PathVariable("id")String id, Principal principal){
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
    @PutMapping("/member/{id}/authority/{authority}")
    public ResponseEntity<?>  updateMemberAuthorityToProject(@PathVariable("id")Long id, @PathVariable("authority") Authority authority, Principal principal){
        return projectService.updateMemberAuthorityToProject(id,authority,principal.getName());
    }
    @DeleteMapping("/member/{id}")
    public ResponseEntity<?>  removeMemberFromProject(@PathVariable("id")Long id, Principal principal){

        return projectService.removeMemberFromProject(id,principal.getName());
    }




    @GetMapping("/note/{id}")
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


}
