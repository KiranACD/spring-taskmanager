package com.example.springtaskmanager.entities;

import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
//import javax.persistence.Index;
import javax.persistence.OneToMany;
//import javax.persistence.Table;

import lombok.Getter;
import lombok.Setter;

//@Entity
@Entity(name="tasks")
//@Table(indexes=@Index(columnList = "title"))
@Getter
@Setter
public class TaskEntity extends BaseEntity {
    
    @Column(name="title", nullable=false, length=150)
    String title;
    @Column(name="description", nullable=true, length=500)
    String description;
    @Column(name="completed", nullable=false, columnDefinition="boolean default false")
    Boolean completed;

    @OneToMany(mappedBy="task", cascade = CascadeType.ALL, fetch=FetchType.EAGER, orphanRemoval=true)
    List<NoteEntity> notes;    

    // @Column(name="completed", nullable=false)
    // Boolean completed = false;

    @Column(name="due_date", nullable=true)
    Date dueDate;
}
