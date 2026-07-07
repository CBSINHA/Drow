CREATE TABLE pages
(
    id UUID PRIMARY KEY,

    section_id UUID NOT NULL,

    title VARCHAR(255) NOT NULL,

    position INTEGER NOT NULL,

    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_page_section
        FOREIGN KEY (section_id)
            REFERENCES sections(id)
);