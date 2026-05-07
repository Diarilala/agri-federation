CREATE TYPE gender_type AS ENUM('MALE', 'FEMALE');

CREATE TYPE occupation_type AS ENUM('JUNIOR', 'SENIOR', 'SECRETARY', 'TREASURER', 'VICE_PRESIDENT', 'PRESIDENT');

CREATE TYPE activity_type  AS ENUM('MEETING', 'TRAINING', 'OTHER');

CREATE TYPE day_of_week AS ENUM('MO', 'TU', 'WE', 'TH', 'FR', 'SA', 'SU');

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






drop table agri_federation_public_collectivity;

CREATE TYPE activity_type AS ENUM ('MEETING', 'TRAINING', 'OTHER');

CREATE TYPE attendance_status AS ENUM ('MISSING', 'ATTENDED', 'UNDEFINED');

CREATE TABLE collectivity_activity (
    id VARCHAR(255) PRIMARY KEY ,
    label VARCHAR(255),
    activity_type activity_type,
    executive_date DATE,
    id_collectivity VARCHAR(255) REFERENCES collectivity(id)
);

ALTER TABLE collectivity_activity
      ADD COLUMN id_monthly_recurrence VARCHAR(255) REFERENCES monthly_recurrence_rule(id);

CREATE TYPE days AS ENUM ('MO', 'TU', 'WE', 'TH', 'FR', 'SA', 'SU');

CREATE TABLE monthly_recurrence_rule (
    id VARCHAR(255) PRIMARY KEY,
    week_ordinal int,
    day_of_week VARCHAR(2)
);



ALTER TABLE monthly_recurrence_rule
    ADD CONSTRAINT week_ordinal_check CHECK (week_ordinal BETWEEN 1 AND 5);

CREATE TABLE activity_occupation (
    id_activity VARCHAR(255) REFERENCES collectivity_activity(id),
    occupation occupation_type,
    PRIMARY KEY (id_activity, occupation)
);

CREATE TABLE activity_attendance (
    id_member VARCHAR(255) REFERENCES member(id),
    id_activity VARCHAR(255) REFERENCES collectivity_activity(id),
    status attendance_status NOT NULL DEFAULT 'UNDEFINED',
    PRIMARY KEY (id_member, id_activity)
);

DELETE FROM collectivity_transaction;
DELETE FROM memberpayment;
DELETE FROM membership_fee;
DELETE FROM bank_account;
DELETE FROM mobile_banking_account;
DELETE FROM cash_account;
DELETE FROM financial_account;
DELETE FROM collectivity_members;
DELETE FROM collectivity_structure;
DELETE FROM referral;
DELETE FROM member;
DELETE FROM collectivity;

INSERT INTO collectivity (id, location, specialty, federation_approval, approval_date, created_at, updated_at, unique_number, unique_name) VALUES
('col-1', 'Ambatondrazaka', 'Riziculture', true, '2024-01-15', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '1', 'Mpanorina'),
('col-2', 'Ambatondrazaka', 'Pisciculture', true, '2024-02-20', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '2', 'Dobo voalahany'),
('col-3', 'Brickaville', 'Apiculture', true, '2024-03-10', CURRENT_TIMESTAMP, CURRENT_TIMESTAMP, '3', 'Tantely mamy');



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


INSERT INTO financial_account (id, type, amount) VALUES
('C1-A-CASH', 'CASH', 0),
('C1-A-MOBILE-1', 'MOBILE', 0),
('C2-A-CASH', 'CASH', 0),
('C2-A-MOBILE-1', 'MOBILE', 0),
('C3-A-CASH', 'CASH', 0);

drop table cash_account;

CREATE TABLE cash_account(
    id VARCHAR(255) PRIMARY KEY REFERENCES financial_account(id),
    collectivity_id VARCHAR(255) REFERENCES collectivity(id),
    amount FLOAT NOT NULL DEFAULT 0
);


INSERT INTO cash_account (id, collectivity_id, amount) VALUES
('C1-A-CASH', 'col-1', 0),
('C2-A-CASH', 'col-2', 0),
('C3-A-CASH', 'col-3', 0);

alter table mobile_banking_account add column collectivity_id VARCHAR(255) REFERENCES collectivity(id);


