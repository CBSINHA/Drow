package com.drow.backend.notebook.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateNotebookRequest(
        @NotBlank(message = "Notebook name is required")
        @Size(max = 100, message = "Notebook name cannot exceed 100 characters")
        String name) {}
