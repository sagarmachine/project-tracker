package com.highbrowape.demo.repository;

import com.highbrowape.demo.entity.Note;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NoteRepository extends JpaRepository<Note,Long> {

    Object findByMissionMissionId(String id);
}
