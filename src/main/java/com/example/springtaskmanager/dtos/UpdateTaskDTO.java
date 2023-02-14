package com.example.springtaskmanager.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UpdateTaskDTO {

    String title;
    String description;
    String dueDate;
    Boolean completed;
}
