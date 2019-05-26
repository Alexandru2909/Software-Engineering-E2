# IMPORTANT NOTICE
Seeing as we now have a functional version of our module, I do not want to see anyone BREAKING the code.
Before committing, make sure the application functions correctly, so that we do not run into more bugs than we'd like.

# ALSO:
To keep everything and everyone synchronized, please do install and utilize GitHub Desktop: https://desktop.github.com/
And make suggestive commit messages. (NO "Add files via upload" or any message of the sort)

# Software Engineering 2018-2019, Project "AR Guide", Back-End Module, Study Group IIE2

# Link to Trello Board 1 of Back-End Module Team: https://trello.com/b/WDHkhf5I/project-ar-guide-back-end-module

List of students working in this module, as well as each one's completed tasks w.r.t the last iteration.
Last iteration timeline: May 13 - May 27, 2019.

# Definition of Done (for the last sprint)

- Integration of the Back-End and the Front-End modules has been completed.

- The Drawing App has been upgraded with extra functionalities and has been made more user-friendly.

- The geographic details of each node in the Building Plan of our faculty have been introduced to help compute the shortest path between any two points in our graph.

- Several handy algorithms have been made for our main application - such as the closest POI computation algorithm, the tweaked BFS algorithm that is run whenever a building plan's graph has all node pair distances equal etc.

- Above algorithms have been tested to make sure they function correctly.

- General bug fixes and optimizations have been to the main application.

# 1. Paul Alexandru Reftu (Scrum Master) (paul.reftu@outlook.de)
Completed tasks:

  a) Assured migration from the formerly used JDBC API to Android's Native SQLite Database API, to move closer towards integration.
  
  b) Assured integration of the Back End and the Front End modules.
  
  c) Extended the DAO (Data Access Object) with more helper methods w.r.t the database.
  
  d) Engineered a method for computing the forward azimuth (a.k.a the bearing) b/w a source and a destination, taking into account their geographic coordinates.
  
  e) Helped split the Web Parser's activities into multiple threads.
  
  f) Eliminated some redundancies found in the Web Parser.
  
  g) Improved the security of the application by protecting against SQL injection vulnerabilities.
  
  h) Solved a bug in the Drawing App that caused the incorrect final graph connection b/w all floors.
  
# 2. Radu Mugur-Bogdan
Completed tasks:

  a) Solved the bug in the Drawing App that caused the incorrect JSON output of the graph w.r.t its edges.
  
  b) Fixed some bugs w.r.t the 'Delete Edge' functionality in the Drawing App.
  
  c) Fixed some problems w.r.t the internal logic of the Web Parser that did not allow its proper execution.
  
  d) Engineered a Signal class, an Auto-update class and a Daemon Thread for the Web Parser that together notify the Front End module whenever an update is available for the faculty working schedule.

# 3. Lungu Stefan
Completed tasks:

  a) Engineered a method for the PathGenerator class that computes the closest node of type "Classroom"/"Amphitheater" which currently has no lecture/seminar taking place.
  
  b) Ran tests on the above method to assure that the algorithm functions correctly.

# 4. Ilisei Bogdan-Razvan
(transfered to the Testing Module)
  
# 5. Balan Ioana Maria
Completed tasks:

  a) Performed tests on the optimized Dijkstra algorithm part of the PathGenerator class to make sure it runs correctly.
  
  b) Helped finish the algorithm w.r.t the closest POI computation.
  
  c) Performed tests on the closest POI algorithm to test its capabilities.

# 6. Gemeniuc Adrian-Gheorghita
Completed tasks:

  a) Engineered an altered BFS algorithm that computes the shortest path b/w two points - algorithm that is executed whenever all distances in our building plan graph are *equal*.
  
  b) Performed tests on the above algorithm to assert its abilities.

# 7. Munteanu Cosmin
Completed tasks:

  a) Adapted the Drawing App to the new structure of our Building Plan that includes the geographic coordinates of each node.

  b) Added a color scheme to the Drawing App that classifies nodes according to their type & added a legend describing aforementioned colors and their meaning.
  
  c) Generally helped make the Drawing App be more user-friendly.
  
  d) Helped extend the functionalities of the Drawing App. 

# 8. Cristian Andrei (andreicristian6(at)protonmail(dot)com)
Completed tasks:

  a) Added an extra panel in the EAST side of the Drawing App where the user can enter a node's details.

  b) Added auto-popup functionalities w.r.t node & edge detail insertion.
  
  c) Added real-time descriptive popup alerts whenever an exception w.r.t what the user is trying to do occurs. 

  d) Helped make the Drawing App more user-friendly.
  
  e) Ran several tests on the Drawing App to proof-test it.
  
  f) Helped fix bugs w.r.t the Drawing App.
  
# 9. Caloian Andrei George
(transfered from the Testing Module)

Completed tasks:

  a) Fixed errors in the Building Plan of our faculty w.r.t its edges.
  
  b) Upgraded the Building Plan of our faculty with the geographic coordinates w.r.t each node in the graph.
  
  c) Kickstarted and developed most of the engineering of the algorithm w.r.t the closest POI computation.
