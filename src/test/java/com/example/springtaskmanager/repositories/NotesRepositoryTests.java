package com.example.springtaskmanager.repositories;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.example.springtaskmanager.entities.TaskEntity;
import com.example.springtaskmanager.entities.NoteEntity;

@DataJpaTest
public class NotesRepositoryTests {
    
    @Autowired
    NotesRepository notesRepository;

    @Autowired
    TasksRepository tasksRepository;

    @Test
    public void testCreateNote() {
        TaskEntity task = new TaskEntity();
        task.setTitle("Test Task");
        task.setDescription("This is description.");
        task.setCompleted(false);
        var savedTask = tasksRepository.save(task);

        TaskEntity task2 = new TaskEntity();
        task.setTitle("Test Task2");
        task.setDescription("This is description 2.");
        task.setCompleted(false);
        var savedTask2 = tasksRepository.save(task);

        NoteEntity note1 = new NoteEntity();
        note1.setBody("This is task 1 note 1");
        note1.setTask(task);
        var savedNote1 = notesRepository.save(note1);

        NoteEntity note2 = new NoteEntity();
        note2.setBody("This is task 1 note 2");
        note2.setTask(task);
        var savedNote2 = notesRepository.save(note2);

        NoteEntity note3 = new NoteEntity();
        note3.setBody("This is task 2 note 1");
        note3.setTask(task2);
        var savedNote3 = notesRepository.save(note3);

        NoteEntity note4 = new NoteEntity();
        note4.setBody("This is task 2 note 2");
        note4.setTask(task2);
        var savedNote4 = notesRepository.save(note4);

        assertNotNull(savedNote1);
        assertNotNull(savedNote2);
        assertNotNull(savedNote3);
        assertNotNull(savedNote4);
    }

    @Test
    public void findNotesByTaskWorks() {
        TaskEntity task = new TaskEntity();
        task.setTitle("Test Task");
        task.setDescription("This is description.");
        task.setCompleted(false);
        var savedTask = tasksRepository.save(task);
        Long taskId1 = savedTask.getId();

        TaskEntity task2 = new TaskEntity();
        task2.setTitle("Test Task2");
        task2.setDescription("This is description 2.");
        task2.setCompleted(false);
        var savedTask2 = tasksRepository.save(task2);
        Long taskId2 = savedTask2.getId();

        NoteEntity note1 = new NoteEntity();
        note1.setBody("This is task 1 note 1");
        note1.setTask(savedTask);
        notesRepository.save(note1);

        NoteEntity note2 = new NoteEntity();
        note2.setBody("This is task 1 note 2");
        note2.setTask(savedTask);
        notesRepository.save(note2);

        NoteEntity note3 = new NoteEntity();
        note3.setBody("This is task 2 note 1");
        note3.setTask(savedTask2);
        notesRepository.save(note3);

        NoteEntity note4 = new NoteEntity();
        note4.setBody("This is task 2 note 2");
        note4.setTask(savedTask2);
        notesRepository.save(note4);

        var savedNotesTask1 = notesRepository.findAllByTaskId(taskId1).get();
        assertEquals(2, savedNotesTask1.size());

        // var savedNotesTask1 = notesRepository.findByTask(savedTask);
        // assertEquals(2, savedNotesTask1.size());

        var savedNotesTask2 = notesRepository.findByTask(savedTask2);
        assertEquals(2, savedNotesTask2.size());
    }

    @Test
    public void findNotesByTaskIdAndNoteIdWorks() {

        TaskEntity task = new TaskEntity();
        task.setTitle("Test Task");
        task.setDescription("This is description.");
        task.setCompleted(false);
        var savedTask = tasksRepository.save(task);
        Long taskId1 = savedTask.getId();

        TaskEntity task2 = new TaskEntity();
        task2.setTitle("Test Task2");
        task2.setDescription("This is description 2.");
        task2.setCompleted(false);
        var savedTask2 = tasksRepository.save(task2);
        Long taskId2 = savedTask2.getId();

        NoteEntity note1 = new NoteEntity();
        note1.setBody("This is task 1 note 1");
        note1.setTask(savedTask);
        var savedNote11 = notesRepository.save(note1);
        Long noteId11 = savedNote11.getId();

        NoteEntity note2 = new NoteEntity();
        note2.setBody("This is task 1 note 2");
        note2.setTask(savedTask);
        var savedNote12 = notesRepository.save(note2);
        Long noteId12 = savedNote12.getId();

        NoteEntity note3 = new NoteEntity();
        note3.setBody("This is task 2 note 1");
        note3.setTask(savedTask2);
        var savedNote21 = notesRepository.save(note3);
        Long noteId21 = savedNote21.getId();

        NoteEntity note4 = new NoteEntity();
        note4.setBody("This is task 2 note 2");
        note4.setTask(savedTask2);
        var savedNote22 = notesRepository.save(note4);
        Long noteId22 = savedNote22.getId();

        var savedNoteTest12Optional = notesRepository.findByTaskIdAndId(taskId1, noteId12);
        
        var savedNoteTest12 = savedNoteTest12Optional.get();
        
        assertNotNull(savedNoteTest12);
        assertEquals(taskId1, savedNoteTest12.getTask().getId());
    }
}
