-- liquibase formatted sql

CREATE TABLE IF NOT EXISTS excel_file_entity (
    full_text_id VARCHAR(255) NOT NULL PRIMARY KEY,
    attribute_name VARCHAR(255),
    brick_name VARCHAR(255),
    definition_name VARCHAR(255),
    excel_file BLOB NOT NULL,file_name VARCHAR(255),version VARCHAR(255)
);

-- rollback DROP TABLE excel_file_entity;