INSERT INTO mobile_banking_account (id, holder_name, mobile_banking_service, mobile_number, collectivity_id, amount) VALUES
('C1-A-MOBILE-1', 'Mpanorina', 'ORANGE_MONEY', '0370489612', 'col-1', 0),
('C2-A-MOBILE-1', 'Dobo voalohany', 'ORANGE_MONEY', '0320489612', 'col-2', 0);


-- new data
INSERT INTO financial_account (id, type, amount) VALUES
    ('C3-A-BANK-1', 'BANK', 0),
    ('C3-A-BANK-2', 'BANK', 0),
    ('C3-A-MOBILE-1', 'MOBILE', 0);

DROP TABLE bank_account;

CREATE TABLE bank_account(
    collectivity_id VARCHAR(255) REFERENCES collectivity(id),
    id VARCHAR(255) PRIMARY KEY REFERENCES financial_account(id),
    holder_name VARCHAR(255) NOT NULL,
    bank_name bank_name NOT NULL,
    bank_code INT NOT NULL,
    bank_branch_code INT NOT NULL,
    bank_account_key INT NOT NULL
);


INSERT INTO bank_account(collectivity_id, id, holder_name, bank_name, bank_code, bank_branch_code, bank_account_key) VALUES
    ('col-3', 'C3-A-BANK-1', 'Koto', 'BMOI', 00004, 00001, 12),
    ('col-3', 'C3-A-BANK-2', 'Naivo', 'BRED',00008, 00003, 58);

INSERT INTO mobile_banking_account(id, holder_name, mobile_banking_service, mobile_number, collectivity_id, amount) VALUES
    ('C3-A-MOBILE-1', 'Kolo', 'MVOLA', '0341889612', 'col-3', 0);

INSERT INTO membership_fee(id, status, eligible_from, frequency, amount, label, collectivity_id) VALUES
    ('cot-1', 'ACTIVE', '2026-01-01', 'ANNUALLY', 200000, 'Cotisation annuelle', 'col-1'),
    ('cot-2', 'ACTIVE', '2026-04-30', 'PUNCTUALLY', 20000, 'Famangiana', 'col-1'),
    ('cot-3', 'ACTIVE', '2026-01-01', 'ANNUALLY', 200000, 'Cotisation annuelle', 'col-2'),
    ('cot-4', 'INACTIVE', '2025-01-01', 'ANNUALLY', 100000, 'Cotisation 2025', 'col-2'),
    ('cot-5', 'ACTIVE', '2026-04-01', 'MONTHLY', 25000, 'Cotisation mensuelle', 'col-3');



INSERT INTO collectivity_transaction (id, creation_date, amount, payment_mode, account_credited_id, account_credited_type, member_debited_id, collectivity_id) VALUES
    (gen_random_uuid(), '2026-01-01', 200000, 'CASH', 'C1-A-CASH', 'CASH', 'C1-M1', 'col-1'),
(gen_random_uuid(), '2026-01-01', 200000, 'CASH', 'C2-A-CASH', 'CASH', 'C2-M1', 'col-2'),
(gen_random_uuid(), '2026-04-01', 25000, 'CASH', 'C3-A-CASH', 'CASH', 'C3-M1', 'col-3');





