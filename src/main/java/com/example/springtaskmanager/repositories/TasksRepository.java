package com.example.springtaskmanager.repositories;

//import java.util.ArrayList;
import java.util.List;
import java.util.Date;
import java.util.Optional;

import com.example.springtaskmanager.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TasksRepository extends JpaRepository<TaskEntity, Long> {
    
    List<TaskEntity> findAll();
    Optional<List<TaskEntity>> findAllByTitle(String title);
    Optional<List<TaskEntity>> findAllByCompletedTrue();
    Optional<List<TaskEntity>> findAllByCompletedFalse();
    List<TaskEntity> findAllByCompletedAndDueDateBefore(Boolean completed, Date dueDate);
      
}
