package com.highbrowape.demo.service;

import com.highbrowape.demo.dto.input.*;
import com.highbrowape.demo.entity.Authority;
import com.highbrowape.demo.entity.Status;
import org.springframework.http.ResponseEntity;

public interface IMissionService {

    ResponseEntity<?> addMissionAtLevel1(MissionAddDto missionAddDto, String id, String loggedInEmail);
    ResponseEntity<?> addMissionAtLevelN(MissionAddDto missionAddDto, String id, String loggedInEmail);


    ResponseEntity<?> getMissionMembers(String id, String name);
    ResponseEntity<?> addMemberToMission(ProjectMemberDto projectMemberDto, String id, String loggedInEmail);
    ResponseEntity<?> updateMemberAuthorityOfMission(Authority authority, Long id, String loggedInEmail);
    ResponseEntity<?> removeMemberFromMission(Long id, String loggedInEmail);

    ResponseEntity<?> getMissionNotes(String id, String name);
    ResponseEntity<?> addNoteToMission(NoteDto noteDto, String id, String loggedInEmail);
    ResponseEntity<?> updateNoteOfMission( NoteDto noteDto,Long id, String loggedInEmail);
    ResponseEntity<?> removeNoteFromMission(Long id, String loggedInEmail);

    ResponseEntity<?> getMissionLinks(String id, String name);
    ResponseEntity<?> addLinkToMission(LinkDto linkDto, String id, String loggedInEmail);
    ResponseEntity<?> updateLinkOfMission(LinkDto linkDto,Long id, String loggedInEmail);
    ResponseEntity<?> removeLinkFromMission(Long id, String loggedInEmail);



    ResponseEntity<?> getMissionObjectives(String id, String name);
    ResponseEntity<?> addObjectiveToMission(ObjectiveDto objectiveDto, String id, String name);
    ResponseEntity<?> updateObjectiveOfMission(ObjectiveDto objectiveDto, Long id, String name);
    ResponseEntity<?> removeObjectiveFromMission(Long id, String name);
    void updateObjectiveStatus(Long id, Status status,String loggedInEmail );
}
