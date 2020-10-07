package com.highbrowape.demo.controller;


import com.highbrowape.demo.dto.input.LinkDto;
import com.highbrowape.demo.dto.input.MissionAddDto;
import com.highbrowape.demo.dto.input.NoteDto;
import com.highbrowape.demo.dto.input.ProjectMemberDto;
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
    public ResponseEntity<?> addMissionAtLevel1(@Valid @RequestBody MissionAddDto missionAddDto, @PathVariable("id") long id,Principal principal){

        return missionService.addMissionAtLevel1(missionAddDto,id,principal.getName());

    }

    @PostMapping("/level/n/{id}")
    public ResponseEntity<?> addMissionAtLevelN(@Valid @RequestBody MissionAddDto missionAddDto, @PathVariable("id") long id,Principal principal){

        return missionService.addMissionAtLevelN(missionAddDto,id,principal.getName());
    }





    @PostMapping("/{id}/member")
    public ResponseEntity<?>  addMemberToMission(@PathVariable("id")Long id, @RequestBody ProjectMemberDto projectMemberDto, Principal principal){
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



    @PostMapping("/{id}/note")
    public ResponseEntity<?>  addNoteToMission(@PathVariable("id")Long id, @RequestBody NoteDto noteDto,Principal principal){
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






    @PostMapping("/{id}/link")
    public ResponseEntity<?>  addLinkToMission(@PathVariable("id")Long id, @RequestBody LinkDto linkDto, Principal principal){
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




    @PostMapping("/conversation/{id}")
    public  void startConversationToMission(@Valid @RequestBody LinkDto linkDto, Principal principal){

    }




}
