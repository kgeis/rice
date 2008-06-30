create table trv_doc_2 (
        FDOC_NBR                       VARCHAR2(14) CONSTRAINT FP_INT_BILL_DOC_TN1 NOT NULL,
        OBJ_ID                         VARCHAR2(36) DEFAULT SYS_GUID() CONSTRAINT FP_INT_BILL_DOC_TN2 NOT NULL,
        VER_NBR                        NUMBER(8) DEFAULT 1 CONSTRAINT FP_INT_BILL_DOC_TN3 NOT NULL,
        FDOC_EXPLAIN_TXT               VARCHAR2(400),
        request_trav varchar2(30) not null,
        traveler          varchar2(200),
        org          varchar2(60),
        dest         varchar2(60),
        CONSTRAINT trv_doc_2P1 PRIMARY KEY (FDOC_NBR)
)
/

create table trv_acct (
    acct_num  varchar2(10) not null,
    acct_name varchar2(50),
    acct_type varchar2(100),
    acct_fo_id number(14),
    constraint trv_acct_pk primary key(acct_num)
)
/

create table trv_doc_acct (
    doc_hdr_id  number(14) not null,
    acct_num    varchar2(10) not null,
    constraint trv_doc_acct_pk primary key(doc_hdr_id, acct_num)
)
/

create table trv_acct_fo (
    acct_fo_id  number(14) not null,
    acct_fo_user_name varchar2(50) not null,
    constraint trv_acct_fo_id_pk primary key(acct_fo_id)
)
/

create table TRAV_DOC_2_ACCOUNTS (
    FDOC_NBR VARCHAR2(14),
    ACCT_NUM varchar2(10),
    CONSTRAINT TRAV_DOC_2_ACCOUNTS_P1 PRIMARY KEY (FDOC_NBR, ACCT_NUM)
)
/

create table TRV_ACCT_TYPE (
    ACCT_TYPE VARCHAR2(10),
    ACCT_TYPE_NAME varchar2(50),
    CONSTRAINT TRV_ACCT_TYPE_PK PRIMARY KEY (ACCT_TYPE)
)
/

create table TRV_ACCT_EXT (
    ACCT_NUM VARCHAR2(10),
    ACCT_TYPE varchar2(100),
    CONSTRAINT TRV_ACCT_TYPE_P1 PRIMARY KEY (ACCT_NUM, ACCT_TYPE)
)
/

CREATE SEQUENCE SEQ_TRAVEL_DOC_ID INCREMENT BY 1 START WITH 1000
/
CREATE SEQUENCE SEQ_TRAVEL_FO_ID INCREMENT BY 1 START WITH 1000
/

alter table trv_acct add constraint trv_acct_fk1 foreign key(acct_fo_id) references trv_acct_fo(acct_fo_id)
/

insert into trv_acct_fo (acct_fo_id, acct_fo_user_name) values (1, 'fred')
/
insert into trv_acct_fo (acct_fo_id, acct_fo_user_name) values (2, 'fran')
/
insert into trv_acct_fo (acct_fo_id, acct_fo_user_name) values (3, 'frank')
/

insert into TRV_ACCT (acct_num, acct_name, acct_fo_id) values ('a1', 'a1', 1)
/
insert into TRV_ACCT (acct_num, acct_name, acct_fo_id) values ('a2', 'a2', 2)
/
insert into TRV_ACCT (acct_num, acct_name, acct_fo_id) values ('a3', 'a3', 3)
/

