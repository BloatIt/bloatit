-- *********************************************************************
-- Update Database Script
-- *********************************************************************
-- Change Log: /home/thomas/bloatit/main/src/main/resources/liquibase/current.liquibase.xml
-- Ran at: 23/03/11 16:55
-- Against: bloatit@jdbc:postgresql://localhost/bloatit
-- Liquibase version: 2.0.2-SNAPSHOT
-- *********************************************************************

-- Create Database Lock Table
CREATE TABLE databasechangeloglock (ID INT NOT NULL, LOCKED BOOLEAN NOT NULL, LOCKGRANTED TIMESTAMP WITH TIME ZONE, LOCKEDBY VARCHAR(255), CONSTRAINT PK_DATABASECHANGELOGLOCK PRIMARY KEY (ID));

INSERT INTO databasechangeloglock (ID, LOCKED) VALUES (1, FALSE);

-- Lock Database
-- Create Database Change Log Table
CREATE TABLE databasechangelog (ID VARCHAR(63) NOT NULL, AUTHOR VARCHAR(63) NOT NULL, FILENAME VARCHAR(200) NOT NULL, DATEEXECUTED TIMESTAMP WITH TIME ZONE NOT NULL, ORDEREXECUTED INT NOT NULL, EXECTYPE VARCHAR(10) NOT NULL, MD5SUM VARCHAR(35), DESCRIPTION VARCHAR(255), COMMENTS VARCHAR(255), TAG VARCHAR(255), LIQUIBASE VARCHAR(20), CONSTRAINT PK_DATABASECHANGELOG PRIMARY KEY (ID, AUTHOR, FILENAME));

-- Changeset liquibase/common.liquibase.xml::hibernate-utils-0::Thomas Guyard::(Checksum: 3:a096db9d56f4a2db11f6d302560b1028)
CREATE SEQUENCE hibernate_sequence START WITH 0 INCREMENT BY 1 MINVALUE 0;

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Custom SQL', 'EXECUTED', 'liquibase/common.liquibase.xml', 'hibernate-utils-0', '2.0.2-SNP', '3:a096db9d56f4a2db11f6d302560b1028', 1);

-- Changeset liquibase/common.liquibase.xml::1300878547256-1::Thomas Guyard::(Checksum: 3:b26db4b21192ea689277f925395449de)
CREATE TABLE daoaccount (id int4 NOT NULL, amount NUMERIC(19,2) NOT NULL, creationdate TIMESTAMP WITH TIME ZONE NOT NULL, lastmodificationdate TIMESTAMP WITH TIME ZONE NOT NULL, actor_id int4, CONSTRAINT daoaccountpk PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Table', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-1', '2.0.2-SNP', '3:b26db4b21192ea689277f925395449de', 2);

-- Changeset liquibase/common.liquibase.xml::1300878547256-2::Thomas Guyard::(Checksum: 3:a5c2fddbd8b01acd87a3878fee4fe4ee)
CREATE TABLE daoactor (id int4 NOT NULL, datecreation TIMESTAMP WITH TIME ZONE NOT NULL, login VARCHAR(255) NOT NULL, externalaccount_id int4 NOT NULL, internalaccount_id int4 NOT NULL, CONSTRAINT daoactorpk PRIMARY KEY (id), UNIQUE (login));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Table', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-2', '2.0.2-SNP', '3:a5c2fddbd8b01acd87a3878fee4fe4ee', 3);

-- Changeset liquibase/common.liquibase.xml::1300878547256-3::Thomas Guyard::(Checksum: 3:b709f7e85fe04c729647d23eccfec499)
CREATE TABLE daobanktransaction (id int4 NOT NULL, creationdate TIMESTAMP WITH TIME ZONE NOT NULL, message TEXT NOT NULL, modificationdate TIMESTAMP WITH TIME ZONE NOT NULL, processinformations VARCHAR(64), reference VARCHAR(255) NOT NULL, state int4 NOT NULL, token VARCHAR(64) NOT NULL, value NUMERIC(19,2) NOT NULL, author_id int4 NOT NULL, CONSTRAINT daobanktransapk PRIMARY KEY (id), UNIQUE (token), UNIQUE (reference));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Table', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-3', '2.0.2-SNP', '3:b709f7e85fe04c729647d23eccfec499', 4);

-- Changeset liquibase/common.liquibase.xml::1300878547256-4::Thomas Guyard::(Checksum: 3:81be9f7fb202d55bf5c6cd6f7d0da5ce)
CREATE TABLE daobug (description VARCHAR(255) NOT NULL, level int4 NOT NULL, locale VARCHAR(255) NOT NULL, state int4 NOT NULL, title VARCHAR(255) NOT NULL, id int4 NOT NULL, milestone_id int4 NOT NULL, CONSTRAINT daobugpk PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Table', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-4', '2.0.2-SNP', '3:81be9f7fb202d55bf5c6cd6f7d0da5ce', 5);

-- Changeset liquibase/common.liquibase.xml::1300878547256-5::Thomas Guyard::(Checksum: 3:7a676a158364526a059e684501aa68e6)
CREATE TABLE daocomment (text TEXT NOT NULL, id int4 NOT NULL, bug_id int4, father_id int4, feature_id int4, release_id int4, CONSTRAINT daocommentpk PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Table', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-5', '2.0.2-SNP', '3:7a676a158364526a059e684501aa68e6', 6);

-- Changeset liquibase/common.liquibase.xml::1300878547256-6::Thomas Guyard::(Checksum: 3:b1dab725c5ed8c449406d078da62db9a)
CREATE TABLE daocontribution (alreadygivenmoney NUMERIC(19,2) NOT NULL, amount NUMERIC(19,2) NOT NULL, comment VARCHAR(144), percentdone int4 NOT NULL, state int4 NOT NULL, id int4 NOT NULL, feature_id int4 NOT NULL, CONSTRAINT daocontributipk PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Table', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-6', '2.0.2-SNP', '3:b1dab725c5ed8c449406d078da62db9a', 7);

