package com.drow.backend.notebook;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface NotebookRepository extends JpaRepository<Notebook, UUID> {
}
