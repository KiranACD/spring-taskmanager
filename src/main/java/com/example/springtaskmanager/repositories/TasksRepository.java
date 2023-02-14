package com.example.springtaskmanager.repositories;

//import java.util.ArrayList;
import java.util.List;
import java.util.Date;

import com.example.springtaskmanager.entities.TaskEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TasksRepository extends JpaRepository<TaskEntity, Long> {
    
    List<TaskEntity> findAll();
    List<TaskEntity> findAllByTitle(String title);
    List<TaskEntity> findAllByCompleted(Boolean completed);
    List<TaskEntity> findAllByCompletedAndDueDateBefore(Boolean completed, Date dueDate);
      
}
