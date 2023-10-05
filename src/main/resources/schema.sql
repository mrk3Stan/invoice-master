CREATE SCHEMA IF NOT EXISTS invoice_master;

DROP TABLE IF EXISTS invoice_master.Users;

CREATE TABLE invoice_master.Users
(
    id              BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    first_name      VARCHAR(50) NOT NULL,
    last_name       VARCHAR(50) NOT NULL,
    email           VARCHAR(100) NOT NULL,
    password        VARCHAR(255) DEFAULT NULL,
    address         VARCHAR(255) DEFAULT NULL,
    phone           VARCHAR(30) DEFAULT NULL,
    title           VARCHAR(50) DEFAULT NULL,
    bio             VARCHAR(255) DEFAULT NULL,
    enabled         BOOLEAN DEFAULT FALSE,
    non_locked      BOOLEAN DEFAULT TRUE,
    using_mfa       BOOLEAN DEFAULT FALSE,
    image_url       VARCHAR(255) DEFAULT NULL,
    created_at      TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    CONSTRAINT UQ_Users_Email UNIQUE (email),
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS invoice_master.Roles;

CREATE TABLE invoice_master.Roles
(
    id          BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    name        VARCHAR(50) NOT NULL,
    permission  VARCHAR(255) NOT NULL, -- user:read,user:delete,customer:read
    CONSTRAINT UQ_Roles_Name UNIQUE (name),
    PRIMARY KEY (id)
);

INSERT INTO invoice_master.Roles (name, permission)
    VALUES  ('ROLE_USER', 'READ:USER,READ:CUSTOMER'),
            ('ROLE_MANAGER', 'READ:USER,READ:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER'),
            ('ROLE_ADMIN', 'READ:USER,READ:CUSTOMER,CREATE:USER,CREATE:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER'),
            ('ROLE_SYSADMIN', 'READ:USER,READ:CUSTOMER,CREATE:USER,CREATE:CUSTOMER,UPDATE:USER,UPDATE:CUSTOMER,DELETE:USER,DELETE:CUSTOMER');

DROP TABLE IF EXISTS invoice_master.UserRoles;

CREATE TABLE invoice_master.UserRoles
(
    id          BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    user_id     BIGINT NOT NULL,
    role_id     BIGINT NOT NULL,
    FOREIGN KEY (user_id) REFERENCES invoice_master.Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (role_id) REFERENCES invoice_master.Roles (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    CONSTRAINT UQ_UserRoles_User_Id UNIQUE (user_id),
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS invoice_master.Events;

CREATE TABLE invoice_master.Events
(
    id          BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    type        VARCHAR(50) NOT NULL CHECK(type IN ('LOGIN_ATTEMPT', 'LOGIN_ATTEMPT_FAILURE', 'LOGIN_ATTEMPT_SUCCESS', 'PROFILE_UPDATE', 'PROFILE_PICTURE_UPDATE', 'ROLE_UPDATE', 'ACCOUNT_SETTINGS_UPDATE', 'PASSWORD_UPDATE', 'MFA_UPDATE')),
    description VARCHAR(255) NOT NULL,
    CONSTRAINT UQ_Events_Type UNIQUE (type),
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS invoice_master.UserEvents;

CREATE TABLE invoice_master.UserEvents
(
    id          BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    user_id     BIGINT NOT NULL,
    event_id    BIGINT NOT NULL,
    device      VARCHAR(100) DEFAULT NULL,
    ip_address  VARCHAR(100) DEFAULT NULL,
    created_at  TIMESTAMP DEFAULT CURRENT_TIMESTAMP,
    FOREIGN KEY (user_id) REFERENCES invoice_master.Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    FOREIGN KEY (event_id) REFERENCES invoice_master.Events (id) ON DELETE RESTRICT ON UPDATE CASCADE,
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS invoice_master.AccountVerifications;

CREATE TABLE invoice_master.AccountVerifications
(
    id          BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    user_id     BIGINT NOT NULL,
    url         VARCHAR(255) NOT NULL,
    -- date  DATETIME NOT NULL,
    FOREIGN KEY (user_id) REFERENCES invoice_master.Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT UQ_AccountVerifications_User_Id UNIQUE (user_id),
    CONSTRAINT UQ_AccountVerifications_Url UNIQUE (url),
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS invoice_master.ResetPasswordVerifications;

CREATE TABLE invoice_master.ResetPasswordVerifications
(
    id              BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    user_id         BIGINT NOT NULL,
    url             VARCHAR(255) NOT NULL,
    expiration_date TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES invoice_master.Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT UQ_ResetPasswordVerifications_User_Id UNIQUE (user_id),
    CONSTRAINT UQ_ResetPasswordVerifications_Url UNIQUE (url),
    PRIMARY KEY (id)
);

DROP TABLE IF EXISTS invoice_master.TwoFactorVerifications;

CREATE TABLE invoice_master.TwoFactorVerifications
(
    id              BIGINT NOT NULL GENERATED ALWAYS AS IDENTITY,
    user_id         BIGINT NOT NULL,
    code            VARCHAR(10) NOT NULL,
    expiration_date TIMESTAMP NOT NULL,
    FOREIGN KEY (user_id) REFERENCES invoice_master.Users (id) ON DELETE CASCADE ON UPDATE CASCADE,
    CONSTRAINT UQ_TwoFactorVerifications_User_Id UNIQUE (user_id),
    CONSTRAINT UQ_TwoFactorVerifications_Code UNIQUE (code),
    PRIMARY KEY (id)
);