package com.highbrowape.demo.repository;

import com.highbrowape.demo.entity.Mission;
import com.highbrowape.demo.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface NoteRepository extends JpaRepository<Note,Long> {

    List<Note> findByMission(Mission mission);
}