-- Changeset liquibase/common.liquibase.xml::1300878547256-7::Thomas Guyard::(Checksum: 3:0017c8b2a10271481e4b56e68c2bfcea)
CREATE TABLE daocontribution_daotransaction (daocontribution_id int4 NOT NULL, transaction_id int4 NOT NULL);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Table', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-7', '2.0.2-SNP', '3:0017c8b2a10271481e4b56e68c2bfcea', 8);

-- Changeset liquibase/common.liquibase.xml::1300878547256-8::Thomas Guyard::(Checksum: 3:530fde0c61eb5d90d896ccc1b5e99f4e)
CREATE TABLE daodescription (id int4 NOT NULL, defaultlocale VARCHAR(255), CONSTRAINT daodescriptiopk PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Table', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-8', '2.0.2-SNP', '3:530fde0c61eb5d90d896ccc1b5e99f4e', 9);

-- Changeset liquibase/common.liquibase.xml::1300878547256-9::Thomas Guyard::(Checksum: 3:6e3f7586cd6cbed40459d5654bc80b27)
CREATE TABLE daoexternalaccount (bankcode VARCHAR(255) NOT NULL, type int4 NOT NULL, id int4 NOT NULL, CONSTRAINT daoexternalacpk PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Table', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-9', '2.0.2-SNP', '3:6e3f7586cd6cbed40459d5654bc80b27', 10);

-- Changeset liquibase/common.liquibase.xml::1300878547256-10::Thomas Guyard::(Checksum: 3:0fea529a642916e932b56450465c26a2)
CREATE TABLE daofeature (contribution NUMERIC(19,2) NOT NULL, featurestate int4 NOT NULL, validationdate TIMESTAMP WITH TIME ZONE, id int4 NOT NULL, description_id int4 NOT NULL, selectedoffer_id int4, software_id int4, CONSTRAINT daofeaturepk PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Table', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-10', '2.0.2-SNP', '3:0fea529a642916e932b56450465c26a2', 11);

-- Changeset liquibase/common.liquibase.xml::1300878547256-11::Thomas Guyard::(Checksum: 3:84f34fdddc1f7454d2a3542b6ebaf9d9)
CREATE TABLE daofeature_daocontribution (daofeature_id int4 NOT NULL, contributions_id int4 NOT NULL);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Table', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-11', '2.0.2-SNP', '3:84f34fdddc1f7454d2a3542b6ebaf9d9', 12);

-- Changeset liquibase/common.liquibase.xml::1300878547256-12::Thomas Guyard::(Checksum: 3:15f0ef18a02f6f3408a5f2477f98d1af)
CREATE TABLE daofilemetadata (filename VARCHAR(255) NOT NULL, shortdescription TEXT, size int4 NOT NULL, type int4 NOT NULL, url VARCHAR(255) NOT NULL, id int4 NOT NULL, relatedcontent_id int4, CONSTRAINT daofilemetadapk PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Table', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-12', '2.0.2-SNP', '3:15f0ef18a02f6f3408a5f2477f98d1af', 13);

-- Changeset liquibase/common.liquibase.xml::1300878547256-13::Thomas Guyard::(Checksum: 3:ac044067d7e0c898a7810152e5471e48)
CREATE TABLE daohighlightfeature (id int4 NOT NULL, activationdate TIMESTAMP WITH TIME ZONE NOT NULL, desactivationdate TIMESTAMP WITH TIME ZONE NOT NULL, position int4 NOT NULL, reason VARCHAR(255), feature_id int4 NOT NULL, CONSTRAINT daohighlightfpk PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Table', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-13', '2.0.2-SNP', '3:ac044067d7e0c898a7810152e5471e48', 14);

-- Changeset liquibase/common.liquibase.xml::1300878547256-14::Thomas Guyard::(Checksum: 3:1893978d3f8077a8512e93cd510f3546)
CREATE TABLE daoimage (id int4 NOT NULL, compression VARCHAR(64), horizontalsize int4 NOT NULL, verticalsize int4 NOT NULL, file_id int4 NOT NULL, CONSTRAINT daoimagepk PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Table', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-14', '2.0.2-SNP', '3:1893978d3f8077a8512e93cd510f3546', 15);

-- Changeset liquibase/common.liquibase.xml::1300878547256-15::Thomas Guyard::(Checksum: 3:58cd0a7a3506cd6b4c04d69aa03a1731)
CREATE TABLE daointernalaccount (blocked NUMERIC(19,2) NOT NULL, id int4 NOT NULL, CONSTRAINT daointernalacpk PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Table', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-15', '2.0.2-SNP', '3:58cd0a7a3506cd6b4c04d69aa03a1731', 16);

-- Changeset liquibase/common.liquibase.xml::1300878547256-16::Thomas Guyard::(Checksum: 3:37d1f57351004efc6a14ecd858d28cd8)
CREATE TABLE daojointeaminvitation (id int4 NOT NULL, state int4 NOT NULL, receiver_id int4 NOT NULL, sender_id int4 NOT NULL, team_id int4 NOT NULL, CONSTRAINT daojointeaminpk PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Table', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-16', '2.0.2-SNP', '3:37d1f57351004efc6a14ecd858d28cd8', 17);

-- Changeset liquibase/common.liquibase.xml::1300878547256-17::Thomas Guyard::(Checksum: 3:0f02e10e47b6223bce37107f290324d9)
CREATE TABLE daokudos (value int4 NOT NULL, id int4 NOT NULL, kudosable_id int4 NOT NULL, CONSTRAINT daokudospk PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Table', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-17', '2.0.2-SNP', '3:0f02e10e47b6223bce37107f290324d9', 18);

