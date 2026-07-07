package com.drow.backend.notebook;

import com.drow.backend.notebook.dto.CreateNotebookRequest;
import com.drow.backend.notebook.dto.NotebookResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/notebooks")
@RequiredArgsConstructor
public class NotebookController {

    private final NotebookService notebookService;

    @PostMapping
    public NotebookResponse createNotebook(
            @RequestBody CreateNotebookRequest request
    ) {
        return notebookService.createNotebook(request);
    }
}