package com.highbrowape.demo.controller;


import com.highbrowape.demo.dto.input.LinkDto;
import com.highbrowape.demo.dto.input.MissionAddDto;
import com.highbrowape.demo.dto.input.NoteDto;
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

    @PostMapping("/note/{id}")
    public  void addNoteToMission(@Valid @RequestBody NoteDto noteDto){

    }

    @PostMapping("/link/{id}")
    public  void addLinkToMission(@Valid @RequestBody LinkDto linkDto){

    }


    @PostMapping("/conversation/{id}")
    public  void startConversationToMission(@Valid @RequestBody LinkDto linkDto, Principal principal){

    }




}
