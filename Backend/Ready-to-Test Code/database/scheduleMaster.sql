/*
 * @author="Paul-Reftu"
 */

SET SERVEROUTPUT ON;

CREATE OR REPLACE PACKAGE ScheduleMaster AS  
  /*
   * current working directory to extract the schedule
   */
  v_currDir VARCHAR2(50);
  
  /*
   * @param p_dirName - the name of the DIRECTORY variable to be created
   * @param p_dir - the path of the directory
   * creates or replaces the DIRECTORY var. named 'p_dirName' with the value 'p_dir'
   */
  PROCEDURE createDirectory (p_dirName VARCHAR2, p_dir VARCHAR2);

  /*
   * @param p_filePath - the path to the file (w/o the file name itself)
   * @param p_fileName - the name of the file
   * stores the given JSON schedule for a classroom inside the database
   */
  PROCEDURE storeSchedule (p_filePath VARCHAR2, p_fileName VARCHAR2);

  /*
   * @param l_jsonString - the character large object that holds the JSON string
   * auxiliary method of storeSchedule() method - it carries out the latter's job
   */
  PROCEDURE storeScheduleAux (l_jsonString CLOB);
  
  /*
   * remove already-existing schedule from our database
   */
  PROCEDURE removeSchedule;
  
  /*
   * @param p_newFilePath - the file path of the new schedule
   * @param p_newFileName - the file name of the new schedule
   * update our already-existing schedule with a new one
   */
  PROCEDURE updateSchedule (p_newFilePath VARCHAR2, p_newFileName VARCHAR2);
END ScheduleMaster;
/

