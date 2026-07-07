CREATE TABLE notebooks
(
    id UUID PRIMARY KEY,

    owner_id UUID NOT NULL,

    name VARCHAR(255) NOT NULL,

    position INTEGER NOT NULL,

    is_favorite BOOLEAN NOT NULL DEFAULT FALSE,

    is_deleted BOOLEAN NOT NULL DEFAULT FALSE,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL,

    CONSTRAINT fk_notebook_owner
        FOREIGN KEY (owner_id)
            REFERENCES users(id)
);