package com.drow.backend.section;


import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "sections")
@Getter
@Setter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Section {

    @Id
    private UUID id;

    @Column(name = "notebook_id", nullable = false)
    private UUID notebookId;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private Integer position;


    @Column(name = "is_deleted", nullable = false)
    private Boolean isDeleted;

    @Column(name = "created_at", nullable = false)
    private LocalDateTime createdAt;

    @Column(name = "updated_at", nullable = false)
    private LocalDateTime updatedAt;
}
