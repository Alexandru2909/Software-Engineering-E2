/*
 * @author="Paul Reftu"
 */
 /*
  * Remark: Testing still has to be made along with several optimizations - but the database *should* 
  * have the correct information inserted w.r.t the courses and their schedule
  */
 

SET SERVEROUTPUT ON;

/*
 * must be replaced by a relative path or your own device's absolute path
 */
CREATE OR REPLACE DIRECTORY SCHEDULES_SOURCE_DIR AS 'D:\Users\Atroxyph\Documents\GitHub\Software-Engineering-E2\scheduleParser\schedules';
/*
 * command below must be executed by an admin (such as SYS)
 */
--GRANT READ ON DIRECTORY SCHEDULES_SOURCE_DIR TO STUDENT;

CREATE OR REPLACE PACKAGE ScheduleMaster AS
  /*
   * @param filePath - the path to the file (w/o the file name itself)
   * @param fileName - the name of the file
   * stores the given JSON schedule for a classroom inside the database
   */
  PROCEDURE storeSchedule (fileName VARCHAR2);
  PROCEDURE storeDay (l_jsonString CLOB, p_day VARCHAR2, p_isNewNode BOOLEAN DEFAULT FALSE);
END ScheduleMaster;
/

CREATE OR REPLACE PACKAGE BODY ScheduleMaster AS

  PROCEDURE storeSchedule (fileName VARCHAR2) AS
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
     * Open, read and close the JSON file
     */
    l_fileHandler := BFileName('SCHEDULES_SOURCE_DIR', fileName);
    DBMS_LOB.OPEN(l_fileHandler, DBMS_LOB.LOB_READONLY);
    
    DBMS_LOB.CREATETEMPORARY(l_jsonString, true);
          
    DBMS_LOB.LoadCLOBFromFile(l_jsonString, l_fileHandler, 
      DBMS_LOB.getLength(l_fileHandler), l_fileHandlerOffset, l_jsonStringOffset,
        l_bfile_csid, l_lang_ctx, l_warning);
    
    DBMS_LOB.CLOSE(l_fileHandler);
    
    storeDay(l_jsonString, 'LUNI', TRUE);
    storeDay(l_jsonString, 'MARTI');
    storeDay(l_jsonString, 'MIERCURI');
    storeDay(l_jsonString, 'JOI');
    storeDay(l_jsonString, 'VINERI');
    
  END storeSchedule;

  PROCEDURE storeDay (l_jsonString CLOB, p_day VARCHAR2, p_isNewNode BOOLEAN DEFAULT FALSE) AS
  j_emptyObject JSON := JSON('{}');
  
  j_string JSON;
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
  BEGIN
  
    DBMS_OUTPUT.PUT_LINE('Call to new day using ' || p_day);
    j_string := JSON(l_jsonString);
    j_roomCode := j_string.get('roomCode');
    
    DBMS_OUTPUT.PUT_LINE('Room code: ' || j_roomCode.get_string);
    
    /*
     * insert room node
     */
    BEGIN
      IF (p_isNewNode = TRUE) THEN
        SELECT name INTO v_temp_nodes FROM nodes WHERE name=j_roomCode.get_string;
      END IF;
      EXCEPTION WHEN NO_DATA_FOUND THEN
        INSERT INTO nodes VALUES(0, null, j_roomCode.get_string, null);
        COMMIT;
    END;
    
    j_roomRecord := JSON(j_string.get('roomRecord'));
    
     IF j_roomRecord.get(p_day) IS NULL THEN
      RETURN;
    END IF;
    
    j_dayRecord := JSON(j_roomRecord.get(p_day));
    
    j_eventList := JSON_LIST(j_dayRecord.get('listaEvenimente'));
    
    DBMS_OUTPUT.PUT_LINE('Evenimente ' || p_day || ': ');

    FOR i IN 1 .. j_eventList.count LOOP
      j_event := JSON(j_eventList.get(i));
      j_eventStart := JSON(j_event.get('oraStart'));
      j_eventStartHour := j_eventStart.get('hour');
      j_eventStartMinute := j_eventStart.get('minute');
      j_eventStartSecond := j_eventStart.get('second');
      j_eventStartNano := j_eventStart.get('nano');
      
      j_eventEnd := JSON(j_event.get('oraFinal'));
      j_eventEndHour := j_eventEnd.get('hour');
      j_eventEndMinute := j_eventEnd.get('minute');
      j_eventEndSecond := j_eventEnd.get('second');
      j_eventEndNano := j_eventEnd.get('nano');
      
      v_eventStartTime := (j_eventStartHour.get_number || ':' || j_eventStartMinute.get_number || ':' 
        || j_eventStartSecond.get_number || ':' || j_eventStartNano.get_number);
      v_eventEndTime := (j_eventEndHour.get_number || ':' || j_eventEndMinute.get_number || ':' 
        || j_eventEndSecond.get_number || ':' || j_eventEndNano.get_number);
      
      DBMS_OUTPUT.PUT_LINE('Event start: ' || v_eventStartTime);
      DBMS_OUTPUT.PUT_LINE('Event end: ' || v_eventEndTime);
      
      j_eventName := j_event.get('numeEveniment');
      j_eventType := j_event.get('tipEveniment');
      
      DBMS_OUTPUT.PUT_LINE('Event name: ' || j_eventName.get_string);
      DBMS_OUTPUT.PUT_LINE('Event type: ' || j_eventType.get_string);
      
      j_professorList := JSON_LIST(j_event.get('listaProfesori'));
      
      FOR i IN 1 .. j_professorList.count LOOP
        j_professorName := j_professorList.get(i);
        
        DBMS_OUTPUT.PUT_LINE('Professor name: ' || j_professorName.get_string);
      END LOOP;
      
      j_groupList := JSON_LIST(j_event.get('listaGrupe'));
      
      FOR i IN 1 .. j_groupList.count LOOP
        j_groupName := j_groupList.get(i);
        
        DBMS_OUTPUT.PUT_LINE('Group name: ' || j_groupName.get_string);
      END LOOP;
      
      DBMS_OUTPUT.PUT_LINE('');
      
      BEGIN
        SELECT name INTO v_temp_courses FROM courses WHERE name=j_eventName.get_string;
        EXCEPTION WHEN NO_DATA_FOUND THEN
          INSERT INTO courses VALUES (0, j_eventName.get_string, j_groupName.get_string);
          COMMIT;
      END;
      
      INSERT INTO schedule VALUES ((SELECT id FROM nodes WHERE name=j_roomCode.get_string), (SELECT id FROM courses WHERE name=j_eventName.get_string), 
        v_eventStartTime, v_eventEndTime, p_day);
      COMMIT;
    END LOOP;
    
    DBMS_OUTPUT.PUT_LINE('Read JSON string: ' || l_jsonString);
  END storeDay;
