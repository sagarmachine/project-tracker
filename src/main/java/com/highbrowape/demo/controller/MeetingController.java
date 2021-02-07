package com.highbrowape.demo.controller;

import com.highbrowape.demo.dto.input.MeetingDto;
import com.highbrowape.demo.dto.input.MeetingMemberDto;
import com.highbrowape.demo.service.IMeetingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;

@RestController
@RequestMapping("/api/v1/meeting")
public class MeetingController {

    @Autowired
    IMeetingService meetingService;

//mission id
    @PostMapping("/mission/{id}")
    public ResponseEntity<?>  addMeetingToMission(@PathVariable String id, @RequestBody MeetingDto meetingDto, Principal principal){

        String email=principal.getName();
        return meetingService.addMeetingToMission(id,meetingDto,email);
    }

    //mission id
    @GetMapping("/mission/{id}")
    public ResponseEntity<?>  getMissionMeeting(@PathVariable String id, Principal principal){

        String email=principal.getName();
        return  meetingService.getMissionMeeting(id,email);
    }

    //meeting id
    @PutMapping("/mission/add/{id}")
    public ResponseEntity<?> addMemberToMissionMeeting(@PathVariable Long id, Principal principal, MeetingMemberDto meetingMemberDto){

        String email=principal.getName();
        return meetingService.addMemberToMissionMeeting(id,email,meetingMemberDto);
    }

   // meeting id
    @PutMapping("/mission/remove/{id}")
    public ResponseEntity<?>  removeMemberFromMissionMeeting(@PathVariable Long id, Principal principal,MeetingMemberDto meetingMemberDto){

        String email=principal.getName();
        return meetingService.removeMemberFromMissionMeeting(id,email,meetingMemberDto);
    }

    //meeting id
    @DeleteMapping("/mission/{id}")
    public ResponseEntity<?>  deleteMissionMeeting(@PathVariable Long id, Principal principal){

        String email=principal.getName();
        return meetingService.deleteMissionMeeting(id,email);
    }
}
