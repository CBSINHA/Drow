package com.drow.backend.section;

import com.drow.backend.common.exception.ResourceNotFoundException;
import com.drow.backend.notebook.Notebook;
import com.drow.backend.notebook.NotebookRepository;
import com.drow.backend.section.dto.CreateSectionRequest;
import com.drow.backend.section.dto.SectionResponse;
import com.drow.backend.section.dto.UpdateSectionRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class SectionService {
    private final SectionRepository sectionRepository;
    private final NotebookRepository notebookRepository;

    //Helper
    private Section getActiveSectionOrThrow(
            UUID notebookId,
            UUID sectionId
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

        return sectionRepository
                .findByIdAndNotebookId(
                        sectionId,
                        notebook.getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Section not found: " + sectionId
                        )
                );
    }

    private Section getSectionOrThrow(
            UUID notebookId,
            UUID sectionId
    ) {

        Notebook notebook = notebookRepository
                .findById(notebookId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Notebook not found: " + notebookId
                        )
                );

        return sectionRepository
                .findByIdAndNotebookId(
                        sectionId,
                        notebook.getId()
                )
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Section not found: " + sectionId
                        )
                );
    }

    public SectionResponse createSection(
            UUID notebookId,
            CreateSectionRequest request) {
        Notebook notebook = notebookRepository
                .findById(notebookId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Notebook not found: " + notebookId
                        )
                );
        if (Boolean.TRUE.equals(notebook.getIsDeleted()))
            throw new ResourceNotFoundException(
                    "Notebook is in Trash, please Restore the Notebook to create new Section");

        long position =
                sectionRepository
                        .countByNotebookIdAndIsDeletedFalse(
                                notebook.getId()
                        );

        LocalDateTime now = LocalDateTime.now();

        Section section = new Section();

        section.setId(UUID.randomUUID());

        section.setNotebookId(notebook.getId());

        section.setName(request.name());

        section.setPosition((int) position);

        section.setIsDeleted(false);

        section.setCreatedAt(now);

        section.setUpdatedAt(now);
        if (
                sectionRepository
                        .existsByNotebookIdAndNameAndIsDeletedFalse(
                                notebookId,
                                request.name()
                        )
        ) {
            throw new IllegalStateException(
                    "Section with this name already exists in the notebook: "+notebook.getName()
            );
        }

        Section savedSection =
                sectionRepository.save(section);

        return new SectionResponse(
                savedSection.getId(),
                savedSection.getName()
        );
    }

    public List<SectionResponse> getSections(UUID notebookId) {

        Notebook notebook = notebookRepository
                .findById(notebookId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Notebook not found: " + notebookId
                        )
                );

        if (Boolean.TRUE.equals(notebook.getIsDeleted())) {
            throw new ResourceNotFoundException(
                    "Notebook is in Trash"
            );
        }

        return sectionRepository.findByNotebookIdAndIsDeletedFalseOrderByPositionAsc(notebookId)
                .stream()
                .map(section -> new SectionResponse(
                        section.getId(),section.getName()
                )).toList();
    }

    public SectionResponse updateSection(
            UUID sectionId,
            UUID notebookId,
            UpdateSectionRequest request
    ){
        Section section= getActiveSectionOrThrow(notebookId,sectionId);
        if (Boolean.TRUE.equals(section.getIsDeleted())) {
            throw new IllegalStateException(
                    "Section is in Trash"
            );
        }

        String newName= request.name();
        if(section.getName().equals(newName))
            return new SectionResponse(
                    sectionId,newName
            );

        if(sectionRepository
                .existsByNotebookIdAndNameAndIsDeletedFalse(
                        section.getNotebookId(), newName
                ))
            throw new IllegalStateException(
                    "Section with this name already exists"
            );
        section.setName(newName);
        LocalDateTime now=LocalDateTime.now();
        section.setUpdatedAt(now);
        Section updatedSection=sectionRepository.save(section);

        return new SectionResponse(
                updatedSection.getId(),
                updatedSection.getName()
        );
    }

    public void moveSectionToTrash(UUID sectionId, UUID notebookId){
        Section section= getActiveSectionOrThrow(notebookId,sectionId);
        if (Boolean.TRUE.equals(section.getIsDeleted())) {
            throw new IllegalStateException(
                    "Section is already in Trash"
            );
        }
        section.setIsDeleted(true);
        section.setUpdatedAt(LocalDateTime.now());
        sectionRepository.save(section);
    }

    public List<SectionResponse> getTrashSections(
            UUID notebookId
    ) {

        Notebook notebook = notebookRepository
                .findById(notebookId)
                .orElseThrow(() ->
                        new ResourceNotFoundException(
                                "Notebook not found: " + notebookId
                        )
                );

        return sectionRepository
                .findByNotebookIdAndIsDeletedTrueOrderByPositionAsc(
                        notebook.getId()
                )
                .stream()
                .map(section ->
                        new SectionResponse(
                                section.getId(),
                                section.getName()
                        )
                )
                .toList();
    }

    public void restoreSection( UUID notebookId,UUID sectionId) {

        Section section= getSectionOrThrow(notebookId,sectionId);
        if(Boolean.FALSE.equals(section.getIsDeleted())){
            throw new IllegalStateException("Section is not in trash!");
        }

        if (
                sectionRepository
                        .existsByNotebookIdAndNameAndIsDeletedFalse(
                                section.getNotebookId(),
                                section.getName()
                        )
        ) {

            String restoredName =
                    section.getName() + " (Restored)";

            int counter = 1;

            while (
                    sectionRepository
                            .existsByNotebookIdAndNameAndIsDeletedFalse(
                                    section.getNotebookId(),
                                    restoredName
                            )
            ) {

                restoredName =
                        section.getName()
                                + " (Restored "
                                + counter++
                                + ")";
            }

            section.setName(restoredName);
        }

        section.setIsDeleted(false);

        section.setUpdatedAt(LocalDateTime.now());

        sectionRepository.save(section);
    }

    public void permanentlyDeleteSection(
            UUID notebookId,UUID sectionId
    ) {

        Section section= getSectionOrThrow(notebookId,sectionId);

        if (!section.getIsDeleted()) {

            throw new IllegalStateException(
                    "Section must be moved to trash before permanent deletion"
            );
        }

        sectionRepository.delete(section);
    }
}
