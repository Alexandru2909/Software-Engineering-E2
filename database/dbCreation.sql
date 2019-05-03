CREATE TABLE nodes (
  id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  floor INTEGER,
  name VARCHAR(50),
  type VARCHAR(15)
)
/
CREATE TABLE edges (
  id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  id1 INTEGER NOT NULL,
  id2 INTEGER NOT NULL,
  cost DOUBLE PRECISION,
  CONSTRAINT fk_edges_nodes1 FOREIGN KEY (id1) REFERENCES nodes(id),
  CONSTRAINT fk_edges_nodes2 FOREIGN KEY (id2) REFERENCES nodes(id)
)
/
CREATE TABLE images (
  node_id INTEGER NOT NULL,
  image VARCHAR(100),
   CONSTRAINT fk_edges_nodes FOREIGN KEY (node_id) REFERENCES nodes(id)
)
/
CREATE TABLE courses (
  id INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT,
  name VARCHAR2(60) NOT NULL,
  studyGroup VARCHAR2(100)
)
/
CREATE TABLE schedule (
  node_id INTEGER not null,
  course_id INTEGER not null,
  starting_time VARCHAR2(20),
  ending_time VARCHAR2(20),
  day VARCHAR(10),
  CONSTRAINT fk_schedule_nodes FOREIGN KEY (node_id) REFERENCES nodes(id),
  CONSTRAINT fk_schedule_courses FOREIGN KEY (course_id) REFERENCES courses(id)
)