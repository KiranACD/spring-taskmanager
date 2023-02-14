package com.example.springtaskmanager.services;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Locale;
import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

//import com.example.springtaskmanager.entities.NoteEntity;
import com.example.springtaskmanager.entities.TaskEntity;
//import com.example.springtaskmanager.repositories.NotesRepository;
import com.example.springtaskmanager.repositories.TasksRepository;

@Service
public class TaskService {

    @Autowired
    private TasksRepository tasksRepository;

    // @Autowired
    // private NotesRepository notesRepository;

    private SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd", Locale.ENGLISH);

    public static class TaskNotFoundException extends IllegalStateException {
        public TaskNotFoundException(long id) {
            super("Task with id " + id + " not found"); 
        }
    }

    public static class TaskNotFoundByTitleException extends IllegalStateException {
        public TaskNotFoundByTitleException(String title) {
            super("Tasks with title " + title + " not found"); 
        }
    }

    public static class CompletedTasksNotFoundException extends IllegalStateException {
        public CompletedTasksNotFoundException() {
            super("Completed tasks not found"); 
        }
    }

    public static class IncorrectDateException extends RuntimeException {
        public IncorrectDateException(String date) {
            super("Format of " + date + " is not correct. It should be ('yyyy-MM-dd')");
        }
    }

    public static class IncorrectDueDateException extends RuntimeException {
        public IncorrectDueDateException(Date date) {
            super("Due date " + date.toString() + " cannot be before today. Format should be ('yyyy-MM-dd')");
        }
    }

    public List<TaskEntity> getTasks() {
        return this.tasksRepository.findAll();
    }

    public TaskEntity getTaskById(Long id) {
        Optional<TaskEntity> taskOptional = this.tasksRepository.findById(id);

        if (taskOptional.isPresent()) {
            return taskOptional.get();
        }
        throw new TaskNotFoundException(id);
    }
    
    public TaskEntity createTask(String title, String description, String dueDate) {

        // TODO: Ensure date is not before today
        Date date = checkAndConvertToDateType(dueDate);
        var newTask = new TaskEntity();
        newTask.setTitle(title);
        newTask.setDescription(description);
        newTask.setCompleted(false);
        newTask.setDueDate(date);
        //newTask.setCreatedAt(new Date);
        return newTask;
        // } catch (IncorrectDateException e) {
        //     throw new IncorrectDateException(dueDate);
        // }
    }

    public TaskEntity saveTask(TaskEntity task) {
        var savedTask = this.tasksRepository.save(task);
        return savedTask;
    }
    
    public Date checkAndConvertToDateType(String date) {

        try {
            Date dueDate = formatter.parse(date);
            Date today = new Date();
            if (dueDate.before(formatter.parse(formatter.format(today)))) {
                throw new IncorrectDueDateException(dueDate);
            }
            return dueDate;
        } catch (ParseException e) {
            throw new IncorrectDateException(date);
        }
    }

    public TaskEntity getReferenceById(Long taskId) {
        return this.tasksRepository.getReferenceById(taskId);
    }

    public TaskEntity updateTask(Long id, String title, String description, String dueDate, Boolean completed) {
        var task = getReferenceById(id);

        if (description != null) task.setDescription(description);
        if (title != null) task.setTitle(title);
        if (dueDate != null) {
            Date date = checkAndConvertToDateType(dueDate);
            task.setDueDate(date);
        }
        if (completed != null) task.setCompleted(completed);
        //this.tasksRepository.save(task);
        return task;
    }

    public TaskEntity deleteTask(Long id) {
        TaskEntity task = getTaskById(id);
        this.tasksRepository.deleteById(id);
        return task;
    }

    public List<TaskEntity> getTasksByTitle(String title) {
        var tasksOptional = this.tasksRepository.findAllByTitle(title);
        if (tasksOptional.isPresent()) {
            return tasksOptional.get();
        }
        throw new TaskNotFoundByTitleException(title);
    }

    public List<TaskEntity> getTasksByCompleted(Boolean completed) {
        var tasksOptional = this.tasksRepository.findAllByCompletedTrue(completed);
        if (tasksOptional.isPresent()) {
            return tasksOptional.get();
        }
        throw new Comp
    }
}
