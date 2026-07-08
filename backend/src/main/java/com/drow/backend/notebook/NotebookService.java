package com.drow.backend.notebook;

import com.drow.backend.common.exception.ResourceNotFoundException;
import com.drow.backend.notebook.dto.CreateNotebookRequest;
import com.drow.backend.notebook.dto.NotebookResponse;
import com.drow.backend.notebook.dto.UpdateNotebookRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
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
        long position =
                notebookRepository.countByOwnerIdAndIsDeletedFalse(
                        notebook.getOwnerId()
                );

        notebook.setId(UUID.randomUUID());
        notebook.setName(request.name());
        notebook.setPosition((int) position);
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

    public List<NotebookResponse> getNotebooks() {

        UUID ownerId = UUID.fromString(
                "00000000-0000-0000-0000-000000000001"
        );

        return notebookRepository
                .findByOwnerIdAndIsDeletedFalseOrderByPositionAsc(ownerId)
                .stream()
                .map(notebook -> new NotebookResponse(
                        notebook.getId(),
                        notebook.getName()
                ))
                .toList();
    }

    public NotebookResponse updateNotebook(
            UUID notebookId,
            UpdateNotebookRequest request
    ) {

        Notebook notebook = notebookRepository
                .findById(notebookId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Notebook not found: " + notebookId
                        )
                );

        notebook.setName(request.name());

        notebook.setUpdatedAt(LocalDateTime.now());

        Notebook updatedNotebook =
                notebookRepository.save(notebook);

        return new NotebookResponse(
                updatedNotebook.getId(),
                updatedNotebook.getName()
        );
    }

    public void moveToTrash(UUID notebookId) {

        Notebook notebook = notebookRepository
                .findById(notebookId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Notebook not found: " + notebookId
                        )
                );

        notebook.setIsDeleted(true);

        notebook.setUpdatedAt(LocalDateTime.now());

        notebookRepository.save(notebook);
    }

    public List<NotebookResponse> getTrashNotebooks() {

        UUID ownerId = UUID.fromString(
                "00000000-0000-0000-0000-000000000001"
        );

        return notebookRepository
                .findByOwnerIdAndIsDeletedTrueOrderByPositionAsc(ownerId)
                .stream()
                .map(notebook -> new NotebookResponse(
                        notebook.getId(),
                        notebook.getName()
                ))
                .toList();
    }

    public void restoreNotebook(UUID notebookId) {

        Notebook notebook = notebookRepository
                .findById(notebookId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Notebook not found: " + notebookId
                        )
                );

        notebook.setIsDeleted(false);

        notebook.setUpdatedAt(LocalDateTime.now());

        notebookRepository.save(notebook);
    }
}