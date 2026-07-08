package com.drow.backend.notebook;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface NotebookRepository extends JpaRepository<Notebook, UUID> {
    List<Notebook> findByOwnerIdAndIsDeletedFalseOrderByPositionAsc(
            UUID ownerId
    );
    List<Notebook> findByOwnerIdAndIsDeletedTrueOrderByPositionAsc(
            UUID ownerId
    );

    long countByOwnerIdAndIsDeletedFalse(UUID ownerId);

}
