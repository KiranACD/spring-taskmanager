package com.example.springtaskmanager.entities;

import javax.persistence.Entity;
import javax.persistence.ManyToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;

import lombok.Getter;
import lombok.Setter;

import javax.persistence.Column;

//@Entity
@Entity(name="notes")
@Getter
@Setter
public class NoteEntity extends BaseEntity {

    @Column(name="body", nullable=false, length=500)
    String body;

    @ManyToOne
    @JsonIgnore
    TaskEntity task;
    
}
