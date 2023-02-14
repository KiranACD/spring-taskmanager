package com.example.springtaskmanager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;


import com.example.springtaskmanager.dtos.ErrorResponse;
import com.example.springtaskmanager.dtos.CreateTaskDTO;
import com.example.springtaskmanager.dtos.TaskResponseDTO;
import com.example.springtaskmanager.dtos.UpdateTaskDTO;
import com.example.springtaskmanager.entities.TaskEntity;
import com.example.springtaskmanager.services.TaskService;

import java.util.ArrayList;
import java.util.List;
import java.net.URI;

@RestController
public class TaskController {

    private TaskService taskService;

    public TaskController(@Autowired TaskService taskService) {
        this.taskService = taskService;
    }

    @GetMapping("/tasks")
    public ResponseEntity<List<TaskResponseDTO>> getTasks(@RequestParam(name="title", required = false) String title, @RequestParam(name="completed", required = false) Boolean completed) {
        if ((title == null) & (completed == null)) {
            List<TaskEntity> tasks = this.taskService.getTasks();
            List<TaskResponseDTO> responseDTOs = createListOfResponseDTOs(tasks);
            return ResponseEntity.ok(responseDTOs);
        } else if (title == null) {
            return getTasksByCompleted(completed);
        } else {
            return getTasksByTitle(title);
        }
    }

    @PostMapping("/tasks")
    public ResponseEntity<TaskResponseDTO> createTask(@RequestBody CreateTaskDTO task) {
        var newTask = this.taskService.createTask(task.getTitle(), task.getDescription(), task.getDueDate());
        var savedNewTask = this.taskService.saveTask(newTask);
        TaskResponseDTO taskResponseDTO = createResponseDTO(savedNewTask);
        return ResponseEntity.created(URI.create("/tasks/"+taskResponseDTO.getId())).body(taskResponseDTO);
    }

    public ResponseEntity<List<TaskResponseDTO>> getTasksByTitle(String title) {
        List<TaskEntity> tasks = this.taskService.getTasksByTitle(title);
        List<TaskResponseDTO> responseDTOs = createListOfResponseDTOs(tasks);
        return ResponseEntity.ok(responseDTOs);

    }

    public ResponseEntity<List<TaskResponseDTO>> getTasksByCompleted(Boolean completed) {
        if (completed == true) {
            List<TaskEntity> tasks = this.taskService.getCompletedTasks();
            List<TaskResponseDTO> responseDTOs = createListOfResponseDTOs(tasks);
            return ResponseEntity.ok(responseDTOs);
        } else {
            List<TaskEntity> tasks = this.taskService.getIncompleteTasks();
            List<TaskResponseDTO> responseDTOs = createListOfResponseDTOs(tasks);
            return ResponseEntity.ok(responseDTOs);
        }
    }

    @GetMapping("/tasks/{id}")
    public ResponseEntity<TaskResponseDTO> getTaskById(@PathVariable("id") Long id) {
        TaskEntity task = this.taskService.getTaskById(id);
        TaskResponseDTO taskResponseDTO = createResponseDTO(task);
        return ResponseEntity.ok(taskResponseDTO);
    }

    @DeleteMapping("/tasks/{id}")
    public ResponseEntity<TaskResponseDTO> deleteTask(@PathVariable("id") Long id) {
        TaskEntity task = this.taskService.deleteTask(id);
        TaskResponseDTO taskResponseDTO = createResponseDTO(task);
        return ResponseEntity.accepted().body(taskResponseDTO);  
    }

    @PatchMapping("/tasks/{id}")
    public ResponseEntity<TaskResponseDTO> modifyTask(@PathVariable("id") Long id, @RequestBody UpdateTaskDTO task) {

        TaskEntity updatedTask = this.taskService.updateTask(id, task.getTitle(), task.getDescription(), task.getDueDate(), task.getCompleted());
        this.taskService.saveTask(updatedTask);
        TaskResponseDTO taskResponseDTO = createResponseDTO(updatedTask);
        return ResponseEntity.accepted().body(taskResponseDTO);
    }

    // @ExceptionHandler({
    //     TaskService.TaskNotFoundException.class,
    //     IllegalStateException.class
    // })
    //  ResponseEntity<ErrorResponse> handleErrors(Exception e) {
    //      if (e instance of TaskService.TaskNotFoundException) {
    //          ...
    //      }
    //      if (e instance of IllegalStateException) {
    //          ...
    //      }  
    //}
    
    public TaskResponseDTO createResponseDTO(TaskEntity task) {
        TaskResponseDTO taskResponseDTO = new TaskResponseDTO();
        taskResponseDTO.setTitle(task.getTitle());
        taskResponseDTO.setDescription(task.getDescription());
        taskResponseDTO.setDueDate(task.getDueDate());
        taskResponseDTO.setCompleted(task.getCompleted());
        taskResponseDTO.setId(task.getId());
        taskResponseDTO.setNotes(task.getNotes());
        return taskResponseDTO;
    }

    public List<TaskResponseDTO> createListOfResponseDTOs(List<TaskEntity> tasks) {
        List<TaskResponseDTO> responseDTOs = new ArrayList<>();
        for (TaskEntity task: tasks) {
            var responseDTO = createResponseDTO(task);
            responseDTOs.add(responseDTO);
        }
        return responseDTOs;
    }

    @ExceptionHandler({
        TaskService.TaskNotFoundException.class,
        TaskService.TaskNotFoundByTitleException.class,
        TaskService.CompletedTasksNotFoundException.class,
        TaskService.IncompleteTasksNotFoundException.class,
        TaskService.IncorrectDateException.class,
        TaskService.IncorrectDueDateException.class,
        RuntimeException.class
    })
    ResponseEntity<ErrorResponse> handleErrors(Exception e) {
        if ((e instanceof TaskService.IncorrectDateException) 
            || (e instanceof TaskService.IncorrectDueDateException)) {
                return new ResponseEntity<ErrorResponse>(
                    new ErrorResponse(e.getMessage()),
                    HttpStatus.BAD_REQUEST
                );
            } else if ((e instanceof TaskService.TaskNotFoundException)
                    || (e instanceof TaskService.TaskNotFoundByTitleException)
                    || (e instanceof TaskService.CompletedTasksNotFoundException)
                    || (e instanceof TaskService.IncompleteTasksNotFoundException)) {
            return new ResponseEntity<ErrorResponse>(
                    new ErrorResponse(e.getMessage()),
                    HttpStatus.NOT_FOUND
                );
            } else {
                return new ResponseEntity<ErrorResponse>(new ErrorResponse(e.getMessage()), HttpStatus.BAD_REQUEST);
            }
    }
}
