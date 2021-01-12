package com.highbrowape.demo.controller;


import com.highbrowape.demo.dto.input.*;
import com.highbrowape.demo.entity.Authority;
import com.highbrowape.demo.entity.Status;
import com.highbrowape.demo.service.IMissionService;
import com.highbrowape.demo.service.INotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;
import java.util.List;

@RestController
@RequestMapping("/api/v1/mission")
public class MissionController {

    @Autowired
    IMissionService missionService;

    @Autowired
    INotificationService  notificationService;


    @PostMapping("/level/1/{id}")
    public ResponseEntity<?> addMissionAtLevel1( @RequestBody MissionAddDto missionAddDto, @PathVariable("id") String id,Principal principal){

        return missionService.addMissionAtLevel1(missionAddDto,id,principal.getName());

    }

    @PostMapping("/level/n")
    public ResponseEntity<?> addMissionAtLevelN(@Valid @RequestBody MissionAddDto missionAddDto, @RequestParam("id") String id,Principal principal){
        System.out.println(id);
        return missionService.addMissionAtLevelN(missionAddDto,id,principal.getName());
    }





    @GetMapping("/{id}/member")
    public ResponseEntity<?>  getMissionMember(@PathVariable("id")String id,  Principal principal){
        return missionService.getMissionMembers(id,principal.getName());
    }
    @PostMapping("/{id}/member")
    public ResponseEntity<?>  addMemberToMission(@PathVariable("id")String id, @RequestBody ProjectMemberDto projectMemberDto, Principal principal){
        return missionService.addMemberToMission(projectMemberDto,id,principal.getName());
    }

    @PostMapping("/{id}/members")
    public ResponseEntity<?>  addMembersToMission(@PathVariable("id")String id, @RequestBody List<ProjectMemberDto> projectMemberDtoList, Principal principal){
        return missionService.addMembersToMission(projectMemberDtoList,id,principal.getName());
    }

    @PutMapping("/member/{id}/authority/{authority}")
    public ResponseEntity<?>  updateMemberAuthorityToMission(@PathVariable("id")Long id, @PathVariable("authority") Authority authority, Principal principal){
        return missionService.updateMemberAuthorityOfMission(authority,id,principal.getName());
    }
    @DeleteMapping("/member/{id}")
    public ResponseEntity<?>  removeMemberFromMission(@PathVariable("id")Long id, Principal principal){

        return missionService.removeMemberFromMission(id,principal.getName());
    }



    @GetMapping("/{id}/note")
    public ResponseEntity<?>  getProjectNote(@PathVariable("id")String id,  Principal principal){
        return missionService.getMissionNotes(id,principal.getName());
    }

    @PostMapping("/{id}/note")
    public ResponseEntity<?>  addNoteToMission(@PathVariable("id")String id, @RequestBody NoteDto noteDto,Principal principal){
        return missionService.addNoteToMission(noteDto,id,principal.getName());
    }
    @PutMapping("/note/{id}")
    public ResponseEntity<?>  updateNoteOfMission(@PathVariable("id")Long id, @RequestBody NoteDto noteDto,Principal principal){

        return missionService.updateNoteOfMission(noteDto,id,principal.getName());
    }
    @DeleteMapping("/note/{id}")
    public ResponseEntity<?>  removeNoteFromMission(@PathVariable("id")Long id, Principal principal){

        return missionService.removeNoteFromMission(id,principal.getName());
    }






    @GetMapping("/{id}/link")
    public ResponseEntity<?>  getProjectLink(@PathVariable("id")String id,  Principal principal){
        return missionService.getMissionLinks(id,principal.getName());
    } @PostMapping("/{id}/link")
    public ResponseEntity<?>  addLinkToMission(@PathVariable("id")String id, @RequestBody LinkDto linkDto, Principal principal){
        return missionService.addLinkToMission(linkDto,id,principal.getName());
    }
    @PutMapping("/link/{id}")
    public ResponseEntity<?>  updateLinkOfMission(@PathVariable("id")Long id, @RequestBody LinkDto linkDto,Principal principal){

        return missionService.updateLinkOfMission(linkDto,id,principal.getName());
    }
    @DeleteMapping("/link/{id}")
    public ResponseEntity<?>  removeLinkFromMission(@PathVariable("id")Long id, Principal principal){


        return missionService.removeLinkFromMission(id,principal.getName());
    }


    @GetMapping("/{id}/objective")
    public ResponseEntity<?>  getMissionObjective(@PathVariable("id")String id,  Principal principal){
        return missionService.getMissionObjectives(id,principal.getName());
    } @PostMapping("/{id}/objective")
    public ResponseEntity<?>  addObjectiveToMission(@PathVariable("id")String id, @RequestBody ObjectiveDto objectiveDto, Principal principal){
        return missionService.addObjectiveToMission(objectiveDto,id,principal.getName());
    }
    @PutMapping("/objective/{id}")
    public ResponseEntity<?>  updateObjectiveOfMission(@PathVariable("id")Long id, @RequestBody ObjectiveDto objectiveDto,Principal principal){

        return missionService.updateObjectiveOfMission(objectiveDto,id,principal.getName());
    }
    @DeleteMapping("/objective/{id}")
    public ResponseEntity<?>  removeObjectiveFromMission(@PathVariable("id")Long id, Principal principal){


        return missionService.removeObjectiveFromMission(id,principal.getName());
    }

    @PutMapping("/objective/{id}/status/{value}")
    public ResponseEntity<?>  updateObjectiveStatus(@PathVariable("id")Long id, @PathVariable("value")Status value,Principal principal){

        return missionService.updateObjectiveStatus(id,value,principal.getName());
    }




    @PostMapping("/{id}/conversation")
    public  ResponseEntity<?> startConversationToMission(@PathVariable("id")String id,@Valid @RequestBody ConversationDto conversationDto, Principal principal){

        return missionService.addMissionConversation(conversationDto,id,principal.getName());

    }

    @GetMapping("/conversation/{id}")
    public ResponseEntity<?> getConversation(@PathVariable("id")Long id, Principal principal)//conversationId
    {
        return missionService.getConversation(id,principal.getName());
    }
    @GetMapping("/{id}/conversation")
    public ResponseEntity<?> getConversations(@PathVariable("id")String id, Principal principal)//missionId
    {
        return missionService.getMissionConversations(id,principal.getName());
    }

   @DeleteMapping("/conversation/{id}")
    public  ResponseEntity<?> deleteConversation(@PathVariable("id")Long id, Principal principal){
    return missionService.removeConversation(id,principal.getName());
    }




    @PostMapping("/conversation/{id}/comment")
    public  ResponseEntity<?> addComment(@PathVariable("id")Long id,@RequestBody CommentDto commentDto,Principal principal){
        return missionService.addComment(commentDto,id,principal.getName());
    }

    @DeleteMapping("/comment/{id}")
    public  ResponseEntity<?> removeComment(@PathVariable("id")Long id,Principal principal){
return missionService.removeComment(id,principal.getName());
    }



    @GetMapping("/{missionId}/notification")
    ResponseEntity<?> getMissionNotification (@PathVariable("missionId")String  missionId){
        return notificationService.getMissionNotifications(missionId);
    }


}