INSERT INTO memberpayment (id, amount, payment_mode, id_membership_fee, account_credited, creation_date, id_member) VALUES
(gen_random_uuid(), 200000, 'CASH', NULL, 'C1-A-CASH', '2026-01-01', 'C1-M1'),
(gen_random_uuid(), 200000, 'CASH', NULL, 'C1-A-CASH', '2026-01-01', 'C1-M2'),
(gen_random_uuid(), 200000, 'MOBILE_BANKING', NULL, 'C1-A-MOBILE-1', '2026-01-01', 'C1-M3'),
(gen_random_uuid(), 200000, 'MOBILE_BANKING', NULL, 'C1-A-MOBILE-1', '2026-01-01', 'C1-M4'),
(gen_random_uuid(), 150000, 'MOBILE_BANKING', NULL, 'C1-A-MOBILE-1', '2026-01-01', 'C1-M5'),
(gen_random_uuid(), 100000, 'CASH', NULL, 'C1-A-CASH', '2026-05-01', 'C1-M6'),
(gen_random_uuid(), 60000, 'CASH', NULL, 'C1-A-CASH', '2026-05-01', 'C1-M7'),
(gen_random_uuid(), 90000, 'CASH', NULL, 'C1-A-CASH', '2026-05-01', 'C1-M8'),
(gen_random_uuid(), 120000, 'CASH', NULL, 'C2-A-CASH', '2026-01-01', 'C2-M1'),
(gen_random_uuid(), 180000, 'CASH', NULL, 'C2-A-CASH', '2026-01-01', 'C2-M2'),
(gen_random_uuid(), 200000, 'CASH', NULL, 'C2-A-CASH', '2026-01-01', 'C2-M3'),
(gen_random_uuid(), 200000, 'CASH', NULL, 'C2-A-CASH', '2026-01-01', 'C2-M4'),
(gen_random_uuid(), 200000, 'CASH', NULL, 'C2-A-CASH', '2026-01-01', 'C2-M5'),
(gen_random_uuid(), 200000, 'CASH', NULL, 'C2-A-CASH', '2026-01-01', 'C2-M6'),
(gen_random_uuid(), 80000, 'MOBILE_BANKING', NULL, 'C2-A-MOBILE-1', '2026-01-01', 'C2-M7'),
(gen_random_uuid(), 120000, 'MOBILE_BANKING', NULL, 'C2-A-MOBILE-1', '2026-01-01', 'C2-M8'),
(gen_random_uuid(), 25000, 'BANK_TRANSFER', NULL, 'C3-A-BANK-1', '2026-04-01', 'C3-M1'),
(gen_random_uuid(), 25000, 'BANK_TRANSFER', NULL, 'C3-A-BANK-1', '2026-04-01', 'C3-M2'),
(gen_random_uuid(), 25000, 'BANK_TRANSFER', NULL, 'C3-A-BANK-1', '2026-04-01', 'C3-M3'),
(gen_random_uuid(), 25000, 'BANK_TRANSFER', NULL, 'C3-A-BANK-1', '2026-04-01', 'C3-M4'),
(gen_random_uuid(), 25000, 'BANK_TRANSFER', NULL, 'C3-A-BANK-2', '2026-04-01', 'C3-M5'),
(gen_random_uuid(), 25000, 'BANK_TRANSFER', NULL, 'C3-A-BANK-2', '2026-04-01', 'C3-M6'),
(gen_random_uuid(), 25000, 'CASH', NULL, 'C3-A-CASH', '2026-04-01', 'C3-M7'),
(gen_random_uuid(), 25000, 'CASH', NULL, 'C3-A-CASH', '2026-04-01', 'C3-M8'),
(gen_random_uuid(), 25000, 'BANK_TRANSFER', NULL, 'C3-A-BANK-1', '2026-05-01', 'C3-M1'),
(gen_random_uuid(), 25000, 'BANK_TRANSFER', NULL, 'C3-A-BANK-1', '2026-05-01', 'C3-M2'),
(gen_random_uuid(), 15000, 'MOBILE_BANKING', NULL, 'C3-A-MOBILE-1', '2026-05-01', 'C3-M3'),
(gen_random_uuid(), 15000, 'MOBILE_BANKING', NULL, 'C3-A-MOBILE-1', '2026-05-01', 'C3-M4'),
(gen_random_uuid(), 20000, 'BANK_TRANSFER', NULL, 'C3-A-BANK-2', '2026-05-01', 'C3-M5'),
(gen_random_uuid(), 25000, 'BANK_TRANSFER', NULL, 'C3-A-BANK-2', '2026-05-01', 'C3-M6'),
(gen_random_uuid(), 5000, 'CASH', NULL, 'C3-A-CASH', '2026-05-01', 'C3-M7'),
(gen_random_uuid(), 5000, 'CASH', NULL, 'C3-A-CASH', '2026-05-01', 'C3-M8');


