package org.example.model.db;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.jetbrains.annotations.NotNull;


@Entity
@Table(name = "DB_TASK")
@Data
@NoArgsConstructor
public class Task {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "TASK_SEQ")
    @SequenceGenerator(name = "TASK_SEQ", sequenceName = "TASK_SEQ", allocationSize = 1)
    private long id;
    @NotNull
    private String title;
    @NotNull
    private String author;
    @NotNull
    private String status;
    @NotNull
    private String description;

}