insert into FP_DOC_TYPE_T (FDOC_TYP_CD, OBJ_ID, VER_NBR, FDOC_NM, FDOC_TYP_ACTIVE_CD) values ('TRAV', '1A6FEB2501C7607EE043814FD881607E', 1, 'TRAV ACCNT', 'Y')
/
insert into FP_DOC_TYPE_T (FDOC_TYP_CD, OBJ_ID, VER_NBR, FDOC_NM, FDOC_TYP_ACTIVE_CD) values ('TRFO', '1A6FEB250342607EE043814FD881607E', 1, 'TRAV FO', 'Y')
/
insert into FP_DOC_TYPE_T (FDOC_TYP_CD, OBJ_ID, VER_NBR, FDOC_NM, FDOC_TYP_ACTIVE_CD) values ('TRD2', '1A6FEB250342607EE043814FD889607E', 1, 'TRAV D2', 'Y')
/
insert into FP_DOC_TYPE_T (FDOC_TYP_CD, OBJ_ID, VER_NBR, FDOC_NM, FDOC_TYP_ACTIVE_CD) values ('RUSR', '1A6FEB253342607EE043814FD889607E', 1, 'RICE USR', 'Y')
/
insert into FP_DOC_TYPE_T (FDOC_TYP_CD, OBJ_ID, VER_NBR, FDOC_NM, FDOC_TYP_ACTIVE_CD) values ('PARM', '1A6FRB253342607EE043814FD889607E', 1, 'System Parms', 'Y')
/
insert into FP_DOC_TYPE_T (FDOC_TYP_CD, OBJ_ID, VER_NBR, FDOC_NM, FDOC_TYP_ACTIVE_CD) values ('BR', '1A6FRB253343337EE043814FD889607E', 1, 'Biz Rules', 'Y')
/
insert into FP_DOC_TYPE_T (FDOC_TYP_CD, OBJ_ID, VER_NBR, FDOC_NM, FDOC_TYP_ACTIVE_CD) values ('TRVA', '1A5FEB250342607EE043814FD889607E', 1,  'TRAV MAINT', 'Y')
/
insert into TRV_ACCT_EXT (ACCT_NUM, ACCT_TYPE) values ('a1', 'IAT')
/
insert into TRV_ACCT_EXT (ACCT_NUM, ACCT_TYPE) values ('a2', 'EAT')
/
insert into TRV_ACCT_EXT (ACCT_NUM, ACCT_TYPE) values ('a3', 'IAT')
/
insert into TRV_ACCT_TYPE (ACCT_TYPE, ACCT_TYPE_NAME) values ('CAT', 'Clearing Account Type')
/
insert into TRV_ACCT_TYPE (ACCT_TYPE, ACCT_TYPE_NAME) values ('EAT', 'Expense Account Type')
/
insert into TRV_ACCT_TYPE (ACCT_TYPE, ACCT_TYPE_NAME) values ('IAT', ' Income Account Type')
/
-- KEN sample data --

-- NOTIFICATION_PRODUCERS --
INSERT INTO NOTIFICATION_PRODUCERS
(ID, NAME, DESCRIPTION, CONTACT_INFO)
VALUES
(2, 'University Library System', 'This producer represents messages sent from the University Library system.', 'kuali-ken-testing@cornell.edu')
/

INSERT INTO NOTIFICATION_PRODUCERS
(ID, NAME, DESCRIPTION, CONTACT_INFO)
VALUES
(3, 'University Events Office', 'This producer represents messages sent from the University Events system.', 'kuali-ken-testing@cornell.edu')
/

-- NOTIFICATION_CHANNELS --
DELETE FROM NOTIFICATION_CHANNELS WHERE NAME != 'KEW'
/

INSERT INTO NOTIFICATION_CHANNELS
(ID, NAME, DESCRIPTION, SUBSCRIBABLE)
VALUES
(1, 'Kuali Rice Channel', 'This channel is used for sending out information about the Kuali Rice effort.', 'Y')
/

INSERT INTO NOTIFICATION_CHANNELS
(ID, NAME, DESCRIPTION, SUBSCRIBABLE)
VALUES
(2, 'Library Events Channel', 'This channel is used for sending out information about Library Events.', 'Y')
/

INSERT INTO NOTIFICATION_CHANNELS
(ID, NAME, DESCRIPTION, SUBSCRIBABLE)
VALUES
(3, 'Overdue Library Books', 'This channel is used for sending out information about your overdue books.', 'N')
/

INSERT INTO NOTIFICATION_CHANNELS
(ID, NAME, DESCRIPTION, SUBSCRIBABLE)
VALUES
(4, 'Concerts Coming to Campus', 'This channel broadcasts any concerts coming to campus.', 'Y')
/

INSERT INTO NOTIFICATION_CHANNELS
(ID, NAME, DESCRIPTION, SUBSCRIBABLE)
VALUES
(5, 'University Alerts', 'This channel broadcasts general announcements for the university.', 'N')
/

-- NOTIFICATION_CHANNEL_SUBSCRIPTIONS

INSERT INTO USER_CHANNEL_SUBSCRIPTIONS
(ID, CHANNEL_ID, USER_ID)
VALUES
(1, 1, 'TestUser4')
/

