package com.example.springtaskmanager.repositories;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.example.springtaskmanager.entities.TaskEntity;
//import com.example.springtaskmanager.repositories.TasksRepository;

@DataJpaTest
public class TasksRepositoryTests {

    @Autowired 
    TasksRepository tasksRepository;

    @Test
    public void testCreateTask() {
        TaskEntity task = new TaskEntity();
        task.setTitle("Test Task");
        task.setDescription("This is description.");
        task.setCompleted(false);
        TaskEntity savedTask = tasksRepository.save(task);
        assertNotNull(savedTask);
    }

    @Test
    public void readTasksWorks() {
        TaskEntity task1 = new TaskEntity();
        task1.setTitle("Test Task1");
        task1.setDescription("This is description 1.");
        task1.setCompleted(false);
        tasksRepository.save(task1);
        TaskEntity task2 = new TaskEntity();
        task2.setTitle("Test Task2");
        task2.setDescription("This is description 2.");
        task2.setCompleted(false);
        tasksRepository.save(task2);
        var tasks = tasksRepository.findAll();
        assertNotNull(tasks);
        assertEquals(2, tasks.size());
    }

    @Test
    public void findByCompletedTasksWorks() {
        TaskEntity task1 = new TaskEntity();
        task1.setTitle("Test Task1");
        task1.setDescription("This is description 1.");
        task1.setCompleted(false);
        tasksRepository.save(task1);
        TaskEntity task2 = new TaskEntity();
        task2.setTitle("Test Task2");
        task2.setDescription("This is description 2.");
        task2.setCompleted(true);
        tasksRepository.save(task2);

        var completedTasks = tasksRepository.findAllByCompletedTrue().get();
        var incompleteTasks = tasksRepository.findAllByCompletedFalse().get();

        assertEquals(1, completedTasks.size());
        assertEquals(1, incompleteTasks.size());
    }

    @Test
    public void findTasksByTitleWorks() {
        TaskEntity task1 = new TaskEntity();
        task1.setTitle("Test Task1");
        task1.setDescription("This is description 1.");
        task1.setCompleted(false);
        tasksRepository.save(task1);
        TaskEntity task2 = new TaskEntity();
        task2.setTitle("Test Task2");
        task2.setDescription("This is description 2.");
        task2.setCompleted(true);
        tasksRepository.save(task2);
        TaskEntity task3 = new TaskEntity();
        task3.setTitle("Test Task2");
        task3.setDescription("This is description 3.");
        task3.setCompleted(true);
        tasksRepository.save(task3);

        var testTask1 = tasksRepository.findAllByTitle("Test Task1").get();
        assertEquals(1, testTask1.size());

        var testTask2 = tasksRepository.findAllByTitle("Test Task2").get();
        assertEquals(2, testTask2.size());
    }

}