package com.highbrowape.demo.service;

import com.highbrowape.demo.dto.input.MeetingDto;
import com.highbrowape.demo.dto.input.MeetingMemberDto;
import org.springframework.http.ResponseEntity;

public interface IMeetingService {
    ResponseEntity<?> addMeetingToMission(String id, MeetingDto meetingDto, String email);

    ResponseEntity<?> getMissionMeeting(String id, String email);

    ResponseEntity<?> addMemberToMissionMeeting(Long id, String email, MeetingMemberDto meetingMemberDto);

    ResponseEntity<?> removeMemberFromMissionMeeting(Long id, String email, MeetingMemberDto meetingMemberDto);

    ResponseEntity<?> deleteMissionMeeting(Long id, String email);
}