-- Changeset liquibase/common.liquibase.xml::1300878547256-18::Thomas Guyard::(Checksum: 3:adf36dbba7f593c8d00ce591965d12d4)
CREATE TABLE daokudosable (ispopularitylocked bool NOT NULL, popularity int4 NOT NULL, state int4 NOT NULL, id int4 NOT NULL, CONSTRAINT daokudosablepk PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Table', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-18', '2.0.2-SNP', '3:adf36dbba7f593c8d00ce591965d12d4', 19);

-- Changeset liquibase/common.liquibase.xml::1300878547256-19::Thomas Guyard::(Checksum: 3:a8bdf3d3d9a12ac4a546492ad6f9ad04)
CREATE TABLE daomember (email VARCHAR(255) NOT NULL, fullname VARCHAR(255), karma int4 NOT NULL, locale VARCHAR(255) NOT NULL, password VARCHAR(255) NOT NULL, role int4 NOT NULL, state int4 NOT NULL, id int4 NOT NULL, avatar_id int4, CONSTRAINT daomemberpk PRIMARY KEY (id), UNIQUE (email));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Table', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-19', '2.0.2-SNP', '3:a8bdf3d3d9a12ac4a546492ad6f9ad04', 20);

-- Changeset liquibase/common.liquibase.xml::1300878547256-20::Thomas Guyard::(Checksum: 3:5b93918fe70100981241ca21bd575228)
CREATE TABLE daomilestone (id int4 NOT NULL, amount NUMERIC(19,2) NOT NULL, expirationdate TIMESTAMP WITH TIME ZONE NOT NULL, fatalbugspercent int4 NOT NULL, leveltovalidate int4, majorbugspercent int4 NOT NULL, milestonestate int4 NOT NULL, secondbeforevalidation int4 NOT NULL, description_id int4, offer_id int4 NOT NULL, CONSTRAINT daomilestonepk PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Table', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-20', '2.0.2-SNP', '3:5b93918fe70100981241ca21bd575228', 21);

-- Changeset liquibase/common.liquibase.xml::1300878547256-21::Thomas Guyard::(Checksum: 3:a8fa849df19a443806ac581f046df85a)
CREATE TABLE daooffer (amount NUMERIC(19,2) NOT NULL, currentmilestone int4 NOT NULL, expirationdate TIMESTAMP WITH TIME ZONE NOT NULL, isdraft bool NOT NULL, id int4 NOT NULL, feature_id int4 NOT NULL, CONSTRAINT daoofferpk PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Table', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-21', '2.0.2-SNP', '3:a8fa849df19a443806ac581f046df85a', 22);

-- Changeset liquibase/common.liquibase.xml::1300878547256-22::Thomas Guyard::(Checksum: 3:9f8fa7c6cf203d71e9b5a60f3fdcac5e)
CREATE TABLE daorelease (description VARCHAR(255) NOT NULL, locale VARCHAR(255) NOT NULL, version VARCHAR(255), id int4 NOT NULL, milestone_id int4 NOT NULL, CONSTRAINT daoreleasepk PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Table', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-22', '2.0.2-SNP', '3:9f8fa7c6cf203d71e9b5a60f3fdcac5e', 23);

-- Changeset liquibase/common.liquibase.xml::1300878547256-23::Thomas Guyard::(Checksum: 3:f93e71cd234c016a17704c01c7cbdb2f)
CREATE TABLE daosoftware (id int4 NOT NULL, name VARCHAR(255) NOT NULL, description_id int4 NOT NULL, image_id int4, CONSTRAINT daosoftwarepk PRIMARY KEY (id), UNIQUE (name));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Table', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-23', '2.0.2-SNP', '3:f93e71cd234c016a17704c01c7cbdb2f', 24);

-- Changeset liquibase/common.liquibase.xml::1300878547256-24::Thomas Guyard::(Checksum: 3:cb60a0fad3450c3b60904f79e395c001)
CREATE TABLE daoteam (contact VARCHAR(255) NOT NULL, description TEXT NOT NULL, team_right int4 NOT NULL, id int4 NOT NULL, avatar_id int4, CONSTRAINT daoteampk PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Table', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-24', '2.0.2-SNP', '3:cb60a0fad3450c3b60904f79e395c001', 25);

-- Changeset liquibase/common.liquibase.xml::1300878547256-25::Thomas Guyard::(Checksum: 3:defff2d22c2260893424d48c4fa6ebf5)
CREATE TABLE daoteammembership (id int4 NOT NULL, bloatitteam_id int4 NOT NULL, member_id int4 NOT NULL, CONSTRAINT daoteammemberpk PRIMARY KEY (id));

