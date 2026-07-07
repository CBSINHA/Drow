package com.drow.backend.section;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SectionRepository extends JpaRepository<Section, UUID> {
}