INSERT INTO member (id, first_name, last_name, birth_date, gender, address, profession, phone_number, email, member_occupation, id_collectivity, registration_fee_paid, membership_dues_paid) VALUES
(gen_random_uuid(), 'Jean', 'Rakoto', '1990-01-15', 'MALE', 'Lot 123 Ambatondrazaka', 'Agriculteur', 341234500, 'jean.rakoto@email.com', 'JUNIOR', 'col-1', true, false),
(gen_random_uuid(), 'Marie', 'Razafy', '1991-02-20', 'FEMALE', 'Lot 456 Ambatondrazaka', 'Commerçante', 341234501, 'marie.razafy@email.com', 'JUNIOR', 'col-1', true, false),
(gen_random_uuid(), 'Paul', 'Andriamanana', '1992-03-10', 'MALE', 'Lot 789 Ambatondrazaka', 'Étudiant', 341234502, 'paul.andriamanana@email.com', 'JUNIOR', 'col-1', true, false),
(gen_random_uuid(), 'Sophie', 'Ralainasolo', '1993-04-05', 'FEMALE', 'Lot 101 Ambatondrazaka', 'Coiffeuse', 341234503, 'sophie.ralainasolo@email.com', 'JUNIOR', 'col-1', true, false),
(gen_random_uuid(), 'Lala', 'Rakotomalala', '1990-01-15', 'FEMALE', 'Lot 202 Mahajanga', 'Enseignante', 341234504, 'lala.rakotomalala@email.com', 'JUNIOR', 'col-2', true, false),
(gen_random_uuid(), 'Hery', 'Randrianasolo', '1991-02-20', 'MALE', 'Lot 303 Mahajanga', 'Mécanicien', 341234505, 'hery.randrianasolo@email.com', 'JUNIOR', 'col-2', true, false),
(gen_random_uuid(), 'Mamy', 'Ravelonirina', '1992-03-10', 'MALE', 'Lot 404 Mahajanga', 'Pêcheur', 341234506, 'mamy.ravelonirina@email.com', 'JUNIOR', 'col-2', true, false),
(gen_random_uuid(), 'Voahangy', 'Rasoanirina', '1990-01-15', 'FEMALE', 'Lot 505 Brickaville', 'Cultivatrice', 341234507, 'voahangy.rasoanirina@email.com', 'JUNIOR', 'col-3', true, false),
(gen_random_uuid(), 'Tojo', 'Rakotovao', '1991-02-20', 'MALE', 'Lot 606 Brickaville', 'Chauffeur', 341234508, 'tojo.rakotovao@email.com', 'JUNIOR', 'col-3', true, false),
(gen_random_uuid(), 'Nirina', 'Razanadrasoa', '1992-03-10', 'FEMALE', 'Lot 707 Brickaville', 'Vendeuse', 341234509, 'nirina.razanadrasoa@email.com', 'JUNIOR', 'col-3', true, false),
(gen_random_uuid(), 'Faniry', 'Ratsimandresy', '1993-04-05', 'MALE', 'Lot 808 Brickaville', 'Apiculteur', 341234510, 'faniry.ratsimandresy@email.com', 'JUNIOR', 'col-3', true, false),
(gen_random_uuid(), 'Hanta', 'Ranaivoarisoa', '1994-05-01', 'FEMALE', 'Lot 909 Brickaville', 'Artisane', 341234511, 'hanta.ranaivoarisoa@email.com', 'JUNIOR', 'col-3', true, false),
(gen_random_uuid(), 'Tiana', 'Razafindramboa', '1995-06-15', 'FEMALE', 'Lot 100 Brickaville', 'Étudiante', 341234512, 'tiana.razafindramboa@email.com', 'JUNIOR', 'col-3', true, false);

INSERT INTO collectivity_members (collectivity_id, member_id, joined_at, is_active)
VALUES ('col-1', (SELECT id FROM member WHERE email = 'jean.rakoto@email.com'), '2026-04-01', true),
       ('col-1', (SELECT id FROM member WHERE email = 'marie.razafy@email.com'), '2026-04-01', true),
       ('col-1', (SELECT id FROM member WHERE email = 'paul.andriamanana@email.com'), '2026-05-01', true),
       ('col-1', (SELECT id FROM member WHERE email = 'sophie.ralainasolo@email.com'), '2026-06-01', true),
       ('col-2', (SELECT id FROM member WHERE email = 'lala.rakotomalala@email.com'), '2026-03-01', true),
       ('col-2', (SELECT id FROM member WHERE email = 'hery.randrianasolo@email.com'), '2026-03-01', true),
       ('col-2', (SELECT id FROM member WHERE email = 'mamy.ravelonirina@email.com'), '2026-03-01', true),
       ('col-3', (SELECT id FROM member WHERE email = 'voahangy.rasoanirina@email.com'), '2026-01-01', true),
       ('col-3', (SELECT id FROM member WHERE email = 'tojo.rakotovao@email.com'), '2026-02-01', true),
       ('col-3', (SELECT id FROM member WHERE email = 'nirina.razanadrasoa@email.com'), '2026-02-01', true),
       ('col-3', (SELECT id FROM member WHERE email = 'faniry.ratsimandresy@email.com'), '2026-03-01', true),
       ('col-3', (SELECT id FROM member WHERE email = 'hanta.ranaivoarisoa@email.com'), '2026-03-01', true),
       ('col-3', (SELECT id FROM member WHERE email = 'tiana.razafindramboa@email.com'), '2026-03-01', true);