CREATE OR REPLACE PACKAGE BODY ScheduleMaster AS
  PROCEDURE createDirectory (p_dirName VARCHAR2, p_dir VARCHAR2) AS
  v_creationQuery VARCHAR2(500);
  BEGIN
    /*
     * also set package's 'v_currDir' var. to 'p_dirName' so that we
     * can use it to know where to extract the schedule from
     */
    v_currDir := p_dirName;
    v_creationQuery := 'CREATE OR REPLACE DIRECTORY ' || p_dirName ||
      ' AS ''' || p_dir || '''';
    EXECUTE IMMEDIATE (v_creationQuery);
    
    DBMS_OUTPUT.PUT_LINE('IMPORTANT NOTICE: You must execute ' ||
      'the following command as an administrator (e.g SYS) for ' ||
      'the storeSchedule() method to successfully complete its task!');
    DBMS_OUTPUT.PUT_LINE('GRANT READ ON DIRECTORY ' ||
      p_dirName || ' TO <yourUsername>;');
  END createDirectory;

  PROCEDURE storeSchedule (p_filePath VARCHAR2, p_fileName VARCHAR2) AS
  /*
   * file handler for reading an external large object
   */
  l_fileHandler BFILE;
  /*
   * character large object (CLOB) used to store the JSON string
   */
  l_jsonString CLOB;
  
  l_fileHandlerOffset INTEGER := 1;
  l_jsonStringOffset INTEGER := 1;
  l_bfile_csid NUMBER := DBMS_LOB.DEFAULT_CSID;
  l_lang_ctx NUMBER := DBMS_LOB.DEFAULT_LANG_CTX;
  l_warning NUMBER;
  BEGIN
    /*
     * create dir. var. corresponding to the given path
     */
    createDirectory('SCHEDULES_SOURCE_DIR', p_filePath);
  
    /*
     * Open, read and close the JSON file
     */
    l_fileHandler := BFileName(v_currDir, p_fileName);
    DBMS_LOB.OPEN(l_fileHandler, DBMS_LOB.LOB_READONLY);
    
    DBMS_LOB.CREATETEMPORARY(l_jsonString, true);
          
    DBMS_LOB.LoadCLOBFromFile(l_jsonString, l_fileHandler, 
      DBMS_LOB.getLength(l_fileHandler), l_fileHandlerOffset, l_jsonStringOffset,
        l_bfile_csid, l_lang_ctx, l_warning);
    
    DBMS_LOB.CLOSE(l_fileHandler);
    
    /*
     * store the schedule information
     */
    storeScheduleAux(l_jsonString);
    
    DBMS_OUTPUT.PUT_LINE('');
  END storeSchedule;

  PROCEDURE storeScheduleAux (l_jsonString CLOB) AS
  j_emptyObject JSON := JSON('{}');
  
  j_string JSON;
  
  j_roomScheduleList JSON_LIST;
  j_roomSchedule JSON;
  
  j_roomCode JSON_VALUE;
  j_roomRecord JSON;
  j_dayRecord JSON;
  j_eventList JSON_LIST;
  
  j_event JSON;
  j_eventStart JSON;
  j_eventEnd JSON;
  j_eventStartHour JSON_VALUE;
  j_eventStartMinute JSON_VALUE;
  j_eventStartSecond JSON_VALUE;
  j_eventStartNano JSON_VALUE;
  
  j_eventEndHour JSON_VALUE;
  j_eventEndMinute JSON_VALUE;
  j_eventEndSecond JSON_VALUE;
  j_eventEndNano JSON_VALUE;
  
  v_eventStartTime VARCHAR(15);
  v_eventEndTime VARCHAR(15);
  
  j_eventName JSON_VALUE;
  j_eventType JSON_VALUE;
  j_professorList JSON_LIST;
  j_professorName JSON_VALUE;
  j_groupList JSON_LIST;
  j_groupName JSON_VALUE;
  
  v_temp_nodes nodes.name%TYPE;
  v_temp_courses courses.name%TYPE;
  
  v_currDay VARCHAR(30);
  BEGIN
    j_string := JSON(l_jsonString);
    j_roomScheduleList := JSON_LIST(j_string.get('roomSchedules'));
    
    FOR i IN 1 .. j_roomScheduleList.count LOOP
      j_roomSchedule := JSON(j_roomScheduleList.get(i));
      
      j_roomCode := j_roomSchedule.get('roomCode');
      
      /*
       * insert room node, if it doesn't already exist
       */
      BEGIN
        SELECT name INTO v_temp_nodes FROM nodes WHERE name=j_roomCode.get_string;
        EXCEPTION WHEN NO_DATA_FOUND THEN
          INSERT INTO nodes VALUES(0, null, j_roomCode.get_string, null);
          COMMIT;
      END;
      
      j_roomRecord := JSON(j_roomSchedule.get('roomRecord'));
      
      /*
       * loop through every set of events in every working day
       */
      FOR dayIdx IN 1 .. 5 LOOP
        IF (dayIdx = 1) THEN
          v_currDay := 'LUNI';
        ELSIF (dayIdx = 2) THEN
          v_currDay := 'MARTI';
        ELSIF (dayIdx = 3) THEN
          v_currDay := 'MIERCURI';
        ELSIF (dayIdx = 4) THEN
          v_currDay := 'JOI';
        ELSIF (dayIdx = 5) THEN
          v_currDay := 'VINERI';
        END IF;
        
        IF j_roomRecord.get(v_currDay) IS NULL THEN
          CONTINUE;
        END IF;
        
        j_dayRecord := JSON(j_roomRecord.get(v_currDay));
        
        j_eventList := JSON_LIST(j_dayRecord.get('listaEvenimente'));
        
        FOR j IN 1 .. j_eventList.count LOOP
          j_event := JSON(j_eventList.get(j));
          
          /*
           * get the time info. regarding the event's start
           */
          j_eventStart := JSON(j_event.get('oraStart'));
          j_eventStartHour := j_eventStart.get('hour');
          j_eventStartMinute := j_eventStart.get('minute');
          j_eventStartSecond := j_eventStart.get('second');
          j_eventStartNano := j_eventStart.get('nano');
        
          /*
           * get the time info. regarding the event's end
           */
          j_eventEnd := JSON(j_event.get('oraFinal'));
          j_eventEndHour := j_eventEnd.get('hour');
          j_eventEndMinute := j_eventEnd.get('minute');
          j_eventEndSecond := j_eventEnd.get('second');
          j_eventEndNano := j_eventEnd.get('nano');
          
          
          /*
           * store the event start time as 'HH:MM:SS:NN'
           */
          v_eventStartTime := (j_eventStartHour.get_number || ':' || j_eventStartMinute.get_number || ':' 
            || j_eventStartSecond.get_number || ':' || j_eventStartNano.get_number);
          /*
           * store the event end time as 'HH:MM:SS:NN'
           */
          v_eventEndTime := (j_eventEndHour.get_number || ':' || j_eventEndMinute.get_number || ':' 
            || j_eventEndSecond.get_number || ':' || j_eventEndNano.get_number);
        
          j_eventName := j_event.get('numeEveniment');
          j_eventType := j_event.get('tipEveniment');
          
          j_professorList := JSON_LIST(j_event.get('listaProfesori'));
          
          /*
           * get the info. regarding the professors holding the curr. event
           */
          FOR k IN 1 .. j_professorList.count LOOP
            j_professorName := j_professorList.get(k);
          END LOOP; -- end of professor list loop
          
          j_groupList := JSON_LIST(j_event.get('listaGrupe'));
        
          /*
           * get the info. regarding the study groups attending the curr. event
           */
          FOR k IN 1 .. j_groupList.count LOOP
            j_groupName := j_groupList.get(k);
          END LOOP; -- end of group list loop
          
          /*
           * insert new course unless it already exists
           */
          BEGIN
            SELECT name INTO v_temp_courses FROM courses WHERE name=j_eventName.get_string;
            EXCEPTION WHEN NO_DATA_FOUND THEN
              INSERT INTO courses VALUES (0, j_eventName.get_string, j_groupName.get_string);
              COMMIT;
          END;
          
          /*
           * insert new schedule information for the current room and course
           */
          INSERT INTO schedule VALUES ((SELECT id FROM nodes WHERE name=j_roomCode.get_string), (SELECT id FROM courses WHERE name=j_eventName.get_string), 
            v_eventStartTime, v_eventEndTime, v_currDay);
          COMMIT;
        END LOOP; -- end of event list loop
      END LOOP; -- end of working day loop
    END LOOP; -- end of schedule list loop
  END storeScheduleAux;

  PROCEDURE removeSchedule AS
  v_truncScheduleQuery VARCHAR(100);
  BEGIN
    v_truncScheduleQuery := 'TRUNCATE TABLE schedule';
  
    EXECUTE IMMEDIATE (v_truncScheduleQuery);
  END removeSchedule;
  
  PROCEDURE updateSchedule (p_newFilePath VARCHAR2, p_newFileName VARCHAR2) AS
  BEGIN
    removeSchedule();
    storeSchedule (p_newFilePath, p_newFileName);
  END;
END ScheduleMaster;
/

/*
 * example of 'ScheduleMaster' package usage
 */
/*
BEGIN
  ScheduleMaster.storeSchedule('C:\Users\Atroxyph\git\Software-Engineering-E2\scheduleParser\schedules', 'facultySchedule.json');
  ScheduleMaster.removeSchedule();
  ScheduleMaster.updateSchedule('C:\Users\Atroxyph\git\Software-Engineering-E2\scheduleParser\schedules', 'facultySchedule.json');
END;
/
*/

/*
 * the following are the modified tables
 */
/*
SELECT * FROM nodes;
SELECT * FROM courses;
SELECT * FROM schedule;
*/

SET SERVEROUTPUT OFF;