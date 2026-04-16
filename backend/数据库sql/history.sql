create table history
(
    word      varchar(255)                        not null,
    timestamp timestamp default CURRENT_TIMESTAMP null,
    userid    varchar(255)                        not null,
    type      int       default 0                 not null,
    constraint word
        unique (word)
);

INSERT INTO wordtest.history (word, timestamp, userid, type) VALUES ('asdfasf', '2024-10-23 11:43:23', '65bbeb70-1fe1-4511-b98e-f10a0e00ee42', 1);
INSERT INTO wordtest.history (word, timestamp, userid, type) VALUES ('example', '2024-10-23 11:39:34', '65bbeb70-1fe1-4511-b98e-f10a0e00ee42', 1);
INSERT INTO wordtest.history (word, timestamp, userid, type) VALUES ('hello', '2024-10-28 14:45:38', '574d1380-bb88-42c9-9eb9-bffdb654ca78', 1);
INSERT INTO wordtest.history (word, timestamp, userid, type) VALUES ('wfsa', '2024-10-23 11:45:34', '65bbeb70-1fe1-4511-b98e-f10a0e00ee42', 0);
INSERT INTO wordtest.history (word, timestamp, userid, type) VALUES ('啊', '2024-10-23 11:50:13', '65bbeb70-1fe1-4511-b98e-f10a0e00ee42', 0);