-- NOTIFICATION_RECIPIENTS_LISTS

INSERT INTO NOTIFICATION_RECIPIENTS_LISTS
(ID, CHANNEL_ID, RECIPIENT_TYPE, RECIPIENT_ID)
values
(1, 4, 'USER', 'TestUser1')
/

INSERT INTO NOTIFICATION_RECIPIENTS_LISTS
(ID, CHANNEL_ID, RECIPIENT_TYPE, RECIPIENT_ID)
values
(2, 4, 'USER', 'TestUser3')
/

-- NOTIFICATION_CHANNEL_REVIEWERS

INSERT INTO NOTIFICATION_REVIEWERS
(ID, CHANNEL_ID, REVIEWER_TYPE, REVIEWER_ID)
VALUES
(1, 1, 'GROUP', 'RiceTeam')
/

INSERT INTO NOTIFICATION_REVIEWERS
(ID, CHANNEL_ID, REVIEWER_TYPE, REVIEWER_ID)
VALUES
(2, 5, 'USER', 'TestUser3')
/

INSERT INTO NOTIFICATION_REVIEWERS
(ID, CHANNEL_ID, REVIEWER_TYPE, REVIEWER_ID)
VALUES
(3, 5, 'GROUP', 'TestGroup1')
/

-- NOTIFICATION_CHANNEL_PRODUCERS --
DELETE FROM NOTIFICATION_CHANNEL_PRODUCERS
/

INSERT INTO NOTIFICATION_CHANNEL_PRODUCERS
(CHANNEL_ID, PRODUCER_ID)
VALUES
(1, 1)
/

INSERT INTO NOTIFICATION_CHANNEL_PRODUCERS
(CHANNEL_ID, PRODUCER_ID)
VALUES
(2, 1)
/

INSERT INTO NOTIFICATION_CHANNEL_PRODUCERS
(CHANNEL_ID, PRODUCER_ID)
VALUES
(3, 1)
/

INSERT INTO NOTIFICATION_CHANNEL_PRODUCERS
(CHANNEL_ID, PRODUCER_ID)
VALUES
(4, 1)
/

INSERT INTO NOTIFICATION_CHANNEL_PRODUCERS
(CHANNEL_ID, PRODUCER_ID)
VALUES
(5, 1)
/

INSERT INTO NOTIFICATION_CHANNEL_PRODUCERS
(CHANNEL_ID, PRODUCER_ID)
VALUES
(2, 2)
/

INSERT INTO NOTIFICATION_CHANNEL_PRODUCERS
(CHANNEL_ID, PRODUCER_ID)
VALUES
(3, 2)
/

INSERT INTO NOTIFICATION_CHANNEL_PRODUCERS
(CHANNEL_ID, PRODUCER_ID)
VALUES
(4, 3)
/

-- Sample data that KCB contributes to the sample app --
-- just add some deliverer configurations

insert into KCB_RECIP_DELIVS (ID, RECIPIENT_ID, CHANNEL, DELIVERER_NAME, DB_LOCK_VER_NBR) values (1, 'TestUser6', 'KEW', 'mock', 0)
/
insert into KCB_RECIP_DELIVS (ID, RECIPIENT_ID, CHANNEL, DELIVERER_NAME, DB_LOCK_VER_NBR) values (2, 'TestUser1', 'KEW', 'mock', 0)
/
insert into KCB_RECIP_DELIVS (ID, RECIPIENT_ID, CHANNEL, DELIVERER_NAME, DB_LOCK_VER_NBR) values (3, 'TestUser2', 'KEW', 'mock', 0)
/
insert into KCB_RECIP_DELIVS (ID, RECIPIENT_ID, CHANNEL, DELIVERER_NAME, DB_LOCK_VER_NBR) values (4, 'quickstart', 'KEW', 'mock', 0)
/
insert into KCB_RECIP_DELIVS (ID, RECIPIENT_ID, CHANNEL, DELIVERER_NAME, DB_LOCK_VER_NBR) values (5, 'TestUser5', 'KEW', 'mock', 0)
/
insert into KCB_RECIP_DELIVS (ID, RECIPIENT_ID, CHANNEL, DELIVERER_NAME, DB_LOCK_VER_NBR) values (6, 'TestUser4', 'KEW', 'mock', 0)
/

commit
/
