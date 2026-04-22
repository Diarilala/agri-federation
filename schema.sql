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
    creationDate TIMESTAMP,
    id_member VARCHAR(255) REFERENCES member(id)
);