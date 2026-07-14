package com.drow.backend.section.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record UpdateSectionRequest(

        @NotBlank(message = "Section name is required")

        @Size(
                max = 100,
                message = "Section name cannot exceed 100 characters"
        )
        String name

) {
}