END ScheduleMaster;
/

/*
 * Oracle 11g XE does not support Java or its installation ; therefore the following code will not work on that version
 */
/*
CREATE OR REPLACE AND COMPILE JAVA SOURCE NAMED "DirSeeker" AS
  import java.io.*;
  import java.sql.*;
  
  public class DirSeeker {
    public static void main(String dir) throws SQLException {
      File f = new File(dir);
      String[] fileList = f.list();
      
      for (int i = 0; i < fileList.length; i++) {
        #sql {
          ScheduleMaster.storeSchedule(fileList[i]);
        };
      }
    }
  }

*/

BEGIN
  /*
   * obviously the following has to be optimized (easiest to do in JAVA)
   */

  ScheduleMaster.storeSchedule('faculty_Acvariu.json');
  ScheduleMaster.storeSchedule('faculty_C2.json');
  ScheduleMaster.storeSchedule('faculty_C112.json');
  ScheduleMaster.storeSchedule('faculty_C114.json');
  ScheduleMaster.storeSchedule('faculty_C210.json');
  ScheduleMaster.storeSchedule('faculty_C308.json');
  ScheduleMaster.storeSchedule('faculty_C309.json');
  ScheduleMaster.storeSchedule('faculty_C401.json');
  ScheduleMaster.storeSchedule('faculty_C403.json');
  ScheduleMaster.storeSchedule('faculty_C405.json');
  ScheduleMaster.storeSchedule('faculty_C409.json');
  ScheduleMaster.storeSchedule('faculty_C411.json');
  ScheduleMaster.storeSchedule('faculty_C412.json');
  ScheduleMaster.storeSchedule('faculty_C413.json');
  ScheduleMaster.storeSchedule('faculty_C901.json');
  ScheduleMaster.storeSchedule('faculty_C903.json');
  ScheduleMaster.storeSchedule('faculty_C905.json');
  ScheduleMaster.storeSchedule('faculty_C909.json');
  ScheduleMaster.storeSchedule('faculty_laptop.json');
  ScheduleMaster.storeSchedule('faculty_Videoproiector+Laptop.json');
END;
/

SELECT * FROM nodes;
SELECT * FROM courses;
SELECT * FROM schedule;

SET SERVEROUTPUT OFF;