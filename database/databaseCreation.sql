DROP TABLE nodes CASCADE CONSTRAINTS;
/
DROP TABLE edges CASCADE CONSTRAINTS;
/
DROP TABLE images CASCADE CONSTRAINTS;
/
DROP TABLE courses CASCADE CONSTRAINTS;
/
DROP TABLE schedule CASCADE CONSTRAINTS;
/
CREATE TABLE nodes (
  id INT NOT NULL PRIMARY KEY,
  floor INT,
  name VARCHAR(50),
  type VARCHAR(15)
)
/
CREATE TABLE edges (
  id INT NOT NULL PRIMARY KEY,
  id1 INT NOT NULL,
  id2 INT NOT NULL,
  cost DOUBLE PRECISION,
  CONSTRAINT fk_edges_nodes1 FOREIGN KEY (id1) REFERENCES nodes(id),
  CONSTRAINT fk_edges_nodes2 FOREIGN KEY (id2) REFERENCES nodes(id)
)
/
CREATE TABLE images (
  node_id INT NOT NULL,
  image VARCHAR(100),
   CONSTRAINT fk_edges_nodes FOREIGN KEY (node_id) REFERENCES nodes(id)
)
/
CREATE TABLE courses (
  id INT NOT NULL PRIMARY KEY,
  name VARCHAR2(60) NOT NULL,
  studyGroup VARCHAR2(100)
)
/
CREATE TABLE schedule (
  node_id int not null,
  course_id int not null,
  starting_time VARCHAR2(20),
  ending_time VARCHAR2(20),
  day VARCHAR(10),
  CONSTRAINT fk_schedule_nodes FOREIGN KEY (node_id) REFERENCES nodes(id),
  CONSTRAINT fk_schedule_courses FOREIGN KEY (course_id) REFERENCES courses(id)
)
/
DROP SEQUENCE id_seq_nodes;
/
DROP SEQUENCE id_seq_edges;
/
DROP SEQUENCE id_seq_courses;
/

CREATE SEQUENCE id_seq_nodes
  INCREMENT BY 1
  START WITH 1;
/
CREATE SEQUENCE id_seq_edges
  INCREMENT BY 1
  START WITH 1;
/
CREATE SEQUENCE id_seq_courses
  INCREMENT BY 1
  START WITH 1;
/

CREATE OR REPLACE TRIGGER id_autoIncrement_nodes
BEFORE INSERT ON nodes FOR EACH ROW
BEGIN
  SELECT id_seq_nodes.NEXTVAL INTO :new.id FROM dual;
END;
/
CREATE OR REPLACE TRIGGER id_autoIncrement_edges
BEFORE INSERT ON edges FOR EACH ROW
BEGIN
  SELECT id_seq_edges.NEXTVAL INTO :new.id FROM dual;
END;
/
CREATE OR REPLACE TRIGGER id_autoIncrement_courses
BEFORE INSERT ON courses FOR EACH ROW
BEGIN
  SELECT id_seq_courses.NEXTVAL INTO :new.id FROM dual;
END;
/
