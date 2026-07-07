package com.drow.backend.notebook;

import com.drow.backend.notebook.dto.CreateNotebookRequest;
import com.drow.backend.notebook.dto.NotebookResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class NotebookService {
    private final NotebookRepository notebookRepository;
    public NotebookResponse createNotebook(CreateNotebookRequest request){
        Notebook notebook= new Notebook();

        notebook.setOwnerId(UUID.fromString(
                "00000000-0000-0000-0000-000000000001"
        ));
        notebook.setId(UUID.randomUUID());
        notebook.setName(request.name());
        notebook.setPosition(0);
        notebook.setIsFavorite(false);
        notebook.setIsDeleted(false);
        LocalDateTime now = LocalDateTime.now();

        notebook.setCreatedAt(now);
        notebook.setUpdatedAt(now);

        Notebook savedNotebook =
                notebookRepository.save(notebook);

        return new NotebookResponse(
                savedNotebook.getId(),
                savedNotebook.getName()
        );
    }
}