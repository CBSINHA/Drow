package com.drow.backend.notebook.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record CreateNotebookRequest(

        @NotBlank(message = "Notebook name is required")

        @Size(
                min = 1,
                max = 100,
                message = "Notebook name must be between 1 and 100 characters"
        )
        String name

) {}