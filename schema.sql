CREATE TYPE gender_type AS ENUM('MALE', 'FEMALE');

CREATE TYPE occupation_type AS ENUM('JUNIOR', 'SENIOR', 'SECRETARY', 'TREASURER', 'VICE_PRESIDENT', 'PRESIDENT');

drop table member;
CREATE TABLE member(
    id VARCHAR(255) PRIMARY KEY NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),
    birth_date DATE NOT NULL,
    gender gender_type NOT NULL,
    address VARCHAR(255) NOT NULL,
    profession VARCHAR(255) NOT NULL,
    phone_number int NOT NULL,
    email VARCHAR(255) NOT NULL,
    member_occupation occupation_type NOT NULL
);

CREATE TABLE referral(
    id VARCHAR(255) PRIMARY KEY NOT NULL,
    id_referee VARCHAR(255) REFERENCES member(id),
    id_referred VARCHAR(255) REFERENCES member(id)
);

CREATE TABLE collectivity(
    id VARCHAR(255) PRIMARY KEY NOT NULL,
    name VARCHAR(255),
    location VARCHAR(255) NOT NULL,
    president VARCHAR(255) NOT NULL,
    vice_president VARCHAR(255) NOT NULL,
    treasurer VARCHAR(255) NOT NULL,
    secretary VARCHAR(255) NOT NULL,
    federation_approved BOOLEAN NOT NULL
);

ALTER TABLE member
ADD COLUMN id_collectivity VARCHAR(255) REFERENCES collectivity(id);

ALTER TABLE member
ADD COLUMN registration_fee_paid BOOLEAN NOT NULL;

ALTER TABLE member
ADD COLUMN membership_dues_paid BOOLEAN NOT NULL;