package com.example.springtaskmanager.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class NoteResponseDTO {
    Long id;
    Long taskId;
    String body;
}
