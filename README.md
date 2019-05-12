# IMPORTANT NOTICE
Seeing as we now have a functional version of our module, I do not want to see anyone BREAKING the code.
Before committing, make sure the application functions correctly, so that we do not run into more bugs than we'd like.

# ALSO:
To keep everything and everyone synchronized, please do install and utilize GitHub Desktop: https://desktop.github.com/
And make suggestive commit messages. (NO "Add files via upload" or any message of the sort)

# Software Engineering 2018-2019, Project "AR Guide", Back-End Module, Study Group IIE2

# Link to Trello Board 1 of Back-End Module Team: https://trello.com/b/WDHkhf5I/project-ar-guide-back-end-module

List of students working in this module, as well as each one's completed tasks w.r.t the last iteration.
Last iteration timeline: Apr 22 - May 13, 2019.

# Definition of Done (for the last sprint)
- The connection between the Back-End and the Front-End modules has been successfully made, and thus integration of the application is assured.

- The Drawing App has been completed and has proven its use in aiding us engineer the Building Plan for our faculty.

- The Building Plan for the Faculty of Computer Science has been engineered.

- An optimized version of Dijkstra's Algorithm which allows O(E * log(V)) time complexity has been engineered. (where G := (V, E) is the undirected graph representing the building plan, w/ V being the set of vertices and E - the set of edges)

- Several algorithms for computing relevant information w.r.t the building have been pondered upon and ultimately designed. (such as figuring out the closest location of a certain type in an efficient way)

- Support for multiple buildings has been thought upon and designed in our application.

# 1. Paul Alexandru Reftu (Scrum Master) (paul.reftu@outlook.de)
Completed tasks:

  a) Engineered a DAO (i.e, a Data Access Object) w.r.t the application's database. (namely, the DatabaseEmissary class)
  
  b) Engineered the application's ARGuide, ARGProcessor, JSONResource, JRProcessor, JRDecoder and JSONResourceException classes.
  
  c) Merged the former application's WorkingSchedule and BuildingPlan classes into the universal JSONResource class.
  
  d) Revised the UML Class Diagram due to the previous change on point c).
  
  e) Assured migration to a new SQLite database in converting the former PL/SQL packages w.r.t the Working Schedule and the Building Plan.
  
  f) Fixed several bugs in the Drawing App w.r.t the JSON output of the application.
  
  g) Created an environment for testing w.r.t this module's functionalities in the Solution class - to prevent future changes that would break our application.
  
  h) Assured integration with the Front End module.
  
# 2. Radu Mugur-Bogdan
Completed tasks:

  a) Helped integrate the Web Parser auxiliary application into our module.
  
  b) Helped fix the bug in the Web Parser that misinterpreted certain resources in the schedule (such as 'Videoproiector+Laptop') as being classrooms.
  
  c) Engineered a Daemon Thread that verifies at a certain time interval whether there is a pending update for our faculty's schedule and notifies the application with a proper signal of this fact.
  
  d) Helped integrate the PathGenerator class into our application.

# 3. Lungu Stefan
Completed tasks:

  a) Upgraded the application such that it can support multiple buildings - providing the basic functionalities to generic buildings, and augmented functionalities to our faculty in particular.

# 4. Ilisei Bogdan-Razvan
(transfered to the Testing Module)
  
# 5. Balan Ioana Maria
Completed tasks:

  a) Engineered the PathGenerator class which uses an optimized version of Dijkstra's algorithm - tweaked specifically for sparse graphs.
  
  b) Helped during the efforts to migrate to a new SQLite database by converting the former PL/SQL package for the path generator.

# 6. Gemeniuc Adrian-Gheorghita
Completed tasks:

  a) Helped integrate the Web Parser auxiliary application into our module.
  
  b) Helped fix the bug in the Web Parser that misinterpreted certain resources in the schedule (such as 'Videoproiector+Laptop') as being classrooms.
  
  c) Engineered the ARGXHandler class.
  
  d) Helped integrate the PathGenerator class into our application.

# 7. Munteanu Cosmin
Completed tasks:

  a) Helped engineer the Drawing Application that aided us in creating the Building Plan for our faculty.
  
  b) Added brief instructions w.r.t the usage of the Drawing App.
  
  c) Cleaned up the last version of the Drawing App for better readability, commented the code and methods of the app and generated proper JavaDoc in this light.

# 8. Cristian Andrei (andreicristian6(at)protonmail(dot)com)
Completed tasks:
  a) Helped engineer the Drawing Application that aided us in creating the Building Plan for our faculty.
  
# 9. Caloian Andrei George
(transfered from the Testing Module>)

Completed tasks:

  a) Engineered the Building Plan for our faculty.
  
  b) Helped engineer the optimized Dijkstra algorithm.
  
  c) Presented several innovative ideas w.r.t certain services our application could offer.
  
  d) Engineered an algorithm for computing the closest POI of a particular type. (e.g the closest bathroom)
