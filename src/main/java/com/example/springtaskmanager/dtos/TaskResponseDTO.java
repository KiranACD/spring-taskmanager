package com.example.springtaskmanager.dtos;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;
import java.util.List;

import com.example.springtaskmanager.entities.NoteEntity;

@Getter
@Setter
public class TaskResponseDTO {
    
    Long id;
    String title;
    String description;
    Date dueDate;
    Boolean completed;
    List<NoteEntity> notes;
}
