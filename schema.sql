CREATE TYPE gender_type AS ENUM('MALE', 'FEMALE');

CREATE TYPE occupation_type AS ENUM('JUNIOR', 'SENIOR', 'SECRETARY', 'TREASURER', 'VICE_PRESIDENT', 'PRESIDENT');

CREATE TABLE member(
    id VARCHAR(255) PRIMARY KEY NOT NULL,
    first_name VARCHAR(255) NOT NULL,
    last_name VARCHAR(255),
    birth_date DATE NOT NULL,
    gender gender_type NOT NULL,
    address VARCHAR(255) NOT NULL,
    profession VARCHAR(255) NOT NULL,
    phone_number bigint NOT NULL,
    email VARCHAR(255) NOT NULL,
    member_occupation occupation_type NOT NULL,
    id_collectivity VARCHAR(255) REFERENCES collectivity(id),
    registration_fee_paid BOOLEAN NOT NULL,
    membership_fee_paid BOOLEAN NOT NULL
);

INSERT INTO member(id, first_name, last_name, birth_date,
                   gender, address, profession, phone_number,
                   email, member_occupation, id_collectivity, registration_fee_paid, membership_fee_paid)
    VALUES (gen_random_uuid(), 'Lee', 'Dain', '2005-01-29',
            'MALE', 'Lot IPA 54 Ter A Ambohidahy', 'agricultural engineer', '0383180458',
            'rora@gmail.com', '')
                                                                                                                                   ()

CREATE TABLE referral(
    id VARCHAR(255) PRIMARY KEY NOT NULL,
    id_referee VARCHAR(255) REFERENCES member(id),
    id_referred VARCHAR(255) REFERENCES member(id)
);

ALTER TABLE member
ALTER COLUMN phone_number TYPE BIGINT;

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