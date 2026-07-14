package com.drow.backend.notebook;

import com.drow.backend.notebook.dto.NotebookResponse;
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

    boolean existsByOwnerIdAndNameAndIsDeletedFalse(
            UUID ownerId,
            String name
    );

    long countByOwnerIdAndIsDeletedFalse(UUID ownerId);


}
