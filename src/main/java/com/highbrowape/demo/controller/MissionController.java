package com.highbrowape.demo.controller;


import com.highbrowape.demo.dto.input.MissionAddDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

@RestController
@RequestMapping("/api/v1/mission")
public class MissionController {



    @PostMapping("/level/1/{id}")
    public ResponseEntity<?> addMissionAtLevel1(@Valid @RequestBody MissionAddDto missionAddDto){

        return null;

    }

}