ALTER TABLE daoteammembership ADD UNIQUE (member_id, bloatitteam_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Table, Add Unique Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-25', '2.0.2-SNP', '3:defff2d22c2260893424d48c4fa6ebf5', 26);

-- Changeset liquibase/common.liquibase.xml::1300878547256-26::Thomas Guyard::(Checksum: 3:f12c8f559e4a93ba5f1977e9c5ed05c6)
CREATE TABLE daoteamright (id int4 NOT NULL, userstatus int4 NOT NULL, membership_id int4 NOT NULL, CONSTRAINT daoteamrightpk PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Table', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-26', '2.0.2-SNP', '3:f12c8f559e4a93ba5f1977e9c5ed05c6', 27);

-- Changeset liquibase/common.liquibase.xml::1300878547256-27::Thomas Guyard::(Checksum: 3:6a844edaae00e8621b4f9f9d3d1d3502)
CREATE TABLE daotransaction (id int4 NOT NULL, amount NUMERIC(19,2) NOT NULL, creationdate TIMESTAMP WITH TIME ZONE NOT NULL, from_id int4 NOT NULL, to_id int4 NOT NULL, CONSTRAINT daotransactiopk PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Table', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-27', '2.0.2-SNP', '3:6a844edaae00e8621b4f9f9d3d1d3502', 28);

-- Changeset liquibase/common.liquibase.xml::1300878547256-28::Thomas Guyard::(Checksum: 3:7a7be785f10bbaa50b6aaa8d3b8971c3)
CREATE TABLE daotranslation (locale VARCHAR(255) NOT NULL, text TEXT NOT NULL, title TEXT NOT NULL, id int4 NOT NULL, description_id int4 NOT NULL, CONSTRAINT daotranslatiopk PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Table', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-28', '2.0.2-SNP', '3:7a7be785f10bbaa50b6aaa8d3b8971c3', 29);

-- Changeset liquibase/common.liquibase.xml::1300878547256-29::Thomas Guyard::(Checksum: 3:15a5eba5baf2e3460ae8c606e7970c0e)
CREATE TABLE daousercontent (id int4 NOT NULL, creationdate TIMESTAMP WITH TIME ZONE NOT NULL, isdeleted bool NOT NULL, asteam_id int4, member_id int4 NOT NULL, CONSTRAINT daousercontenpk PRIMARY KEY (id));

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Table', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-29', '2.0.2-SNP', '3:15a5eba5baf2e3460ae8c606e7970c0e', 30);

-- Changeset liquibase/common.liquibase.xml::1300878547256-30::Thomas Guyard::(Checksum: 3:133f7b6ea169f27fb87301bcbb205430)
CREATE INDEX UK643905A38E765AEA ON daoactor(internalaccount_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Index', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-30', '2.0.2-SNP', '3:133f7b6ea169f27fb87301bcbb205430', 31);

-- Changeset liquibase/common.liquibase.xml::1300878547256-31::Thomas Guyard::(Checksum: 3:4e1321ade142b43061791c84fe2bc51d)
CREATE INDEX UK643905A3A2D17E78 ON daoactor(externalaccount_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Index', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-31', '2.0.2-SNP', '3:4e1321ade142b43061791c84fe2bc51d', 32);

-- Changeset liquibase/common.liquibase.xml::1300878547256-32::Thomas Guyard::(Checksum: 3:b7329055d6f4d9bad544db6297a13383)
CREATE INDEX UK988DA1493C3C4A1C ON daocontribution_daotransaction(transaction_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Index', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-32', '2.0.2-SNP', '3:b7329055d6f4d9bad544db6297a13383', 33);

-- Changeset liquibase/common.liquibase.xml::1300878547256-33::Thomas Guyard::(Checksum: 3:7d48666f47ddc53cf1de4b2317b1ff68)
CREATE INDEX UK44EDA3241AAB9C3E ON daofeature(description_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Index', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-33', '2.0.2-SNP', '3:7d48666f47ddc53cf1de4b2317b1ff68', 34);

-- Changeset liquibase/common.liquibase.xml::1300878547256-34::Thomas Guyard::(Checksum: 3:787410d424e2cfbadc39a47cba04d787)
CREATE INDEX UK3A3C9987A0A8D477 ON daofeature_daocontribution(contributions_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Index', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-34', '2.0.2-SNP', '3:787410d424e2cfbadc39a47cba04d787', 35);

-- Changeset liquibase/common.liquibase.xml::1300878547256-35::Thomas Guyard::(Checksum: 3:bdf962249d8fd362fbef58e0c6f91850)
CREATE INDEX UK64AE0509CD09BABE ON daoimage(file_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Index', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-35', '2.0.2-SNP', '3:bdf962249d8fd362fbef58e0c6f91850', 36);

-- Changeset liquibase/common.liquibase.xml::1300878547256-36::Thomas Guyard::(Checksum: 3:1c1298f52b161e5125d9eb5f1d10c4bd)
CREATE INDEX key1 ON daoteammembership(member_id, bloatitteam_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Index', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-36', '2.0.2-SNP', '3:1c1298f52b161e5125d9eb5f1d10c4bd', 37);

-- Changeset liquibase/common.liquibase.xml::1300878547256-37::Thomas Guyard::(Checksum: 3:52952360de23f66f9badfb2e0f84ba05)
CREATE INDEX key2 ON daotranslation(locale, description_id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Create Index', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-37', '2.0.2-SNP', '3:52952360de23f66f9badfb2e0f84ba05', 38);

