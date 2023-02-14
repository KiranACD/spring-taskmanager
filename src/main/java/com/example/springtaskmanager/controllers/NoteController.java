package com.example.springtaskmanager.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.DeleteMapping;

import com.example.springtaskmanager.dtos.ErrorResponse;
import com.example.springtaskmanager.dtos.CreateNoteDTO;
import com.example.springtaskmanager.dtos.NoteResponseDTO;
import com.example.springtaskmanager.services.NoteService;
import com.example.springtaskmanager.services.TaskService;
import com.example.springtaskmanager.entities.NoteEntity;

import java.util.ArrayList;
import java.util.List;
import java.net.URI;

@RestController
public class NoteController {
    
    private NoteService noteService;

    public NoteController(@Autowired NoteService noteService) {
        this.noteService = noteService;
    }

    @GetMapping("/tasks/{id}/notes")
    public ResponseEntity<List<NoteResponseDTO>> getNotes(@PathVariable("id") Long id) {
        List<NoteEntity> notes = this.noteService.getAllNotesByTaskId(id);
        List<NoteResponseDTO> noteDTOs = new ArrayList<>();
        for (NoteEntity note: notes) {
            NoteResponseDTO noteDTO = new NoteResponseDTO();
            noteDTO.setBody(note.getBody());
            noteDTO.setId(note.getId());
            noteDTO.setTaskId(note.getTask().getId());
            noteDTOs.add(noteDTO);
        }
        return ResponseEntity.ok(noteDTOs);
    }

    @PostMapping("/tasks/{id}/notes")
    public ResponseEntity<NoteResponseDTO> createNote(@PathVariable("id") Long task_id, @RequestBody CreateNoteDTO note) {
        NoteEntity newNote = this.noteService.createNote(task_id, note.getBody());
        NoteEntity newSavedNote = this.noteService.saveNote(newNote);
        
        NoteResponseDTO noteResponseDTO = new NoteResponseDTO();
        noteResponseDTO.setBody(newSavedNote.getBody());
        noteResponseDTO.setId(newSavedNote.getId());
        noteResponseDTO.setTaskId(newSavedNote.getTask().getId());

        return ResponseEntity.created(URI.create("/tasks/"+noteResponseDTO.getTaskId()+"/notes/"+noteResponseDTO.getId())).body(noteResponseDTO);
    }

    @GetMapping("/tasks/{id}/notes/{note_id}")
    public ResponseEntity<NoteResponseDTO> getNoteById(@PathVariable("id") Long task_id, @PathVariable("note_id") Long note_id) {
        NoteEntity note = this.noteService.getNoteByTaskIdAndNoteId(task_id, note_id);
        NoteResponseDTO noteResponseDTO = new NoteResponseDTO();
        noteResponseDTO.setBody(note.getBody());
        noteResponseDTO.setId(note.getId());
        noteResponseDTO.setTaskId(note.getTask().getId());
        return ResponseEntity.ok(noteResponseDTO);
    }

    @DeleteMapping("/tasks/{id}/notes/{note_id}")
    public ResponseEntity<NoteResponseDTO> deleteNoteById(@PathVariable("id") Long task_id, @PathVariable("note_id") Long note_id) {
        NoteEntity note = this.noteService.deleteNoteByTaskIdAndNoteId(task_id, note_id);
        NoteResponseDTO noteResponseDTO = new NoteResponseDTO();
        noteResponseDTO.setBody(note.getBody());
        noteResponseDTO.setId(note.getId());
        noteResponseDTO.setTaskId(note.getTask().getId());
        return ResponseEntity.accepted().body(noteResponseDTO);
    }

    @ExceptionHandler({
        TaskService.TaskNotFoundException.class,
        NoteService.NoteNotFoundException.class,
        NoteService.NotesNotFoundException.class
    })
    ResponseEntity<ErrorResponse> handleErrors(Exception e) {
        return new ResponseEntity<ErrorResponse>(
                new ErrorResponse(e.getMessage()),
                HttpStatus.NOT_FOUND
            );
    }
}
