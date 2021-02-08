package com.highbrowape.demo.service;


import com.highbrowape.demo.dto.input.MeetingDto;
import com.highbrowape.demo.dto.input.MeetingMemberDto;
import com.highbrowape.demo.entity.*;
import com.highbrowape.demo.exception.*;
import com.highbrowape.demo.repository.MemberRepository;
import com.highbrowape.demo.repository.MissionMeetingRepository;
import com.highbrowape.demo.repository.MissionRepository;
import com.highbrowape.demo.repository.UserRepository;
import org.modelmapper.ModelMapper;
import org.modelmapper.convention.MatchingStrategies;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Optional;
import java.util.UUID;

@Service
public class MeetingServiceImpl implements IMeetingService {
    @Autowired
    UserRepository userRepository;

    @Autowired
    MissionRepository missionRepository;

    @Autowired
    MemberRepository memberRepository;

    @Autowired
    MissionMeetingRepository missionMeetingRepository;

    @Override
    public ResponseEntity<?> addMeetingToMission(String id, MeetingDto meetingDto, String email) {

        HashMap<String, Object> userMap = isValidUser(email);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(email + " is not a valid user ");
        User user = (User) userMap.get("user");

        HashMap<String, Object> missionMap = isValidMission(id);
        if (!(boolean) missionMap.get("isValid"))
            throw new MissionNotFoundException(" No Mission found with mission id :" + id);
        Mission mission = (Mission) missionMap.get("mission");

        HashMap<String, Object> memberMap = isValidProjectMember(mission.getProject(), email);
        if (!(boolean) memberMap.get("isValid"))
            throw new MemberNotFoundException(email + " is not a member of the project ");

        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        MissionMeeting missionMeeting = mapper.map(meetingDto, MissionMeeting.class);

        String uuid= UUID.randomUUID().toString();
        missionMeeting.setPassword(uuid);
        missionMeeting.setAddedBy(email);
        missionMeeting.setAddedOn(new java.util.Date());
        missionMeeting.setMission(mission);
        missionMeeting.setName(mission.getProject().getProjectName()+" "+mission.getName()+" "+mission.getMissionId()+" "+email+" "+new java.util.Date());

        Optional<MissionMeeting> missionMeetingOptional=missionMeetingRepository.findByMission(mission);
        if(missionMeetingOptional.isPresent()) missionMeetingRepository.delete(missionMeetingOptional.get());

        return new ResponseEntity<>(missionMeetingRepository.save(missionMeeting), HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> getMissionMeeting(String id, String email) {
        HashMap<String, Object> userMap = isValidUser(email);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(email + " is not a valid user ");
        User user = (User) userMap.get("user");

        HashMap<String, Object> missionMap = isValidMission(id);
        if (!(boolean) missionMap.get("isValid"))
            throw new MissionNotFoundException(" No Mission found with mission id :" + id);
        Mission mission = (Mission) missionMap.get("mission");

        HashMap<String, Object> memberMap = isValidProjectMember(mission.getProject(), email);
        if (!(boolean) memberMap.get("isValid"))
            throw new MemberNotFoundException(email + " is not a member of the project ");

        Optional<MissionMeeting> missionMeetingOptional=missionMeetingRepository.findByMission(mission);
        if(!missionMeetingOptional.isPresent())
            throw new MeetingNotFoundException("No meeting found for mission with id "+ id);
        return new ResponseEntity<>(missionMeetingOptional.get(), HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> addMemberToMissionMeeting(Long id, String email) {
        HashMap<String, Object> userMap = isValidUser(email);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(email + " is not a valid user ");
        User user = (User) userMap.get("user");


        Optional<MissionMeeting> missionMeetingOptional=missionMeetingRepository.findById(id);
        if(!missionMeetingOptional.isPresent())
            throw new MeetingNotFoundException("No meeting found  with id "+ id);

        MissionMeeting missionMeeting=missionMeetingOptional.get();
        String missionId = missionMeeting.getMission().getMissionId();

        HashMap<String, Object> missionMap = isValidMission(missionId);
        if (!(boolean) missionMap.get("isValid"))
            throw new MissionNotFoundException(" No Mission found with mission id :" + missionId);
        Mission mission = (Mission) missionMap.get("mission");

        HashMap<String, Object> memberMap = isValidProjectMember(mission.getProject(), email);
        if (!(boolean) memberMap.get("isValid"))
            throw new MemberNotFoundException(email + " is not a member of the project ");


        MeetingMemberDto meetingMemberDto = new MeetingMemberDto(user.getEmail(),user.getFirstName()+" "+user.getLastName(),user.getImageUrl());
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        MeetingMember meetingMember = mapper.map(meetingMemberDto, MeetingMember.class);

        missionMeeting.addMeetingMember(meetingMember);


        return new ResponseEntity<>(missionMeetingRepository.save(missionMeeting), HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> removeMemberFromMissionMeeting(Long id, String email) {
        HashMap<String, Object> userMap = isValidUser(email);
        if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(email + " is not a valid user ");
        User user = (User) userMap.get("user");

        Optional<MissionMeeting> missionMeetingOptional=missionMeetingRepository.findById(id);
        if(!missionMeetingOptional.isPresent())
            throw new MeetingNotFoundException("No meeting found  with id "+ id);

        MissionMeeting missionMeeting=missionMeetingOptional.get();
        String missionId = missionMeeting.getMission().getMissionId();

        HashMap<String, Object> missionMap = isValidMission(missionId);
        if (!(boolean) missionMap.get("isValid"))
            throw new MissionNotFoundException(" No Mission found with mission id :" + missionId);
        Mission mission = (Mission) missionMap.get("mission");

        HashMap<String, Object> memberMap = isValidProjectMember(mission.getProject(), email);
        if (!(boolean) memberMap.get("isValid"))
            throw new MemberNotFoundException(email + " is not a member of the project ");

        MeetingMemberDto meetingMemberDto = new MeetingMemberDto(user.getEmail(),user.getFirstName()+" "+user.getLastName(),user.getImageUrl());
        ModelMapper mapper = new ModelMapper();
        mapper.getConfiguration().setMatchingStrategy(MatchingStrategies.STRICT);
        MeetingMember meetingMember = mapper.map(meetingMemberDto, MeetingMember.class);

        missionMeeting.removeMeetingMember(meetingMember);


        return new ResponseEntity<>(missionMeetingRepository.save(missionMeeting), HttpStatus.ACCEPTED);
    }

    @Override
    public ResponseEntity<?> deleteMissionMeeting(Long id, String email) {

            HashMap<String, Object> userMap = isValidUser(email);
            if (!(boolean) userMap.get("isValid")) throw new UserNotFoundException(email + " is not a valid user ");
            User user = (User) userMap.get("user");

        Optional<MissionMeeting> missionMeetingOptional=missionMeetingRepository.findById(id);
        if(!missionMeetingOptional.isPresent())
            throw new MeetingNotFoundException("No meeting found  with id "+ id);

        MissionMeeting missionMeeting=missionMeetingOptional.get();
        String missionId = missionMeeting.getMission().getMissionId();

            HashMap<String, Object> missionMap = isValidMission(missionId);
            if (!(boolean) missionMap.get("isValid"))
                throw new MissionNotFoundException(" No Mission found with mission id :" + missionId);
            Mission mission = (Mission) missionMap.get("mission");

            HashMap<String, Object> memberMap = isValidProjectMember(mission.getProject(), email);
            if (!(boolean) memberMap.get("isValid"))
                throw new MemberNotFoundException(email + " is not a member of the project ");


        try {
            missionMeetingRepository.delete(missionMeeting);
        } catch (Exception ex) {
            throw new MeetingNotFoundException("No meeting found with id  " + id);
        }

        return new ResponseEntity<>("MEETING DELETED SUCCESSFULLY", HttpStatus.ACCEPTED);
    }



    public HashMap<String, Object> isValidUser(String email) {

        HashMap<String, Object> result = new HashMap<>();
        Optional<User> optionalUser = userRepository.findByEmail(email);
        if (optionalUser.isPresent()) {
            result.put("isValid", true);
            result.put("user", optionalUser.get());
            return result;
        }
        result.put("isValid", false);
        return result;
    }
    public HashMap<String, Object> isValidMission(String id) {
        HashMap<String, Object> result = new HashMap<>();
        Optional<Mission> optionalMission = missionRepository.findByMissionId(id);
        if (optionalMission.isPresent()) {
            result.put("isValid", true);
            result.put("mission", optionalMission.get());
            return result;
        }
        result.put("isValid", false);
        return result;
    }

    public HashMap<String, Object> isValidProjectMember(Project project, String email) {
        HashMap<String, Object> result = new HashMap<>();
        Optional<Member> optionalMember = memberRepository.findByProjectAndUserEmail(project, email);
        if (optionalMember.isPresent()) {
            result.put("isValid", true);
            result.put("member", optionalMember.get());
            return result;
        }
        result.put("isValid", false);
        return result;
    }

}