-- Changeset liquibase/common.liquibase.xml::1300878547256-38::Thomas Guyard::(Checksum: 3:301ee2c38570658b6ab788884ce9eae1)
ALTER TABLE daoaccount ADD CONSTRAINT FK391EB25B7BA58734 FOREIGN KEY (actor_id) REFERENCES daoactor (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-38', '2.0.2-SNP', '3:301ee2c38570658b6ab788884ce9eae1', 39);

-- Changeset liquibase/common.liquibase.xml::1300878547256-39::Thomas Guyard::(Checksum: 3:6c286aeafd70ab2741a5dd141927d081)
ALTER TABLE daoactor ADD CONSTRAINT FK643905A3E25C0F74 FOREIGN KEY (externalaccount_id) REFERENCES daoexternalaccount (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-39', '2.0.2-SNP', '3:6c286aeafd70ab2741a5dd141927d081', 40);

-- Changeset liquibase/common.liquibase.xml::1300878547256-40::Thomas Guyard::(Checksum: 3:d1e7a67205dc14754699040f584e1927)
ALTER TABLE daoactor ADD CONSTRAINT FK643905A362C4B4B4 FOREIGN KEY (internalaccount_id) REFERENCES daointernalaccount (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-40', '2.0.2-SNP', '3:d1e7a67205dc14754699040f584e1927', 41);

-- Changeset liquibase/common.liquibase.xml::1300878547256-41::Thomas Guyard::(Checksum: 3:d724071a35e88e0bab97e39c050a8c29)
ALTER TABLE daobanktransaction ADD CONSTRAINT FKEB8E913035FADFDE FOREIGN KEY (author_id) REFERENCES daoactor (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-41', '2.0.2-SNP', '3:d724071a35e88e0bab97e39c050a8c29', 42);

-- Changeset liquibase/common.liquibase.xml::1300878547256-42::Thomas Guyard::(Checksum: 3:6efb9e068f6a6cbb45674afdbd7ca8e1)
ALTER TABLE daobug ADD CONSTRAINT FK7993EDA21EA65D63 FOREIGN KEY (id) REFERENCES daousercontent (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-42', '2.0.2-SNP', '3:6efb9e068f6a6cbb45674afdbd7ca8e1', 43);

-- Changeset liquibase/common.liquibase.xml::1300878547256-43::Thomas Guyard::(Checksum: 3:4b313a7de043d701500daff7fac0ce96)
ALTER TABLE daobug ADD CONSTRAINT FK7993EDA2735AB454 FOREIGN KEY (milestone_id) REFERENCES daomilestone (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-43', '2.0.2-SNP', '3:4b313a7de043d701500daff7fac0ce96', 44);

-- Changeset liquibase/common.liquibase.xml::1300878547256-44::Thomas Guyard::(Checksum: 3:06d1422caca070af6846b8ca5f0112a8)
ALTER TABLE daocomment ADD CONSTRAINT FKB7F1168DDA0B6154 FOREIGN KEY (bug_id) REFERENCES daobug (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-44', '2.0.2-SNP', '3:06d1422caca070af6846b8ca5f0112a8', 45);

-- Changeset liquibase/common.liquibase.xml::1300878547256-45::Thomas Guyard::(Checksum: 3:4fb137564e6c0477d4cf0147916f8c05)
ALTER TABLE daocomment ADD CONSTRAINT FKB7F1168D154DE457 FOREIGN KEY (father_id) REFERENCES daocomment (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-45', '2.0.2-SNP', '3:4fb137564e6c0477d4cf0147916f8c05', 46);

-- Changeset liquibase/common.liquibase.xml::1300878547256-46::Thomas Guyard::(Checksum: 3:c4478f02e53ee2ab11726f661ac5de52)
ALTER TABLE daocomment ADD CONSTRAINT FKB7F1168D7A295D14 FOREIGN KEY (feature_id) REFERENCES daofeature (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-46', '2.0.2-SNP', '3:c4478f02e53ee2ab11726f661ac5de52', 47);

-- Changeset liquibase/common.liquibase.xml::1300878547256-47::Thomas Guyard::(Checksum: 3:78bd7a22836304185ffd1045b6b2d797)
ALTER TABLE daocomment ADD CONSTRAINT FKB7F1168D46A823AD FOREIGN KEY (id) REFERENCES daokudosable (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-47', '2.0.2-SNP', '3:78bd7a22836304185ffd1045b6b2d797', 48);

-- Changeset liquibase/common.liquibase.xml::1300878547256-48::Thomas Guyard::(Checksum: 3:0767b9bf64eff0aad2d2eb61b84e3c1c)
ALTER TABLE daocomment ADD CONSTRAINT FKB7F1168DA415F774 FOREIGN KEY (release_id) REFERENCES daorelease (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-48', '2.0.2-SNP', '3:0767b9bf64eff0aad2d2eb61b84e3c1c', 49);

-- Changeset liquibase/common.liquibase.xml::1300878547256-49::Thomas Guyard::(Checksum: 3:ebbba860954f3b029a7f142569355c17)
ALTER TABLE daocontribution ADD CONSTRAINT FK39F068A27A295D14 FOREIGN KEY (feature_id) REFERENCES daofeature (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-49', '2.0.2-SNP', '3:ebbba860954f3b029a7f142569355c17', 50);

-- Changeset liquibase/common.liquibase.xml::1300878547256-50::Thomas Guyard::(Checksum: 3:ca0f7a1ab9b0c09ff0cf6a3af0a8e9df)
ALTER TABLE daocontribution ADD CONSTRAINT FK39F068A21EA65D63 FOREIGN KEY (id) REFERENCES daousercontent (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-50', '2.0.2-SNP', '3:ca0f7a1ab9b0c09ff0cf6a3af0a8e9df', 51);

-- Changeset liquibase/common.liquibase.xml::1300878547256-51::Thomas Guyard::(Checksum: 3:dd697fdd87a58c37f8ab6615acc936ed)
ALTER TABLE daocontribution_daotransaction ADD CONSTRAINT FK988DA1499508E70E FOREIGN KEY (daocontribution_id) REFERENCES daocontribution (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-51', '2.0.2-SNP', '3:dd697fdd87a58c37f8ab6615acc936ed', 52);

-- Changeset liquibase/common.liquibase.xml::1300878547256-52::Thomas Guyard::(Checksum: 3:51f3b43393fb6d9288e76d475ebe6a54)
ALTER TABLE daocontribution_daotransaction ADD CONSTRAINT FK988DA149B3CA5714 FOREIGN KEY (transaction_id) REFERENCES daotransaction (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-52', '2.0.2-SNP', '3:51f3b43393fb6d9288e76d475ebe6a54', 53);

-- Changeset liquibase/common.liquibase.xml::1300878547256-53::Thomas Guyard::(Checksum: 3:5a6f98f2366bf2f918106fc028859f3e)
ALTER TABLE daoexternalaccount ADD CONSTRAINT FK2D4B0E107755EE62 FOREIGN KEY (id) REFERENCES daoaccount (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-53', '2.0.2-SNP', '3:5a6f98f2366bf2f918106fc028859f3e', 54);

-- Changeset liquibase/common.liquibase.xml::1300878547256-54::Thomas Guyard::(Checksum: 3:de45a595ac5a674a3d571c5bbb7dac25)
ALTER TABLE daofeature ADD CONSTRAINT FK44EDA324ABCE5A54 FOREIGN KEY (description_id) REFERENCES daodescription (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-54', '2.0.2-SNP', '3:de45a595ac5a674a3d571c5bbb7dac25', 55);

-- Changeset liquibase/common.liquibase.xml::1300878547256-55::Thomas Guyard::(Checksum: 3:aae9ef2f357ce075d08a47bfbb7fc2e5)
ALTER TABLE daofeature ADD CONSTRAINT FK44EDA32446A823AD FOREIGN KEY (id) REFERENCES daokudosable (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-55', '2.0.2-SNP', '3:aae9ef2f357ce075d08a47bfbb7fc2e5', 56);

-- Changeset liquibase/common.liquibase.xml::1300878547256-56::Thomas Guyard::(Checksum: 3:84ff8d2d205bd760494f31df04ecea2a)
ALTER TABLE daofeature ADD CONSTRAINT FK44EDA3249961816F FOREIGN KEY (selectedoffer_id) REFERENCES daooffer (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-56', '2.0.2-SNP', '3:84ff8d2d205bd760494f31df04ecea2a', 57);

-- Changeset liquibase/common.liquibase.xml::1300878547256-57::Thomas Guyard::(Checksum: 3:c1540eee51588b34c81e94e995aa9840)
ALTER TABLE daofeature ADD CONSTRAINT FK44EDA324784E4880 FOREIGN KEY (software_id) REFERENCES daosoftware (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-57', '2.0.2-SNP', '3:c1540eee51588b34c81e94e995aa9840', 58);

-- Changeset liquibase/common.liquibase.xml::1300878547256-58::Thomas Guyard::(Checksum: 3:79de145644a4b6b3e6154411f96aa6db)
ALTER TABLE daofeature_daocontribution ADD CONSTRAINT FK3A3C9987F86681AD FOREIGN KEY (contributions_id) REFERENCES daocontribution (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-58', '2.0.2-SNP', '3:79de145644a4b6b3e6154411f96aa6db', 59);

-- Changeset liquibase/common.liquibase.xml::1300878547256-59::Thomas Guyard::(Checksum: 3:732ba83b3fe773f44e12d49b15364f1a)
ALTER TABLE daofeature_daocontribution ADD CONSTRAINT FK3A3C99871ED11226 FOREIGN KEY (daofeature_id) REFERENCES daofeature (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-59', '2.0.2-SNP', '3:732ba83b3fe773f44e12d49b15364f1a', 60);

-- Changeset liquibase/common.liquibase.xml::1300878547256-60::Thomas Guyard::(Checksum: 3:d11d72af5bafb971d85fc48ea6ba8880)
ALTER TABLE daofilemetadata ADD CONSTRAINT FK81855D5D1EA65D63 FOREIGN KEY (id) REFERENCES daousercontent (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-60', '2.0.2-SNP', '3:d11d72af5bafb971d85fc48ea6ba8880', 61);

-- Changeset liquibase/common.liquibase.xml::1300878547256-61::Thomas Guyard::(Checksum: 3:bda1b04b80e60936f5990b620b2bf207)
ALTER TABLE daofilemetadata ADD CONSTRAINT FK81855D5DDC01B154 FOREIGN KEY (relatedcontent_id) REFERENCES daousercontent (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-61', '2.0.2-SNP', '3:bda1b04b80e60936f5990b620b2bf207', 62);

-- Changeset liquibase/common.liquibase.xml::1300878547256-62::Thomas Guyard::(Checksum: 3:bc4b5ed39292695d72376467bc6149e6)
ALTER TABLE daohighlightfeature ADD CONSTRAINT FK38A7BF347A295D14 FOREIGN KEY (feature_id) REFERENCES daofeature (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-62', '2.0.2-SNP', '3:bc4b5ed39292695d72376467bc6149e6', 63);

-- Changeset liquibase/common.liquibase.xml::1300878547256-63::Thomas Guyard::(Checksum: 3:7b8383843a57bd230f4a20b8762e6b1d)
ALTER TABLE daoimage ADD CONSTRAINT FK64AE05096C5C5CAF FOREIGN KEY (file_id) REFERENCES daofilemetadata (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-63', '2.0.2-SNP', '3:7b8383843a57bd230f4a20b8762e6b1d', 64);

-- Changeset liquibase/common.liquibase.xml::1300878547256-64::Thomas Guyard::(Checksum: 3:2b7bb3542d15a708d28c214775617f97)
ALTER TABLE daointernalaccount ADD CONSTRAINT FKC20ED6DE7755EE62 FOREIGN KEY (id) REFERENCES daoaccount (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-64', '2.0.2-SNP', '3:2b7bb3542d15a708d28c214775617f97', 65);

-- Changeset liquibase/common.liquibase.xml::1300878547256-65::Thomas Guyard::(Checksum: 3:6598e9e5df97760253eb2221ce75e7b4)
ALTER TABLE daojointeaminvitation ADD CONSTRAINT FK7AFAECB27A1975AB FOREIGN KEY (receiver_id) REFERENCES daomember (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-65', '2.0.2-SNP', '3:6598e9e5df97760253eb2221ce75e7b4', 66);

-- Changeset liquibase/common.liquibase.xml::1300878547256-66::Thomas Guyard::(Checksum: 3:ec759af93f6cfbba86988587afbbec14)
ALTER TABLE daojointeaminvitation ADD CONSTRAINT FK7AFAECB2F95717A5 FOREIGN KEY (sender_id) REFERENCES daomember (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-66', '2.0.2-SNP', '3:ec759af93f6cfbba86988587afbbec14', 67);

-- Changeset liquibase/common.liquibase.xml::1300878547256-67::Thomas Guyard::(Checksum: 3:f21558327dc5b2d05ad9e1df752acd56)
ALTER TABLE daojointeaminvitation ADD CONSTRAINT FK7AFAECB23FD07C0 FOREIGN KEY (team_id) REFERENCES daoteam (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-67', '2.0.2-SNP', '3:f21558327dc5b2d05ad9e1df752acd56', 68);

-- Changeset liquibase/common.liquibase.xml::1300878547256-68::Thomas Guyard::(Checksum: 3:2d3baef869967b2f934c86dc877a346c)
ALTER TABLE daokudos ADD CONSTRAINT FK64CDE34C1EA65D63 FOREIGN KEY (id) REFERENCES daousercontent (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-68', '2.0.2-SNP', '3:2d3baef869967b2f934c86dc877a346c', 69);

-- Changeset liquibase/common.liquibase.xml::1300878547256-69::Thomas Guyard::(Checksum: 3:d92ca97ae07df879e31ab88b723fd4ef)
ALTER TABLE daokudos ADD CONSTRAINT FK64CDE34CA38A0754 FOREIGN KEY (kudosable_id) REFERENCES daokudosable (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-69', '2.0.2-SNP', '3:d92ca97ae07df879e31ab88b723fd4ef', 70);

-- Changeset liquibase/common.liquibase.xml::1300878547256-70::Thomas Guyard::(Checksum: 3:b9cc5c1c3532ac4553a1b9df01da44e3)
ALTER TABLE daokudosable ADD CONSTRAINT FKB980F2A61EA65D63 FOREIGN KEY (id) REFERENCES daousercontent (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-70', '2.0.2-SNP', '3:b9cc5c1c3532ac4553a1b9df01da44e3', 71);

-- Changeset liquibase/common.liquibase.xml::1300878547256-71::Thomas Guyard::(Checksum: 3:8c79e60b19fda71cba45b47109d35060)
ALTER TABLE daomember ADD CONSTRAINT FK377AA4AC85C627B2 FOREIGN KEY (avatar_id) REFERENCES daofilemetadata (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-71', '2.0.2-SNP', '3:8c79e60b19fda71cba45b47109d35060', 72);

-- Changeset liquibase/common.liquibase.xml::1300878547256-72::Thomas Guyard::(Checksum: 3:cf7250106d16ec1cee5a35030168a21e)
ALTER TABLE daomember ADD CONSTRAINT FK377AA4ACDE070CAA FOREIGN KEY (id) REFERENCES daoactor (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-72', '2.0.2-SNP', '3:cf7250106d16ec1cee5a35030168a21e', 73);

-- Changeset liquibase/common.liquibase.xml::1300878547256-73::Thomas Guyard::(Checksum: 3:5b86447b999b3086c659c2c70d0bbbf3)
ALTER TABLE daomilestone ADD CONSTRAINT FK99A5EE1EABCE5A54 FOREIGN KEY (description_id) REFERENCES daodescription (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-73', '2.0.2-SNP', '3:5b86447b999b3086c659c2c70d0bbbf3', 74);

-- Changeset liquibase/common.liquibase.xml::1300878547256-74::Thomas Guyard::(Checksum: 3:50b1bea5645f328c6c168933252e8688)
ALTER TABLE daomilestone ADD CONSTRAINT FK99A5EE1EB0FE59D4 FOREIGN KEY (offer_id) REFERENCES daooffer (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-74', '2.0.2-SNP', '3:50b1bea5645f328c6c168933252e8688', 75);

-- Changeset liquibase/common.liquibase.xml::1300878547256-75::Thomas Guyard::(Checksum: 3:6fd3c1884f5eda34a7d81515090c1ff7)
ALTER TABLE daooffer ADD CONSTRAINT FK64FF760A7A295D14 FOREIGN KEY (feature_id) REFERENCES daofeature (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-75', '2.0.2-SNP', '3:6fd3c1884f5eda34a7d81515090c1ff7', 76);

-- Changeset liquibase/common.liquibase.xml::1300878547256-76::Thomas Guyard::(Checksum: 3:5eaf29f0b983a561535ddf27621cf33c)
ALTER TABLE daooffer ADD CONSTRAINT FK64FF760A46A823AD FOREIGN KEY (id) REFERENCES daokudosable (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-76', '2.0.2-SNP', '3:5eaf29f0b983a561535ddf27621cf33c', 77);

-- Changeset liquibase/common.liquibase.xml::1300878547256-77::Thomas Guyard::(Checksum: 3:74770b4f71d20bcadf7d3933cdccec9c)
ALTER TABLE daorelease ADD CONSTRAINT FKC04C50351EA65D63 FOREIGN KEY (id) REFERENCES daousercontent (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-77', '2.0.2-SNP', '3:74770b4f71d20bcadf7d3933cdccec9c', 78);

-- Changeset liquibase/common.liquibase.xml::1300878547256-78::Thomas Guyard::(Checksum: 3:4a661c10284f1991b84f713972fbd0fa)
ALTER TABLE daorelease ADD CONSTRAINT FKC04C5035735AB454 FOREIGN KEY (milestone_id) REFERENCES daomilestone (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-78', '2.0.2-SNP', '3:4a661c10284f1991b84f713972fbd0fa', 79);

-- Changeset liquibase/common.liquibase.xml::1300878547256-79::Thomas Guyard::(Checksum: 3:9ac9a3f25fc3543fc70373df357969ef)
ALTER TABLE daosoftware ADD CONSTRAINT FKB8BD3F39ABCE5A54 FOREIGN KEY (description_id) REFERENCES daodescription (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-79', '2.0.2-SNP', '3:9ac9a3f25fc3543fc70373df357969ef', 80);

-- Changeset liquibase/common.liquibase.xml::1300878547256-80::Thomas Guyard::(Checksum: 3:7edf33026d86a109bdaf373bfdbe4fdc)
ALTER TABLE daosoftware ADD CONSTRAINT FKB8BD3F396C162870 FOREIGN KEY (image_id) REFERENCES daofilemetadata (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-80', '2.0.2-SNP', '3:7edf33026d86a109bdaf373bfdbe4fdc', 81);

-- Changeset liquibase/common.liquibase.xml::1300878547256-81::Thomas Guyard::(Checksum: 3:70e6a1fb6d493a47f7a0d4ab41759863)
ALTER TABLE daoteam ADD CONSTRAINT FKB8F1B8EF85C627B2 FOREIGN KEY (avatar_id) REFERENCES daofilemetadata (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-81', '2.0.2-SNP', '3:70e6a1fb6d493a47f7a0d4ab41759863', 82);

-- Changeset liquibase/common.liquibase.xml::1300878547256-82::Thomas Guyard::(Checksum: 3:911d7520dabf6b25931ec6a45ac43d1f)
ALTER TABLE daoteam ADD CONSTRAINT FKB8F1B8EFDE070CAA FOREIGN KEY (id) REFERENCES daoactor (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-82', '2.0.2-SNP', '3:911d7520dabf6b25931ec6a45ac43d1f', 83);

-- Changeset liquibase/common.liquibase.xml::1300878547256-83::Thomas Guyard::(Checksum: 3:fe5b9b1b0d04f53671d05513d05a793b)
ALTER TABLE daoteammembership ADD CONSTRAINT FK889DFE256E3A82DD FOREIGN KEY (bloatitteam_id) REFERENCES daoteam (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-83', '2.0.2-SNP', '3:fe5b9b1b0d04f53671d05513d05a793b', 84);

-- Changeset liquibase/common.liquibase.xml::1300878547256-84::Thomas Guyard::(Checksum: 3:e8a168e7e604e54f159176f0ac9364ab)
ALTER TABLE daoteammembership ADD CONSTRAINT FK889DFE25476C9120 FOREIGN KEY (member_id) REFERENCES daomember (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-84', '2.0.2-SNP', '3:e8a168e7e604e54f159176f0ac9364ab', 85);

-- Changeset liquibase/common.liquibase.xml::1300878547256-85::Thomas Guyard::(Checksum: 3:6f509b4daf65352711a7ed5507dc81f6)
ALTER TABLE daoteamright ADD CONSTRAINT FKCDEC566D24B03FDD FOREIGN KEY (membership_id) REFERENCES daoteammembership (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-85', '2.0.2-SNP', '3:6f509b4daf65352711a7ed5507dc81f6', 86);

-- Changeset liquibase/common.liquibase.xml::1300878547256-86::Thomas Guyard::(Checksum: 3:8e3c2a13839f15bd4bff6a28d740a25b)
ALTER TABLE daotransaction ADD CONSTRAINT FK97A0340CB0E19B9A FOREIGN KEY (from_id) REFERENCES daointernalaccount (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-86', '2.0.2-SNP', '3:8e3c2a13839f15bd4bff6a28d740a25b', 87);

-- Changeset liquibase/common.liquibase.xml::1300878547256-87::Thomas Guyard::(Checksum: 3:be3f801434d6086b564d5bc5801163e5)
ALTER TABLE daotransaction ADD CONSTRAINT FK97A0340C7DEC6EA6 FOREIGN KEY (to_id) REFERENCES daoaccount (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-87', '2.0.2-SNP', '3:be3f801434d6086b564d5bc5801163e5', 88);

-- Changeset liquibase/common.liquibase.xml::1300878547256-88::Thomas Guyard::(Checksum: 3:266f1982a0ec8a1f071060283b45b178)
ALTER TABLE daotranslation ADD CONSTRAINT FKAA4953DFABCE5A54 FOREIGN KEY (description_id) REFERENCES daodescription (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-88', '2.0.2-SNP', '3:266f1982a0ec8a1f071060283b45b178', 89);

-- Changeset liquibase/common.liquibase.xml::1300878547256-89::Thomas Guyard::(Checksum: 3:83f6a20a5fbf5641936839284ed50b2b)
ALTER TABLE daotranslation ADD CONSTRAINT FKAA4953DF46A823AD FOREIGN KEY (id) REFERENCES daokudosable (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-89', '2.0.2-SNP', '3:83f6a20a5fbf5641936839284ed50b2b', 90);

-- Changeset liquibase/common.liquibase.xml::1300878547256-90::Thomas Guyard::(Checksum: 3:248aa1da27e7763864c4472baa92109c)
ALTER TABLE daousercontent ADD CONSTRAINT FK3EB8775CDC123F4E FOREIGN KEY (asteam_id) REFERENCES daoteam (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-90', '2.0.2-SNP', '3:248aa1da27e7763864c4472baa92109c', 91);

-- Changeset liquibase/common.liquibase.xml::1300878547256-91::Thomas Guyard::(Checksum: 3:15c4a15477124085f0f2565a64dd4592)
ALTER TABLE daousercontent ADD CONSTRAINT FK3EB8775C476C9120 FOREIGN KEY (member_id) REFERENCES daomember (id);

INSERT INTO databasechangelog (AUTHOR, COMMENTS, DATEEXECUTED, DESCRIPTION, EXECTYPE, FILENAME, ID, LIQUIBASE, MD5SUM, ORDEREXECUTED) VALUES ('Thomas Guyard', '', NOW(), 'Add Foreign Key Constraint', 'EXECUTED', 'liquibase/common.liquibase.xml', '1300878547256-91', '2.0.2-SNP', '3:15c4a15477124085f0f2565a64dd4592', 92);

