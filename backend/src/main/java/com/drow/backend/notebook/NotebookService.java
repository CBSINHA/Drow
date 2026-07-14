package com.drow.backend.notebook;

import com.drow.backend.common.exception.ResourceNotFoundException;
import com.drow.backend.notebook.dto.CreateNotebookRequest;
import com.drow.backend.notebook.dto.NotebookResponse;
import com.drow.backend.notebook.dto.UpdateNotebookRequest;
import com.drow.backend.section.SectionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;


@Service
@RequiredArgsConstructor
public class NotebookService {
    private final NotebookRepository notebookRepository;
    private final SectionRepository sectionRepository;
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
        if (
                notebookRepository
                        .existsByOwnerIdAndNameAndIsDeletedFalse(
                                notebook.getOwnerId(),
                                request.name()
                        )
        ) {
            throw new IllegalStateException(
                    "Notebook with this name already exists"
            );
        }

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
        if (Boolean.TRUE.equals(notebook.getIsDeleted())) {
            throw new IllegalStateException(
                    "Notebook is in Trash"
            );
        }


        String newName = request.name();

        // No-op rename
        if (notebook.getName().equals(newName)) {

            return new NotebookResponse(
                    notebook.getId(),
                    notebook.getName()
            );
        }

        if (
                notebookRepository
                        .existsByOwnerIdAndNameAndIsDeletedFalse(
                                notebook.getOwnerId(),
                                newName
                        )
        ) {
            throw new IllegalStateException(
                    "Notebook with this name already exists"
            );
        }

        notebook.setName(newName);

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

        if (Boolean.TRUE.equals(notebook.getIsDeleted())) {
            throw new IllegalStateException(
                    "Notebook is already in Trash"
            );
        }

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

        if (Boolean.FALSE.equals(notebook.getIsDeleted())) {
            throw new IllegalStateException(
                    "Notebook is not in Trash"
            );
        }

        if (
                notebookRepository
                        .existsByOwnerIdAndNameAndIsDeletedFalse(
                                notebook.getOwnerId(),
                                notebook.getName()
                        )
        ) {

            String restoredName =
                    notebook.getName() + " (Restored)";

            int counter = 1;

            while (
                    notebookRepository
                            .existsByOwnerIdAndNameAndIsDeletedFalse(
                                    notebook.getOwnerId(),
                                    restoredName
                            )
            ) {

                restoredName =
                        notebook.getName()
                                + " (Restored "
                                + counter++
                                + ")";
            }

            notebook.setName(restoredName);
        }

        notebook.setIsDeleted(false);

        notebook.setUpdatedAt(LocalDateTime.now());

        notebookRepository.save(notebook);
    }

    public void permanentlyDeleteNotebook(UUID notebookId) {

        Notebook notebook = notebookRepository
                .findById(notebookId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Notebook not found: " + notebookId
                        )
                );

        if (!notebook.getIsDeleted()) {
            throw new IllegalStateException(
                    "Notebook must be moved to trash before permanent deletion"
            );
        }

        if(sectionRepository.existsByNotebookId(notebookId)){
            throw new IllegalStateException(
                    "Notebook contains sections. Delete sections first."
            );
        }

        notebookRepository.delete(notebook);
    }
}