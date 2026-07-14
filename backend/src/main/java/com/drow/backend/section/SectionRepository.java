package com.drow.backend.section;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface SectionRepository extends JpaRepository<Section, UUID> {
    List<Section> findByNotebookIdAndIsDeletedFalseOrderByPositionAsc(
            UUID notebookId
    );
    List<Section> findByNotebookIdAndIsDeletedTrueOrderByPositionAsc(
            UUID notebookId
    );

    long countByNotebookIdAndIsDeletedFalse(
            UUID notebookId
    );

    boolean existsByNotebookIdAndNameAndIsDeletedFalse(
            UUID notebookId,
            String name
    );

    Optional<Section> findByIdAndNotebookId(
            UUID sectionId,
            UUID notebookId
    );

    boolean existsByNotebookId(UUID notebookId);

}
