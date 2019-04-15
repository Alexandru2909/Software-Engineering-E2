DROP table nodes cascade constraints
/
drop table edges cascade constraints
/
drop table images cascade constraints
/
drop table courses cascade constraints
/
drop table schedule cascade constraints
/
CREATE TABLE nodes (
  id INT NOT NULL PRIMARY KEY,
  floor INT,
  name VARCHAR(15),
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
create table images (
  node_id INT NOT NULL,
  image VARCHAR(100),
   CONSTRAINT fk_edges_nodes FOREIGN KEY (node_id) REFERENCES nodes(id)
)
/
CREATE TABLE courses (
  id INT NOT NULL PRIMARY KEY,
  name VARCHAR2(30) NOT NULL,
  an NUMBER(1)
)
/
CREATE TABLE schedule (
  node_id int not null,
  course_id int not null,
  starting_time date,
  ending_time date,
  day VARCHAR(10),
  CONSTRAINT fk_schedule_nodes FOREIGN KEY (node_id) REFERENCES nodes(id),
  CONSTRAINT fk_schedule_courses FOREIGN KEY (course_id) REFERENCES nodes(id)
)