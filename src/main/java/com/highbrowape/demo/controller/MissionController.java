package com.highbrowape.demo.controller;


import com.highbrowape.demo.dto.input.LinkDto;
import com.highbrowape.demo.dto.input.MissionAddDto;
import com.highbrowape.demo.dto.input.NoteDto;
import com.highbrowape.demo.dto.input.ProjectMemberDto;
import com.highbrowape.demo.entity.Authority;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.security.Principal;

@RestController
@RequestMapping("/api/v1/mission")
public class MissionController {



    @PostMapping("/level/1/{id}")
    public ResponseEntity<?> addMissionAtLevel1(@Valid @RequestBody MissionAddDto missionAddDto, @PathVariable("id") long id){

        return null;

    }

    @PostMapping("/level/n/{id}")
    public ResponseEntity<?> addMissionAtLevelN(@Valid @RequestBody MissionAddDto missionAddDto, @PathVariable("id") long id){
    return null;
    }





    @PostMapping("/{id}/member")
    public ResponseEntity<?>  addMemberToMission(@PathVariable("id")Long id, @RequestBody ProjectMemberDto projectMemberDto, Principal principal){
        return null;
    }
    @PutMapping("/member/{id}/authority/{authority}")
    public ResponseEntity<?>  updateMemberAuthorityToMission(@PathVariable("id")Long id, @PathVariable("authority") Authority authority, Principal principal){
        return null;
    }
    @DeleteMapping("/member/{id}")
    public ResponseEntity<?>  removeMemberFromMission(@PathVariable("id")Long id, Principal principal){

        return null;
    }





    @PostMapping("/{id}/note")
    public ResponseEntity<?>  addNoteToMission(@PathVariable("id")Long id, @RequestBody NoteDto noteDto,Principal principal){
        return null;
    }
    @PutMapping("/note/{id}")
    public ResponseEntity<?>  updateNoteOfMission(@PathVariable("id")Long id, @RequestBody NoteDto noteDto,Principal principal){
        return null;
    }
    @DeleteMapping("/note/{id}")
    public ResponseEntity<?>  removeNoteFromMission(@PathVariable("id")Long id, Principal principal){

        return null;
    }






    @PostMapping("/{id}/link")
    public ResponseEntity<?>  addLinkToMission(@PathVariable("id")Long id, @RequestBody LinkDto linkDto, Principal principal){
        return null;
    }
    @PutMapping("/link/{id}")
    public ResponseEntity<?>  updateLinkOfMission(@PathVariable("id")Long id, @RequestBody LinkDto linkDto,Principal principal){
        return null;
    }
    @DeleteMapping("/link/{id}")
    public ResponseEntity<?>  removeLinkFromMission(@PathVariable("id")Long id, Principal principal){

        return null;
    }




    @PostMapping("/conversation/{id}")
    public  void startConversationToMission(@Valid @RequestBody LinkDto linkDto, Principal principal){

    }




}
