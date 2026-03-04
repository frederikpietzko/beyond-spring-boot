-- Migration for Pet and Visit tables

CREATE SEQUENCE pet_seq INCREMENT BY 50;
CREATE SEQUENCE visit_seq INCREMENT BY 50;

-- Create pet table
CREATE TABLE pet
(
    id   BIGINT PRIMARY KEY DEFAULT nextval('pet_seq'),
    name VARCHAR(255) NOT NULL,
    age  INT          NOT NULL CHECK (age >= 1),
    type VARCHAR(50)  NOT NULL CHECK (type IN ('DOG', 'CAT', 'BIRD', 'FISH', 'REPTILE', 'RODENT', 'OTHER'))
);

-- Create visit table
CREATE TABLE visit
(
    id          BIGINT PRIMARY KEY DEFAULT nextval('visit_seq'),
    description VARCHAR(255)             NOT NULL,
    date_time   TIMESTAMP WITH TIME ZONE NOT NULL,
    pet_id      BIGINT                   NOT NULL,
    CONSTRAINT fk_visit_pet FOREIGN KEY (pet_id) REFERENCES pet (id) ON DELETE CASCADE
);

-- Create indexes for better query performance
CREATE INDEX idx_visit_pet_id ON visit (pet_id);
CREATE INDEX idx_visit_date_time ON visit (date_time);
