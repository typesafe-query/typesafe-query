--コメント
	/* コメント */
CREATE TABLE ap_user
(
  user_id character varying(12) NOT NULL,
  name character varying(256) NOT NULL,
  lock_flg character(1) NOT NULL DEFAULT '0',
  valid_from date,
  valid_to date,
  unit_id character varying(3) NOT NULL,
  role_id character varying(2) NOT NULL,
  PRIMARY KEY (user_id)
);

CREATE TABLE role
(
  unit_id character varying(3) NOT NULL,
  role_id character varying(2) NOT NULL,
  name character varying(32) NOT NULL,
  PRIMARY KEY (unit_id, role_id)
);

CREATE TABLE unit
(
  unit_id character varying(3) NOT NULL,
  name character varying(32) NOT NULL,
  PRIMARY KEY (unit_id)
);

CREATE TABLE typemodel
(
	string varchar(256),
	boolean1 char(1),
	boolean2 char(1),
	byte1 char(1),
	byte2 char(1),
	short1 smallint,
	short2 smallint,
	int1 integer,
	int2 integer,
	long1 bigint,
	long2 bigint,
	float1 number(1,1),
	float2 number(1,1),
	double1 number(1,1),
	double2 number(1,1),
	bigInteger1 bigint,
	bigDecimal number(1,1),
	clob1 clob,
	blob1 blob,
	enum1 char(1),
	date1 date,
	time1 time,
	timestamp1 timestamp
);

INSERT INTO typemodel values('もじ','1','1','1','1',1,2,3,4,5,6,0.1,0.2,0.3,0.4,7,0.8,'hogehogehogeohgeohgeohgeoh','1013','1','2015-07-01','12:00:00','2015-07-01 12:00:00.555');

INSERT INTO ap_user (user_id,name,lock_flg,valid_from,valid_to,unit_id,role_id) VALUES ('A1','ゆーざー1','1','2015-01-10',null,'U1','R1');
INSERT INTO ap_user (user_id,name,lock_flg,valid_from,valid_to,unit_id,role_id) VALUES ('A2','ゆーざー2','0',null,'2015-01-10','U1','R1');
INSERT INTO ap_user (user_id,name,lock_flg,valid_from,valid_to,unit_id,role_id) VALUES ('A3','ゆーざー3','1',null,'2015-01-10','U1','R1');
INSERT INTO ap_user (user_id,name,lock_flg,valid_from,valid_to,unit_id,role_id) VALUES ('A4','ゆーざー4','0','2015-01-10','2015-01-10','U2','R2');

INSERT INTO role (unit_id,role_id,name) VALUES ('U1','R1','ろーる1');
INSERT INTO role (unit_id,role_id,name) VALUES ('U2','R2','ろーる2');

INSERT INTO unit (unit_id,name) VALUES ('U1','ゆにっと1');
INSERT INTO unit (unit_id,name) VALUES ('U2','ゆにっと2');
INSERT INTO unit (unit_id,name) VALUES ('U3','ゆにっと3');

