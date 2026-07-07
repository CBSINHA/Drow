CREATE TABLE sections
(
    id UUID PRIMARY KEY,

    notebook_id UUID NOT NULL,

    name VARCHAR(255) NOT NULL,

    position INTEGER NOT NULL,

    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_section_notebook
        FOREIGN KEY (notebook_id)
            REFERENCES notebooks(id)
);