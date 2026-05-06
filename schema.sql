CREATE TYPE gender_type AS ENUM('MALE', 'FEMALE');

CREATE TYPE occupation_type AS ENUM('JUNIOR', 'SENIOR', 'SECRETARY', 'TREASURER', 'VICE_PRESIDENT', 'PRESIDENT');

CREATE TYPE activity_type  AS ENUM('MEETING', 'TRAINING', 'OTHER');

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

CREATE TYPE payment_mode AS ENUM('CASH','MOBILE_BANKING','BANK_TRANSFER');

CREATE TYPE activity_status AS ENUM('ACTIVE', 'INACTIVE');

CREATE TYPE frequency AS ENUM('WEEKLY', 'MONTHLY', 'ANNUALLY', 'PUNCTUALLY');

CREATE TABLE membership_fee(
    id VARCHAR(255) PRIMARY KEY,
    status activity_status NOT NULL,
    eligible_from DATE NOT NULL,
    frequency frequency NOT NULL,
    amount FLOAT NOT NULL,
    label VARCHAR(255) NOT NULL
);

CREATE TYPE account_type AS ENUM('CASH', 'BANK', 'MOBILE');

CREATE TYPE bank_name AS ENUM('BRED', 'MCB', 'BMOI', 'BOA', 'BGFI', 'AFG', 'ACCES_BANQUE', 'BAOBAB', 'SIPEM');

CREATE TYPE mobile_banking_service AS ENUM('ORANGE_MONEY', 'MVOLA', 'AIRTEL_MONEY');

CREATE TABLE financial_account(
    id VARCHAR(255) PRIMARY KEY,
    type account_type NOT NULL,
    amount FLOAT NOT NULL
);

create table referral (
    id VARCHAR(255) PRIMARY KEY NOT NULL ,
    id_referee VARCHAR(255) REFERENCES member(id),
    id_referred VARCHAR(255) REFERENCES member(id)
);

CREATE TABLE cash_account(
    id VARCHAR(255) PRIMARY KEY REFERENCES financial_account(id)
);

CREATE TABLE bank_account(
    id VARCHAR(255) PRIMARY KEY REFERENCES financial_account(id),
    holder_name VARCHAR(255) NOT NULL,
    bank_name bank_name NOT NULL,
    bank_code INT NOT NULL,
    bank_branch_code INT NOT NULL,
    bank_account_key INT NOT NULL
);

CREATE TABLE mobile_banking_account(
    id VARCHAR(255) PRIMARY KEY REFERENCES financial_account(id),
    holder_name VARCHAR(255) NOT NULL,
    mobile_banking_service mobile_banking_service NOT NULL,
    mobile_number VARCHAR(255)
);

CREATE TABLE memberPayment(
    id VARCHAR(255) PRIMARY KEY NOT NULL,
    amount FLOAT,
    payment_mode payment_mode NOT NULL,
    id_membership_fee VARCHAR(255) REFERENCES membership_fee(id),
    account_credited VARCHAR(255) REFERENCES financial_account(id),
    creation_date TIMESTAMP,
    id_member VARCHAR(255) REFERENCES member(id)
);

CREATE TABLE IF NOT EXISTS collectivity_transaction (
    id VARCHAR(150) PRIMARY KEY DEFAULT gen_random_uuid(),
    creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    amount DECIMAL(10,2) NOT NULL,
    payment_mode VARCHAR(50) NOT NULL,
    account_credited_id VARCHAR(150) NOT NULL,
    account_credited_type VARCHAR(150) NOT NULL,
    member_debited_id VARCHAR(150) NOT NULL references member(id)
);


alter table cash_account add column amount FLOAT not null default 0;


alter table mobile_banking_account add column amount FLOAT not null default 0;


INSERT INTO collectivity (id, location, specialty, federation_approval, approval_date, approved_by, created_at, updated_at, unique_number, unique_name) VALUES
('col-1', 'Ambatondrazaka', 'Riziculture', true, '2024-01-15', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '1', 'Mpanorina'),
('col-2', 'Ambatondrazaka', 'Pisciculture', true, '2024-02-20', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '2', 'Dobo voalahany'),
('col-3', 'Brickaville', 'Apiculture', true, '2024-03-10', NULL, CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '3', 'Tantely mamy');

INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email, member_occupation, id_collectivity, registration_fee_paid, membership_dues_paid) VALUES
('C1-M1', 'Nom membre 1', 'Prénom membre 1', '1980-02-01', 'MALE', 'Lot II V M Ambato', 'Riziculteur', 341234567, 'member.1@fed-agri.mg', 'PRESIDENT', 'col-1', true, true),
('C1-M2', 'Nom membre 2', 'Prénom membre 2', '1982-03-05', 'MALE', 'Lot II F Ambato', 'Agriculteur', 321234567, 'member.2@fed-agri.mg', 'VICE_PRESIDENT', 'col-1', true, true),
('C1-M3', 'Nom membre 3', 'Prénom membre 3', '1992-03-10', 'MALE', 'Lot II J Ambato', 'Collecteur', 331234567, 'member.3@fed-agri.mg', 'SECRETARY', 'col-1', true, true),
('C1-M4', 'Nom membre 4', 'Prénom membre 4', '1988-05-22', 'FEMALE', 'Lot A K 50 Ambato', 'Distributeur', 381234567, 'member.4@fed-agri.mg', 'TREASURER', 'col-1', true, true),
('C1-M5', 'Nom membre 5', 'Prénom membre 5', '1999-08-21', 'MALE', 'Lot UV 80 Ambato', 'Riziculteur', 373434567, 'member.5@fed-agri.mg', 'SENIOR', 'col-1', true, true),
('C1-M6', 'Nom membre 6', 'Prénom membre 6', '1998-08-22', 'FEMALE', 'Lot UV 6 Ambato', 'Riziculteur', 372234567, 'member.6@fed-agri.mg', 'SENIOR', 'col-1', true, true),
('C1-M7', 'Nom membre 7', 'Prénom membre 7', '1998-01-31', 'MALE', 'Lot UV 7 Ambato', 'Riziculteur', 374234567, 'member.7@fed-agri.mg', 'SENIOR', 'col-1', true, true),
('C1-M8', 'Nom membre 8', 'Prénom membre 8', '1975-08-20', 'MALE', 'Lot UV 8 Ambato', 'Riziculteur', 370234567, 'member.8@fed-agri.mg', 'SENIOR', 'col-1', true, true);


INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email, member_occupation, id_collectivity, registration_fee_paid, membership_dues_paid) VALUES
('C2-M1', 'Nom membre 1', 'Prénom membre 1', '1980-02-01', 'MALE', 'Lot II V M Ambato', 'Riziculteur', 341234567, 'member.1@fed-agri.mg', 'SENIOR', 'col-2', true, true),
('C2-M2', 'Nom membre 2', 'Prénom membre 2', '1982-03-05', 'MALE', 'Lot II F Ambato', 'Agriculteur', 321234567, 'member.2@fed-agri.mg', 'SENIOR', 'col-2', true, true),
('C2-M3', 'Nom membre 3', 'Prénom membre 3', '1992-03-10', 'MALE', 'Lot II J Ambato', 'Collecteur', 331234567, 'member.3@fed-agri.mg', 'SENIOR', 'col-2', true, true),
('C2-M4', 'Nom membre 4', 'Prénom membre 4', '1988-05-22', 'FEMALE', 'Lot A K 50 Ambato', 'Distributeur', 381234567, 'member.4@fed-agri.mg', 'SENIOR', 'col-2', true, true),
('C2-M5', 'Nom membre 5', 'Prénom membre 5', '1999-08-21', 'MALE', 'Lot UV 80 Ambato', 'Riziculteur', 373434567, 'member.5@fed-agri.mg', 'PRESIDENT', 'col-2', true, true),
('C2-M6', 'Nom membre 6', 'Prénom membre 6', '1998-08-22', 'FEMALE', 'Lot UV 6 Ambato', 'Riziculteur', 372234567, 'member.6@fed-agri.mg', 'VICE_PRESIDENT', 'col-2', true, true),
('C2-M7', 'Nom membre 7', 'Prénom membre 7', '1998-01-31', 'MALE', 'Lot UV 7 Ambato', 'Riziculteur', 374234567, 'member.7@fed-agri.mg', 'SECRETARY', 'col-2', true, true),
('C2-M8', 'Nom membre 8', 'Prénom membre 8', '1975-08-20', 'MALE', 'Lot UV 8 Ambato', 'Riziculteur', 370234567, 'member.8@fed-agri.mg', 'TREASURER', 'col-2', true, true);

INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email, member_occupation, id_collectivity, registration_fee_paid, membership_dues_paid) VALUES
('C3-M1', 'Nom membre 9', 'Prénom membre 9', '1988-01-02', 'MALE', 'Lot 33 J Antsirabe', 'Apiculteur', 34034567, 'member.9@fed-agri.mg', 'PRESIDENT', 'col-3', true, true),
('C3-M2', 'Nom membre 10', 'Prénom membre 10', '1982-03-05', 'MALE', 'Lot 2 J Antsirabe', 'Agriculteur', 338634567, 'member.10@fed-agri.mg', 'VICE_PRESIDENT', 'col-3', true, true),
('C3-M3', 'Nom membre 11', 'Prénom membre 11', '1992-03-12', 'MALE', 'Lot 8 KM Antsirabe', 'Collecteur', 338234567, 'member.11@fed-agri.mg', 'SECRETARY', 'col-3', true, true),
('C3-M4', 'Nom membre 12', 'Prénom membre 12', '1988-05-10', 'FEMALE', 'Lot A K 50 Antsirabe', 'Distributeur', 382334567, 'member.12@fed-agri.mg', 'TREASURER', 'col-3', true, true),
('C3-M5', 'Nom membre 13', 'Prénom membre 13', '1999-08-11', 'MALE', 'Lot UV 80 Antsirabe', 'Apiculteur', 373365567, 'member.13@fed-agri.mg', 'SENIOR', 'col-3', true, true),
('C3-M6', 'Nom membre 14', 'Prénom membre 14', '1998-08-09', 'FEMALE', 'Lot UV 6 Antsirabe', 'Apiculteur', 378234567, 'member.14@fed-agri.mg', 'SENIOR', 'col-3', true, true),
('C3-M7', 'Nom membre 15', 'Prénom membre 15', '1998-01-13', 'MALE', 'Lot UV 7 Antsirabe', 'Apiculteur', 374914567, 'member.15@fed-agri.mg', 'SENIOR', 'col-3', true, true),
('C3-M8', 'Nom membre 16', 'Prénom membre 16', '1975-08-02', 'MALE', 'Lot UV 8 Antsirabe', 'Apiculteur', 370634567, 'member.16@fed-agri.mg', 'SENIOR', 'col-3', true, true);


INSERT INTO collectivity_structure (collectivity_id, president_id, vice_president_id, treasurer_id, secretary_id) VALUES
('col-1', 'C1-M1', 'C1-M2', 'C1-M4', 'C1-M3'),
('col-2', 'C2-M5', 'C2-M6', 'C2-M8', 'C2-M7'),
('col-3', 'C3-M1', 'C3-M2', 'C3-M4', 'C3-M3');



INSERT INTO collectivity_members (collectivity_id, member_id, joined_at, is_active) VALUES
('col-3', 'C3-M1', '2024-01-01', true),
('col-3', 'C3-M2', '2024-01-01', true),
('col-3', 'C3-M3', '2024-01-01', true),
('col-3', 'C3-M4', '2024-01-01', true),
('col-3', 'C3-M5', '2024-01-01', true),
('col-3', 'C3-M6', '2024-01-01', true),
('col-3', 'C3-M7', '2024-01-01', true),
('col-3', 'C3-M8', '2024-01-01', true);



drop table agri_federation_public_collectivity