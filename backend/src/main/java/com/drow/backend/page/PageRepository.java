package com.drow.backend.page;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface PageRepository extends JpaRepository<Page, UUID> {

    boolean existsBySectionIdAndTitleAndIsDeletedFalse(
            UUID SectionId,
            String title
    );

}
