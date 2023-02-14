package com.example.springtaskmanager.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.springtaskmanager.entities.NoteEntity;
import com.example.springtaskmanager.entities.TaskEntity;
import com.example.springtaskmanager.repositories.NotesRepository;
//import com.example.springtaskmanager.repositories.TasksRepository;

@Service
public class NoteService {

    // @Autowired
    // private TasksRepository tasksRepository;

    @Autowired
    private NotesRepository notesRepository;

    @Autowired
    private TaskService taskService;

    public static class NoteNotFoundException extends IllegalStateException {
        public NoteNotFoundException(Long id) {
            super("Note with id " + id + " not found");
        }
    }

    public static class NotesNotFoundException extends IllegalStateException {
        public NotesNotFoundException(Long id) {
            super("Notes for task with id " + id + " not found");
        }
    }

    public List<NoteEntity> getAllNotesByTaskId(Long taskId) {
        // TaskEntity task = this.taskService.getTaskById(taskId);
        // List<NoteEntity> notes = this.notesRepository.findByTask(task);
        var notesOptional = this.notesRepository.findAllByTaskId(taskId);
        if (notesOptional.isPresent()) {
            return notesOptional.get();
        }
        throw new NotesNotFoundException(taskId);
        // List<NoteEntity> notes = this.notesRepository.findAll();
        // List<NoteEntity> taskNotes = new ArrayList<>();
        // for (NoteEntity note: notes) {
        //     if (note.getTask().getId() == taskId) {
        //         taskNotes.add(note);
        //     }
        // }
        // return taskNotes;
    }

    public NoteEntity getNoteByTaskIdAndNoteId(Long taskId, Long noteId) {
        //var task = this.taskService.getTaskById(taskId);
        var noteOptional = this.notesRepository.findByTaskIdAndId(taskId, noteId);
        if (noteOptional.isPresent()) {
            return noteOptional.get();
        }
        throw new NoteNotFoundException(noteId);
    }

    public NoteEntity createNote(Long taskId, String body) {
        var task = this.taskService.getTaskById(taskId);
        NoteEntity newNote = new NoteEntity();
        newNote.setBody(body);
        newNote.setTask(task);
        return newNote;
    }

    public NoteEntity saveNote(NoteEntity note) {
        var savedNote = this.notesRepository.save(note);
        return savedNote;
    }

    // public NoteEntity getNoteOfTaskById(Integer taskId, Integer noteId) {
    //     Optional<NoteEntity> noteOptional = this.notesRepository.findByTaskIdAndId(taskId, noteId);
    //     if (noteOptional.isPresent()) {
    //         return noteOptional.get();
    //     }
    //     throw new NoteNotFoundException(noteId);
    // }

    public NoteEntity deleteNoteByTaskIdAndNoteId(Long taskId, Long noteId) {
        TaskEntity task = this.taskService.getReferenceById(taskId);
        NoteEntity note = getNoteByTaskIdAndNoteId(taskId, noteId);
        //this.notesRepository.deleteById(note.getId());
        task.getNotes().remove(note);
        this.taskService.saveTask(task);
        return note;
    }
    
}
