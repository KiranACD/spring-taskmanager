package com.example.springtaskmanager.dtos;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateTaskDTO {
    String title;
    String description;
    String dueDate;
}
