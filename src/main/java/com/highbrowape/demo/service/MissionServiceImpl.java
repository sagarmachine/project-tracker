package com.highbrowape.demo.service;

import com.highbrowape.demo.dto.input.LinkDto;
import com.highbrowape.demo.dto.input.MissionAddDto;
import com.highbrowape.demo.dto.input.NoteDto;
import com.highbrowape.demo.dto.input.ProjectMemberDto;
import com.highbrowape.demo.entity.Authority;
import org.springframework.http.ResponseEntity;

public class MissionServiceImpl implements IMissionService {

    @Override
    public ResponseEntity<?> addMissionAtLevel1(MissionAddDto missionAddDto, Long id, String loggedInEmail) {
        return null;
    }

    @Override
    public ResponseEntity<?> addMissionAtLevelN(MissionAddDto missionAddDto, Long id, String loggedInEmail) {
        return null;
    }

    @Override
    public ResponseEntity<?> addMemberToMission(ProjectMemberDto projectMemberDto, Long id, String loggedInEmail) {
        return null;
    }

    @Override
    public ResponseEntity<?> updateMemberAuthorityOfMission(Authority authority, Long id, String loggedInEmail) {
        return null;
    }

    @Override
    public ResponseEntity<?> removeMemberFromMission(Long id, String loggedInEmail) {
        return null;
    }

    @Override
    public ResponseEntity<?> addNoteToMission(NoteDto noteDto, Long id, String loggedInEmail) {
        return null;
    }

    @Override
    public ResponseEntity<?> updateNoteOfMission(NoteDto noteDto, Long id, String loggedInEmail) {
        return null;
    }

    @Override
    public ResponseEntity<?> removeNoteFromMission(Long id, String loggedInEmail) {
        return null;
    }

    @Override
    public ResponseEntity<?> addLinkToMission(LinkDto linkDto, Long id, String loggedInEmail) {
        return null;
    }

    @Override
    public ResponseEntity<?> updateLinkOfMission(LinkDto linkDto, Long id, String loggedInEmail) {
        return null;
    }

    @Override
    public ResponseEntity<?> removeLinkFromMission(Long id, String loggedInEmail) {
        return null;
    }
}
