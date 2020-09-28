package com.highbrowape.demo.service;

import com.highbrowape.demo.dto.input.LinkDto;
import com.highbrowape.demo.dto.input.MissionAddDto;
import com.highbrowape.demo.dto.input.NoteDto;
import com.highbrowape.demo.dto.input.ProjectMemberDto;
import com.highbrowape.demo.entity.Authority;
import org.springframework.http.ResponseEntity;

public interface IMissionService {

    ResponseEntity<?> addMissionAtLevel1(MissionAddDto missionAddDto,Long id, String loggedInEmail);
    ResponseEntity<?> addMissionAtLevelN(MissionAddDto missionAddDto,Long id, String loggedInEmail);


    ResponseEntity<?> addMemberToMission(ProjectMemberDto projectMemberDto, Long id, String loggedInEmail);
    ResponseEntity<?> updateMemberAuthorityOfMission(Authority authority, Long id, String loggedInEmail);
    ResponseEntity<?> removeMemberFromMission(Long id, String loggedInEmail);

    ResponseEntity<?> addNoteToMission(NoteDto noteDto, Long id, String loggedInEmail);
    ResponseEntity<?> updateNoteOfMission( NoteDto noteDto,Long id, String loggedInEmail);
    ResponseEntity<?> removeNoteFromMission(Long id, String loggedInEmail);

    ResponseEntity<?> addLinkToMission(LinkDto linkDto, Long id, String loggedInEmail);
    ResponseEntity<?> updateLinkOfMission(LinkDto linkDto,Long id, String loggedInEmail);
    ResponseEntity<?> removeLinkFromMission(Long id, String loggedInEmail);



}
