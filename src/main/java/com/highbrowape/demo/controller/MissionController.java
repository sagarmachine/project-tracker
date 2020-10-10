package com.highbrowape.demo.controller;


import com.highbrowape.demo.dto.input.*;
import com.highbrowape.demo.entity.Authority;
import com.highbrowape.demo.service.IMissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/mission")
public class MissionController {

    @Autowired
    IMissionService missionService;


    @PostMapping("/level/1/{id}")
    public ResponseEntity<?> addMissionAtLevel1( @RequestBody MissionAddDto missionAddDto, @PathVariable("id") String id,Principal principal){

        return missionService.addMissionAtLevel1(missionAddDto,id,principal.getName());

    }

    @PostMapping("/level/n/{id}")
    public ResponseEntity<?> addMissionAtLevelN(@Valid @RequestBody MissionAddDto missionAddDto, @PathVariable("id") String id,Principal principal){

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
    } @PostMapping("/{id}/note")
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




    @PostMapping("/conversation/{id}")
    public  void startConversationToMission(@Valid @RequestBody LinkDto linkDto, Principal principal){

    }




}