INSERT INTO referral (id, id_referee, id_referred)
VALUES (gen_random_uuid(), 'C1-M1', (SELECT id FROM member WHERE email = 'jean.rakoto@email.com')),
       (gen_random_uuid(), 'C1-M2', (SELECT id FROM member WHERE email = 'jean.rakoto@email.com')),
       (gen_random_uuid(), 'C1-M1', (SELECT id FROM member WHERE email = 'marie.razafy@email.com')),
       (gen_random_uuid(), 'C1-M2', (SELECT id FROM member WHERE email = 'marie.razafy@email.com')),
       (gen_random_uuid(), 'C1-M1', (SELECT id FROM member WHERE email = 'paul.andriamanana@email.com')),
       (gen_random_uuid(), 'C1-M2', (SELECT id FROM member WHERE email = 'paul.andriamanana@email.com')),
       (gen_random_uuid(), 'C1-M1', (SELECT id FROM member WHERE email = 'sophie.ralainasolo@email.com')),
       (gen_random_uuid(), 'C1-M2', (SELECT id FROM member WHERE email = 'sophie.ralainasolo@email.com')),
       (gen_random_uuid(), 'C1-M1', (SELECT id FROM member WHERE email = 'lala.rakotomalala@email.com')),
       (gen_random_uuid(), 'C1-M2', (SELECT id FROM member WHERE email = 'lala.rakotomalala@email.com')),
       (gen_random_uuid(), 'C1-M1', (SELECT id FROM member WHERE email = 'hery.randrianasolo@email.com')),
       (gen_random_uuid(), 'C1-M2', (SELECT id FROM member WHERE email = 'hery.randrianasolo@email.com')),
       (gen_random_uuid(), 'C1-M1', (SELECT id FROM member WHERE email = 'mamy.ravelonirina@email.com')),
       (gen_random_uuid(), 'C1-M2', (SELECT id FROM member WHERE email = 'mamy.ravelonirina@email.com')),
       (gen_random_uuid(), 'C3-M1', (SELECT id FROM member WHERE email = 'voahangy.rasoanirina@email.com')),
       (gen_random_uuid(), 'C3-M2', (SELECT id FROM member WHERE email = 'voahangy.rasoanirina@email.com')),
       (gen_random_uuid(), 'C3-M1', (SELECT id FROM member WHERE email = 'tojo.rakotovao@email.com')),
       (gen_random_uuid(), 'C3-M2', (SELECT id FROM member WHERE email = 'tojo.rakotovao@email.com')),
       (gen_random_uuid(), 'C3-M1', (SELECT id FROM member WHERE email = 'nirina.razanadrasoa@email.com')),
       (gen_random_uuid(), 'C3-M2', (SELECT id FROM member WHERE email = 'nirina.razanadrasoa@email.com')),
       (gen_random_uuid(), 'C3-M1', (SELECT id FROM member WHERE email = 'faniry.ratsimandresy@email.com')),
       (gen_random_uuid(), 'C3-M2', (SELECT id FROM member WHERE email = 'faniry.ratsimandresy@email.com')),
       (gen_random_uuid(), 'C3-M1', (SELECT id FROM member WHERE email = 'hanta.ranaivoarisoa@email.com')),
       (gen_random_uuid(), 'C3-M2', (SELECT id FROM member WHERE email = 'hanta.ranaivoarisoa@email.com')),
       (gen_random_uuid(), 'C3-M1', (SELECT id FROM member WHERE email = 'tiana.razafindramboa@email.com')),
       (gen_random_uuid(), 'C3-M2', (SELECT id FROM member WHERE email = 'tiana.razafindramboa@email.com'));