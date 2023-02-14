package com.example.springtaskmanager.repositories;

import java.util.List;
import java.util.Optional;

import com.example.springtaskmanager.entities.NoteEntity;
import com.example.springtaskmanager.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NotesRepository extends JpaRepository<NoteEntity, Long> {
    
    List<NoteEntity> findByTask(TaskEntity task);

    Optional<List<NoteEntity>> findAllByTaskId(Long taskId);

    Optional<NoteEntity> findByTaskAndId(TaskEntity task, Long noteId);

    Optional<NoteEntity> findByTaskIdAndId(Long taskId, Long noteId);

}
