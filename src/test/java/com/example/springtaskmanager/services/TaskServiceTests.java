package com.example.springtaskmanager.services;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.example.springtaskmanager.repositories.TasksRepository;
import com.example.springtaskmanager.entities.TaskEntity;


import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.text.SimpleDateFormat;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

@ExtendWith(MockitoExtension.class)
public class TaskServiceTests {

    // @TestConfiguration
    // static class TaskServiceTestsContextConfiguration {

    //     @Bean
    //     public TaskService taskService() {
    //         return new TaskService();
    //     }
    // }
    
    @InjectMocks
    TaskService taskService;

    @Mock
    private TasksRepository tasksRepository;

    @Test
    public void testCreateAndSaveTask() {

        String title = "Task title 1";
        String description = "Description of task 1";
        String dueDate = "2023-03-01";
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        // TaskEntity task = new TaskEntity();
        // task.setTitle(title);
        // task.setDescription(description);
        
        var createdTask = taskService.createTask(title, description, dueDate);
        assertNotNull(createdTask);
        assertEquals(title, createdTask.getTitle());
        assertEquals(description, createdTask.getDescription());
        assertEquals(dueDate, formatter.format(createdTask.getDueDate()));

        var savedTask = taskService.saveTask(createdTask);
        assertNotNull(savedTask);
        verify(tasksRepository, times(1)).save(createdTask);
    }

    @Test
    public void getTasksWorks() {

        String title1 = "Task title 1";
        String description1 = "Description of task 1";
        String dueDate1 = "2023-03-01";
        var task1 = this.taskService.createTask(title1, description1, dueDate1);

        String title2 = "Task title 2";
        String description2 = "Description of task 2";
        String dueDate2 = "2023-03-01";
        var task2 = this.taskService.createTask(title2, description2, dueDate2);

        String title3 = "Task title 3";
        String description3 = "Description of task 3";
        String dueDate3 = "2023-03-01";
        var task3 = this.taskService.createTask(title3, description3, dueDate3);

        when(tasksRepository.findAll()).thenReturn(List.of(task1, task2, task3));

        var tasks = this.taskService.getTasks();

        assertNotNull(tasks);
        assertEquals(3, tasks.size());
    }

    @Test
    public void getTaskByIdWorks() {
        
    }
}
