CREATE TABLE users
(
    id UUID PRIMARY KEY,

    clerk_id VARCHAR(255) NOT NULL UNIQUE,

    email VARCHAR(255) NOT NULL UNIQUE,

    display_name VARCHAR(255) NOT NULL,

    profile_picture_url TEXT,

    created_at TIMESTAMP NOT NULL,

    updated_at TIMESTAMP NOT NULL
);