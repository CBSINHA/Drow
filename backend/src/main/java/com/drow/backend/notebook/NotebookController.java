package com.drow.backend.notebook;

import com.drow.backend.notebook.dto.CreateNotebookRequest;
import com.drow.backend.notebook.dto.NotebookResponse;
import com.drow.backend.notebook.dto.UpdateNotebookRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1/notebooks")
@RequiredArgsConstructor
public class NotebookController {

    private final NotebookService notebookService;

    @PostMapping
    public ResponseEntity<NotebookResponse> createNotebook(
            @Valid @RequestBody CreateNotebookRequest request
    ) {

        NotebookResponse response =
                notebookService.createNotebook(request);

        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(response);
    }

    @GetMapping
    public ResponseEntity<List<NotebookResponse>> getNotebooks() {

        return ResponseEntity.ok(
                notebookService.getNotebooks()
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<NotebookResponse> updateNotebook(
            @PathVariable UUID id,
            @Valid @RequestBody UpdateNotebookRequest request
    ) {

        return ResponseEntity.ok(
                notebookService.updateNotebook(id, request)
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> moveToTrash(
            @PathVariable UUID id
    ) {

        notebookService.moveToTrash(id);

        return ResponseEntity.noContent().build();
    }

    @GetMapping("/trash")
    public ResponseEntity<List<NotebookResponse>> getTrashNotebooks() {

        return ResponseEntity.ok(
                notebookService.getTrashNotebooks()
        );
    }

    @PatchMapping("/restore/{id}")
    public ResponseEntity<Void> restoreNotebook(
            @PathVariable UUID id
    ) {

        notebookService.restoreNotebook(id);

        return ResponseEntity.noContent().build();
    }

    @DeleteMapping("/{id}/permanent")
    public ResponseEntity<Void> permanentlyDeleteNotebook(
            @PathVariable UUID id
    ) {

        notebookService.permanentlyDeleteNotebook(id);

        return ResponseEntity.noContent().build();
    }
}