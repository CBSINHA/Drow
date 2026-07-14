package com.drow.backend.section;


import com.drow.backend.section.dto.CreateSectionRequest;
import com.drow.backend.section.dto.SectionResponse;
import com.drow.backend.section.dto.UpdateSectionRequest;
import jakarta.validation.Valid;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notebooks/{notebookId}/sections")
@RequiredArgsConstructor
public class SectionController {

    private final SectionService sectionService;

    @PostMapping
    public ResponseEntity<SectionResponse> createSection(
            @PathVariable UUID notebookId,
            @Valid @RequestBody CreateSectionRequest request
    ) {

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(
                        sectionService.createSection(
                                notebookId,
                                request
                        )
                );
    }

    @GetMapping
    public ResponseEntity<List<SectionResponse>> getSections(
            @PathVariable UUID notebookId
    ) {

        return ResponseEntity.ok(
                sectionService.getSections(notebookId)
        );
    }

    @PatchMapping("/{sectionId}")
    public ResponseEntity<SectionResponse> updateSection(
            @PathVariable UUID notebookId,
            @PathVariable UUID sectionId,
            @Valid @RequestBody UpdateSectionRequest request
    ) {

        return ResponseEntity.ok(
                sectionService.updateSection(
                        sectionId,
                        notebookId,
                        request
                )
        );
    }

    @DeleteMapping("/{sectionId}")
    public ResponseEntity<Void> moveSectionToTrash(
            @PathVariable UUID notebookId,
            @PathVariable UUID sectionId
    ) {

        sectionService.moveSectionToTrash(sectionId,notebookId);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/trash")
    public ResponseEntity<List<SectionResponse>> getTrashSections(
            @PathVariable UUID notebookId
    ) {

        return ResponseEntity.ok(
                sectionService.getTrashSections(notebookId)
        );
    }

    @PatchMapping("/{sectionId}/restore")
    public ResponseEntity<Void> restoreSection(
            @PathVariable UUID notebookId,
            @PathVariable UUID sectionId
    ) {

        sectionService.restoreSection(notebookId,sectionId);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{sectionId}/permanent")
    public ResponseEntity<Void> permanentlyDeleteSection(
            @PathVariable UUID notebookId,
            @PathVariable UUID sectionId
    ) {

        sectionService.permanentlyDeleteSection(notebookId,sectionId);

        return ResponseEntity.noContent().build();
    }
}